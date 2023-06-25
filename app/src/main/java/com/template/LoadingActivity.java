package com.template;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.wrappers.InstantApps;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoadingActivity extends AppCompatActivity {
    TextView textView;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAnalytics mFirebaseAnalytics;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Calendar FirebaseInstanceId;
    SharedPreferences sPref;
    URL url = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){

                }
            }
        };

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        String token = String.valueOf(FirebaseMessaging.getInstance().getToken());


        database = FirebaseDatabase.getInstance();
       myRef = database.getReference("link");
       try {
            url = new URL(myRef.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String domenFromFirebase = url.getProtocol() +"://" + url.getHost();

        textView = findViewById(R.id.load);
       textView.setText(domenFromFirebase);

      starNewActivity(getUrl(domenFromFirebase));

    }
    public String getUrl(String value) {
        String packageName = getApplicationContext().getPackageName();          //метод возвращает название пакета
        UUID uuid = UUID.randomUUID();
        String timeZone = "Europe/Moscow";
        return value + "/?packageid=" + packageName + "&usserid=" + uuid +
                "&getz=" + timeZone + "&getr=utm_source=google-play&utm_medium=organic";
    }

    public void starNewActivity(String url) {
       myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                textView = findViewById(R.id.load);
                textView.setText(url);
                savePref(url);;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
         savePref(url);
        Intent intent = new Intent(this, WebActivity.class); //класс для работы с разл. активити (откуда,куда)
        intent.setData(Uri.parse(url));
       // intent.putExtra("url", url);
        startActivity(intent);                                              //осуществляем переход
    }
    void savePref(String url) {
        sPref = getPreferences(MODE_PRIVATE);                 //получаем объект класса Preferences
        SharedPreferences.Editor ed = sPref.edit();           //объект для редактирования данных
        ed.putString("url", url);
        ed.commit();                                           //сохранение данных
    }

}

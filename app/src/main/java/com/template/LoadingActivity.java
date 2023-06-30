package com.template;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
public class LoadingActivity extends AppCompatActivity {
    private static String user_agent;
    TextView textView;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseAnalytics mFirebaseAnalytics;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseFirestore db;
    SharedPreferences sPref;
    URL url = null, urlDB = null;
    HttpURLConnection httpConn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        String token = String.valueOf(FirebaseMessaging.getInstance().getToken());
        db = FirebaseFirestore.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        textView = findViewById(R.id.load);
       try {
            urlDB = new URL(myRef.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String domenFromFirebase = urlDB.getProtocol() +"://" + urlDB.getHost();
        String urlLink = getUrl(domenFromFirebase);

        user_agent = System.getProperty("http.agent");
        try {
            url = new URL(urlLink);                       //URL-соединение создано...
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            httpConn = (HttpURLConnection) url.openConnection();   // Создано HTTP-соединение...
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("User-Agent", user_agent);
            //создаём не основной поток (асинхронная функция)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            int responseCode = httpConn.getResponseCode();         // используется для получения кода состояния из ответного HTTP-сообщения
            if (responseCode == HttpURLConnection.HTTP_OK) {        // HTTP_OK : Код состояния HTTP 200: OK.
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                URL finalUrl = url;
                savePref(String.valueOf(finalUrl));
                starNewActivity(String.valueOf(finalUrl));
            } else {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // textView.setText(userAgent);
    }
    public String getUrl(String value) {
        String packageName = getApplicationContext().getPackageName();          //метод возвращает название пакета
        UUID uuid = UUID.randomUUID();
        String timeZone = "Europe/Moscow";
        return value + "/?packageid=" + packageName + "&usserid=" + uuid +
                "&getz=" + timeZone + "&getr=utm_source=google-play&utm_medium=organic";
    }
    public void starNewActivity(String url) {
        Intent intent = new Intent(this, WebActivity.class); //класс для работы с разл. активити (откуда,куда)
       // intent.setData(Uri.parse(url));
         intent.putExtra("url", url);
        startActivity(intent);
    }
    void savePref(String url) {
        sPref = getPreferences(MODE_PRIVATE);                 //получаем объект класса Preferences
        SharedPreferences.Editor ed = sPref.edit();           //объект для редактирования данных
        ed.putString("url", url);
        ed.commit();                                           //сохранение данных
    }
}

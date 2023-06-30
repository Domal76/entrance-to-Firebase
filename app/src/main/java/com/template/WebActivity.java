package com.template;

import static android.content.Intent.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class WebActivity extends AppCompatActivity {
    TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        textView = findViewById(R.id.webText);
        String urlWeb = (String) getIntent().getSerializableExtra("url");

        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
        CustomTabsIntent customBuild = customIntent.build();
        //customBuild.launchUrl(this, Uri.parse(String.valueOf(urlWeb)));
         openCustomTubs(WebActivity.this, customBuild, Uri.parse(urlWeb));
        // textView.setText(response.toString());
    }
    public static void openCustomTubs (Activity activity, CustomTabsIntent customTabsIntent, Uri uri){
        String packageName = "com.android.chrome";
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity,uri);
        }
        else {
            activity.startActivities(new Intent[]{new Intent(ACTION_VIEW, uri)});
        }
    }
}
    }


}

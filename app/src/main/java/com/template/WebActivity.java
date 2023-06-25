package com.template;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends AppCompatActivity {
    WebView webActivity;
    TextView textView;
                            //класс Preferences для работы с данными

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webActivity = findViewById(R.id.webView);
        textView = findViewById(R.id.webText);
      //   String urlWeb = getIntent().getExtras().getString("url");
        // String urlWeb = (String) getIntent().getSerializableExtra("url");
     //   textView.setText(urlWeb);
       /* WebSettings webSettings = webActivity.getSettings();
        String userAgent = String.format("%s [%s/%s]", webSettings.getUserAgentString(), "App Android", BuildConfig.VERSION_NAME);
        webActivity.getSettings().setUserAgentString(userAgent);
        webActivity.loadUrl(urlWeb);*/                // указываем страницу загрузки

        webActivity.getSettings().setJavaScriptEnabled(true);         // включаем поддержку JavaScript
        webActivity.getSettings().setDomStorageEnabled(true);
        webActivity.getSettings().setUserAgentString("example_android_app");
        webActivity.setWebViewClient(webClient());                    // передаём в WebView
        webActivity.loadUrl(getIntent().getDataString());

    }

    public WebViewClient webClient(){                                     //открытие ссылки внутри приложения
        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation") @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @TargetApi(Build.VERSION_CODES.N) @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };
        return webViewClient;
    }


}
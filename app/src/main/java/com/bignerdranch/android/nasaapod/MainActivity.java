package com.bignerdranch.android.nasaapod;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.bignerdranch.android.nasaapod.model.Apod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  private WebView webView;
  private String apiKey;
  private ProgressBar loading;
  private ApodService service;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupWebView();
    setContentView(R.layout.activity_main);
    //TODO Set up the web view.
    //TODO Set up service.
    //TODO Load today's image.
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void setupWebView() {
    webView = findViewById(R.id.web_view);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        loading.setVisibility(View.GONE);
        // TODO Show toast
      }
    });
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
  }

  private void setupService() {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setDateFormat("yyyy-MM-dd")
        .create();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create(gson));
        .build();
    service = retrofit.create(ApodService.class);
    apiKey = BuildConfig.API_KEY;
  }

  private class ApodTas extends AsyncTask<Date, Void, Apod> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Apod apod) {
      super.onPostExecute(apod);
    }

    @Override
    protected void onCancelled(Apod apod) {
      super.onCancelled(apod);
    }

    @Override
    protected Apod doInBackground(Date... dates) {
      return null;
    }
  }


}

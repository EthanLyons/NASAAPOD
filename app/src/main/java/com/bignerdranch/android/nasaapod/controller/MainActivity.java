package com.bignerdranch.android.nasaapod.controller;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bignerdranch.android.nasaapod.BuildConfig;
import com.bignerdranch.android.nasaapod.R;
import com.bignerdranch.android.nasaapod.controller.DateTimePickerFragment.Mode;
import com.bignerdranch.android.nasaapod.controller.DateTimePickerFragment.OnChangeListener;
import com.bignerdranch.android.nasaapod.model.Apod;
import com.bignerdranch.android.nasaapod.service.ApodService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final String CALENDAR_KEY = "calender_ms";
  private static final String APOD_KEY = "apod";


  private WebView webView;
  private String apiKey;
  private ProgressBar loading;
  private ApodService service;
  private Apod apod;
  private Calendar calendar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupWebView();
    setupDatePicker();
    setupService();
    calendar = Calendar.getInstance();
    if (savedInstanceState != null) {
      long savedMillis = savedInstanceState.getLong(CALENDAR_KEY, calendar.getTimeInMillis());
      calendar.setTimeInMillis(savedMillis);
      apod = (Apod) savedInstanceState.getSerializable(APOD_KEY);
    }
    if (apod != null) {
      webView.loadUrl(apod.getUrl());
      loading.setVisibility(View.VISIBLE);
    } else {
      new ApodTask().execute(new Date()); // TODO Deal with time zones.
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.info)  {
      showInfo();
    return true;
  } else {
    return super.onOptionsItemSelected(item);
  }
}

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putLong(CALENDAR_KEY, calendar.getTimeInMillis());
    outState.putSerializable(APOD_KEY);
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void setupWebView() {
    webView = findViewById(R.id.web_view);
    loading = findViewById(R.id.loading);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        loading.setVisibility(View.GONE);
        showInfo();
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

  private void showInfo() {
    if (apod != null) {
      Toast.makeText(MainActivity.this, apod.getTitle(), Toast.LENGTH_LONG).show();
    }
  }

  private void setupDatePicker()  {
    FloatingActionButton jumpDate = findViewById(R.id.jump_date);
    jumpDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DateTimePickerFragment picker = new DateTimePickerFragment();
        picker.setMode(Mode.DATE);
        picker.setCalendar(calendar);
        picker.setListener(new OnChangeListener() {
          @Override
          public void onChange(Calendar cal) {
            new ApodTask().execute(cal.getTime());
          }
        });
        picker.show(getSupportFragmentManager(), picker.getClass().getSimpleName());
      }
    });
  }


  private void setupService() {
    Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setDateFormat(DATE_FORMAT)
        .create();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    service = retrofit.create(ApodService.class);
    apiKey = BuildConfig.API_KEY;
  }

  private class ApodTask extends AsyncTask<Date, Void, Apod> {

    @Override
    protected void onPreExecute() {
      loading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Apod apod) {
      MainActivity.this.apod = apod;
      webView.loadUrl(apod.getUrl());
    }

    @Override
    protected void onCancelled(Apod apod) {
      loading.setVisibility(View.GONE);
      Toast.makeText(MainActivity.this, R.string.error_message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected Apod doInBackground(Date... dates) {
      Apod apod = null;
      try {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Response<Apod> response = service.get(apiKey, format.format(dates[0])).execute();
        if (response.isSuccessful()) {
          apod = response.body();
          calendar.setTime(dates[0]);
        }
      } catch (IOException e) {
        Log.e(getClass().getSimpleName(), e.toString());
      } finally {
        if (apod == null) {
          cancel(true);
        }
      }
      return apod;
    }

  }

}

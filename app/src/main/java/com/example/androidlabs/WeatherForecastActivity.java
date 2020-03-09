package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecastActivity extends AppCompatActivity {
    class ForecastQuery extends AsyncTask<String, Integer, String> {
        private float uv;
        private String min;
        private String max;
        private String currentTemp;

        private Bitmap bitmap;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String icon = null;
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();

                factory.setNamespaceAware(false);
                xpp.setInput(response, "UTF-8");


                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equals("temperature")) {
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(25);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(50);
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(75);
                        } else if (xpp.getName().equals("weather")) {
                            icon = xpp.getAttributeValue(null, "icon");
                        }
                    }
                    eventType = xpp.next();
                }

                url = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                urlConnection = (HttpURLConnection) url.openConnection();
                response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONObject json = new JSONObject(result);
                uv = (float) json.getDouble("value");

                String fileName = icon + ".png";

                try {
                    Log.i(WeatherForecastActivity.class.toString(), "Looking for " + fileName);
                    bitmap = BitmapFactory.decodeStream(openFileInput(fileName));
                    publishProgress(100);
                } catch (FileNotFoundException e) {
                    Log.i(WeatherForecastActivity.class.toString(), "Cannot find locally " + fileName + ". Re-downloading " + fileName);
                    url = new URL("http://openweathermap.org/img/w/" + fileName);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == 200) {
                        bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                        FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        publishProgress(100);
                    }
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView maxTemp = findViewById(R.id.max_temperature);
            maxTemp.setText(max);

            TextView minTemp = findViewById(R.id.min_temperature);
            minTemp.setText(min);

            TextView currentTempView = findViewById(R.id.current_temp);
            currentTempView.setText(currentTemp);

            TextView uvView = findViewById(R.id.uv_rating);
            uvView.setText(Float.toString(uv));

            ImageView imgView = findViewById(R.id.weather_img_view);
            imgView.setImageBitmap(bitmap);

            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

        ForecastQuery req = new ForecastQuery();
        req.execute();
    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

}

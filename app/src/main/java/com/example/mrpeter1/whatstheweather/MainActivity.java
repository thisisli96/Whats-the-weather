package com.example.mrpeter1.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView hasilview;

    public class DownloadTask extends AsyncTask<String, Void, String>
    {


        @Override
        protected String doInBackground(String... urls) {
            String result ="";
            URL url;
            HttpURLConnection urlConnection = null;

            try{

                url = new URL(urls[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1){

                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e){

                e.printStackTrace();
                return "failed";
            }

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            //  Log.i("JSON", s);
            try { // step 2
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("weather content", weatherInfo); // data yang diambil hanya weather dari API

                JSONArray arr = new JSONArray(weatherInfo);
                String massage = "";
                for (int i=0; i < arr.length(); i++){
                    JSONObject jsonpart = arr.getJSONObject(i);
                    String main = jsonpart.getString("main");
                    String description = jsonpart.getString("description");

//                    Log.i("main",jsonpart.getString("main"));
//                    Log.i("description",jsonpart.getString("description"));

                    if (!main.equals("") && !description.equals("")){

                        massage+= main + ":" + description + "\r\n"; //jawaban dari result yang di cari

                    }
                }

                    if (!massage.equals("")){
                        hasilview.setText(massage);
                    }


            }catch (Exception e){

                e.printStackTrace();
            }
        }
    }//part one function atau apakah namanya ini pokoknya buat mengambil api dari web yang sudah menyediakan



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        hasilview = findViewById(R.id.hasilview);
    }

    public  void  klikbutton (View view){ // step 2 menampilkan data dari api di logcat

        DownloadTask task = new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q=" + editText.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22"); // api yang ingin di akses

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0); // langsung menurun keyboard saat enter
    }
}

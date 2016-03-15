package com.example.hermon.clima;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    String base;
    String we;
    String te;
    String te_mi;
    String te_ma;
    public class DowloadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params){
            URL url = null;
            HttpURLConnection connection = null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder buldier = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine())  != null){
                    buldier.append(line);
                }
                return  buldier.toString();
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
       @Override
        protected  void onPostExecute(String s){
            super.onPostExecute(s);
            //Log.d("OnPostExecute", s);
            try {
                EditText ciudad = (EditText) findViewById(R.id.cityText);
                TextView response = (TextView) findViewById(R.id.responseText);
                String city = ciudad.getText().toString();
                JSONObject jsonObject = new JSONObject(s);
                Log.d("JsonObjet",jsonObject.toString());
                JSONObject jsonObject1 = jsonObject.getJSONObject("main");
                te = jsonObject1.getString("temp");
                te_mi = jsonObject1.getString("temp_min");
                te_ma = jsonObject1.getString("temp_max");
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                for (int i=0; i<weatherArray.length(); i++){
                    JSONObject weather = weatherArray.getJSONObject(i);
                    we = weather.getString("description");
                    Log.d("Weather",we);
                }
                if(TextUtils.isEmpty(city)) {
                    ciudad.setError("Porfavor ingresa una ciudad");
                    return;
                }
                if(ciudad.getText().toString().matches("[0-9]+")){
                    ciudad.setError("No se aceptan numeros");
                    return;
                }
                //if (s.isEmpty()){
                 //   Toast.makeText(getApplicationContext(),"La ciudad no existe", Toast.LENGTH_LONG).show();
                   // response.setText("");
                    //return;
                //}else{
                    String resu = "General: "+ we+" \n" + "Temperatua Actual: "+ te +"\n"+ "Temperatura maxima: "+ te_ma+"\n"+ "Temperatura minima: "+ te_mi;
                    response.setText(resu);
                    //we = null;
                //}

                //Log.d("Json", base);
            } catch (JSONException e) {
                TextView response = (TextView) findViewById(R.id.responseText);
                Toast.makeText(getApplicationContext(),"La ciudad no existe", Toast.LENGTH_LONG).show();
                response.setText("");
                e.printStackTrace();
            }
        }
    }


    public void checkWeather (View view){
        EditText ciudad = (EditText) findViewById(R.id.cityText);
        TextView response = (TextView) findViewById(R.id.responseText);
        String city = ciudad.getText().toString();
        String cityURL = null;
        try {
            cityURL = URLEncoder.encode(city, "UTF-8");
            Log.d("URLcity",cityURL);
            DowloadTask task = new DowloadTask();
            //Mario chécale esto, el link caduca, y después de cierto tiempo ya no te regresa nada
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityURL+"&appid=b1b15e88fa797225412429c1c50c122a");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
       // DowloadTask task = new DowloadTask();
        //task.execute("http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=44db6a862fba0b067b1930da0d769e98");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView mDate,mCity,mTemp,mDescription,mTempfeel,mTempmax;
    ImageView imgIcon;
    String maVille="Toronto";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mTempmax=findViewById(R.id.mTempmax);
        mTempfeel=findViewById(R.id.mTempfeel);
        mDate=findViewById(R.id.mDate);
        mCity=findViewById(R.id.mCity);
        mTemp=findViewById(R.id.mTemp);
        mDescription=findViewById(R.id.mDescription);
        afficher();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Ecrire le nom de la vile");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                maVille = query;
                afficher();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null)
                {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    public void afficher()
    {
        String url="https://api.openweathermap.org/data/2.5/weather?q="+ maVille +"&appid=dc2b739d4cbc31bc30750c3d1061e98d";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object=response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    Log.d("message1", "message1"+ response.getString("name"));
                    int tempC=(int)Math.round((main_object.getDouble("temp")));
                    String temp=String.valueOf(tempC);
                    int feels_likeC=(int)Math.round((main_object.getDouble("feels_like")));
                    String feels_like=String.valueOf(feels_likeC);
                    int temp_maxC=(int)Math.round((main_object.getDouble("temp_max")));
                    String temp_max=String.valueOf(temp_maxC);
                    String description=object.getString("description");
                    String city=response.getString("name");
                    String icon=object.getString("icon");
                    mCity.setText(city);
                    mTemp.setText(temp);
                    mTempfeel.setText(feels_like);
                    mDescription.setText(description);
                    mTempmax.setText(temp_max);
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd");
                    String formatted_date=simpleDateFormat.format(calendar.getTime());
                    mDate.setText((formatted_date));
                    String imageUri="http://openweathermap.org/img/w/"+ icon+ ".png";
                    imgIcon=findViewById(R.id.imgIcon);
                    Uri myUri=Uri.parse(imageUri);
                    Picasso.with(MainActivity.this).load(myUri).resize(200, 200).into(imgIcon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("message", "message"+ error);

            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);

    }
}
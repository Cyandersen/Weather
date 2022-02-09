package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
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
    TextView mDate,mCity,mTemp,mDescription;
    ImageView imgIcon;
    String maVille="Toronto";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDate.findViewById(R.id.mDate);
        mCity.findViewById(R.id.mCity);
        mTemp.findViewById(R.id.mTemp);
        mDescription.findViewById(R.id.mDescription);
    }
    public void afficher()
    {
        String url="https://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=dc2b739d4cbc31bc30750c3d1061e98d";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object=response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    int tempC=(int)Math.round((main_object.getDouble("temp")));
                    String temp=String.valueOf(tempC);
                    String description=object.getString("description");
                    String city=response.getString("name");
                    String icon=object.getString("icon");
                    mCity.setText(city);
                    mTemp.setText(temp);
                    mDescription.setText(description);
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

            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);

    }
}
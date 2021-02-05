package com.example.bitcoinstats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textViewBitcoinPrice;
    private RequestQueue mQueue;
    final String URL = "https://blockchain.info/ticker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        navigationView = (NavigationView)findViewById(R.id.navigationView);
        textViewBitcoinPrice = (TextView)findViewById(R.id.textViewBitcoinPrice);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        mQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("USD");

                            textViewBitcoinPrice.setText(jsonObject.getString("last") + " $");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.marketPrice) {
            startActivity(new Intent(MainActivity.this, MarketPriceActivity.class));
        } else if (id == R.id.txPerDay) {
            startActivity(new Intent(MainActivity.this, TxPerDayActivity.class));
        } else if (id == R.id.totalTxFees) {
            startActivity(new Intent(MainActivity.this, TotalTxFeesActivity.class));
        } else if (id == R.id.mempool) {
            startActivity(new Intent(MainActivity.this, MempoolActivity.class));
        }



        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
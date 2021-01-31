package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TxPerDayActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;
    final String URL = "https://api.blockchain.info/charts/transactions-per-second?timespan=3hours&rollingAverage=8hours&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_per_day);

        mTextViewResult = (TextView)findViewById(R.id.textView_result);

        mQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("values");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jedan = jsonArray.getJSONObject(i);

                                String x = jedan.getString("x");
                                String y = jedan.getString("y");

                                long dv = Long.valueOf(x)*1000;
                                Date df = new java.util.Date(dv);
                                String vv = new SimpleDateFormat("dd MM yyyy").format(df);

                                mTextViewResult.append("X = " + vv + " | Y = " + y + "\n\n");
                            }
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
}
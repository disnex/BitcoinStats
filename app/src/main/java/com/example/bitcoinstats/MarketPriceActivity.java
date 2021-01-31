package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MarketPriceActivity extends AppCompatActivity {

    LineChart mpLineChart;
    private RequestQueue mQueue;
    final String URL = "https://api.blockchain.info/charts/market-price?timespan=1year&rollingAverage=24hours&format=json";
    ArrayList<Entry> dataVals = new ArrayList<Entry>();
    LineDataSet lineDataSet;
    ArrayList<ILineDataSet> dataSet = new ArrayList<>();
    LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_price);

        mpLineChart = (LineChart)findViewById(R.id.line_chart);
        mpLineChart.setTouchEnabled(true);
        mpLineChart.setPinchZoom(true);

        mQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("values");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject oneObject = jsonArray.getJSONObject(i);

                                //String unixTimestamp = oneObject.getString("x");
                                Float unixTimestamp = Float.valueOf(oneObject.getString("x"));
                                Float value = Float.valueOf(oneObject.getString("y"));
/*
                                long dv = Long.valueOf(unixTimestamp)*1000;
                                Date df = new java.util.Date(dv);
                                String dateConverted = new SimpleDateFormat("dd MM yyyy").format(df);
*/
                                dataVals.add(new Entry(unixTimestamp, value));
                            }
                            lineDataSet = new LineDataSet(dataVals, "Data Set 1");
                            dataSet.add(lineDataSet);
                            data =  new LineData(dataSet);
                            mpLineChart.setData(data);
                            mpLineChart.invalidate();
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
/*
        LineDataSet lineDataSet = new LineDataSet(dataYValues, "test");
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        LineData lineData = new LineData(dataXValues, lineDataSet);
        lineData.setValueTextSize(13f);
        lineData.setValueTextColor(Color.BLACK);

 */
/*
        //AxisValueFormatter xAxisFormatter = new HourAxis

        //ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        //dataSets.add(lineDataSet1);

        LineData data = new LineData(dataSets);
        */

    }
}
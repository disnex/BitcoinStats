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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        mpLineChart = (LineChart)findViewById(R.id.line_chart_market_price);
        mpLineChart.setTouchEnabled(true);
        mpLineChart.setPinchZoom(true);
        mpLineChart.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        Description description = new Description();
        description.setText("Market price");
        description.setTextSize(20);
        mpLineChart.setDescription(description);

        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
        xAxis.setLabelCount(5, true);

        YAxis yAxisLeft = mpLineChart.getAxisLeft();
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return value + " $";
            }
        });

        YAxis yAxisRight = mpLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        mQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("values");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject oneObject = jsonArray.getJSONObject(i);

                                Float unixTimestamp = Float.valueOf(oneObject.getString("x"));
                                Float value = Float.valueOf(oneObject.getString("y"));

                                dataVals.add(new Entry(unixTimestamp, value));
                            }
                            lineDataSet = new LineDataSet(dataVals, "Price");
                            lineDataSet.setLineWidth(2);
                            lineDataSet.setValueTextSize(12);
                            lineDataSet.setValueFormatter(new LargeValueFormatter());
                            dataSet.add(lineDataSet);
                            data =  new LineData(dataSet);
                            mpLineChart.setData(data);
                            mpLineChart.animateXY(2000,2000);
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
    }
}
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
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TxPerDayActivity extends AppCompatActivity {
    LineChart mpLineChart;
    private RequestQueue mQueue;
    final String URL = "https://api.blockchain.info/charts/n-transactions?timespan=1year&format=json";
    ArrayList<Entry> dataVals = new ArrayList<Entry>();
    LineDataSet lineDataSet;
    ArrayList<ILineDataSet> dataSet = new ArrayList<>();
    LineData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_per_day);

        mpLineChart = (LineChart)findViewById(R.id.line_chart_total_tx_fees);
        mpLineChart.setTouchEnabled(true);
        mpLineChart.setPinchZoom(true);
        mpLineChart.setDoubleTapToZoomEnabled(true);
        mpLineChart.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        Description description = new Description();
        description.setText("Tx per day");
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

        YAxis yAxisRight = mpLineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mpLineChart.setMarker(mv);

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
                            lineDataSet = new LineDataSet(dataVals, "Txs");
                            lineDataSet.setLineWidth(2);
                            lineDataSet.setValueTextSize(12);
                            dataSet.add(lineDataSet);
                            data =  new LineData(dataSet);
                            mpLineChart.setData(data);
                            mpLineChart.invalidate();
                            mpLineChart.animateXY(2000,2000);
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
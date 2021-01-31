package com.example.bitcoinstats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class TotalTxFeesActivity extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_tx_fees);

        lineChart = (LineChart)findViewById(R.id.line_chart2);
        LineDataSet lineDataSet = new LineDataSet(dataValues1(), "Data Set 1");
        ArrayList<ILineDataSet> dataSet = new ArrayList<>();
        dataSet.add(lineDataSet);

        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private ArrayList<Entry> dataValues1() {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0,20));
        dataVals.add(new Entry(1,22));
        dataVals.add(new Entry(2,24));
        dataVals.add(new Entry(3,26));
        dataVals.add(new Entry(4,28));
        dataVals.add(new Entry(5,30));

        return dataVals;
    }
}
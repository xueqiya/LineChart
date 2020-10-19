package com.apkmatrix.demo.linechart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        refresh.setOnClickListener(view -> {
            Map<String, Float> map = getData();
            lineChart.startDraw(map);
        });
    }

    private Map<String, Float> getData() {
        Map<String, Float> map = new LinkedHashMap<>();
        String[] dates = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        for (int i = 0; i < 7; i++) {
            final double d = Math.random();
            final float num = (int) (d * 50);
            map.put(dates[i], num);
        }
        return map;
    }

    private void initView() {
        lineChart = findViewById(R.id.lineChart);
        refresh = findViewById(R.id.refresh);
    }
}
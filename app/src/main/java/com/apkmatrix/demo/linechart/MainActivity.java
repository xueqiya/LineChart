package com.apkmatrix.demo.linechart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //x轴坐标对应的数据
    private final List<String> xValue = new ArrayList<>();
    //y轴坐标对应的数据
    private final List<Integer> yValue = new ArrayList<>();
    //折线对应的数据
    private final Map<String, Integer> value = new HashMap<>();
    private ChartView chartView;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        refresh.setOnClickListener(view -> {
            setData();
        });
    }

    private void setData() {
        xValue.clear();
        yValue.clear();
        value.clear();
        for (int i = 0; i < 7; i++) {
            String x = "周" + (i + 1);
            xValue.add(x);
            if (i == 6) {
                value.put(x, 0);
            } else {
                value.put(x, (int) (Math.random() * 100));
            }
        }
        int maxValue = 0;
        for (Map.Entry<String, Integer> entry : value.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
            }
        }
        for (int i = 0; i < 6; i++) {
            yValue.add((maxValue) / 5 * i);
        }
        chartView.setValue(value, xValue, yValue);
    }

    private void initView() {
        chartView = findViewById(R.id.chartview);
        refresh = findViewById(R.id.refresh);
    }
}
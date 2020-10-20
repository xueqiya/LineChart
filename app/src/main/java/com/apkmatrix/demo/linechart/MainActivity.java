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
    private ChartView chartview;
    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        for (int i = 0; i < 12; i++) {
            xValue.add((i + 1) + "月");
            value.put((i + 1) + "月", (int) (Math.random() * 181 + 60));//60--240
        }

        for (int i = 0; i < 6; i++) {
            yValue.add(i * 60);
        }

        ChartView chartView = (ChartView) findViewById(R.id.chartview);
        chartView.setValue(value, xValue, yValue);

        refresh.setOnClickListener(view -> {
            Map<String, Double> map = getData();
//            chartview.startDraw(map);
        });
    }


    private Map<String, Double> getData() {
        Map<String, Double> map = new LinkedHashMap<>();
        String[] dates = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        int size = dates.length;
        for (int i = 0; i < size; i++) {
            if (i != size - 1) {
                final double d = Math.random();
                final double num = (d * 100);
                map.put(dates[i], num);
            } else {
                map.put(dates[i], 0.0);
            }
        }
        return map;
    }

    private void initView() {
        chartview = findViewById(R.id.chartview);
        refresh = findViewById(R.id.refresh);
    }
}
package com.apkmatrix.demo.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

public class LineChart extends View {
//    y轴数据
    private final String[] ysplit = {"10", "20", "30", "40", "50"};
    private Paint XPaint;
    private Paint YPaint;
    private Paint pointPaint;
    private Paint effectPaint;
    private final Context context;
    private float max;
    private final int textSize = 10;
    private boolean start = false;
    private Map<String, Float> mapx;

    public LineChart(Context context) {
        super(context);
        this.context = context;
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void initView() {
//        折线
        XPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        XPaint.setAntiAlias(true);
        XPaint.setColor(Color.BLACK);
        XPaint.setStrokeWidth(dp2px(1));

//        空心原点
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.BLACK);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeWidth(dp2px(1));

//        xy轴
        YPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        YPaint.setAntiAlias(true);
        YPaint.setColor(Color.BLACK);
        YPaint.setStrokeWidth(dp2px(2));

//        xy轴文字
        effectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        effectPaint.setAntiAlias(true);
        effectPaint.setColor(Color.BLACK);
        effectPaint.setTextSize(sp2px(textSize));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (start) {
            int margin = 20;
//            原点x坐标
            float yandianx = dp2px(margin);
//            原点y坐标
            float yuandiany = getMeasuredHeight() - dp2px(margin);
//            控件的宽
            float wigth = getMeasuredWidth() - dp2px(margin * 2);
//            控件的高
            float height = getMeasuredHeight() - dp2px(margin * 2);
            float point[] = new float[]{yandianx, yuandiany, yandianx, yandianx, yandianx - dp2px(3), yandianx + dp2px(textSize / 2)};
//            绘制y轴
            canvas.drawLines(point, 0, 4, YPaint);
//            绘制y轴左侧箭头
            canvas.drawLines(point, 2, 4, YPaint);
//            绘制y轴右侧箭头
            canvas.drawLine(yandianx, yandianx, yandianx + dp2px(3), yandianx + dp2px(textSize / 2), YPaint);
//            绘制x轴
            canvas.drawLine(yandianx, yuandiany, yandianx + wigth, yuandiany, YPaint);
//            绘制x轴上侧箭头
            canvas.drawLine(yandianx + wigth, yuandiany, yandianx + wigth - dp2px(textSize / 2), yuandiany - dp2px(3), YPaint);
//            绘制x轴下侧箭头
            canvas.drawLine(yandianx + wigth, yuandiany, yandianx + wigth - dp2px(textSize / 2), yuandiany + dp2px(3), YPaint);
//            绘制原点坐标数字0
            canvas.drawText("0", yandianx - sp2px(textSize) - dp2px(2), yuandiany + sp2px(textSize) + dp2px(2), effectPaint);

//            绘制y轴短横线和数字
            float gao = height / (ysplit.length + 1);
            for (int i = 0; i < ysplit.length; i++) {
                float a = Float.parseFloat(ysplit[i]);
                if (max < a) {
                    max = a;
                }
                canvas.drawLine(yandianx, yuandiany - (i + 1) * gao, yandianx + dp2px(3), yuandiany - (i + 1) * gao, YPaint);
                canvas.drawText(ysplit[i], yandianx - (sp2px(textSize) * (ysplit[i].length())), yuandiany - (i + 1) * gao + sp2px(textSize / 2), effectPaint);
            }

            float kuan = wigth / (mapx.size() + 1);
            Object o[] = mapx.keySet().toArray();
            for (int i = 0; i < o.length; i++) {
                String s = o[i].toString();
                float x = yandianx + (i + 1) * kuan;
                float y = yuandiany - (height - gao) / max * mapx.get(o[i]);
//                绘制x轴短竖线
                canvas.drawLine(x, yuandiany, x, yuandiany - dp2px(3), YPaint);
//                绘制x轴文字
                canvas.drawText(s, x - (sp2px(textSize) * (s.length() / 2)), yuandiany + sp2px(textSize) + dp2px(3), effectPaint);
                if (i > 0) {
//                    绘制折线
                    canvas.drawLine(yandianx + i * kuan, yuandiany - (height - gao) / max * mapx.get(o[i - 1]), x, y, XPaint);
                }
            }
//            绘制折线连接处
            for (int i = 0; i < o.length; i++) {
                float x = yandianx + (i + 1) * kuan;
                float y = yuandiany - (height - gao) / max * mapx.get(o[i]);
                canvas.drawCircle(x, y, dp2px(3), pointPaint);
            }
        }
    }


    public void startDraw(Map<String, Float> mapx) {
        start = true;
        this.mapx = mapx;
        initView();
        invalidate();
    }

    /**
     * sp转换成px
     */
    private int sp2px(float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * dp转换成px
     */
    private int dp2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
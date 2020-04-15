package com.example.covidtracker.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.covidtracker.R;
import com.example.covidtracker.models.dataModels.DailyRecord;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class LineChartHelper {
    private LineChart lineChart;
    private Context context;

    private int[] colorArray;
    private int textGreyColor;
    private final String[] legendName = {"Active", "Recovered", "Dead"};

    public LineChartHelper(LineChart lineChart, Context context) {
        this.lineChart = lineChart;
        this.context = context;

        textGreyColor = ContextCompat.getColor(context, R.color.colorAccent);
        colorArray = new int[]{
                ContextCompat.getColor(context, R.color.color_graph_active),
                ContextCompat.getColor(context, R.color.color_graph_recovered),
                ContextCompat.getColor(context, R.color.color_graph_dead)};
    }

    public void formatLineGraph() {


        LegendEntry[] legendEntries = new LegendEntry[3];
        for (int i = 0; i < legendEntries.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colorArray[i];
            entry.label = legendName[i];
            legendEntries[i] = entry;
        }

        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormLineWidth(8);
        legend.setXEntrySpace(15);
        legend.setFormSize(10);
        legend.setCustom(legendEntries);

        lineChart.setGridBackgroundColor(ContextCompat.getColor(context, R.color.graph_bg));
        lineChart.setBorderColor(ContextCompat.getColor(context, R.color.colorPrimary));
        lineChart.invalidate();
        lineChart.setNoDataTextColor(Color.WHITE);


        Description description = new Description();
        description.setText("Covid-19");
        description.setTextColor(textGreyColor);
        description.setTextSize(18);
        description.setXOffset(8);
        description.setYOffset(8);
        description.setTypeface(Typeface.SANS_SERIF);
        lineChart.setDescription(description);
        lineChart.setDrawGridBackground(true);
        lineChart.setBorderColor(Color.BLUE);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(textGreyColor);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new MyFormattedValue());
        xAxis.setLabelCount(4);
        xAxis.setLabelRotationAngle(-45);
        xAxis.setLabelCount(12, true);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextColor(textGreyColor);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setTextColor(textGreyColor);

    }

    public void setDataset(List<DailyRecord> records){
        List<List<Entry>> dataVals = getDataset(records);

        LineDataSet dataSet1 = new LineDataSet(dataVals.get(0), "Active");
        dataSet1.setColor(colorArray[0]);
        dataSet1.setLineWidth(5);
        dataSet1.setDrawCircleHole(false);
        dataSet1.setDrawCircles(false);

        LineDataSet dataSet2 = new LineDataSet(dataVals.get(1), "Confirmed");
        dataSet2.setColor(colorArray[1]);
        dataSet2.setLineWidth(3);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setDrawCircles(false);

        LineDataSet dataSet3 = new LineDataSet(dataVals.get(2), "Dead");
        dataSet3.setColor(colorArray[2]);
        dataSet3.setLineWidth(3);
        dataSet3.setDrawCircles(false);
        dataSet3.setDrawCircleHole(false);

        LineData data = new LineData(dataSet1, dataSet2, dataSet3);
        data.setDrawValues(false);
        lineChart.setData(data);
    }

    public void animate(){
        lineChart.animateXY(800, 1000, Easing.EaseInOutBack, Easing.EaseInOutBack);
    }

    private List<List<Entry>> getDataset(List<DailyRecord> stateRecord) {
        List<List<Entry>> dataVals = new ArrayList<>();
        dataVals.add(new ArrayList<>());
        dataVals.add(new ArrayList<>());
        dataVals.add(new ArrayList<>());
        for(int i=0;i<stateRecord.size();i++) {
            DailyRecord record = stateRecord.get(i);
            dataVals.get(0).add(new Entry(i, record.getActive()));
            dataVals.get(1).add(new Entry(i, record.getRecovered()));
            dataVals.get(2).add(new Entry(i, record.getDeaths()));
        }
        return dataVals;
    }

    private class MyFormattedValue extends ValueFormatter {

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return "Day " + Math.round(value);
        }
    }


}

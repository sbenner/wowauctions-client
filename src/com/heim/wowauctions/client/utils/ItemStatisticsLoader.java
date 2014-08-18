package com.heim.wowauctions.client.utils;

/**
 * Created with IntelliJ IDEA.
 * User: sergey
 * Date: 3/5/14
 * Time: 3:14 AM
 * To change this template use File | Settings | File Templates.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Pair;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;

import java.util.Date;
import java.util.Map;


public class ItemStatisticsLoader extends AsyncTask<Long, Void, Pair<String, XYMultipleSeriesDataset>> {
    private ProgressDialog dialog;
    Context ctx;

    public ItemStatisticsLoader(Context ctx) {
        this.ctx = ctx;
        this.dialog = new ProgressDialog(this.ctx);

    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Building chart...");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(Pair<String, XYMultipleSeriesDataset> dataset) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        Intent intent = ChartFactory.getTimeChartIntent(ctx, dataset.second, getRenderer(dataset.first), null);
        ctx.startActivity(intent);
    }


    @Override
    protected Pair<String, XYMultipleSeriesDataset> doInBackground(Long... params) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        long averagePrice = 0;

        String auctions = NetUtils.getResourceFromUrl("http://192.168.1.4:8080/itemchart?id=" + params[0].toString());
        Map<Long, Long> auctionList = null;
        try {
            auctionList = AuctionUtils.buildArchivedAuctionsFromString(auctions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TimeSeries series = new TimeSeries(String.format("PPI"));
        for (Map.Entry e : auctionList.entrySet()) {
            long price = Long.parseLong(e.getKey().toString());
            long timestamp = Long.parseLong(e.getValue().toString());
            averagePrice += price;
            series.add(new Date(timestamp), price / 10000);

        }

        averagePrice = averagePrice / auctionList.size();

        dataset.addSeries(series);

        Pair<String, XYMultipleSeriesDataset> rv = new Pair(AuctionUtils.buildPrice(averagePrice), dataset);

        return rv;


    }


    private XYMultipleSeriesRenderer getRenderer(String title) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(35);
        renderer.setChartTitleTextSize(35);
        renderer.setLabelsTextSize(35);
        renderer.setLegendTextSize(35);
        renderer.setPointSize(10f);
        renderer.setMargins(new int[]{100, 70, 100, 0});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setLineWidth(9f);

        renderer.setFitLegend(true);
        renderer.setLegendHeight(50);
        renderer.setChartTitle("AVG PPI: " + title);

        r.setChartValuesSpacing(5);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setColor(Color.YELLOW);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
        renderer.setAxesColor(Color.DKGRAY);
        renderer.setLabelsColor(Color.LTGRAY);
        return renderer;
    }

}

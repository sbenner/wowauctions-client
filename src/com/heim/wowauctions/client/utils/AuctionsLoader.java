package com.heim.wowauctions.client.utils;

/**
 * Created with IntelliJ IDEA.
 * User: sergey
 * Date: 3/5/14
 * Time: 3:14 AM
 * To change this template use File | Settings | File Templates.
 */

import android.app.ListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.heim.wowauctions.client.models.Auction;
import com.heim.wowauctions.client.models.Reply;
import com.heim.wowauctions.client.ui.ItemListAdapter;
import org.json.JSONException;

import java.util.List;


public class AuctionsLoader extends AsyncTask<String, Void, Reply> {
    Context ctx;

    public AuctionsLoader(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Reply reply) {


        List<Auction> dataset = reply.getAuctions();
        ListActivity lv = (ListActivity) this.ctx;

        if (dataset != null &&
                !dataset.isEmpty()) {
            ItemListAdapter adapter = null;
            if (reply.getNumber() == 0) {
                UIUtils.showToast(this.ctx, reply.getTotalElements() + " items were found");

                adapter = new ItemListAdapter(this.ctx, dataset);
                lv.setListAdapter(adapter);
            } else {
                adapter = (ItemListAdapter) lv.getListAdapter();
                adapter.addAll(dataset);
                adapter.notifyDataSetChanged();
            }


            reply.setAuctions(null);
            lv.getListView().setTag(reply);
        } else {
            if (reply.getTotalElements() == 0) {
                lv.setListAdapter(null);
                UIUtils.showToast(this.ctx, "no items were found");
            }

        }

    }


    @Override
    protected Reply doInBackground(String... params) {

        String auctions;
        String searchString = params[0];
        Log.v("started", searchString);

        if (params.length == 1)
            auctions = NetUtils.getResourceFromUrl("http://10.0.2.2:8080/items?name=" + Uri.encode(searchString));
        else
            auctions = NetUtils.getResourceFromUrl("http://10.0.2.2:8080/items?name=" + Uri.encode(searchString) + "&page=" + params[1]);

        Reply reply = null;
        try {
            reply = AuctionUtils.buildAuctionsFromString(auctions);
            reply.setSearchString(searchString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reply;

    }


}

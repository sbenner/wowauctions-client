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

import android.util.Pair;
import com.heim.wowauctions.client.models.Auction;
import com.heim.wowauctions.client.models.Reply;
import com.heim.wowauctions.client.ui.ItemListAdapter;
import org.json.JSONException;

import java.util.List;


public class AuctionsLoader extends AsyncTask<String, Void, Reply> {
    Context ctx;
    Pair p;

    public AuctionsLoader(Context ctx,Pair p) {
        this.ctx = ctx;
        this.p = p;

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Reply reply) {

        if(reply==null)
            return;

        List<Auction> dataset = reply.getAuctions();
        ListActivity lv = (ListActivity) this.ctx;
        lv.getListView().setTag(reply);
        if (dataset != null &&
                !dataset.isEmpty()) {
            ItemListAdapter adapter = null;
            if (reply.getNumber() == 0) {
                UIUtils.showToast(this.ctx, reply.getTotalElements() + " items were found");

                adapter = new ItemListAdapter(this.ctx, dataset,p);
                lv.setListAdapter(adapter);
            } else {
                adapter = (ItemListAdapter) lv.getListAdapter();
                adapter.addAll(dataset);
                adapter.notifyDataSetChanged();
            }


            reply.setAuctions(null);

        } else {
            if (reply.getTotalElements() == 0) {
                lv.setListAdapter(null);
                UIUtils.showToast(this.ctx, "no items were found");
            }

        }
        if(lv.getListAdapter()!=null)
             ((ItemListAdapter)lv.getListAdapter()).setLoading(false);

    }


    @Override
    protected Reply doInBackground(String... params) {

        String auctions=null;
        String searchString = params[0];

        Log.v("started", searchString);
           try{
        if (params.length == 1)
            auctions = NetUtils.getResourceFromUrl(p.first+"items?name=" + Uri.encode(searchString),p.second.toString());
        else
            auctions = NetUtils.getResourceFromUrl(p.first+"items?name=" + Uri.encode(searchString) + "&page=" + params[1],p.second.toString());

           }catch (Exception e){e.printStackTrace();}
        Reply reply = null;
        try {
            if(auctions!=null){
                reply = AuctionUtils.buildAuctionsFromString(auctions);
                reply.setSearchString(searchString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reply;

    }


}

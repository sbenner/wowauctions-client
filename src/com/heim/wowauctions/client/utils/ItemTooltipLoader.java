package com.heim.wowauctions.client.utils;

/**
 * Created with IntelliJ IDEA.
 * User: sergey
 * Date: 3/5/14
 * Time: 3:14 AM
 * To change this template use File | Settings | File Templates.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.webkit.WebView;
import org.json.JSONException;
import org.json.JSONObject;


public class ItemTooltipLoader extends AsyncTask<String, Void, String> {
    private ProgressDialog dialog;
    WebView webView;

    public ItemTooltipLoader(WebView webView) {
        this.webView = webView;
        this.dialog = new ProgressDialog(webView.getContext());
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Loading item info...");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(String reply) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

//        try {
//            JSONObject obj = new JSONObject(reply);
//            reply = obj.getString("Tooltip");
//        } catch (JSONException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }

        //reply = reply.replace("//media", "http://media").replaceAll("<a\\b[^>]+>","").replaceAll("</a>","");
//        http://www.wowdb.com/tooltips



        String summary = "<html>" +
          //      "<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js'></script>" +
            //    "<script type='text/javascript' src='tt.js'></script>" +
                "<link rel='stylesheet' type='text/css' media='all' href='wow.css' />"+
                "<body bgcolor='#000000'>" +
                reply +
                "</body></html>";


        webView.loadDataWithBaseURL("file:///android_asset/", summary, "text/html", "UTF-8", null);


    }


    @Override
    protected String doInBackground(String... params) {


     //  String url = "http://www.wowdb.com/items/" + params[0] + "/tooltip";
        String url = "http://us.battle.net/wow/en/item/" + params[0] + "/tooltip";

        String reply = NetUtils.getResourceFromUrl(url,null);
        //text = text.substring(1, text.length() - 1);
          // text=text.replaceAll("\t","");


        String icon = reply.substring(reply.indexOf("<span  class=\"icon"),reply.indexOf("</span>")+7);
        reply=reply.replace(icon,"");
        reply=icon+reply;

        reply=reply.replaceAll("<a\\b[^>]+>","").replaceAll("</a>","");

        return reply;
    }


}

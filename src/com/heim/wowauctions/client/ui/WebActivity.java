package com.heim.wowauctions.client.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.heim.wowauctions.client.utils.ItemTooltipLoader;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/23/14
 * Time: 12:53 AM
 */
public class WebActivity extends Activity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Bundle extras = getIntent().getExtras();

        String id = String.valueOf(extras.getLong("itemId"));
        String name = extras.getString("name");

        Log.e("name", name);
        Log.e("itemId", id);


        WebView webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String summary = "<html>" +
                "<body bgcolor='#000000'>" +
                "</body></html>";


        webView.loadDataWithBaseURL(null, summary, "text/html", "UTF-8", null);

        new ItemTooltipLoader(webView).execute(id);


        setContentView(webView);

    }


}

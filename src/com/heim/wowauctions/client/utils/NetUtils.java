package com.heim.wowauctions.client.utils;

import android.net.Uri;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/13/14
 * Time: 8:51 PM
 */

public class NetUtils {


    public static String getResourceFromUrl(String url) {

        StringBuilder sb = new StringBuilder();

        try {



            HttpGet httpGet = new HttpGet(url);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            HttpResponse response = defaultHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());

            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }


        return sb.toString();
    }


}

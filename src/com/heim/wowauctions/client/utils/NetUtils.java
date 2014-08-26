package com.heim.wowauctions.client.utils;

import android.net.Uri;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/13/14
 * Time: 8:51 PM
 */

public class NetUtils {

    private static HttpGet createGetRequest(String url,String key) throws Exception{

        HttpGet httpGet = new HttpGet(url);
         if(key!=null){
             httpGet.addHeader("apikey","RS00001");
             httpGet.addHeader("timestamp",""+System.currentTimeMillis());

             try {
                 httpGet.addHeader("signature",CryptoUtils.createSignature(httpGet,key).replace("\n",""));
             } catch (Exception e) {
               throw e;
             }
         }

        return httpGet;
    }



    public static String getResourceFromUrl(String url,String key) {

        StringBuilder sb = new StringBuilder();

        try {


            HttpGet httpGet = null;
            try {
                httpGet = createGetRequest(url,key);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

            defaultHttpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

            HttpResponse response = defaultHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            if (response.getStatusLine().getStatusCode() != 200) {
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + response.getStatusLine().getStatusCode());
                return null;
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

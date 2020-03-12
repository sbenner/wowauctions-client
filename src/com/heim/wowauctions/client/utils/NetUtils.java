package com.heim.wowauctions.client.utils;

import android.util.Log;
import com.heim.wowauctions.client.models.Reply;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/13/14
 * Time: 8:51 PM
 */

public class NetUtils {

    private static Request okGetReq(String url, String key) throws Exception {

        Request.Builder requestBuilder = null;
        if (key != null) {
            requestBuilder = new Request.Builder()
                    .addHeader("apikey", "RS00001")
                    .addHeader("timestamp", "" + System.currentTimeMillis())
                    .url(url);

            String sig = CryptoUtils.createSignature(requestBuilder.build(), key).replace("\n", "");

            requestBuilder.addHeader("signature", sig);
        } else {
            requestBuilder = new Request.Builder().url(url);
        }

        return requestBuilder.build();

    }

    public static Reply getDataFromUrl(String url, String key) {

        StringBuilder sb = new StringBuilder();
        Reply reply = new Reply();

        try {


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Response response = null;

            TLSSocketFactory tlsTocketFactory = new TLSSocketFactory();
            OkHttpClient client = new OkHttpClient.Builder()
                    .sslSocketFactory(tlsTocketFactory, tlsTocketFactory.getTrustManager())
                    .build();

            response = client.newCall(okGetReq(url, key)).execute();
            InputStream is = response.body().byteStream();

            if (!response.isSuccessful()) {

                Log.e("error", response.message());
                reply.setStatus(response.code());
                reply.setError(response.message());

            } else {
                reply.setStatus(200);
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                reply.setData(sb.toString());
            }

        } catch (Exception e) {
            Log.e("error", e.getMessage(), e);
        }

        return reply;
    }


}

package com.heim.wowauctions.client.utils;

import android.util.Log;
import com.heim.wowauctions.client.models.Reply;
import okhttp3.OkHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

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

    private static OkHttpClient client() {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
        return client;
    }


    private static HttpGet createGetRequest(String url, String key) throws Exception {

        HttpGet httpGet = new HttpGet(url);
        if (key != null) {
            httpGet.addHeader("apikey", "RS00001");
            httpGet.addHeader("timestamp", "" + System.currentTimeMillis());

            try {
                httpGet.addHeader("signature", CryptoUtils.createSignature(httpGet, key).replace("\n", ""));
            } catch (Exception e) {
                throw e;
            }
        }

        return httpGet;
    }

    public static Reply getDataFromUrl(String url, String key) {

        StringBuilder sb = new StringBuilder();
        Reply reply = new Reply();

        try {
            HttpGet httpGet = null;
            try {
                httpGet = createGetRequest(url, key);
            } catch (Exception e) {
                Log.e("error", e.getMessage(), e);
            }


            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();


            defaultHttpClient.getParams()
                    .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);


            HttpResponse response = defaultHttpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            if (response.getStatusLine().getStatusCode() != 200) {

                Log.e("error", response.getStatusLine().toString());
                reply.setStatus(response.getStatusLine().getStatusCode());
                reply.setError(response.getStatusLine().toString());

            } else {
                reply.setStatus(response.getStatusLine().getStatusCode());
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

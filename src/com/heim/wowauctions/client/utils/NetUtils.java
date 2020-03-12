package com.heim.wowauctions.client.utils;

import android.util.Log;
import com.heim.wowauctions.client.models.Reply;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/13/14
 * Time: 8:51 PM
 */

public class NetUtils {

    private static OkHttpClient.Builder configureToIgnoreCertificate(OkHttpClient.Builder builder) {
        Log.w("warn", "Ignore Ssl Certificate");
        try {

            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance(TlsVersion.TLS_1_0.javaName());
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return builder;
    }

//    private static OkHttpClient client() throws Exception {
//        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
//                TrustManagerFactory.getDefaultAlgorithm());
//        trustManagerFactory.init((KeyStore) null);
//        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//            throw new IllegalStateException("Unexpected default trust managers:"
//                    + Arrays.toString(trustManagers));
//        }
//        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
//
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, new TrustManager[]{trustManager}, null);
//        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//        return new OkHttpClient.Builder()
//                .sslSocketFactory(sslSocketFactory, trustManager)
//                .build();
//    }

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
//            HttpGet httpGet = null;
//            try {
//                httpGet = createGetRequest(url, key);
//            } catch (Exception e) {
//                Log.e("error", e.getMessage(), e);
//            }


//            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
//
//
//            defaultHttpClient.getParams()
//                    .setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Response response = null;
            builder = configureToIgnoreCertificate(builder);

            builder.connectionSpecs(Arrays.asList(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT));

            response = builder.build().newCall(okGetReq(url, key)).execute();
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

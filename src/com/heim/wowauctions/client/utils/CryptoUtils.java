package com.heim.wowauctions.client.utils;

import android.util.Base64;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/25/14
 * Time: 9:28 PM
 */
class CryptoUtils {

    private static final String APIKEY_HEADER = "apikey";
    private static final String TIMESTAMP_HEADER = "timestamp";

    private static final List<String> SIGNATURE_KEYWORDS = Arrays.asList(APIKEY_HEADER, TIMESTAMP_HEADER);

    static  String createSignature(HttpRequestBase method, String privateKey) throws Exception {


        String sortedUrl = createSortedUrl(method);

        KeyFactory keyFactory = KeyFactory.getInstance("DSA");

        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.getBytes("UTF-8"),Base64.NO_WRAP));

        Signature sig = Signature.getInstance("DSA");
        sig.initSign(keyFactory.generatePrivate(privateKeySpec));
        sig.update(sortedUrl.getBytes("UTF-8"));

        return Base64.encodeToString(sig.sign(),Base64.NO_WRAP);

    }

    private static String createSortedUrl(HttpRequestBase method) {


        TreeMap<String, String> headersAndParams = new TreeMap<String, String>();

           for(Header header: method.getAllHeaders()){
               if (SIGNATURE_KEYWORDS.contains(header.getName())) {
                   headersAndParams.put(header.getName(), header.getValue());
               }
           }

            headersAndParams.put("query", method.getURI().getQuery());

            return createSortedUrl(
                    method.getURI().getPath(),
                    headersAndParams);

    }

    private static String createSortedUrl(String url, TreeMap<String, String> headersAndParams) {
        // build the url with headers and parms sorted
        String params = headersAndParams.get("query") != null ? headersAndParams.get("query") : "";

        StringBuilder sb = new StringBuilder();

        url="/wow/v1"+url;
        if (!url.endsWith("?")) url += "?";
        sb.append(url);

        if (params.length() > 0)
            sb.append(params).append("?apikey=").append(headersAndParams.get("apikey"))
                    .append("@timestamp=").append(headersAndParams.get("timestamp"));

        return sb.toString();
    }



}

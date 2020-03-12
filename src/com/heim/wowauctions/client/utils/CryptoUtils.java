package com.heim.wowauctions.client.utils;

import android.util.Base64;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    static String createSignature(Request req, String privateKey) throws Exception {


        String sortedUrl = createSortedUrl(req.url(), req.headers());

        KeyFactory keyFactory = KeyFactory.getInstance("DSA");

        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.getBytes("UTF-8"), Base64.NO_WRAP));

        Signature sig = Signature.getInstance("DSA");
        sig.initSign(keyFactory.generatePrivate(privateKeySpec));
        sig.update(sortedUrl.getBytes("UTF-8"));

        return Base64.encodeToString(sig.sign(),Base64.NO_WRAP);

    }

    private static String createSortedUrl(HttpUrl url, Headers headers) {


        TreeMap<String, String> headersAndParams = new TreeMap<String, String>();

        for (Map.Entry e : headers.toMultimap().entrySet()) {
            if (SIGNATURE_KEYWORDS.contains(e.getKey().toString())) {
                headersAndParams.put(e.getKey().toString(), ((List<String>) e.getValue()).get(0));
            }
        }

        headersAndParams.put("query", url.encodedQuery());

        return createSortedUrl(
                url.encodedPath(),
                headersAndParams);

    }

    private static String createSortedUrl(String url, TreeMap<String, String> headersAndParams) {
        // build the url with headers and parms sorted
        String params = headersAndParams.get("query") != null ?
                headersAndParams.get("query") : "";

        StringBuilder sb = new StringBuilder();

        url = "/wow/v1" + url;
        if (!url.endsWith("?")) url += "?";
        sb.append(url);

        if (params.length() > 0)
            sb.append(params).append("?apikey=").append(headersAndParams.get("apikey"))
                    .append("@timestamp=").append(headersAndParams.get("timestamp"));

        return sb.toString();
    }



}

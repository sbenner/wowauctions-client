package com.heim.wowauctions.client;

import android.app.Application;
import android.util.Log;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/25/14
 * Time: 11:06 PM
 */
public class AuctionsApplication extends Application {

    private String privateKey;

    private String ipAddress;

    @Override
    public void onCreate() {
        super.onCreate();

        Properties props=new Properties();

        try {
            InputStream inputStream =
                    getResources().getAssets().open("wowac.properties");
            props.load(inputStream);
        } catch (IOException e) {
            Log.e("error", e.getMessage(), e);
        }
        setPrivateKey(props.getProperty("private.key"));
        setIpAddress(props.getProperty("prod.ip"));
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }



    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}

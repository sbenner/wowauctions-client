package com.heim.wowauctions.client.utils;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;
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
        Properties props=new Properties();

        try {
            InputStream inputStream =
                    getResources().getAssets().open("wowac.properties");
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

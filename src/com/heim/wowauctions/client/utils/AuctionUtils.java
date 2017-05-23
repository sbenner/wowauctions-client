package com.heim.wowauctions.client.utils;

import android.util.Log;

import com.heim.wowauctions.client.models.ArchivedAuction;

import com.heim.wowauctions.client.models.Auction;
import com.heim.wowauctions.client.models.Item;
import com.heim.wowauctions.client.models.Reply;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/13/14
 * Time: 9:05 PM
 */
public class AuctionUtils {


    public static Map<Long, Long> buildArchivedAuctionsFromString(String contents) throws JSONException {

        Map<Long, Long> map = new HashMap<Long, Long>();
        try {
            JSONObject auctionsArray = new JSONObject(contents);
            Iterator iterator = auctionsArray.keys();
            while (iterator.hasNext()){
                String key = iterator.next().toString();
                Long val = Long.parseLong(auctionsArray.get(key).toString());
                map.put(Long.parseLong(key), val);
            }


        } catch (JSONException jsone) {
            Log.e("jsone", jsone.getMessage());
        }

        return map;
    }

    public static void setAuctionsFromStringToReply(String contents, Reply reply) throws JSONException {

        List<Auction> auctions = new ArrayList<Auction>();
        JSONObject obj = new JSONObject(contents);
        try {
            reply.setFirst(obj.getBoolean("first"));
            reply.setLast(obj.getBoolean("last"));
            reply.setTotalElements(obj.getInt("totalElements"));
            reply.setTotalPages(obj.getInt("totalPages"));
            reply.setSize(obj.getInt("size"));
            reply.setNumber(obj.getInt("number"));
            reply.setNumberOfElements(obj.getInt("numberOfElements"));
        } catch (JSONException jse) {
            Log.e("jsonerr", jse.getMessage());
        }
        JSONArray auctionsArray = obj.getJSONArray("content");

        for (int i = 0; i < auctionsArray.length(); i++) {
            obj = (JSONObject) auctionsArray.get(i);
            Auction auction = new Auction();
            JSONObject itemObject = (JSONObject) obj.get("item");

            Item item = new Item();
            item.setId(itemObject.getLong("id"));
            item.setName(itemObject.getString("name"));
            item.setItemLevel(itemObject.getInt("itemLevel"));
            item.setQuality(itemObject.getInt("quality"));

            auction.setItem(item);

            auction.setBid(obj.getString("bid"));
            auction.setBuyout(obj.getString("buyout"));
            auction.setPpi(obj.getString("ppi"));
            auction.setOwner(obj.getString("owner"));

            auction.setQuantity(obj.getInt("quantity"));
            auction.setTimeLeft(obj.getString("timeLeft"));
            auction.setTimestamp(obj.getLong("timestamp"));
            auctions.add(auction);
        }

        reply.setData(null);
        reply.setAuctions(auctions);

    }

    public static String buildPrice(long price) {
        String newprice = "";
        try {
            String oldprice = Long.toString(price);

            int len = oldprice.length();
            if (len > 4) {
                newprice = oldprice.substring(0, len - 4) + "g ";
                newprice += oldprice.substring(len - 4, len - 2) + "s ";
                newprice += oldprice.substring(len - 2, len) + "c";
            }
            if (len > 2 && len <= 4) {
                newprice += oldprice.substring(0, len - 2) + "s ";
                newprice += oldprice.substring(len - 2, len) + "c";

            }
            if (len <= 2)
                newprice += oldprice.substring(0, len) + "c";

        } catch (Exception e) {
            System.out.println(price);
            Log.e("error", e.getMessage(), e);
        }

        return newprice;
    }


}

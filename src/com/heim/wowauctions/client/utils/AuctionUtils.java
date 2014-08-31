package com.heim.wowauctions.client.utils;

import com.heim.wowauctions.client.models.ArchivedAuction;

import com.heim.wowauctions.client.models.Auction;
import com.heim.wowauctions.client.models.Item;
import com.heim.wowauctions.client.models.Reply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/13/14
 * Time: 9:05 PM
 */
public class AuctionUtils {


     public static Map<Long,Long> buildArchivedAuctionsFromString(String contents) throws JSONException {

        Map<Long,Long> map = new HashMap<Long, Long>();


        JSONArray auctionsArray = new JSONArray(contents);

        for (int i = 0; i < auctionsArray.length(); i++) {
            JSONObject obj = (JSONObject) auctionsArray.get(i);
            map.put(obj.getLong("key"),obj.getLong("value"));

        }

        return map;
    }





    // ,"totalPages":3,"totalElements":14,"firstPage":true,"lastPage":false,"last":false,"size":5,"number":0,"sort":
    // [{"direction":"DESC","property":"buyout","ignoreCase":false,"nullHandling":"NATIVE","ascending":false}],"numberOfElements":5,"first":true}

    public static Reply buildAuctionsFromString(String contents) throws JSONException {


        Reply reply = new Reply();
        List<Auction> auctions = new ArrayList<Auction>();
        JSONObject obj = new JSONObject(contents);
        reply.setFirst(obj.getBoolean("first"));
        reply.setLast(obj.getBoolean("last"));
        reply.setTotalElements(obj.getInt("totalElements"));
        reply.setTotalPages(obj.getInt("totalPages"));
        reply.setFirstPage(obj.getBoolean("firstPage"));
        reply.setLastPage(obj.getBoolean("lastPage"));
        reply.setSize(obj.getInt("size"));
        reply.setNumber(obj.getInt("number"));
        reply.setNumberOfElements(obj.getInt("numberOfElements"));

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

        reply.setAuctions(auctions);

        return reply;
    }

    public static String buildPrice(long price){
        String newprice="";
        try{
            String oldprice = Long.toString(price);

            int len = oldprice.length();
            if(len>4)
            {   newprice = oldprice.substring(0, len-4) + "g ";
                newprice += oldprice.substring(len-4, len-2) + "s ";
                newprice += oldprice.substring(len-2,len) + "c";
            }
            if(len>2&&len<=4)
            {
                newprice += oldprice.substring(0, len-2) + "s ";
                newprice += oldprice.substring(len-2,len) + "c";

            }
            if(len<=2)
                newprice += oldprice.substring(0,len) + "c";

        }catch (Exception e){
            System.out.println(price);
            e.printStackTrace();
        }

        return newprice;
    }


}

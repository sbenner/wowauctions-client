package com.heim.wowauctions.client.models;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/4/14
 * Time: 11:47 PM
 */


public class Auction {
    public String getDate() {
        return new Date(this.timestamp).toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }


    public interface BaseView {}


    private long auc;

    private Item item;

    private long itemId;

    private String owner;


    private String ownerRealm;

    private String bid;
    private String buyout;
    private int quantity;
    private String timeLeft;

    private int rand;

    private long seed;

    private Date date;
    private long timestamp;


    public long getAuc() {
        return auc;
    }

    public void setAuc(Long auc) {
        this.auc = auc;
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public String getBid() {
        return this.bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBuyout() {
        return this.buyout;
    }

    public void setBuyout(String buyout) {
        this.buyout = buyout;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getRand() {
        return rand;
    }

    public void setRand(int rand) {
        this.rand = rand;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }





    public String toString(){
        StringBuilder sb = new StringBuilder();
                sb.append(" itemName: ").append(this.getItem().getName()).
                append("\n itemLevel: ").append(this.getItem().getItemLevel()).
                append("\n quality: ").append(this.getItem().getQuality()).
                append("\n owner: ").append(this.getOwner()).
                append("\n bid: ").append(this.getBid()).
                append("\n buyout: ").append(this.getBuyout()).
                append("\n quantity: ").append(this.getQuantity()).
                append("\n timeleft: ").append(this.getTimeLeft())
                .append("");

        return sb.toString();
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

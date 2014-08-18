package com.heim.wowauctions.client.models;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/5/14
 * Time: 1:01 AM
 */

public class Item {
    private long itemId;
    private String name;
    private int itemLevel;
    private int quality;

    public long getId() {
        return itemId;
    }

    public void setId(Long id) {
        this.itemId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }


    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}



package com.example.lostandfound;

public class details {
    private String lostitem,description;

    public details(String lostitem, String description) {
        this.lostitem = lostitem;
        this.description = description;
    }

    public details() {

    }

    public String getLostitem() {
        return lostitem;
    }

    public void setLostitem(String lostitem) {
        this.lostitem = lostitem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

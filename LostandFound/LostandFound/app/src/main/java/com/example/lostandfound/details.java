package com.example.lostandfound;

public class details {
    private String lostitem,description,owner,imagepath;
    private boolean permission;


    public details(String lostitem, String description,String owner, String imagepath) {
        this.lostitem = lostitem;
        this.description = description;
        this.owner = owner;
        this.imagepath = imagepath;
        this.permission = permission;
    }


    public details() {
    }




    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
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

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

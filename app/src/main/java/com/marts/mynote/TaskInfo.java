package com.marts.mynote;

public class TaskInfo {
    public String tName;
    public String tDate;
    public String key;

    public TaskInfo(String tName, String tDate,String key) {
        this.tName = tName;
        this.tDate = tDate;
        this.key = key;
    }

    public String getTName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }
    public String toString(){
        return this.tName+" : "+this.tDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}


package com.marts.mynote;

public class Task {
    public String tName;
    public String tDate;

    public Task(String tName, String tDate) {
        this.tName = tName;
        this.tDate = tDate;
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
}


package com.marts.mynote;

public class User {
    String Email;
    String Name;
    String fCode;
    public User(){

    }
    public User(String Email, String Name, String fCode){
        this.Email = Email;
        this.Name  = Name;
        this.fCode = fCode;

    }

    public String getName(){
        return Name;
    }
    public String getfCode(){
        return fCode;
    }
}

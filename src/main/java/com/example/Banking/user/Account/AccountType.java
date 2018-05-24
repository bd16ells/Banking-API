package com.example.Banking.user.Account;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AccountType {
    SAVINGS,
    CHECKING;

//    private String shortname;
//
//    AccountType(String shortname){
//        this.shortname = shortname;
//    }
//
//    @Override
//    public String toString(){
//        return shortname;
//    }
//    @JsonCreator
//    public static AccountType fromText(String text){
//        if(text == null) throw new IllegalArgumentException();
//        if(text.equals("s")){
//            return SAVINGS;
//        }
//        else return CHECKING;
//
//    }
}

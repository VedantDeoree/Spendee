package com.example.spend;

public class Data {

    String item,date,id,note,itemNday,itemNweek,itemNmonth;
    int amount,month,week;

    public Data() {

    }

    public Data(String item, String date, String id, String note, String itemNday, String itemNweek, String itemNmonth, int amount, int month, int week) {
        this.item = item;
        this.date = date;
        this.id = id;
        this.note = note;
        this.itemNday = itemNday;
        this.itemNweek = itemNweek;
        this.itemNmonth = itemNmonth;
        this.amount = amount;
        this.month = month;
        this.week = week;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getItemNday() {
        return itemNday;
    }

    public void setItemNday(String itemNday) {
        this.itemNday = itemNday;
    }

    public String getItemNweek() {
        return itemNweek;
    }

    public void setItemNweek(String itemNweek) {
        this.itemNweek = itemNweek;
    }

    public String getItemNmonth() {
        return itemNmonth;
    }

    public void setItemNmonth(String itemNmonth) {
        this.itemNmonth = itemNmonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}

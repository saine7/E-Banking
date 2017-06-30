package com.gayetech.alagiesaine.e_banking;

//model class for recycler view
public class RecyclerItem {
    private String account_no;
    private double balance;

    public RecyclerItem(String account_no, double balance) {
        this.account_no = account_no;
        this.balance = balance;
    }

    public String getAccount_no() {
        return account_no;
    }

    public double getBalance() {
        return balance;
    }
}

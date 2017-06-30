package com.gayetech.alagiesaine.e_banking;

import android.content.Context;

import java.util.ArrayList;

public class TransactionsHelper {

    private Context ctx;
    private TransactionsTableViewModel t;
    String[] header = {"Sender","Receiver","Amount","Date"};
    String[][] data;

    public TransactionsHelper(Context ctx) {
        this.ctx = ctx;
    }
    public String[] returnHeader(){
        return header;
    }
    public String[][] returnData(ArrayList<TransactionsTableViewModel> transactions){
        data = new String[transactions.size()][4];
        for (int i=0; i<transactions.size(); i++){
            t = transactions.get(i);
            data[i][0] = t.getSender();
            data[i][1] = t.getReceiver();
            data[i][2] = t.getAmount();
            data[i][3] = t.getDate();
        }
        return data;
    }
}

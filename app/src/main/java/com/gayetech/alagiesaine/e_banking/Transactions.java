package com.gayetech.alagiesaine.e_banking;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Transactions extends AppCompatActivity {
    TableView<String[]> tableView;
    String[][] data;
    TransactionsTableViewModel t;
    SimpleTableDataAdapter adapter;
    ProgressDialog progessDialog;
    boolean portraitMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        progessDialog = new ProgressDialog(this);
        progessDialog.setMessage("Loading transactions...");
        progessDialog.show();
        populateTable(tableView);

        TransactionsHelper tH = new TransactionsHelper(this);

        tableView = (TableView<String[]>) findViewById(R.id.transactionTableView);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getApplicationContext(), tH.returnHeader()));
        tableView.setColumnCount(4);
        portraitMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;


    }


    public void populateTable(final TableView tb) {
        final ArrayList<TransactionsTableViewModel> ttList = new ArrayList<>();

        AndroidNetworking.post(Constants.TRANSACTIONS_URL)
                .addBodyParameter("sender_account_no",SharedPrefManager.getAccountNo())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progessDialog.dismiss();
                        if (portraitMode)
                            Toast.makeText(getApplicationContext(),"Make your device on landscape mode to see full detail of transactions",Toast.LENGTH_LONG).show();
                        JSONObject obj;
                        TransactionsTableViewModel ttModel;

                        try {
                        for (int i = 0; i < response.length(); i++) {

                            obj = response.getJSONObject(i);
                            String sender = obj.getString("sender_account_no");
                            String receiver = obj.getString("receiver_account_no");
                            String amountSend = obj.getString("amount");
                            String date = obj.getString("date_send");

                            ttModel = new TransactionsTableViewModel();
                            ttModel.setSender(sender);
                            ttModel.setReceiver(receiver);
                            ttModel.setAmount("D" + amountSend);
                            ttModel.setDate(date);
                            ttList.add(ttModel);
                        }

                            adapter = new SimpleTableDataAdapter(getApplicationContext(),new TransactionsHelper(getApplicationContext()).returnData(ttList));
                            tableView.setDataAdapter(adapter);
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        progessDialog.dismiss();
                        anError.printStackTrace();
                        Toast.makeText(getApplicationContext(),"You yet to make any transaction",Toast.LENGTH_LONG).show();
                    }
                });
    }
}

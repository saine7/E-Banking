package com.gayetech.alagiesaine.e_banking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class TransferBetweenAccounts extends AppCompatActivity {
    EditText fromAccountTransfer,toAccountTransfer,amountTransfer;
    Button makeTransferButton;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_between_accounts);

        fromAccountTransfer = (EditText) findViewById(R.id.fromAccountTransfer);
        toAccountTransfer = (EditText) findViewById(R.id.toAccountTransfer);
        amountTransfer = (EditText) findViewById(R.id.amountTransfer);
        makeTransferButton = (Button) findViewById(R.id.makeTransferButton);
        progressDialog = new ProgressDialog(TransferBetweenAccounts.this);
        progressDialog.setMessage("Processing transfer...");

        fromAccountTransfer.setText(SharedPrefManager.getAccountNo());
        fromAccountTransfer.setEnabled(false);

        makeTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String senderAccountNo = fromAccountTransfer.getText().toString().trim();
                final String receiverAccountNo = toAccountTransfer.getText().toString().trim();
                final String amount = amountTransfer.getText().toString().trim();
                //create a dialog builder for transfer approval

                if(senderAccountNo.length() == 0 || receiverAccountNo.length() == 0 || amount.length() == 0){
                    Toast.makeText(getApplicationContext(),"You have to fill in all fields to make a transfer",Toast.LENGTH_LONG).show();
                }else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(TransferBetweenAccounts.this);
                    builder.setTitle("Transfer confirmation");
                    builder.setMessage("You are about to transfer D" + amount + " to Account No. " +
                            receiverAccountNo + ". Are you sure you want to proceed?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"Transfer cancelled",Toast.LENGTH_LONG).show();
                            //set transfer fields empty
                            toAccountTransfer.setText("");
                            amountTransfer.setText("");
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //run string request if user approve transfer
                            progressDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.TRANSFER_BETWEEN_ACCOUNTS_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            progressDialog.dismiss();
                                            //set transfer fields empty
                                            toAccountTransfer.setText("");
                                            amountTransfer.setText("");
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                if (!obj.getBoolean("error")){
                                                    //logic error here
                                                    //SharedPrefManager.setBalance(obj.getInt("balance"));
                                                    Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(getApplicationContext(),UserProfile.class));
                                                    finish();
                                                    return;
                                                }else {
                                                    Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Fail to connect. Please check your network connection and try again",Toast.LENGTH_LONG).show();
                                    //set transfer fields empty
                                    toAccountTransfer.setText("");
                                    amountTransfer.setText("");
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<>();
                                    params.put("fromAccountTransfer",senderAccountNo);
                                    params.put("toAccountTransfer",receiverAccountNo);
                                    params.put("amountTransfer",amount);
                                    return params;
                                }
                            };
                            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                        }
                    });
                    //show alert alert dialog
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                }


        });
    }
}

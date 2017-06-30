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

public class DeleteAccount extends AppCompatActivity {
    EditText delete_customer_no,delete_passcode;
    Button deleteAccountButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        delete_customer_no = (EditText) findViewById(R.id.delete_customer_no);
        delete_passcode = (EditText) findViewById(R.id.delete_passcode);
        deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);
        progressDialog = new ProgressDialog(DeleteAccount.this);
        progressDialog.setMessage("Deleting account...");

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String customer_no = delete_customer_no.getText().toString().trim();
                final String passcode = delete_passcode.getText().toString().trim();

                //create a dialog builder for transfer approval
                final AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAccount.this);
                builder.setTitle("Account deletion");
                builder.setMessage("Are you sure you want to delete your account? A deleted account can never be recovered");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Account deletion cancelled",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();

                        //execute string request if account deletion confirm
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DELETE_ACCOUNT_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            if (!obj.getBoolean("error")){
                                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                                                SharedPrefManager.userLogout();
                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                finish();

                                            }else {
                                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Fail to connect. Please check your network connection and try again",Toast.LENGTH_LONG).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("customer_no",customer_no);
                                params.put("passcode",passcode);
                                return params;
                            }
                        };
                        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}

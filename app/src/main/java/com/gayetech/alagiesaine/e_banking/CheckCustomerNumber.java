
package com.gayetech.alagiesaine.e_banking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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


public class CheckCustomerNumber extends Activity {
    private Button proccedButton;
    private EditText checkCustomerNoField;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check_customer_number);

        if (SharedPrefManager.getInstance(getApplicationContext()).isUserLogin()){
            startActivity(new Intent(getApplicationContext(),UserProfile.class));
            finish();
            return;
        }

        proccedButton = (Button) findViewById(R.id.proccedButton);
        checkCustomerNoField = (EditText) findViewById(R.id.checkCustomerNoField);
        progressDialog = new ProgressDialog(CheckCustomerNumber.this);
        progressDialog.setMessage("Checking if customer number exist...");
        proccedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String customer_no = checkCustomerNoField.getText().toString().trim();
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CHECK_CUSTOMER_NUMBER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (!obj.getBoolean("error")){
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),CreateAccount.class));
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
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("customer_no",customer_no);
                        return params;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
}

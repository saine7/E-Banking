package com.gayetech.alagiesaine.e_banking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class CreateAccount extends Activity {
    EditText full_name,address,tel_no,email,customer_no,pass,conf_passcode;
    Button finishCreateAccountButton;
    RadioGroup sexRadioGroup;
    RadioButton maleButton,femaleButton;
    ProgressDialog progressDialog;
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_account);

        if (SharedPrefManager.getInstance(getApplicationContext()).isUserLogin()){
            startActivity(new Intent(getApplicationContext(),UserProfile.class));
            finish();
            return;
        }

        full_name = (EditText) findViewById(R.id.full_name);
        address = (EditText) findViewById(R.id.address);
        tel_no = (EditText) findViewById(R.id.tel_no);
        email = (EditText) findViewById(R.id.email);
        customer_no = (EditText) findViewById(R.id.customer_no);
        pass = (EditText) findViewById(R.id.password);
        conf_passcode = (EditText) findViewById(R.id.conf_passcode);
        sexRadioGroup = (RadioGroup) findViewById(R.id.sexRadioGroup);
        maleButton = (RadioButton) findViewById(R.id.maleButton);
        femaleButton = (RadioButton) findViewById(R.id.femaleButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account...");
        finishCreateAccountButton = (Button) findViewById(R.id.finishCreateAccountButton);



        finishCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                //retrieve string values
                final String name = full_name.getText().toString().toUpperCase().trim();
                final String phone_no = tel_no.getText().toString().trim();
                final String user_address = address.getText().toString().toUpperCase().trim();
                final String email_address = email.getText().toString().trim();
                final String customer_number = customer_no.getText().toString().trim();
                final String passcode = pass.getText().toString().trim();
                final String confirmed_passcode = conf_passcode.getText().toString().trim();

                int checkedSex = sexRadioGroup.getCheckedRadioButtonId();

                if (checkedSex == R.id.maleButton){
                    gender = "male";
                }else if (checkedSex == R.id.femaleButton){
                    gender = "female";
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ACCOUNT_REGISTRATION_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (!obj.getBoolean("error")){
                                        Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
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

                        params.put("customer_no",customer_number);
                        params.put("full_name",name);
                        params.put("address",user_address);
                        params.put("sex",gender);
                        params.put("tel_no",phone_no);
                        params.put("email",email_address);
                        params.put("passcode",passcode);
                        params.put("con_passcode",confirmed_passcode);
                        return params;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        });
    }
}

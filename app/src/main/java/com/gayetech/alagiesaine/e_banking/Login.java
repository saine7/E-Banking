package com.gayetech.alagiesaine.e_banking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class Login extends Activity {
    Button signinButton;
    EditText passcode;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(getApplicationContext()).isUserLogin()){
            startActivity(new Intent(getApplicationContext(),UserProfile.class));
            finish();
            return;
        }

        //initialize views
        passcode = (EditText) findViewById(R.id.passcode);
        progressDialog = new ProgressDialog(this);
        signinButton = (Button) findViewById(R.id.signinButton);

        progressDialog.setMessage("Signing in...");
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final String passcode_num = passcode.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (!obj.getBoolean("error")){

                                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                                String.valueOf(obj.getString("account_no")),obj.getString("email"),obj.getString("full_name"),obj.getInt("balance"));
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
                        params.put("passcode",passcode_num);
                        return params;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
}
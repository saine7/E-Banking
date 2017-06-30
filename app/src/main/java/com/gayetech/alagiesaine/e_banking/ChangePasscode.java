package com.gayetech.alagiesaine.e_banking;

import android.app.ProgressDialog;
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

import static com.gayetech.alagiesaine.e_banking.Constants.CHANGE_PASSCODE_URL;

public class ChangePasscode extends AppCompatActivity {
    EditText old_passcode,new_passcode,con_new_passcode;
    Button changePasscodeButton;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passcode);


        old_passcode = (EditText) findViewById(R.id.old_passcode);
        new_passcode = (EditText) findViewById(R.id.new_passcode);
        con_new_passcode = (EditText) findViewById(R.id.con_new_passcode);
        changePasscodeButton = (Button) findViewById(R.id.changePasscodeButton);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Changing passcode...");

        changePasscodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String old = old_passcode.getText().toString().trim();
                final String new_pass = new_passcode.getText().toString().trim();
                final String con_new = con_new_passcode.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, CHANGE_PASSCODE_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
                                //clear password fields
                                old_passcode.setText("");
                                new_passcode.setText("");
                                con_new_passcode.setText("");
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
                        params.put("old_passcode",old);
                        params.put("new_passcode",new_pass);
                        params.put("con_new_passcode",con_new);
                        return params;
                    }
                };
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }

        });
    }
}

package com.gayetech.alagiesaine.e_banking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {
    Button loginButton,createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(this).isUserLogin()){
            startActivity(new Intent(getApplicationContext(),UserProfile.class));
            finish();
            return;
        }

        loginButton = (Button) findViewById(R.id.signinButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CheckCustomerNumber.class));
            }
        });
    }
}

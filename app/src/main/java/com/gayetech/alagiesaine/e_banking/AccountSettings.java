package com.gayetech.alagiesaine.e_banking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AccountSettings extends AppCompatActivity {
    Button changePasscode,delete_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        changePasscode = (Button) findViewById(R.id.changePasscode);
        delete_account = (Button) findViewById(R.id.delete_account);

        changePasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ChangePasscode.class));
                finish();
                return;
            }
        });
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DeleteAccount.class));
            }
        });
    }
}

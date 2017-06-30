package com.gayetech.alagiesaine.e_banking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<RecyclerItem> list;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (!SharedPrefManager.isUserLogin()){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return;
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        for (int i=0; i<1; i++){
            list.add(new RecyclerItem(SharedPrefManager.getAccountNo(),SharedPrefManager.getBalance()));
        }
        recyclerAdapter = new RecyclerAdapter(list,this);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int seletedItem = item.getItemId();
        if (seletedItem == R.id.logout){
            SharedPrefManager.getInstance(getApplicationContext()).userLogout();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return true;
        }else if (seletedItem == R.id.account_settings){
            startActivity(new Intent(getApplicationContext(),AccountSettings.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

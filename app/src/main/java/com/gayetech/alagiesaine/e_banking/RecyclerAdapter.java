package com.gayetech.alagiesaine.e_banking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

//class to adapt the recycler view
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private List<RecyclerItem> listItem;
    private Context context;

    public RecyclerAdapter(List<RecyclerItem> listAdapter, Context context) {
        this.listItem = listAdapter;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        RecyclerItem recyclerItem = listItem.get(position);
        holder.recycleView_accountNo.setText(String.valueOf(recyclerItem.getAccount_no()));
        holder.recycleView_balance.setText("D" + String.valueOf(recyclerItem.getBalance()));
        holder.recycleView_optionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.recycleView_optionDigit);
                popupMenu.inflate(R.menu.recyler_option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.transfer_btw_accounts:
                                context.startActivity(new Intent(context,TransferBetweenAccounts.class));
                                break;
                            case R.id.transactions:
                                context.startActivity(new Intent(context,Transactions.class));
                                break;
                            default:
                                Toast.makeText(context,"Unknown action",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return listItem.size();
    }

    //inner class
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView recycleView_accountNo;
        TextView recycleView_balance;
        TextView recycleView_optionDigit;

        //initialize instance variables inside constructor
        public ViewHolder(View itemView) {
            super(itemView);
            recycleView_accountNo = (TextView) itemView.findViewById(R.id.recycleView_accountNo);
            recycleView_balance = (TextView) itemView.findViewById(R.id.recycleView_balance);
            recycleView_optionDigit = (TextView) itemView.findViewById(R.id.recycleView_optionDigit);
        }
    }
}

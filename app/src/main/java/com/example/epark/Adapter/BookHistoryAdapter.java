package com.example.epark.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epark.Model.ParkSlot;
import com.example.epark.R;
import com.example.epark.USerParkingSlotBookingActivity;

import java.util.ArrayList;

public class BookHistoryAdapter extends RecyclerView.Adapter<BookHistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<ParkSlot> list;
    String activity;

    public BookHistoryAdapter(Context context, ArrayList<ParkSlot> list, String activity){
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user_booked_history, parent, false);
        return new BookHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkSlot parkSlot = list.get(position);
        holder.book_address.setText(parkSlot.getAddress());
        holder.book_date.setText(parkSlot.getBooking_date());

        if(activity.equals("UserUnVerifiedBookingSlotFragment")){
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Not Verified Yet!!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(activity.equals("UserVerifiedBookingSlotFragment")){
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.get_invoice_dialog);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);

                    TextView confirm = dialog.findViewById(R.id.download_invoice);
                    TextView cancel = dialog.findViewById(R.id.cancel_invoice);

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // getInvoice download
                            dialog.dismiss();
                        }
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView book_address, book_date;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            book_address = itemView.findViewById(R.id.book_address);
            book_date = itemView.findViewById(R.id.book_date);
            linearLayout = itemView.findViewById(R.id.linear_layout_1);

        }
    }
}

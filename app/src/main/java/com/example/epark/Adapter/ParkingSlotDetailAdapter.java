package com.example.epark.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epark.AddParkingActivity;
import com.example.epark.Model.ParkSlot;
import com.example.epark.OwnerCustomerListActivity;
import com.example.epark.R;
import com.example.epark.USerParkingSlotBookingActivity;

import java.util.ArrayList;

public class ParkingSlotDetailAdapter extends RecyclerView.Adapter<ParkingSlotDetailAdapter.ViewHolder>{

    ArrayList<ParkSlot> list;
    Context context;
    String activity;

    public ParkingSlotDetailAdapter(Context context, ArrayList<ParkSlot> list, String activity){
        this.context = context;
        this.list = list;
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_parking_detail, parent, false);
        return new ParkingSlotDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkSlot parkSlot = list.get(position);
        holder.address.setText(parkSlot.getAddress());
        String total_available_slot = String.valueOf(Integer.parseInt(parkSlot.getAvailable_2_wheeler_slot()) + Integer.parseInt(parkSlot.getAvailable_4_wheeler_slot()));
        holder.totalAvailableSlots.setText(total_available_slot);
        holder.bikeSLots.setText(parkSlot.getAvailable_2_wheeler_slot());
        holder.carSlots.setText(parkSlot.getAvailable_4_wheeler_slot());

        if(activity.equals("OwnerHomeFragment")){
            holder.edit_park.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, holder.edit_park);
                    popup.inflate(R.menu.park_slot_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.park_slot_edit:
                                    Intent i = new Intent(context, AddParkingActivity.class);
                                    i.putExtra("selectedAddress", parkSlot.getAddress());
                                    i.putExtra("latitude", parkSlot.getLatitude());
                                    i.putExtra("longitude", parkSlot.getLongitude());
                                    i.putExtra("2_wheeler_slot", parkSlot.getTotal_2_wheeler_slot());
                                    i.putExtra("4_wheeler_slot", parkSlot.getTotal_4_wheeler_slot());
                                    i.putExtra("2_wheeler_slot_rate", parkSlot.getRate_2_wheelerPerHrs());
                                    i.putExtra("4_wheeler_slot_rate", parkSlot.getRate_4_wheelerPerHrs());
                                    i.putExtra("upiId", parkSlot.getUpi_Id());
                                    i.putExtra("city", parkSlot.getCity());
                                    i.putExtra("Landmark", parkSlot.getLandmark());
                                    i.putExtra("state", parkSlot.getState());
                                    i.putExtra("activity", "OwnerHomeFragment");
                                    i.putExtra("adapterId", parkSlot.getAdapterId());
                                    context.startActivity(i);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                }
            });
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, OwnerCustomerListActivity.class);
                    i.putExtra("adapterId", parkSlot.getAdapterId());
                    context.startActivity(i);
                }
            });

        }

        if(activity.equals("UserHomeFragment")){
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, USerParkingSlotBookingActivity.class);
                    i.putExtra("OwnerId", parkSlot.getOwnerId());
                    i.putExtra("adapterId", parkSlot.getAdapterId());
                    i.putExtra("selectedAddress", parkSlot.getAddress());
                    i.putExtra("latitude", parkSlot.getLatitude());
                    i.putExtra("longitude", parkSlot.getLongitude());
                    i.putExtra("2_wheeler_slot_rate", parkSlot.getRate_2_wheelerPerHrs());
                    i.putExtra("4_wheeler_slot_rate", parkSlot.getRate_4_wheelerPerHrs());
                    i.putExtra("total_2_wheeler_slots", parkSlot.getTotal_2_wheeler_slot());
                    i.putExtra("total_4_wheeler_slots", parkSlot.getTotal_4_wheeler_slot());
                    i.putExtra("upiId", parkSlot.getUpi_Id());
                    context.startActivity(i);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void Filteredlist(ArrayList<ParkSlot> filterList) {
        list = filterList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView address, distance, totalAvailableSlots, bikeSLots, carSlots;
        ImageView edit_park;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.s_address);
            distance = itemView.findViewById(R.id.s_distance);
            totalAvailableSlots = itemView.findViewById(R.id.s_slots_available);
            bikeSLots = itemView.findViewById(R.id.s_bike_slot);
            carSlots = itemView.findViewById(R.id.s_car_slot);
            edit_park = itemView.findViewById(R.id.menu_1);
            layout = itemView.findViewById(R.id.sample_park_slot_layout);
        }
    }




}

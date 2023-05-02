package com.example.epark.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epark.Model.ParkSlot;
import com.example.epark.Model.Person;
import com.example.epark.Notifications.ApiUtilities;
import com.example.epark.Notifications.NotificationData;
import com.example.epark.Notifications.PushNotification;
import com.example.epark.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {

    Context context;
    ArrayList<ParkSlot> list;
    String activity;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public CustomerListAdapter(Context context, ArrayList<ParkSlot> list, String activity){
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_customer_list, parent, false);
        return new CustomerListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.imageView.getDrawable(R.drawable.user);
        ParkSlot parkSlot = list.get(position);
        holder.booking_date.setText(parkSlot.getBooking_date());
        holder.name.setText(parkSlot.getCustomer_name());
        holder.contact_no.setText(parkSlot.getCustomer_phone());

        if(parkSlot.getVehicle_selected().equals("Bike")){
            holder.imageView.setImageResource(R.drawable.motorcycle);
        }else{
            holder.imageView.setImageResource(R.drawable.electriccar);
        }

        if(parkSlot.getIsVerified().equals("Yes")) holder.is_verify.setImageResource(R.drawable.baseline_verified_24);
        else holder.is_verify.setImageResource(R.drawable.delete_button);

        if(parkSlot.getIsVerified().equals("Yes")){
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if(parkSlot.getIsVerified().equals("No")){
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Customer Verification");
                    builder.setMessage("Verify the Customer?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String slot = parkSlot.getBookedSlot();
                           // Log.e("TAG", " j " + slot.charAt(0) + slot.charAt(1) + slot.charAt(2) + slot.charAt(3) + slot.charAt(4));
                            for(int i=4; i<slot.length(); i++){
                                String time = "";
                                while(slot.charAt(i) != '$'){
                                    time = time + slot.charAt(i);
                                    i++;
                                }
                                ParkSlot parkSlot1 = new ParkSlot(auth.getCurrentUser().getUid());
                                Log.e("TAG", parkSlot.getOwnerId() + " "+ parkSlot.getAdapterId() + " "+ parkSlot.getBooking_date() + " "+ parkSlot.getVehicle_selected() );
                                database.getReference().child("Parking Slots Details").child(parkSlot.getOwnerId()).child(parkSlot.getAdapterId())
                                        .child("Booking User List").child(parkSlot.getBooking_date())
                                        .child(parkSlot.getVehicle_selected()).child(time).push().setValue(parkSlot1);
                            }

                            HashMap<String, Object> obj = new HashMap<>();
                            obj.put("isVerified", "Yes");
                            database.getReference().child("Parking Slots Details").child(parkSlot.getOwnerId()).child(parkSlot.getAdapterId())
                                    .child("Customer List").child(parkSlot.getCustomerId()).child(parkSlot.getCustomer_adapter())
                                    .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("Users").child(parkSlot.getCustomerId())
                                                    .child("Booked History").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                                                ParkSlot parkSlot3 = dataSnapshot.getValue(ParkSlot.class);
                                                                if(parkSlot3.getAdapterId().equals(parkSlot.getAdapterId())){
                                                                    Log.e("TAG", parkSlot3.getAdapterId() + " " + parkSlot.getAdapterId() + " " + dataSnapshot.getKey());
                                                                    database.getReference().child("Users").child(parkSlot.getCustomerId())
                                                                            .child("Booked History").child(dataSnapshot.getKey()).updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
//                                                                                    String titletxt = "Parking Slot Confirmation";
//                                                                                    String msgTxt = "Congrats!! your Slot has been booked";
//                                                                                    Log.e("TAG", parkSlot.getCustomerId());
//                                                                                    PushNotification notification = new PushNotification(new NotificationData(titletxt, msgTxt), "/topics/" + parkSlot.getCustomerId());
//                                                                                    sendNotification(notification);
                                                                                    generateInvoice();
                                                                                    dialog.dismiss();
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void generateInvoice(){



    }

//    private void sendNotification(PushNotification notification) {
//
//        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
//            @Override
//            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
//                if(response.isSuccessful()){
//                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PushNotification> call, Throwable t) {
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView, is_verify;
        TextView name, contact_no, booking_date;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.is_verified);
            name = itemView.findViewById(R.id.customer_name);
            contact_no = itemView.findViewById(R.id.customer_phone_no);
            booking_date = itemView.findViewById(R.id.customer_book_date);
            is_verify = itemView.findViewById(R.id.is_verify);
            layout = itemView.findViewById(R.id.linear_layout_1);

        }
    }
}

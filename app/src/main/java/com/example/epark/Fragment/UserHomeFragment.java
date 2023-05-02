package com.example.epark.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.epark.Adapter.DistrictDetailAdapter;
import com.example.epark.Adapter.ParkingSlotDetailAdapter;
import com.example.epark.Adapter.StateDetailAdapter;
import com.example.epark.Model.DistrictListDetail;
import com.example.epark.Model.ParkSlot;
import com.example.epark.Model.Person;
import com.example.epark.Model.StateListDetail;
import com.example.epark.R;
import com.example.epark.USerParkingSlotBookingActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserHomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String STATE_DETAIL_REQUEST_URL = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
    private static final String BASE_DISTRICT_DETAIL_REQUEST_URL = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/";
    private ArrayList<StateListDetail> state_list;
    private ArrayList<DistrictListDetail> district_list;
    private StateDetailAdapter madapter;
    private DistrictDetailAdapter madapter2;
    String STATE_ID;
    String DISTRICT_ID;
    RecyclerView recyclerView;
    ParkingSlotDetailAdapter adapter;
    ArrayList<ParkSlot> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    ImageView filter;
    Spinner state_spinner, city_spinner;
    String selected_state, selected_city;
    private MaterialSearchBar materialSearchBar;

    public UserHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        materialSearchBar = view.findViewById(R.id.searchBar2);

        state_list = new ArrayList<>();
        district_list = new ArrayList<>();

        fetchParkPlace();

        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFilter();
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = view.findViewById(R.id.recycler_view_2);
        adapter = new ParkingSlotDetailAdapter(getActivity(), list, "UserHomeFragment");
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;

    }

    private void setFilter() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.filter_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        state_spinner = dialog.findViewById(R.id.select_state);
        city_spinner = dialog.findViewById(R.id.select_city);
        TextView confirm = dialog.findViewById(R.id.confirm1);
        TextView cancel = dialog.findViewById(R.id.cancel1);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATE_DETAIL_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject baseJsonResponse = new JSONObject(response);
                    JSONArray stateArray = baseJsonResponse.getJSONArray("states");
                    for(int i=0;i<stateArray.length();i++){
                        JSONObject currentStateDetail = stateArray.getJSONObject(i);
                        String stateName = currentStateDetail.getString("state_name");
                        int stateId = currentStateDetail.getInt("state_id");

                        Log.e("TAG",stateName);

                        StateListDetail s = new StateListDetail(stateName,stateId);
                        state_list.add(s);
                        Log.e("TAG",stateName+stateId);
                    }

                    madapter = new StateDetailAdapter(getContext(),state_list);
                    state_spinner.setAdapter(madapter);

                    state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                            if(adapterView.getItemAtPosition(position).equals("Select State Name")){

                            }else{
                                StateListDetail clickedItem = (StateListDetail)adapterView.getItemAtPosition(position);
                                String STATE_NAME = clickedItem.getStateName();
                                selected_state = clickedItem.getStateName();
                                STATE_ID = String.valueOf(clickedItem.getStateId());
                                Log.e("TAG",STATE_NAME+STATE_ID);

                                fetchDistrictId(STATE_ID);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error while parsing",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), selected_state + " " + selected_city , Toast.LENGTH_SHORT).show();
                filter1(selected_city);
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

    private void fetchParkPlace() {

        database.getReference().child("Parking Slots Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    database.getReference().child("Parking Slots Details").child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                                ParkSlot parkSlot = dataSnapshot1.getValue(ParkSlot.class);
                                parkSlot.setAdapterId(dataSnapshot1.getKey());
                                parkSlot.setOwnerId(key);
                                list.add(parkSlot);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case(R.id.select_state):
                selected_state = parent.getItemAtPosition(position).toString();
                break;
            case(R.id.select_city):
                selected_city = parent.getItemAtPosition(position).toString();
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void fetchDistrictId(String state_id) {

        String STATE_ID1=state_id;
        String DISTRICT_URL = BASE_DISTRICT_DETAIL_REQUEST_URL + STATE_ID1;

        Log.e("TAG", DISTRICT_URL);

        StringRequest request2 = new StringRequest(Request.Method.GET, DISTRICT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                district_list.clear();

                try{
                    Log.e("TAG", response);

                    JSONObject baseJsonResponse = new JSONObject(response);
                    JSONArray districtArray = baseJsonResponse.getJSONArray("districts");
                    for(int i=0;i<districtArray.length();i++){
                        JSONObject currentDistrictDetail = districtArray.getJSONObject(i);
                        String districtName = currentDistrictDetail.getString("district_name");
                        int districtId = currentDistrictDetail.getInt("district_id");

                        Log.e("TAG", districtName+districtId);

                        DistrictListDetail d = new DistrictListDetail(districtName,districtId);
                        district_list.add(d);

                    }
                    madapter2 = new DistrictDetailAdapter(getContext(),district_list);
                    city_spinner.setAdapter(madapter2);

                    city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            if(adapterView.getItemAtPosition(position).equals("Select District Name")){

                            }else{
                                DistrictListDetail clickedItem = (DistrictListDetail)adapterView.getItemAtPosition(position);
                                String DISTRICT_NAME = clickedItem.getDistrictName();
                                selected_city = clickedItem.getDistrictName();
                                DISTRICT_ID = String.valueOf(clickedItem.getDistrictId());
                                Log.e("TAG",DISTRICT_NAME+DISTRICT_ID);

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"error while parsing",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        queue2.add(request2);
    }

    private void filter(String text){
        ArrayList<ParkSlot> filterList = new ArrayList<>();
        for(ParkSlot parkSlot: list){
            if(parkSlot.getAddress().toLowerCase().contains(text.toLowerCase())){
                filterList.add(parkSlot);
            }
        }
        adapter.Filteredlist(filterList);

    }

    private void filter1(String text){
        ArrayList<ParkSlot> filterList = new ArrayList<>();
        for(ParkSlot parkSlot: list){
            if(parkSlot.getCity().toLowerCase().contains(text.toLowerCase())){
                filterList.add(parkSlot);
            }
        }
        adapter.Filteredlist(filterList);

    }

}
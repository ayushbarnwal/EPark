package com.example.epark.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.epark.Model.StateListDetail;
import com.example.epark.R;

import java.util.ArrayList;

public class StateDetailAdapter extends ArrayAdapter<StateListDetail> {
    public StateDetailAdapter(Context context, ArrayList<StateListDetail> state_list) {
        super(context, 0,state_list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return inItView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return inItView(position, convertView, parent);
    }

    private View inItView(int position,View convertView,ViewGroup parent){

        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.state_list,parent,false);
        }

        TextView stateName = convertView.findViewById(R.id.mstate_name);
        TextView stateId = convertView.findViewById(R.id.mstate_id);

        StateListDetail statedetail = getItem(position);

        if(convertView!=null){
        stateName.setText(statedetail.getStateName());
        stateId.setText(String.valueOf(statedetail.getStateId()));
        stateId.setVisibility(View.GONE);}

        return convertView;

    }
}

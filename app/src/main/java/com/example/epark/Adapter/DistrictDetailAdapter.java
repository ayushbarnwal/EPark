package com.example.epark.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.epark.Model.DistrictListDetail;
import com.example.epark.R;

import java.util.ArrayList;

public class DistrictDetailAdapter extends ArrayAdapter<DistrictListDetail> {
    public DistrictDetailAdapter(Context context, ArrayList<DistrictListDetail> district_list) {
        super(context, 0,district_list);
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
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.district_list,parent,false);
        }

        TextView districtName = convertView.findViewById(R.id.mdistrict_name);
        TextView districtId = convertView.findViewById(R.id.mdistrict_id);

        DistrictListDetail districtdetail = getItem(position);

        if(convertView!=null){
            districtName.setText(districtdetail.getDistrictName());
            districtId.setText(String.valueOf(districtdetail.getDistrictId()));
            districtId.setVisibility(View.GONE);}

        return convertView;

    }
}

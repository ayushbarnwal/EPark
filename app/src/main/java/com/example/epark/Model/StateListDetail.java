package com.example.epark.Model;

public class StateListDetail {

    private String mStateName;
    private int mStateId;


    public StateListDetail(String stateName, int stateId){
        mStateName = stateName;
        mStateId = stateId;
    }

    public String getStateName(){
        return mStateName;
    }

    public int getStateId(){
        return mStateId;
    }

}

package com.example.epark.Model;

public class ParkSlot {

    public ParkSlot(){}

    String total_2_wheeler_slot;
    String total_4_wheeler_slot;
    String available_2_wheeler_slot;
    String available_4_wheeler_slot;
    String upi_Id;
    String address;
    String rate_2_wheelerPerHrs;
    String rate_4_wheelerPerHrs;
    String latitude;
    String longitude;
    String landmark;
    String city;
    String state;
    String adapterId;
    String ownerId;
    String bookedSlot;
    String customerId;
    String utr_no;
    String isVerified;
    String vehicle_selected;
    String booking_date;
    String customer_name, customer_adapter;

    public ParkSlot(String customer_name, String customer_phone, String customer_adapter) {
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.customer_adapter = customer_adapter;
    }

    String customer_phone;

    public ParkSlot(String total_2_wheeler_slot, String total_4_wheeler_slot, String available_2_wheeler_slot, String available_4_wheeler_slot, String upi_Id, String address, String rate_2_wheelerPerHrs, String rate_4_wheelerPerHrs, String latitude, String longitude, String city, String landmark, String state) {
        this.total_2_wheeler_slot = total_2_wheeler_slot;
        this.total_4_wheeler_slot = total_4_wheeler_slot;
        this.available_2_wheeler_slot = available_2_wheeler_slot;
        this.available_4_wheeler_slot = available_4_wheeler_slot;
        this.upi_Id = upi_Id;
        this.address = address;
        this.rate_2_wheelerPerHrs = rate_2_wheelerPerHrs;
        this.rate_4_wheelerPerHrs = rate_4_wheelerPerHrs;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.city = city;
        this.landmark = landmark;
    }

    public ParkSlot(String utr_no, String isVerified, String bookedSlot, String vehicle_selected, String booking_date) {
        this.utr_no = utr_no;
        this.isVerified = isVerified;
        this.bookedSlot = bookedSlot;
        this.vehicle_selected = vehicle_selected;
        this.booking_date = booking_date;
    }

    public ParkSlot(String utr_no, String isVerified, String bookedSlot, String vehicle_selected, String booking_date, String ownerId, String adapterId, String address) {
        this.utr_no = utr_no;
        this.isVerified = isVerified;
        this.bookedSlot = bookedSlot;
        this.vehicle_selected = vehicle_selected;
        this.booking_date = booking_date;
        this.ownerId = ownerId;
        this.adapterId = adapterId;
        this.address = address;
    }

    public String getCustomer_adapter() {
        return customer_adapter;
    }

    public void setCustomer_adapter(String customer_adapter) {
        this.customer_adapter = customer_adapter;
    }

    public String getVehicle_selected() {
        return vehicle_selected;
    }

    public void setVehicle_selected(String vehicle_selected) {
        this.vehicle_selected = vehicle_selected;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getUtr_no() {
        return utr_no;
    }

    public void setUtr_no(String utr_no) {
        this.utr_no = utr_no;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public ParkSlot(String adapterId, String ownerId, String bookedSlot, String utr_no) {
        this.adapterId = adapterId;
        this.ownerId = ownerId;
        this.bookedSlot = bookedSlot;
        this.utr_no = utr_no;
    }

    public ParkSlot(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBookedSlot() {
        return bookedSlot;
    }

    public void setBookedSlot(String bookedSlot) {
        this.bookedSlot = bookedSlot;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(String adapterId) {
        this.adapterId = adapterId;
    }

    public String getTotal_2_wheeler_slot() {
        return total_2_wheeler_slot;
    }

    public void setTotal_2_wheeler_slot(String total_2_wheeler_slot) {
        this.total_2_wheeler_slot = total_2_wheeler_slot;
    }

    public String getTotal_4_wheeler_slot() {
        return total_4_wheeler_slot;
    }

    public void setTotal_4_wheeler_slot(String total_4_wheeler_slot) {
        this.total_4_wheeler_slot = total_4_wheeler_slot;
    }

    public String getAvailable_2_wheeler_slot() {
        return available_2_wheeler_slot;
    }

    public void setAvailable_2_wheeler_slot(String available_2_wheeler_slot) {
        this.available_2_wheeler_slot = available_2_wheeler_slot;
    }

    public String getAvailable_4_wheeler_slot() {
        return available_4_wheeler_slot;
    }

    public void setAvailable_4_wheeler_slot(String available_4_wheeler_slot) {
        this.available_4_wheeler_slot = available_4_wheeler_slot;
    }

    public String getUpi_Id() {
        return upi_Id;
    }

    public void setUpi_Id(String upi_Id) {
        this.upi_Id = upi_Id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRate_2_wheelerPerHrs() {
        return rate_2_wheelerPerHrs;
    }

    public void setRate_2_wheelerPerHrs(String rate_2_wheelerPerHrs) {
        this.rate_2_wheelerPerHrs = rate_2_wheelerPerHrs;
    }

    public String getRate_4_wheelerPerHrs() {
        return rate_4_wheelerPerHrs;
    }

    public void setRate_4_wheelerPerHrs(String rate_4_wheelerPerHrs) {
        this.rate_4_wheelerPerHrs = rate_4_wheelerPerHrs;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

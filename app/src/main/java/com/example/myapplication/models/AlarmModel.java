package com.example.myapplication.models;

public class AlarmModel {

    int id;
    String ndt;
    int numberOfSlot;
    String firstSlotTime;
    String secondSlotTime;
    String thirdSlotTime;
    int firstSlotRequestCode;
    int secondSlotRequestCode;
    int thirdSlotRequestCode;

    public AlarmModel(int id, String ndt, int numberOfSlot, String firstSlotTime, int firstSlotRequestCode) {
        this.id = id;
        this.ndt = ndt;
        this.numberOfSlot = numberOfSlot;
        this.firstSlotTime = firstSlotTime;
        this.firstSlotRequestCode = firstSlotRequestCode;

    }

    public AlarmModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNdt() {
        return ndt;
    }

    public void setNdt(String ndt) {
        this.ndt = ndt;
    }

    public int getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(int numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }

    public String getFirstSlotTime() {
        return firstSlotTime;
    }

    public void setFirstSlotTime(String firstSlotTime) {
        this.firstSlotTime = firstSlotTime;
    }

    public int getFirstSlotRequestCode() {
        return firstSlotRequestCode;
    }

    public void setFirstSlotRequestCode(int firstSlotRequestCode) {
        this.firstSlotRequestCode = firstSlotRequestCode;
    }

}

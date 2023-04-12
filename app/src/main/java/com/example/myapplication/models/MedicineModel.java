package com.example.myapplication.models;

public class MedicineModel {

    int id;
    String date;
    String medicineName;
    String medicineType;
    int numberOfSlot;
    String firstSlotTime;
    String secondSlotTime;
    String thirdSlotTime;
    int numberOfDays;
    boolean isEveryday;
    boolean isSpecificDaysOfWeek;
    boolean isDaysInterval;
    String daysNameOfWeek;
    int daysInterval;
    String startDate;
    String status;
    String medicineMeal;
    int uniqueCode;

    public MedicineModel() {
    }

    public MedicineModel(int id, String date, String medicineName,
                         int numberOfSlot, String firstSlotTime,
                         int numberOfDays, boolean isEveryday, boolean isSpecificDaysOfWeek, boolean isDaysInterval,
                         String daysNameOfWeek, int daysInterval, String startDate, String status, String medicineMeal, int uniqueCode) {
        this.id = id;
        this.date = date;
        this.medicineName = medicineName;

        this.numberOfSlot = numberOfSlot;
        this.firstSlotTime = firstSlotTime;
        this.numberOfDays = numberOfDays;
        this.isEveryday = isEveryday;
        this.isSpecificDaysOfWeek = isSpecificDaysOfWeek;
        this.isDaysInterval = isDaysInterval;
        this.daysNameOfWeek = daysNameOfWeek;
        this.daysInterval = daysInterval;
        this.startDate = startDate;
        this.status = status;
        this.medicineMeal = medicineMeal;
        this.uniqueCode = uniqueCode;



    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
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

    public String getSecondSlotTime() {
        return secondSlotTime;
    }

    public void setSecondSlotTime(String secondSlotTime) {
        this.secondSlotTime = secondSlotTime;
    }

    public String getThirdSlotTime() {
        return thirdSlotTime;
    }

    public void setThirdSlotTime(String thirdSlotTime) {
        this.thirdSlotTime = thirdSlotTime;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public boolean isEveryday() {
        return isEveryday;
    }

    public void setEveryday(boolean everyday) {
        isEveryday = everyday;
    }

    public boolean isSpecificDaysOfWeek() {
        return isSpecificDaysOfWeek;
    }

    public void setSpecificDaysOfWeek(boolean specificDaysOfWeek) {
        isSpecificDaysOfWeek = specificDaysOfWeek;
    }

    public boolean isDaysInterval() {
        return isDaysInterval;
    }

    public void setDaysInterval(boolean daysInterval) {
        isDaysInterval = daysInterval;
    }

    public String getDaysNameOfWeek() {
        return daysNameOfWeek;
    }

    public void setDaysNameOfWeek(String daysNameOfWeek) {
        this.daysNameOfWeek = daysNameOfWeek;
    }

    public int getDaysInterval() {
        return daysInterval;
    }

    public void setDaysInterval(int daysInterval) {
        this.daysInterval = daysInterval;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedicineMeal() {
        return medicineMeal;
    }

    public void setMedicineMeal(String medicineMeal) {
        this.medicineMeal = medicineMeal;
    }

    public int getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(int uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}

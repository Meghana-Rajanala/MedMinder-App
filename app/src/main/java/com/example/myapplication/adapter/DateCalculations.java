package com.example.myapplication.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateCalculations {

    String daysName;


    public String addDays(String fromDate, String noOfdays) {



        Calendar c = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);

        try {
            Date myDate = df.parse(fromDate.trim());
            c.setTime(myDate);
            c.add(Calendar.DATE, Integer.parseInt(noOfdays));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String toDate = df.format(c.getTime());
        return toDate;

    }


    public String addForHorizontalCalender(String fromDate, String noOfdays){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM - yyyy");

        try {
            Date myDate = df.parse(fromDate.trim());
            c.setTime(myDate);
            c.add(Calendar.DATE, Integer.parseInt(noOfdays));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String toDate = df.format(c.getTime());
        return toDate;

    }

    public   String subtractDays(String fromDate, String noOfdays) {

        Calendar c = Calendar.getInstance();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        try {
            Date myDate = df.parse(fromDate.trim());
            c.setTime(myDate);
            c.add(Calendar.DATE, (Integer.parseInt(noOfdays)*-1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String toDate = df.format(c.getTime());
        return toDate;

    }


    public String subForHorizontalCalender(String fromDate, String noOfdays){


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM - yyyy");


        try {
            Date myDate = df.parse(fromDate.trim());
            c.setTime(myDate);
            c.add(Calendar.DATE, (Integer.parseInt(noOfdays)*-1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String toDate = df.format(c.getTime());
        return toDate;
    }


    public String daysNameOfWeek(String inputDate){

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        try {

            Date date = df.parse(inputDate.trim());
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            daysName  = outFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return daysName;
    }


    public String compareDate(String inputDate){

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        String result = "";

        try {
            Date strDate = sdf.parse(inputDate);

            if (new Date().after(strDate)) {

                result = "big";

            }else if (new Date().before(strDate)){

                result = "small";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
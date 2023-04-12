package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.models.AlarmModel;

import java.util.ArrayList;
import java.util.List;

public class AlarmDatabase extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "alarm_manager";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "alarm_table";

    private static final String COLUMN_1 = "ID";
    private static final String COLUMN_2 = "NDT";
    private static final String COLUMN_3 = "NUMBER_OF_SLOT";
    private static final String COLUMN_4 = "FIRST_SLOT_TIME";
    private static final String COLUMN_5 = "FIRST_SLOT_RC";

    public AlarmDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE "
                + TABLE_NAME + "("
                + COLUMN_1 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COLUMN_2 + " TEXT,"
                + COLUMN_3 + " INTEGER,"
                + COLUMN_4 + " TEXT,"
                + COLUMN_5 + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_QUERY);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertAlarn(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, alarmModel.getNdt());
        values.put(COLUMN_3, alarmModel.getNumberOfSlot());
        values.put(COLUMN_4, alarmModel.getFirstSlotTime());

      // values.put(COLUMN_5, alarmModel.getFirstSlotRequestCode());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int updateAlarm(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_2, alarmModel.getNdt());
        values.put(COLUMN_3, alarmModel.getNumberOfSlot());
        values.put(COLUMN_4, alarmModel.getFirstSlotTime());
      //  values.put(COLUMN_5, alarmModel.getFirstSlotRequestCode());


        return db.update(TABLE_NAME, values, COLUMN_1 + " = ?",
                new String[]{String.valueOf(alarmModel.getId())});

    }

    public List<AlarmModel> getAllAlarmList() {
        List<AlarmModel> alarmModels = new ArrayList<>();
        String seletQuery = "Select * FROM " + TABLE_NAME;

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(seletQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setId(Integer.parseInt(cursor.getString(1)));
                alarmModel.setNdt(cursor.getColumnName(2));
                alarmModel.setNumberOfSlot(Integer.parseInt(cursor.getString(3)));
                alarmModel.setFirstSlotTime(cursor.getString(4));

           //   alarmModel.setFirstSlotRequestCode(Integer.parseInt(cursor.getString(5)));


                alarmModels.add(alarmModel);

            } while (cursor.moveToNext());
        }
        return alarmModels;
    }

    public void deletAlarm(AlarmModel alarmModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_1 + " = ?",
                new String[]{String.valueOf(alarmModel.getId())});
        db.close();
    }

    public List<AlarmModel> getSelectedAlarm(String searchKeyword) {

        List<AlarmModel> alarmModelList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE NDT=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchKeyword});

        if (cursor.moveToFirst()) {

            do {

                AlarmModel alarmModel = new AlarmModel();
                alarmModel.setId(Integer.parseInt(cursor.getString(0)));
                alarmModel.setNdt(cursor.getString(1));
                alarmModel.setNumberOfSlot(Integer.parseInt(cursor.getString(2)));
                alarmModel.setFirstSlotTime(cursor.getString(3));

             // alarmModel.setFirstSlotRequestCode(Integer.parseInt(cursor.getString(5)));



                alarmModelList.add(alarmModel);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return alarmModelList;
    }

}

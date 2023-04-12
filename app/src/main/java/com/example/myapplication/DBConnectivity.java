package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DBConnectivity extends SQLiteOpenHelper {

    private String db_path;
    private static String db_name = "Disease_Symp.db";
    private SQLiteDatabase myDatabase;
    private Context context;

    DBConnectivity(Context context) {
        super(context, db_name, null, 10);
        this.context = context;
        this.db_path = context.getDatabasePath(db_name).toString();
        Log.e("Path of Database", db_path);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                copyData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Cursor fetchAll() {
        return myDatabase.query("Disease_table", null, null, null, null, null, null);
    }
    public void createDatabase() throws IOException {
        boolean dbExists = checkDataBase();
        if (dbExists) {

        } else {
            this.getReadableDatabase();
            try {
                copyData();
            } catch (IOException e) {
                throw new Error("Error Copying Database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase db = null;
        try {
            String myPath = db_path;
            Log.e("Databse path :", db_path);
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return db != null;
    }
    private void copyData() throws IOException {
        InputStream myInput = context.getAssets().open(db_name);
        Log.e("File opened", "true");
        String outFileName = db_path + db_name;
        Log.e("File Name is ", outFileName);
        OutputStream outFile = new FileOutputStream(outFileName);

        byte[] buffer = new byte[10];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            outFile.write(buffer, 0, length);
        }
        outFile.flush();
        outFile.close();
        myInput.close();
    }
    public void openDatabase() throws SQLException {
        String myPath = db_path + db_name;
        Log.e("openDatabase(),Db path", myPath);
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    public void close() {
        if (myDatabase != null)
            myDatabase.close();
        super.close();
    }

    public ArrayList<String> getDiseases() {

        ArrayList<String> diseases = new ArrayList<>();
        try {
            Cursor c = fetchAll();
            if (c.moveToFirst()) {
                do {
                    int index = c.getColumnIndex("Disease");
                    String disease = c.getString(index);
                    diseases.add(disease);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "No Diseases Available", Toast.LENGTH_SHORT).show();
        }
        return diseases;
    }
    public ArrayList<String> getSymptoms() {
        ArrayList<String> symptoms = new ArrayList<>();
        try {
            @SuppressLint("Recycle") Cursor c = myDatabase.rawQuery("SELECT * FROM Symptoms_table", null);
            if (c.moveToFirst()) {
                do {
                    int index = c.getColumnIndex("Symptoms");
                    String symptom = c.getString(index);
                    symptoms.add(symptom);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return symptoms;
    }

    public ArrayList<String> getSymptomsFromDisease(String disease) {

        ArrayList<String> symptoms = new ArrayList<>();
        try {
            @SuppressLint("Recycle") Cursor c = myDatabase.rawQuery("SELECT Symptoms, Disease FROM Symptoms_table a INNER JOIN Map b on a.Sym_id=b.sym_id INNER JOIN Disease_table c ON b.dis_id=c.Dis_id ORDER BY Disease ", null);
            //given query
            if (c.moveToFirst()) {
                do {
                    int pos = c.getColumnIndex("Symptoms");
                    String symptom = c.getString(pos);

                    pos = c.getColumnIndex("Disease");
                    String dis = c.getString(pos);

                    if (dis.equals(disease))
                        symptoms.add(symptom);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return symptoms;
    }

    public Set<String> getCommonDiseaseFromSymptoms(ArrayList<String> symptoms) {
        Set<String> diseases = new HashSet<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT Disease, COUNT(Disease) AS Count FROM Symptoms_table a ");
            sb.append("INNER JOIN Map b ON a.Sym_id = b.sym_id ");
            sb.append("INNER JOIN Disease_table c ON b.dis_id = c.Dis_id ");
            sb.append("WHERE Symptoms IN ('" + TextUtils.join("', '", symptoms) + "') ");
            sb.append("GROUP BY Disease ");
            sb.append("HAVING COUNT(Disease) = " + symptoms.size());

            Cursor c = myDatabase.rawQuery(sb.toString(), null);
            if (c.moveToFirst()) {
                do {
                    int index = c.getColumnIndex("Disease");
                    String disease = c.getString(index);
                    diseases.add(disease);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "No Diseases Available", Toast.LENGTH_LONG).show();
        }
        return diseases;
    }

}
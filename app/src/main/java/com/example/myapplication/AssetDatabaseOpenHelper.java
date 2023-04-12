package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class AssetDatabaseOpenHelper {

    private final Context context;
     final String DB_NAME;

    public AssetDatabaseOpenHelper(Context context, String database_name) {
        this.context = context;
        this.DB_NAME = database_name;
    }

    public void saveDatabase() {

        File dbFile = context.getDatabasePath(DB_NAME);

        if (!dbFile.exists()) {
            try {
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream is = context.getAssets().open(DB_NAME);
        OutputStream os = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            os = Files.newOutputStream(dbFile.toPath());
        }

        byte[] buffer = new byte[1024];
        while (is.read(buffer) > 0) {
            assert os != null;
            os.write(buffer);
        }

        assert os != null;
        os.flush();
        os.close();
        is.close();
    }

}
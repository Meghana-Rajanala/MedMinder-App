package com.example.myapplication;

import static android.icu.lang.UCharacter.toLowerCase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResultActivity extends AppCompatActivity {
    public static String med_name;
    public static String med_time="";
    public static String med_condition="";

    TextView textView;
    Button alarm_button;
    String DB_NAME = "Drugs_related_database.db";
    String TABLE_NAME = "drugs_for_common_treatments";
    DataBaseHelper myDBHelper;
    public static String uriStr="";
    static ArrayList<String> med_time_taken=new ArrayList<String>(Arrays.asList(med_time));

    static ArrayList<String> med_condition_detail=new ArrayList<String>(Arrays.asList(med_condition));
    public static String getValue() {
        return med_name;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<String> getMedTime(){
        med_time_taken.removeIf(Objects::isNull);
        return med_time_taken;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<String> getMedCondtion(){
        med_condition_detail.removeIf(Objects::isNull);
        return med_condition_detail;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textView = findViewById(R.id.edittext1);
        alarm_button =findViewById(R.id.set_alarm_button);

        uriStr = getIntent().getStringExtra("uri");
        Uri uri = Uri.parse(uriStr);
        _extractTextFromUri(getApplicationContext(), uri);
        alarm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ResultActivity.this, SetAlarm.class));
            }
        });
    }

    public void _extractTextFromUri(Context context, Uri _uri) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        AssetDatabaseOpenHelper assetDatabaseOpenHelper = new AssetDatabaseOpenHelper(this, DB_NAME);
        myDBHelper = new DataBaseHelper(this, DB_NAME);
        assetDatabaseOpenHelper.saveDatabase();
        try {
            InputImage image = InputImage.fromFilePath(context, _uri);
            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onSuccess(Text visionText) {
                                    // Task completed successfully
                                    String[] lines = visionText.getText().split("\n");
                                    StringBuilder buffer = new StringBuilder();
                                    for (String line : lines) {
                                        String[] words = line.split(" ");
                                        for (String word : words) {
                                            int m = 1;
                                            Cursor res = myDBHelper.getAllData(TABLE_NAME, toLowerCase(word));
                                            if (res.getCount() != 0) {
                                                med_time_taken.clear();
                                                med_condition_detail.clear();
                                                while (res.moveToNext()) {
                                                    buffer.append("   " + "<b><big> " + "<p style=" + "color:#143D38" + ">").append(m).append(". ").append(res.getString(0)).append("</p>").append("</big></b>").append("<br>");
                                                    //buffer.append("---------- + <br>");
                                                    buffer.append("<b>Medical Condition:  </b>  ").append(res.getString(1)).append("<br>");
                                                    //buffer.append("<br> ");
                                                    buffer.append("<b>Medicine Description:  </b>  ").append(res.getString(2)).append("<br>");
                                                    buffer.append("<b>Medical URL:  </b>  ").append(res.getString(9)).append("<br>");
                                                    buffer.append("<span style=" + "background-color:yellow" + ">" + "<b>Time to take Medicine:  </b>").append(res.getString(11)).append("</span> <br>");
                                                    String side_effect = res.getString(5);
                                                    med_name = res.getString(0);
                                                    med_time = res.getString(11);
                                                    med_condition = res.getString(1);
                                                    med_time_taken.add(med_time);
                                                    med_condition_detail.add(med_condition);

                                                    int a = res.getInt(0);


                                                    if (a < 3) {

                                                        buffer.append("<b> Drug Link:</b> ").append(res.getString(10)).append("</span> <br>");

                                                    } else {

                                                        buffer.append("<b>Drug Link:  </b>").append(res.getString(10)).append("<br>");

                                                    }


                                                    buffer.append("<br> ");
                                                    buffer.append("<br>");

                                                    m = m + 1;
                                                }
                                            } else {
                                                continue;
                                            }
                                        }
                                        if (buffer.length() != 0) {
                                            textView.setText(Html.fromHtml(buffer.toString()));

                                        } else {
                                            textView.setText("No Data Found");
                                        }
                                    }

                                }

                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            Toast.makeText(ResultActivity.this,"Text not captured",Toast.LENGTH_SHORT).show();
                                        }
                                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

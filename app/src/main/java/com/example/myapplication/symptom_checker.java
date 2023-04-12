package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class symptom_checker extends AppCompatActivity {
    WebView webView;
    ListView listView;
    Button Show_Deseases;
    DBConnectivity db;
    ArrayList<String> listContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checker);

        db = new DBConnectivity(getApplicationContext());
        try {
            db.createDatabase();
            db.openDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listContent = db.getSymptoms();
        Show_Deseases = findViewById(R.id.buttonB);
        listView = findViewById(R.id.sym_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listContent);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        Show_Deseases.setOnClickListener(new Button.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 String checked_symptoms;
                                                 int cntChoice = listView.getCount();
                                                 ArrayList<String> symptoms_ar_list = new ArrayList<>();
                                                 SparseBooleanArray pos_Array = listView.getCheckedItemPositions();
                                                 for (int i = 0; i < cntChoice; i++) {
                                                     if (pos_Array.get(i)) {

                                                         symptoms_ar_list.add(listView.getItemAtPosition(i).toString());
                                                     }
                                                 }

                                                 Set<String> disease = db.getCommonDiseaseFromSymptoms(symptoms_ar_list);
                                                 openDialog(disease);
                                             }
                                         }
        );
    }

    private void openDialog(Set<String> diseases) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogue_view = inflater.inflate(R.layout.dialogue_box, null);
        dialogBuilder.setView(dialogue_view);


        dialogBuilder.setTitle("Possible diseases : \n");
        String end_result = "";

        for (String diseas : diseases) {
            end_result = end_result + diseas + "\n";
        }

        dialogBuilder.setMessage(end_result);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog box = dialogBuilder.create();
        box.show();
    }
}
       /* CustomWebViewClient client =new CustomWebViewClient(this);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://symptomate.com/interview/0");
        //webView.loadUrl("https://familydoctor.org/your-health-resources/health-tools/symptom-checker/");
        //webView.loadUrl("https://www.mediktor.com/en-us/session?sessionId=19c1ebba-84d2-4979-933b-69cbfbd0b461&impersonate=0");

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK && this.webView.canGoBack()){
            this.webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
  class CustomWebViewClient extends WebViewClient {
      private Activity activity;

      public CustomWebViewClient(Activity activity) {
          this.activity = activity;
      }

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
          return super.shouldOverrideUrlLoading(view, request);
      }

      @Override
      public boolean shouldOverrideUrlLoading(WebView webView, String url) {
          return false;
      }

 */
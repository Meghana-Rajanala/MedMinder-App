package com.example.myapplication.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailsMedicineActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.AlarmDatabase;
import com.example.myapplication.database.MedicineDatabase;
import com.example.myapplication.models.AlarmModel;
import com.example.myapplication.models.MedicineModel;
import com.example.myapplication.recicver.AlarmReceiver;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private Context mContext;
    private Activity activity;
    private List<MedicineModel> medicineModelList;
    private String tableName;
    private RecyclerView recyclerView;
    MedicineAdapter adapter;
    String searchKeyword;
    String daysInterval;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, subTitle;
        public ImageView medicineIV, optionIV;
        public CardView fullChildCV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_TV);
            subTitle = itemView.findViewById(R.id.subtitle);

            optionIV = itemView.findViewById(R.id.overflow);
            fullChildCV = itemView.findViewById(R.id.fullChildCV);

        }

    }

    public MedicineAdapter(Context mContext, Activity activity,
                           List<MedicineModel> medicineModelList, String tableName,
                           RecyclerView recyclerView, MedicineAdapter adapter, String searchKeyword) {

        this.mContext = mContext;
        this.activity = activity;
        this.medicineModelList = medicineModelList;
        this.tableName = tableName;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.searchKeyword = searchKeyword;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.view_medicine, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        final MedicineModel medicineModel = medicineModelList.get(i);

        myViewHolder.title.setText(medicineModel.getMedicineName());
        myViewHolder.subTitle.setText(medicineModel.getFirstSlotTime());

        myViewHolder.optionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(myViewHolder.optionIV, medicineModel, mContext, tableName, activity, recyclerView, adapter, searchKeyword);

            }
        });

        myViewHolder.fullChildCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToDetailsActivity(medicineModel, mContext);
            }
        });

        myViewHolder.fullChildCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showPopupMenu(myViewHolder.fullChildCV, medicineModel, mContext, tableName, activity, recyclerView, adapter, searchKeyword);

                return false;
            }
        });


    }

    private void showPopupMenu(View view, MedicineModel medicineModel, Context context, String tableName,
                               Activity activity, RecyclerView recyclerView, MedicineAdapter adapter, String searchKeyword) {

        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_medicine, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener(medicineModel, context, activity, tableName, recyclerView, adapter, searchKeyword));
        popupMenu.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        MedicineModel medicineModel;
        Context context;
        Activity activity;
        String tableName;
        RecyclerView recyclerView;
        MedicineAdapter adapter;
        String searchKeyword;

        public MyMenuItemClickListener(MedicineModel medicineModel, Context context, Activity activity, String tableName
                , RecyclerView recyclerView, MedicineAdapter adapter, String searchKeyword) {
            this.medicineModel = medicineModel;
            this.context = context;
            this.activity = activity;
            this.tableName = tableName;
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            this.searchKeyword = searchKeyword;
        }

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_view: {
                    sendDataToDetailsActivity(medicineModel, context);
                    return true;
                }
                case R.id.action_delete: {
                    AlertDialog.Builder builder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                  //  AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                    builder.setTitle("Are you really want to delete alert ?")
                            .setMessage("Are you really want to delete ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do something when the OK button is clicked
                            deleteFile(medicineModel, context, medicineModelList, activity, tableName,
                                    recyclerView, adapter, searchKeyword);
                        }
                    })
                   .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do something when the Cancel button is clicked
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                default:
            }
            return false;
        }
    }


    ;
    private void deleteFile(MedicineModel medicineModel, Context context,
                            List<MedicineModel> medicineModelList, Activity activity,
                            String tableName, RecyclerView recyclerView,
                            MedicineAdapter adapter, String searchKeyword) {

     MedicineDatabase database = new MedicineDatabase(context);
        database.deleteData(medicineModel, tableName);
        // Update the id of the remaining files after the deleted file
        int deletedFileId = medicineModel.getId();
        for (int i = deletedFileId; i < medicineModelList.size(); i++) {
            MedicineModel file = medicineModelList.get(i);
            file.setId(file.getId() - 1);
            database.updateData(file, tableName);
        }
        medicineModelList.clear();
        medicineModelList = database.getSelectedList(searchKeyword, tableName);
        adapter = new MedicineAdapter(context, activity, medicineModelList, tableName,
                recyclerView, adapter, searchKeyword);

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        AlarmDatabase alarmDatabase = new AlarmDatabase(context);
        String searchKeywordForAlarm = medicineModel.getMedicineName() +
                medicineModel.getUniqueCode();
        List<AlarmModel> alarmModelList = alarmDatabase.getSelectedAlarm(searchKeywordForAlarm);
        if (!alarmModelList.isEmpty() && medicineModel.getId() < alarmModelList.size()) {
            AlarmModel alarmModel = alarmModelList.get(deletedFileId);
            // Rest of the code to cancel the alarm for the alarmModel object


            int firstRC = alarmModel.getFirstSlotRequestCode();
            if (firstRC <= 0) {
                //nothing
            } else {
                AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlarmReceiver.class);
               intent.putExtra("id",medicineModel.getId());
                intent.putExtra("medName", medicineModel.getMedicineName());
                intent.putExtra("status", medicineModel.getStatus());
                intent.putExtra("mealStatus", medicineModel.getMedicineMeal());
                intent.putExtra("time", medicineModel.getDate() + " " + medicineModel.getFirstSlotTime());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, firstRC, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);

            }
        }
    }

    private void sendDataToDetailsActivity(MedicineModel medicineModel, Context context) {
        String medName = medicineModel.getMedicineName();
        String dateTime = medicineModel.getDate();
        String numberOfSlot = String.valueOf(medicineModel.getNumberOfSlot());
        String firstSlotTime = medicineModel.getFirstSlotTime();
        String numberOfDays = String.valueOf(medicineModel.getNumberOfDays());
        String startDate = medicineModel.getStartDate();
        String medicineMeal=medicineModel.getMedicineMeal();
        if ((medicineModel.getDaysNameOfWeek().equals("null") ||
                medicineModel.getDaysNameOfWeek().equals(""))
                &&
                (medicineModel.getDaysInterval() == 0)) {
            daysInterval = "EveryDay";

        } else if (medicineModel.getDaysInterval() > 0) {

            daysInterval = String.valueOf(medicineModel.getDaysInterval());

        } else if (!medicineModel.getDaysNameOfWeek().equals("")
                || !medicineModel.getDaysNameOfWeek().equals("null")) {

            daysInterval = medicineModel.getDaysNameOfWeek();
        }


        String status = medicineModel.getStatus();


        Intent intent = new Intent(context, DetailsMedicineActivity.class);
        intent.putExtra("medName", medName);
        intent.putExtra("dateTime", dateTime);
        intent.putExtra("numberOfSlot", numberOfSlot);
        intent.putExtra("firstSlotTime", firstSlotTime);
        intent.putExtra("numberOfDays", numberOfDays);
        intent.putExtra("startDate", startDate);
        intent.putExtra("daysInterval", daysInterval);
        intent.putExtra("status", status);
        intent.putExtra("medicineMeal",medicineMeal);

        //    Log.d("DaysInterVal", "Never In");

        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return medicineModelList.size();
    }


}

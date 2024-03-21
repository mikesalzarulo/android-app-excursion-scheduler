package com.example.msalzar_d308_mobile_application_development_android.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.msalzar_d308_mobile_application_development_android.R;
import com.example.msalzar_d308_mobile_application_development_android.database.Repository;
import com.example.msalzar_d308_mobile_application_development_android.entities.Excursion;
import com.example.msalzar_d308_mobile_application_development_android.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    String name;
    String hotelName;
    int vacationID;
    String vacationStartDate;
    String startDate;
    String endDate;
    EditText editName;
    EditText editHotel;
    TextView editStart;
    TextView editEnd;
    Repository repository;
    int numExcursions;
    Vacation currentVacation;
    DatePickerDialog.OnDateSetListener vStartDate;
    DatePickerDialog.OnDateSetListener vEndDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.vacationDetailsFloatingActionButton);

        editName = findViewById(R.id.vacationname);
        editHotel = findViewById(R.id.vacationhotel);
        editStart = findViewById(R.id.vacationStartDate);
        editEnd = findViewById(R.id.vacationEndDate);
        vacationID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        hotelName = getIntent().getStringExtra("hotel");
        startDate = getIntent().getStringExtra("start");
        endDate = getIntent().getStringExtra("end");
        Log.d("VacationDetails", "Start Date: " + startDate);
        Log.d("VacationDetails", "End Date: " + endDate);
        editName.setText(name);
        editHotel.setText(hotelName);
        editStart.setText(startDate);
        editEnd.setText(endDate);

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info=editStart.getText().toString();
                if(info.equals(""))info="01/01/23";
                try{
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, vStartDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        vStartDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }

        };
        editEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info=editEnd.getText().toString();
                if(info.equals(""))info="01/01/23";
                try{
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, vEndDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        vEndDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelEnd();
            }

        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                intent.putExtra("start", startDate);
                intent.putExtra("end", endDate);
                startActivity(intent);
            }
        });

        // This is where users will be able to see the recycler view of excursion associated with a vacation.
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this, startDate, endDate);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStart.setText(sdf.format(myCalendarStart.getTime()));
    }
    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEnd.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
            return true;}
        // This is where a user will be able to enter, edit, and delete information. This was a decomposition in order to meet admittance standards.
        if(item.getItemId()== R.id.vacationsave){
            Vacation vacation;
            String editStartStr = editStart.getText().toString();
            String editEndStr = editEnd.getText().toString();
            Date editStartDate = null;
            Date editEndDate = null;

            try {
                editStartDate = new SimpleDateFormat("MM/dd/yy").parse(editStartStr);
                editEndDate = new SimpleDateFormat("MM/dd/yy").parse(editEndStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            if (editStartDate.after(editEndDate)) {
                Toast.makeText(this, "Start Date cannot be after End Date!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (vacationID == -1) {
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else
                    vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
                repository.insert(vacation);
                this.finish();
            } else {
                try{
                    vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
                    repository.update(vacation);
                    this.finish();
                } catch (Exception e){

                }
            }
            return true;}
        if(item.getItemId()== R.id.vacationdelete) {
            for (Vacation prod : repository.getmAllVacations()) {
                if (prod.getVacationID() == vacationID) currentVacation = prod;
            }

            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getVacationID() == vacationID) ++numExcursions;
            }

            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if (item.getItemId()== R.id.vacationshare) {
            String editStartStr = editStart.getText().toString();
            String editEndStr = editEnd.getText().toString();
            Date editStartDate = null;
            Date editEndDate = null;

            try {
                editStartDate = new SimpleDateFormat("MM/dd/yy").parse(editStartStr);
                editEndDate = new SimpleDateFormat("MM/dd/yy").parse(editEndStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            if (editStartDate.after(editEndDate)) {
                Toast.makeText(this, "Start Date cannot be after End Date!", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String excursionsShare = new String();
            for (Excursion e : repository.getAllExcursions()) {
                if (e.getVacationID() == vacationID) excursionsShare += ("\nName: " + e.getExcursionName().toString() + "\nDate: " + e.getExcursionDate().toString());
            }
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Vacation:\nName:" + editName.getText().toString()
                    + "\nHotel: " + editHotel.getText().toString() + "\nStart Date: " + editStart.getText().toString() + "\nEnd Date: " + editEnd.getText().toString()
            + "\nExcursions:" + excursionsShare);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "My Vacation Details!");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        if(item.getItemId()== R.id.vacationstartnotify) {
            String editStartStr = editStart.getText().toString();
            String editEndStr = editEnd.getText().toString();
            Date editStartDate = null;
            Date editEndDate = null;

            try {
                editStartDate = new SimpleDateFormat("MM/dd/yy").parse(editStartStr);
                editEndDate = new SimpleDateFormat("MM/dd/yy").parse(editEndStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            if (editStartDate.after(editEndDate)) {
                Toast.makeText(this, "Start Date cannot be after End Date!", Toast.LENGTH_SHORT).show();
                return false;
            }
            String dateFromScreen = editStart.getText().toString();
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try{
                Long trigger = myDate.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", "Reminder! Vacation: " + editName.getText().toString() + " starts today!");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_MUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
            catch (Exception e){

            }
            return true;
        }
        if(item.getItemId()== R.id.vacationendnotify) {
            String editStartStr = editStart.getText().toString();
            String editEndStr = editEnd.getText().toString();
            Date editStartDate = null;
            Date editEndDate = null;

            try {
                editStartDate = new SimpleDateFormat("MM/dd/yy").parse(editStartStr);
                editEndDate = new SimpleDateFormat("MM/dd/yy").parse(editEndStr);
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
            if (editStartDate.after(editEndDate)) {
                Toast.makeText(this, "Start Date cannot be after End Date!", Toast.LENGTH_SHORT).show();
                return false;
            }
            String dateFromScreen = editEnd.getText().toString();
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try{
                Long trigger = myDate.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", "Reminder! Vacation: " + editName.getText().toString() + " ends today!");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_MUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
            catch (Exception e){

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this, startDate, endDate);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        //Toast.makeText(VacationDetails.this,"refresh list",Toast.LENGTH_LONG).show();
    }
}
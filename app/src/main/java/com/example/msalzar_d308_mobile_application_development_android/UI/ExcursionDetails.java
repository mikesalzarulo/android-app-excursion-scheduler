package com.example.msalzar_d308_mobile_application_development_android.UI;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.msalzar_d308_mobile_application_development_android.dao.VacationDAO;
import com.example.msalzar_d308_mobile_application_development_android.database.Repository;
import com.example.msalzar_d308_mobile_application_development_android.entities.Excursion;
import com.example.msalzar_d308_mobile_application_development_android.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String name;
    String excursionDate;
    int excursionID;
    int vacationID;
    EditText editName;
    TextView editDate;
    String vacationStart;
    String vacationEnd;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        // Here is where the user's excursion information will be grabbed through intent and populated on the current file page.
        repository=new Repository(getApplication());
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.name);
        editName.setText(name);
        editDate = findViewById(R.id.excursiondate);
        excursionID = getIntent().getIntExtra("id", -1);
        excursionDate = getIntent().getStringExtra("date");
        editDate.setText(excursionDate);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        vacationStart = getIntent().getStringExtra("start");
        vacationEnd = getIntent().getStringExtra("end");
        Log.d("VacationDetails", "Vacation Start Date: " + vacationStart);
        Log.d("VacationDetails", "Vacation End Date: " + vacationEnd);
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info=editDate.getText().toString();
                if(info.equals(""))info="01/01/23";
                try{
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startDate = new DatePickerDialog.OnDateSetListener() {

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
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.excursionsave) {
            Excursion excursion;
            String editStartStr = vacationStart;
            String editEndStr = vacationEnd;
            String editExcStr = editDate.getText().toString();

            Date editStartDate = null;
            Date editEndDate = null;
            Date editExcDate = null;

            try {
                editStartDate = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(editStartStr);
                editEndDate = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(editEndStr);
                editExcDate = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(editExcStr);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (editExcDate.before(editStartDate) || editExcDate.after(editEndDate)) {
                Toast.makeText(this, "Excursion Start Date must be during Vacation Start and End Date!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (excursionID == -1) {
                if (repository.getAllExcursions().size() == 0)
                    excursionID = 1;
                else
                    excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vacationID);
                repository.insert(excursion);
                this.finish();
            } else {
                excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vacationID);
                repository.update(excursion);
                this.finish();
            }
            return true;
        }

        if (item.getItemId() == R.id.excursiondelete) {
            Excursion excursionToDelete = null;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getExcursionID() == excursionID) {
                    excursionToDelete = excursion;
                    break;
                }
            }
            if (excursionToDelete != null) {
                repository.delete(excursionToDelete);
                Toast.makeText(ExcursionDetails.this, excursionToDelete.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Toast.makeText(ExcursionDetails.this, "Excursion not found", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if(item.getItemId()== R.id.excursionnotify) {
            String editStartStr = vacationStart;
            String editEndStr = vacationEnd;
            String editExcStr = editDate.getText().toString();

            Date editStartDate = null;
            Date editEndDate = null;
            Date editExcDate = null;

            try {
                editStartDate = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(editStartStr);
                editEndDate = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(editEndStr);
                editExcDate = new SimpleDateFormat("MM/dd/yy", Locale.US).parse(editExcStr);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (editExcDate.before(editStartDate) || editExcDate.after(editEndDate)) {
                Toast.makeText(this, "Excursion Start Date must be during Vacation Start and End Date!", Toast.LENGTH_SHORT).show();
                return false;
            }
            String dateFromScreen = editDate.getText().toString();
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
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", "Reminder! Excursion: " + editName.getText().toString() + " starts today!");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_MUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
            catch (Exception e){

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
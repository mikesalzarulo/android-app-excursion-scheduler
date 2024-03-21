package com.example.msalzar_d308_mobile_application_development_android.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.msalzar_d308_mobile_application_development_android.R;
import com.example.msalzar_d308_mobile_application_development_android.database.Repository;
import com.example.msalzar_d308_mobile_application_development_android.entities.Excursion;
import com.example.msalzar_d308_mobile_application_development_android.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab = findViewById(R.id.vacationListFloatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
//                Intent intent=new Intent(ProductDetails.this,MainActivity.class);
//                startActivity(intent);
            return true;
        }

        //if (item.getItemId() == R.id.addVacation) {
            //Repository repo = new Repository(getApplication());
            //Vacation vacation = new Vacation(0,"Spring Break", "Pearson Hotel", "02/21/1995", "02/21/1995");
            //repo.insert(vacation);
            //Vacation vacationn = new Vacation(0,"LAPOOLZA", "Pearson Hotel", "02/21/1995", "02/21/1995");
            //repo.insert(vacationn);
            //Excursion excursion = new Excursion(0,"LALA", "02/21/1995", 1);
            //repo.insert(excursion);
            //Excursion excursionn = new Excursion(0,"LOLO", "02/21/1995", 1);
            //repo.insert(excursionn);
            //Toast.makeText(VacationList.this, "Vacation added!",Toast.LENGTH_LONG).show();
            //return true;
        //}
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        //Toast.makeText(VacationDetails.this,"refresh list",Toast.LENGTH_LONG).show();
    }
}
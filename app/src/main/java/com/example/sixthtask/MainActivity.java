package com.example.sixthtask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private RecyclerView RV;
    private FloatingActionButton FAB;
    private CarRVAdapter adapter;
    private DatabaseAccess DBA;

    private static final int PERM_REQ_CODE = 3;
    private static final int ADD_CAR_REQ_CODE = 1;
    private static final int EDIT_CAR_REQ_CODE = 2;
    public static final String CAR_KEY = "car_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // take permission from user to get into gallery for Version +6 :
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_REQ_CODE);
        }

        toolBar = findViewById(R.id.Main_ToolBar);
        setSupportActionBar(toolBar);
        RV = findViewById(R.id.RecyclerView);
        FAB = findViewById(R.id.FAB);

        // obj from DATABASE :
        DBA = DatabaseAccess.getInstance(this);

        DBA.open();
        ArrayList<Car> cars = DBA.getAllCars();
        DBA.close();

        adapter = new CarRVAdapter(cars, new onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int carId) {
                // this code will be initialized when user click on any item in recyclerView .
                Intent intent = new Intent(getBaseContext(), ViewCarActivity.class);
                intent.putExtra(CAR_KEY, carId);
                startActivityForResult(intent, EDIT_CAR_REQ_CODE);
            }
        });
        RV.setAdapter(adapter);
        RecyclerView.LayoutManager GLM = new GridLayoutManager(this, 2);
        RV.setLayoutManager(GLM);
        RV.setHasFixedSize(true);


        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ViewCarActivity.class);
                startActivityForResult(intent, ADD_CAR_REQ_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // inflating menu search item :
        SearchView SV = (SearchView) menu.findItem(R.id.MainSearch).getActionView();
        // enabling (>) submit button to be clicked to search after writing :
        SV.setSubmitButtonEnabled(true);

        // to enable search proccess :
        SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String SearchText) {
                // search after user click submit button and end typing :
                DBA.open();
                ArrayList<Car> cars = DBA.getCars(SearchText);
                DBA.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String NewSearchText) {
                // search while user still typing :
                DBA.open();
                ArrayList<Car> cars = DBA.getCars(NewSearchText);
                DBA.close();
                adapter.setCars(cars);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        // if user canceled searching :
        SV.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DBA.open();
        ArrayList<Car> cars = DBA.getAllCars();
        DBA.close();
        adapter.setCars(cars);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERM_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    public boolean xyBalance(String str) {

        if (str.length() < 2 || str.isEmpty()) {
            return false;
        }
        return true;
    }


}
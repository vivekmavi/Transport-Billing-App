package com.example.paras.transportmanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

public class ConfirmDetails extends AppCompatActivity
{
    TextView busNoTV,modelTV,yearTV,depotTV,routeTV,lastEntryTV;
    Button confirmBTN,cancleBTN;
    static boolean permissionChecked = false;
    static int vehicleIndex = 0;
    static String optionSelected="addEntries";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        busNoTV = (TextView) findViewById(R.id.c_show_busNo_TV);
        modelTV = (TextView) findViewById(R.id.c_show_model_TV);
        yearTV  = (TextView) findViewById(R.id.c_show_yearReg_TV);
        depotTV  = (TextView) findViewById(R.id.c_show_depot_TV);
        routeTV  = (TextView) findViewById(R.id.c_show_route_TV);
        lastEntryTV  = (TextView) findViewById(R.id.c_show_lastEntry_TV);
        confirmBTN = (Button) findViewById(R.id.confirm_confirm_BTN);
        cancleBTN = (Button) findViewById(R.id.c_show_cancle_BTN);

        if (!permissionChecked)
        {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M)
            {
                Toast.makeText(this, "Permission checking", Toast.LENGTH_SHORT).show();
                checkPermission();
            }
            permissionChecked=true;
        }


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        if (intent==null)
        {
            Toast.makeText(this, "intent is null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            vehicleIndex = intent.getIntExtra("vehicleIndex",0);
            optionSelected = intent.getStringExtra("optionSelected");
            showData(vehicleIndex);
        }
    }

    private void showData(int vIndex)
    {
        // get data from arrays.xml

        Resources resources = getResources();
        String[] busNo = resources.getStringArray(R.array.vehicleNo);
        String[] model = resources.getStringArray(R.array.model);
        String[] year = resources.getStringArray(R.array.year);
        String[] depot = resources.getStringArray(R.array.depot);
        String[] route = resources.getStringArray(R.array.route);

        // load them on activity

        busNoTV.setText(busNo[vIndex]);
        modelTV.setText(model[vIndex]);
        yearTV.setText(year[vIndex]);
        depotTV.setText(depot[vIndex]);
        routeTV.setText(route[vIndex]);
        String fileName = busNo[vIndex]+".txt";
        checkForFiles(fileName);
    }

    public void canclePressed(View view)
    {
        Intent intent = new Intent(this, StartScreen.class);
        startActivity(intent);
    }
    public void confirmPressed(View view)
    {
        if (optionSelected.equalsIgnoreCase("addEntries"))
        {
            Intent intent = new Intent(this, AddEntries.class);
            intent.putExtra("vehicleIndex",vehicleIndex);
            startActivity(intent);
        }
        else if (optionSelected.equalsIgnoreCase("deleteEntries"))
        {
            Intent intent = new Intent(this, DeleteEntries.class);
            intent.putExtra("vehicleIndex",vehicleIndex);
            startActivity(intent);
        }
        else if (optionSelected.equalsIgnoreCase("generateBill"))
        {
            Intent intent = new Intent(this, GenerateBill.class);
            intent.putExtra("vehicleIndex",vehicleIndex);
            startActivity(intent);
        }
        else if (optionSelected.equalsIgnoreCase("viewAllEntries"))
        {
            Intent intent = new Intent(this, ViewAllEntries.class);
            intent.putExtra("vehicleIndex",vehicleIndex);
            startActivity(intent);
        }

    }
    protected void checkForFiles(String fileName)
    {
        //check if file exist or not.
        File folder = getExternalCacheDir();
        File file = new File(folder,fileName);
        Log.e("check file",file.getAbsolutePath());


        if(!file.exists())
        {
            String currentDate = new UsefullEditingMethods(this).getCurrentDate();
            String newFileData = currentDate+":0001;";// to know when was file created code 0001
            new UsefullEditingMethods(this).writeData(file,newFileData);
        }
        String lastDate = new UsefullEditingMethods(this).getLastEntryDateInFile(file);
        lastEntryTV.setText(lastDate);


    }

    /* Checks if external storage is available to at least read */

    public String isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) &&
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            return "read only";
        }
        else if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return  "only mounted";
        }
        return "not mounted";
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case 123: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)     {
                    //Peform your task here if any
                } else {

                    checkPermission();
                }
                return;
            }
        }
    }
}

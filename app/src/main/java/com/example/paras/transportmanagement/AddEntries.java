package com.example.paras.transportmanagement;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddEntries extends AppCompatActivity
{

    static int vehicleIndex = 0;
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
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        AddEntriesFragment fragmentObj = (AddEntriesFragment) fragmentManager.findFragmentById(R.id.addEntries_frag_in_addActivity);
        fragmentObj.loadDetails(vehicleIndex);
        // pass vehicle no to add fragment to load data
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entries);
    }
}

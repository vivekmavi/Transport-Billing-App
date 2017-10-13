package com.example.paras.transportmanagement;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartScreen extends AppCompatActivity implements Communicator
{
    Button addEntries , deleteEntries , generateBill ,viewAllEntries ;
    static String optionSelected = "notSelected" ;

    SelectVehicleDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        addEntries = (Button) this.findViewById(R.id.addEntries_btn);
        deleteEntries = (Button) this.findViewById(R.id.deleteEntries_btn);
        generateBill = (Button) this.findViewById(R.id.generateBill_btn);
        viewAllEntries = (Button) this.findViewById(R.id.viewAllEntriess_btn);

        addEntries.setOnClickListener(new onOptionButtonClicked(this));
        deleteEntries.setOnClickListener(new onOptionButtonClicked(this));
        generateBill.setOnClickListener(new onOptionButtonClicked(this));
        viewAllEntries.setOnClickListener(new onOptionButtonClicked(this));

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (dialog != null)
        {
            dialog.dismiss();
        }
    }

    @Override
    public void vehicleNoSelectedResponse(int itemIndex)
    {
        if (dialog != null)
        {
            dialog.dismiss();
        }
        if(optionSelected.equalsIgnoreCase("addEntries"))
        {
            Intent intent = new Intent(this,ConfirmDetails.class);
            intent.putExtra("vehicleIndex",itemIndex);
            intent.putExtra("optionSelected","addEntries");
            startActivity(intent);
        }
        if(optionSelected.equalsIgnoreCase("deleteEntries"))
        {
            Intent intent = new Intent(this,ConfirmDetails.class);
            intent.putExtra("vehicleIndex",itemIndex);
            intent.putExtra("optionSelected","deleteEntries");
            startActivity(intent);
        }
        if(optionSelected.equalsIgnoreCase("generateBill"))
        {
            Intent intent = new Intent(this,ConfirmDetails.class);
            intent.putExtra("vehicleIndex",itemIndex);
            intent.putExtra("optionSelected","generateBill");
            startActivity(intent);
        }
        if(optionSelected.equalsIgnoreCase("viewAllEntries"))
        {
            Intent intent = new Intent(this,ConfirmDetails.class);
            intent.putExtra("vehicleIndex",itemIndex);
            intent.putExtra("optionSelected","viewAllEntries");
            startActivity(intent);
        }

    }
}

class onOptionButtonClicked implements View.OnClickListener
{
    StartScreen startScreen;

    public onOptionButtonClicked(StartScreen startScreen)
    {
        this.startScreen = startScreen;
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.addEntries_btn)
        {
            android.support.v4.app.FragmentManager fragmentManager = startScreen.getSupportFragmentManager();

            startScreen.dialog = new SelectVehicleDialog();
            startScreen.dialog.show(fragmentManager, "add entry");
            startScreen.optionSelected = "addEntries";
        }
        else if(view.getId()==R.id.deleteEntries_btn)
        {
            android.support.v4.app.FragmentManager fragmentManager = startScreen.getSupportFragmentManager();
            startScreen.dialog = new SelectVehicleDialog();
            startScreen.dialog.show(fragmentManager, "delete entry");
            startScreen.optionSelected = "deleteEntries";
        }
        else if(view.getId()==R.id.generateBill_btn)
        {
            android.support.v4.app.FragmentManager fragmentManager = startScreen.getSupportFragmentManager();
            startScreen.dialog = new SelectVehicleDialog();
            startScreen.dialog.show(fragmentManager, "generate bill");
            startScreen.optionSelected = "generateBill" ;
        }
        else if(view.getId()==R.id.viewAllEntriess_btn)
        {
            android.support.v4.app.FragmentManager fragmentManager = startScreen.getSupportFragmentManager();
            startScreen.dialog = new SelectVehicleDialog();
            startScreen.dialog.show(fragmentManager, "view all entries");
            startScreen.optionSelected = "viewAllEntries" ;
        }

    }


}
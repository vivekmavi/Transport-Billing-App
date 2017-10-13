package com.example.paras.transportmanagement;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class DeleteEntries extends AppCompatActivity implements Button.OnClickListener
        , SearchDateFoundDialog.Communicator2
{
    TextView vehicleNo , lastEntryDate , lastEntryKm ;
    EditText searchEntryDate ;
    Button searchBtn , clearBtn , exitBtn ;

    static String lastDate="00/00/0000";
    static String lastKm ="000";
    static String vehicleNumber ="xx17Txxxx";
    static File file = null;
    static UsefullEditingMethods myMethods = null;
    static File folder = null;
    static int vehicleIndex = 0;
    static List<String> entriesList = null;
    static int copyIndex = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_entries);

        vehicleNo = (TextView) findViewById(R.id.vehicleNo_deletefrag_tv);
        lastEntryDate = (TextView) findViewById(R.id.lastEntryDate_deletefrag_tv);
        lastEntryKm = (TextView) findViewById(R.id.lastEntryKM_deletefrag_tv);

        searchEntryDate = (EditText) findViewById(R.id.searchDate_deleteFrag_ET);

        searchBtn = (Button) findViewById(R.id.searchBtn_deleteFrag_Btn);
        clearBtn = (Button) findViewById(R.id.clear_deletefrag_btn);
        exitBtn = (Button) findViewById(R.id.exit_deletefrag_btn);

        searchBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

        myMethods = new UsefullEditingMethods(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadDetails();
    }

    protected void loadDetails()
    {
        Intent intent = getIntent();
        if (intent==null)
        {
            Toast.makeText(this, "intent is null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            vehicleIndex = intent.getIntExtra("vehicleIndex",0);
        }

         intaliazeAllagain();


    }

    protected void intaliazeAllagain()
    {
        folder = getExternalCacheDir();
        String fileName = myMethods.getVehicleFileName(vehicleIndex);
        file = new File(folder,fileName);

        vehicleNumber = myMethods.getVehicleNumber(vehicleIndex);
        entriesList = myMethods.extractEntriesDelimiter(file); // with delimiter
        lastDate = myMethods.getLastEntryDateInFile(file);
        lastKm = myMethods.getLastEntryKmInFile(file);

        searchEntryDate.setText(myMethods.getCurrentDate());

        vehicleNo.setText(vehicleNumber);
        lastEntryDate.setText(lastDate);
        lastEntryKm.setText(lastKm);
    }


    @Override
    public void onClick(View view)
    {
        if( view.getId()==R.id.clear_deletefrag_btn)
        {
            lastEntryDate.setText(lastDate);
            lastEntryKm.setText(lastKm);
            searchEntryDate.setText(myMethods.getCurrentDate());

        }
        if( view.getId()==R.id.exit_deletefrag_btn)
        {
            Intent intent = new Intent(this,StartScreen.class);
            startActivity(intent);
        }
        if( view.getId()==R.id.searchBtn_deleteFrag_Btn)
        {
          String searchEntrydate = searchEntryDate.getText().toString();
            String[] copyExist = myMethods.checkForCopy(entriesList,searchEntrydate);

            // check if entry exist

            if (copyExist[0].compareTo("true")==0)
            {
                copyIndex= Integer.parseInt(copyExist[1]);
                SearchDateFoundDialog dialog = new SearchDateFoundDialog();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialog.show(fragmentManager,"search dialog tag");
            }
            else
            {
                Toast.makeText(this, "no entry on "+searchEntrydate, Toast.LENGTH_SHORT).show();
            }


        }


    }
    protected void deleteEntry()
    {
        Toast.makeText(this, "entry deleted", Toast.LENGTH_SHORT).show();

        entriesList.remove(copyIndex);

        List<String> sortedlist = myMethods.sortList(entriesList);
        String writeDataFinal = "";
        for (int i =0;i<sortedlist.size();i++)
        {
            if (i==0)
                writeDataFinal=sortedlist.get(i);
            else
                writeDataFinal=writeDataFinal+"\n"+sortedlist.get(i);
        }
        myMethods.writeData(file,writeDataFinal);
        intaliazeAllagain();
    }

    @Override
    public void respond()
    {
        // to get confirmation respose from delete dialog
        // it is called when yes pressed on delete entries dialog
        deleteEntry();
    }
}

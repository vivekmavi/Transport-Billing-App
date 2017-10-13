package com.example.paras.transportmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class ViewAllEntries extends AppCompatActivity {

    static String vehicleNumber ="xx17Txxxx";
    static String totalNoOfEntries ="0";
    static File file = null;
    static UsefullEditingMethods myMethods = null;
    static File folder = null;
    static int vehicleIndex = 0;
    static List<String> entriesList = null;

    TextView vehicleNo , totalEntries ;
    ListView entriesListView;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_entries);

        vehicleNo= (TextView) findViewById(R.id.vehicleNo_viewAllEnt_tv);
        totalEntries= (TextView) findViewById(R.id.totalEntriesCount_viewAllEnt_tv);
        entriesListView= (ListView) findViewById(R.id.listView_viewAllEnt_lv);
        exit= (Button) findViewById(R.id.exit_viewAllEnt_btn);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ViewAllEntries.this,StartScreen.class);
                startActivity(intent);
            }
        });
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

        }

        loadAllEntries();
    }

    private void loadAllEntries()
    {
        folder = getExternalCacheDir();
        myMethods = new UsefullEditingMethods(this);
        String fileName = myMethods.getVehicleFileName(vehicleIndex);
        file = new File(folder,fileName);

        vehicleNumber = myMethods.getVehicleNumber(vehicleIndex);
        entriesList = myMethods.extractEntries(file); // with delimiter
        totalNoOfEntries = String.valueOf(entriesList.size());


        vehicleNo.setText("Vehicle No : "+vehicleNumber);
        totalEntries.setText("Total no of Entries : "+totalNoOfEntries);

        String[] listEntries = entriesList.toArray(new String[0]);

        for (int i = 0 ; i < entriesList.size() ; i++)
        {
            String entryNo = String.format("%02d",i+1)+".";
            String date = myMethods.getEntryDate(entriesList.get(i));
            String km = myMethods.getEntryKm(entriesList.get(i));

            listEntries[i]=entryNo+"   "+date+"    "+km;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                        android.R.layout.simple_list_item_1,listEntries);
        entriesListView.setAdapter(adapter);
    }


}

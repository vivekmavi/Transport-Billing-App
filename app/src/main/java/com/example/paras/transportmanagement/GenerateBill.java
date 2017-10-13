package com.example.paras.transportmanagement;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.paras.transportmanagement.DatesListViewDialog.datesList;

public class GenerateBill extends AppCompatActivity implements DatesListViewDialog.Communicator3
{
    TextView vehicleNo , totalEntries ;
    EditText firstEntry , lastEntry , billFrom , billTo , ratePkm;
    Button exit , generate ;

    static String firstEntryDate="00/00/0000";
    static String lastEntryDate="00/00/0000";
    static String startDate="00/00/0000";
    static String endDate="00/00/0000";
    static String dateNeededIs = "";
    static String vehicleNumber ="xx17Txxxx";
    static String totalNoOfEntries ="0";
    static double ratePerKm = 0.1;
    static double totalAmount = 0.0;
    static double totalKM = 0.0;

    static File file = null;
    static UsefullEditingMethods myMethods = null;
    static File folder = null;
    static int vehicleIndex = 0;
    static List<String> entriesList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);

        vehicleNo = (TextView) findViewById(R.id.vehicleNo_genBill_tv);
        totalEntries = (TextView) findViewById(R.id.totalEntriesCount_genBill_Tv);
        firstEntry = (EditText) findViewById(R.id.firstEntryDate_genBill_Tv);
        lastEntry = (EditText) findViewById(R.id.lastEntryDate_genBill_tv);
        billFrom = (EditText) findViewById(R.id.billFrom_genBill_Tv);
        billTo = (EditText) findViewById(R.id.billTo_genBill_Tv);
        ratePkm = (EditText) findViewById(R.id.perKmRate_genBill_tv);
        exit = (Button) findViewById(R.id.exit_genBill_btn);
        generate = (Button) findViewById(R.id.generate_genBill_btn);

        billFrom.setOnClickListener(new HandleOnClickEvents(this));
        billTo.setOnClickListener(new HandleOnClickEvents(this));
        exit.setOnClickListener(new HandleOnClickEvents(this));
        generate.setOnClickListener(new HandleOnClickEvents(this));


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

        intaliazeAllagain();
    }

    private void intaliazeAllagain()
    {
        folder = getExternalCacheDir();
        myMethods = new UsefullEditingMethods(this);
        String fileName = myMethods.getVehicleFileName(vehicleIndex);
        file = new File(folder,fileName);

        vehicleNumber = myMethods.getVehicleNumber(vehicleIndex);
        entriesList = myMethods.extractEntriesDelimiter(file); // with delimiter
        lastEntryDate = myMethods.getLastEntryDateInFile(file);
        firstEntryDate = myMethods.getFirstEntryDateInFile(file);
        totalNoOfEntries = String.valueOf(entriesList.size());
        ratePerKm = Double.parseDouble(ratePkm.getText().toString());
        vehicleNo.setText(vehicleNumber);
        totalEntries.setText(totalNoOfEntries);
        firstEntry.setText(firstEntryDate);
        lastEntry.setText(lastEntryDate);

    }

    @Override
    public void selectedDate(String[] selectedDate)
    {
        if (selectedDate[0].equalsIgnoreCase("startingdate"))
        {
            startDate=selectedDate[1];
            billFrom.setText(startDate);

        }
        else if ( selectedDate[0].equalsIgnoreCase("endingdate") )
        {
            endDate=selectedDate[1];
            billTo.setText(endDate);
        }

        boolean isCorrect = dateCheck(startDate,endDate);


        if (!isCorrect)
        {
            IncorrectDateSequenceDialog dialog = new IncorrectDateSequenceDialog();
            FragmentManager fragmentManager = getSupportFragmentManager();
            dialog.show(fragmentManager,"search dialog tag");
        }
    }

    protected void showTotal()
    {

        totalKM = getTotalKm();

        if (startDate.equalsIgnoreCase("00/00/0000") || endDate.equalsIgnoreCase("00/00/0000"))
        {ratePerKm=0.0; totalKM = 0.0;}
        else   ratePerKm = Double.parseDouble(ratePkm.getText().toString());

        totalAmount = totalKM*ratePerKm;

        GeneratedBillDialog dialog = new GeneratedBillDialog();
        Bundle args = new Bundle();
        args.putDouble("ratePerKm", ratePerKm);
        args.putDouble("totalAmount", totalAmount);
        args.putDouble("totalKM", totalKM);
        dialog.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        dialog.show(fragmentManager,"genratd bill dialg");
    }

    private boolean dateCheck(String startDate, String endDate)
    {
        boolean isGreater = false;

        DateHandling dateHandling = new DateHandling();
        String datei ="",datej ="";
        String monthi ="",monthj ="";
        String yeari ="",yearj ="";

                datei = DateHandling.getDate(startDate);
                datej = DateHandling.getDate(endDate);

                monthi = DateHandling.getMonth(startDate);
                monthj = DateHandling.getMonth(endDate);

                yeari = DateHandling.getYear(startDate);
                yearj = DateHandling.getYear(endDate);

        Log.e("datei",datei);
        Log.e("monthi",monthi);
        Log.e("yeari",yeari);
        Log.e("datej",datej);
        Log.e("monthj",monthj);
        Log.e("yearj",yearj);

                if (endDate.equalsIgnoreCase("00/00/0000"))
                    return true;

                if ( yeari.compareToIgnoreCase(yearj)<0)
                {
                    return true;
                }
                else if (yeari.compareToIgnoreCase(yearj)==0 && monthi.compareToIgnoreCase(monthj)<0)
                {
                    return true;
                }
                else if (yeari.compareToIgnoreCase(yearj)==0 && monthi.compareToIgnoreCase(monthj)==0
                        && datei.compareToIgnoreCase(datej)<0)
                {
                    return true;
                }
                else if (yeari.compareToIgnoreCase(yearj)==0 && monthi.compareToIgnoreCase(monthj)==0
                        && datei.compareToIgnoreCase(datej)==0)
                {
                    return true;
                }


        return isGreater;
    }


    protected List<String> getDatesList()
    {
        List<String> dates = new ArrayList<String>();

        for (int i=0; i<entriesList.size();i++)
        {
            dates.add(myMethods.getEntryDate(entriesList.get(i)));
        }
        return dates;
    }
    protected double getTotalKm()
    {
        List<Double> km = new ArrayList<Double>();
        int startingIndex =0 , endingIndex=0 ;

        double totalKm = 0 ;

        for (int i=0; i<entriesList.size();i++)
        {
            if (startDate.equalsIgnoreCase(myMethods.getEntryDate(entriesList.get(i))))
            {
                startingIndex = i;
            }
            else if (endDate.equalsIgnoreCase(myMethods.getEntryDate(entriesList.get(i))))
            {
                endingIndex = i;
            }
        }
        for (int i = startingIndex ; i<=endingIndex ; i++)
        {
            totalKm = totalKm + Double.parseDouble(myMethods.getEntryKm(entriesList.get(i)));
        }
        return totalKm;
    }
}

class HandleOnClickEvents implements View.OnClickListener
{
    GenerateBill generateBill ;

    public HandleOnClickEvents(GenerateBill generateBill)
    {
        this.generateBill = generateBill;
    }

    @Override
    public void onClick(View view)
    {
     if (view.getId()==R.id.exit_genBill_btn)
     {

         Intent intent = new Intent(generateBill,StartScreen.class);
         generateBill.startActivity(intent);
     }
     if (view.getId()==R.id.billFrom_genBill_Tv)
     {
         FragmentManager fragmentManager = generateBill.getSupportFragmentManager();
         DatesListViewDialog datesListViewDialog = new DatesListViewDialog();
         datesListViewDialog.show(fragmentManager,"select start Date");
         generateBill.dateNeededIs="startingdate";
     }
     if (view.getId()==R.id.billTo_genBill_Tv)
     {
         FragmentManager fragmentManager = generateBill.getSupportFragmentManager();
         DatesListViewDialog datesListViewDialog = new DatesListViewDialog();
         datesListViewDialog.show(fragmentManager,"select start Date");
         generateBill.dateNeededIs="endingdate";
     }
        if (view.getId()==R.id.generate_genBill_btn)
        {
            generateBill.showTotal();
        }

    }
}
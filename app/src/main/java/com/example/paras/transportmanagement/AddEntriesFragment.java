package com.example.paras.transportmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class AddEntriesFragment extends Fragment implements View.OnClickListener
{
    TextView vehicleNo , lastEntryDate , lastEntryKm ;
    EditText newEntryDate , newEntryKm ;
    CheckBox busOnRoute ;
    Button saveBtn , clearBtn , exitBtn , nextBtn ;

    static String lastDate="00/00/0000";
    static String defaultDate="00/00/0000";
    static String lastKm ="999";
    static String defaultDistance ="000";
    static File file = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_add_entries, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        vehicleNo = (TextView) getActivity().findViewById(R.id.vehicleNo_addfrag_tv);
        lastEntryDate = (TextView) getActivity().findViewById(R.id.lastEntryDate_addfrag_tv);
        lastEntryKm = (TextView) getActivity().findViewById(R.id.lastEntryKM_addfrag_tv);

        newEntryDate = (EditText) getActivity().findViewById(R.id.newEntryDate_addfrag_ET);
        newEntryKm = (EditText) getActivity().findViewById(R.id.newEntryKm_addfrag_ET);

        defaultDate = new UsefullEditingMethods(getActivity()).getCurrentDate();
        newEntryDate.setText(defaultDate);

        saveBtn = (Button) getActivity().findViewById(R.id.save_addfrag_btn);
        clearBtn = (Button) getActivity().findViewById(R.id.clear_addfrag_btn);
        exitBtn = (Button) getActivity().findViewById(R.id.exit_addfrag_btn);
        nextBtn = (Button) getActivity().findViewById(R.id.next_addfrag_btn);

        busOnRoute = (CheckBox) getActivity().findViewById(R.id.busOnRoute_addfrag_CB);

        saveBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        busOnRoute.setOnCheckedChangeListener(new handleCheckBox());

    }

    public void loadDetails(int vehicleIndex)
    {
        // called by add entry activity
        File folder = getActivity().getExternalCacheDir();
        UsefullEditingMethods myMethods = new UsefullEditingMethods(getActivity());
        String fileName = myMethods.getVehicleFileName(vehicleIndex);
        file = new File(folder,fileName);

        lastDate = myMethods.getLastEntryDateInFile(file);

        lastKm = myMethods.getLastEntryKmInFile(file);
        lastEntryDate.setText(lastDate);
        lastEntryKm.setText(lastKm);

        Resources resources = getResources();
        String[] defaultKM = resources.getStringArray(R.array.defaultKilometer);
        defaultDistance = defaultKM[vehicleIndex];
        newEntryKm.setText(defaultDistance);

    }

    @Override
    public void onClick(View view)
    {
        if( view.getId()==R.id.save_addfrag_btn)
        {
            String entryDateSave = newEntryDate.getText().toString();
            String entryKmSave  = newEntryKm.getText().toString();

            String finalEntryToSave = entryDateSave+":"+entryKmSave+";";

            SaveEntry(finalEntryToSave);

        }
        if (view.getId()==R.id.exit_addfrag_btn)
        {
            Intent intent = new Intent(getActivity(),StartScreen.class);
            startActivity(intent);

        }
        if( view.getId()==R.id.clear_addfrag_btn) //reset
        {
            lastEntryDate.setText(lastDate);
            lastEntryKm.setText(lastKm);
            newEntryKm.setText(defaultDistance);
            newEntryDate.setText(defaultDate);
        }
        if( view.getId()==R.id.next_addfrag_btn)
        {
            String nextDate = new UsefullEditingMethods(getActivity()).nextDate(newEntryDate.getText().toString());

            lastEntryDate.setText(lastDate);
            lastEntryKm.setText(lastKm);
            newEntryKm.setText(defaultDistance);
            newEntryDate.setText(nextDate);
        }

    }

    private void SaveEntry(String finalEntryToSave)
    {
        Toast.makeText(getActivity(), "entry saved", Toast.LENGTH_SHORT).show();
//
//       Log.e("final entry",finalEntryToSave);
        UsefullEditingMethods myMethods = new UsefullEditingMethods(getActivity());
        List<String> finalList = myMethods.extractEntriesDelimiter(file); // with delimiter ;

//        for (int i =0 ; i<finalList.size();i++)
//            Log.e("final list b+", " "+i+" "+finalList.get(i));
        String[] copyExist = myMethods.checkForCopy(finalList,finalEntryToSave);

        // check if entry exist

        if (copyExist[0].compareTo("true")==0)
        {
//            Toast.makeText(getActivity(), "addfrag cpy exist called", Toast.LENGTH_SHORT).show();
            finalList.remove(Integer.parseInt(copyExist[1]));
            finalList.add(finalEntryToSave);
        }
        else
        {
            finalList.add(finalEntryToSave);
        }

//        for (int i =0 ; i<finalList.size();i++)
//            Log.e("final list after+", " "+i+" "+finalList.get(i));

        List<String> sortedlist = myMethods.sortList(finalList);
//        for (int i=0 ; i<sortedlist.size() ; i++)
//            Log.e("sortedList",sortedlist.get(i));
        String writeDataFinal = "";
        for (int i =0;i<sortedlist.size();i++)
        {
            if (i==0)
                writeDataFinal=sortedlist.get(i);
            else
                writeDataFinal=writeDataFinal+"\n"+sortedlist.get(i);


        }

        myMethods.writeData(file,writeDataFinal);
    }
    class handleCheckBox implements CheckBox.OnCheckedChangeListener
    {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
        {
         if(isChecked)
         {
             newEntryKm.setText("000");
             newEntryKm.setEnabled(false);
         }
         if (!isChecked)
         {
             newEntryKm.setEnabled(true);
             newEntryKm.setText(defaultDistance);
         }
        }
    }


}

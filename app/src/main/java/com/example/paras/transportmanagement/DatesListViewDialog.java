package com.example.paras.transportmanagement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class DatesListViewDialog extends DialogFragment implements AdapterView.OnItemClickListener {
    ListView listView;
    static String dateIs = ""; // starting or ending
    static List<String> datesList ;
    String[] vehicleNo;
    Communicator3 communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
       View view = inflater.inflate(R.layout.fragment_dates_list_view_dialog, container, false);
        listView = (ListView) view.findViewById(R.id.selectDate_lv);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        GenerateBill generateBill = new GenerateBill();
        dateIs = generateBill.dateNeededIs;
        datesList = generateBill.getDatesList();
        String[] dates = datesList.toArray(new String[0]); // requiers string array to fill

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,dates);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        setCommunicator3((Communicator3) context);
    }

    public void setCommunicator3(Communicator3 communicator)
    {
        this.communicator = communicator;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int itemNo, long rowId)
    {
        String[] selectedDate = {"",""};
        selectedDate[0] = dateIs;
        selectedDate[1] = datesList.get(itemNo);
        communicator.selectedDate(selectedDate);
        dismiss();
    }

    interface Communicator3
    {
        public void selectedDate(String[] selectedDate);

        // String[0] = check if it is startDate or endDate
        // String[1] = endDate

    }

}

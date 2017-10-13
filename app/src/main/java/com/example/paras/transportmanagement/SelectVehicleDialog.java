package com.example.paras.transportmanagement;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class SelectVehicleDialog extends DialogFragment {

    ListView listView;
    int busIconResource = R.drawable.busicon;
    String[] vehicleNo;
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_select_vehicle_dialog, container, false);
        listView = (ListView) view.findViewById(R.id.vehicleNo_lv);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Resources resources = getResources();
        vehicleNo = resources.getStringArray(R.array.vehicleNo);
        MyCustomAdapter adapter = new MyCustomAdapter(getActivity(),R.layout.custom_row,vehicleNo,busIconResource);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new onVehicleNoSelected());
        communicator = (Communicator) getActivity();

    }

    class MyCustomAdapter extends ArrayAdapter
    {
        Context context;
        String[] vehicleNo3;
        int resource;
        int busIconResrc;
        LayoutInflater inflater;
        public MyCustomAdapter(Context context,int resource,String[] vehicleNo2,int busIconRescr)
        {
            super(context, resource,busIconRescr, vehicleNo2);

            this.context = context ;
            this.resource = resource ;
            this.busIconResrc = busIconRescr;
            vehicleNo3 = vehicleNo2 ;
        }


        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            ViewHolder viewHolder = null ;
            if(convertView == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource,parent,false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            else
            {
             viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.busIcon.setImageResource(busIconResource);
            viewHolder.vehicleNo_TV.setText(vehicleNo3[position]);
            return convertView;
        }

    }

    class ViewHolder
    {
        TextView vehicleNo_TV ;
        ImageView busIcon;
        ViewHolder(View convertedView)
        {
            vehicleNo_TV = (TextView) convertedView.findViewById(R.id.vehicleNo_tv_cr);
            busIcon = (ImageView) convertedView.findViewById(R.id.imageView_in_row);
        }
    }
    class onVehicleNoSelected implements ListView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long rowId)
        {
            communicator.vehicleNoSelectedResponse(itemIndex);
        }

    }



}


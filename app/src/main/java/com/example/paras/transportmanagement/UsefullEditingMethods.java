package com.example.paras.transportmanagement;

// use this class for methods which are used multiple times in different activity


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import static android.R.id.input;

public class UsefullEditingMethods extends DateHandling
{   private Context context;

    public UsefullEditingMethods(Context current)
    {
        this.context = current;
    }


    protected String read(File file)
    {
        FileInputStream fileInputStream = null ;

        try
        {
            fileInputStream = new FileInputStream(file);
            int read = -1;
            StringBuffer stringBuffer = new StringBuffer();
            while ((read=fileInputStream.read())!=-1)
            {
                if (read!=10) // i.e new line
                    stringBuffer.append((char) read);
            }
            return stringBuffer.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "no data";
    }

    protected List<String> extractEntries(File file)
    {
        // dont provide ; with entries
        String fileData = read(file);
        List<String> entriesList = new ArrayList<String>();


        StringTokenizer st = new StringTokenizer(fileData,";",false);
        while (st.hasMoreTokens())
        {
            entriesList.add(st.nextToken()); // no delimiter added
        }
        if (entriesList.isEmpty())
        {
            // if file is empty
            entriesList.add(getCurrentDate()+":000;");
            writeData(file,entriesList.get(0));
        }
        return entriesList;
    }

    protected List<String> extractEntriesDelimiter(File file)
    {
        // provide ; along with entries
        String fileData = read(file);
        List<String> entriesList = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(fileData,";",false); //false bcs we added ; below
        while (st.hasMoreTokens())
        {
            entriesList.add(st.nextToken()+";"); // delimiter added
        }

//        for (int i =0 ; i<entriesList.size();i++)
//            Log.e("raw list with ;", " "+i+" "+entriesList.get(i));
        return entriesList;
    }
    protected String getLastEntryInFile(File file)
    {
        List<String> entriesList = extractEntries(file);
        String lastEntry = entriesList.get(entriesList.size()-1); // last entry

        return lastEntry;
    }
    protected String getLastEntryDateInFile(File file)
    {
        String lastEntry = getLastEntryInFile(file);
        int indexOfDelimiter = lastEntry.indexOf(':');
        String lastDate = lastEntry.substring(0,indexOfDelimiter);

        return lastDate;
    }
    protected String getLastEntryKmInFile(File file)
    {
        String lastEntry = getLastEntryInFile(file);
        int indexOfDelimiter = lastEntry.indexOf(':');
        String DistanceTraveled = lastEntry.substring(indexOfDelimiter+1);

        return DistanceTraveled;
    }
    protected String getFirstEntryInFile(File file)
    {
        List<String> entriesList = extractEntries(file);
        String firstEntry = entriesList.get(0); // last entry

        return firstEntry;
    }
    protected String getFirstEntryDateInFile(File file)
    {
        String firstEntry = getFirstEntryInFile(file);
        int indexOfDelimiter = firstEntry.indexOf(':');
        String firstDate = firstEntry.substring(0,indexOfDelimiter);

        return firstDate;
    }
    protected String getEntryKm(String entry)
    {
        int indexOfDelimiter = entry.indexOf(':');
        int indexOfDelimiter2 = entry.indexOf(';');

        // if entries extracted with delimiter
        String km = entry.substring(indexOfDelimiter+1);
        // if entries extracted without delimiter
        if (indexOfDelimiter2!=-1)
        km = entry.substring(indexOfDelimiter+1,indexOfDelimiter2);
        return km;
    }

    protected void writeData(File file, String newFileData)
    {
//        Toast.makeText(context, "write data called", Toast.LENGTH_SHORT).show();
        FileOutputStream fileOStrem = null;
        try
        {
            fileOStrem = new FileOutputStream(file,false);
            fileOStrem.write(newFileData.getBytes());

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fileOStrem != null)
            {
                try
                {
                    fileOStrem.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
    protected void writeDataWithAppend(File file, String newFileData)
    {
//        Toast.makeText(context, "write data append called", Toast.LENGTH_SHORT).show();
        FileOutputStream fileOStrem = null;
        try
        {
            fileOStrem = new FileOutputStream(file,true);
            fileOStrem.write(newFileData.getBytes());

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fileOStrem != null)
            {
                try
                {
                    fileOStrem.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    protected String getVehicleFileName(int vIndex)
    {

        Resources resources = context.getResources();
        String[] busNo = resources.getStringArray(R.array.vehicleNo);
        String fileName = busNo[vIndex]+".txt";

        return fileName;
    }
    protected String getVehicleNumber(int vIndex)
    {

        Resources resources = context.getResources();
        String[] busNo = resources.getStringArray(R.array.vehicleNo);
        String vehicleNo = busNo[vIndex];

        return vehicleNo;
    }

    protected  String getCurrentDate()
    {
        Long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        return dateString;
    }
    protected String nextDate(String previousDatec)
    {
        String retunDate = previousDatec;
        int month = Integer.parseInt(previousDatec.substring(3,5));
        int year =  Integer.parseInt(previousDatec.substring(6));
        int previousDate = Integer.parseInt(previousDatec.substring(0,2));


        int maxDate = 31;
        if(month==0x02)
        {
            boolean leap = false;

            if(year % 4 == 0)
            {
                if( year % 100 == 0)
                {
                    // year is divisible by 400, hence the year is a leap year
                    if ( year % 400 == 0)
                    {leap = true; maxDate = 29;}
                    else{leap = false; maxDate = 28;}
                }
                else{leap = true; maxDate = 29;}

            }
            else{leap = false; maxDate = 28;}
        }
        else if (month==0x04 || month==0x06 || month==0x09 || month==0x11 )
        {
            maxDate = 30;
        }

        if(previousDate>=maxDate)
        {
            previousDate = 1;
            if (month == 12)
            {
                month = 0x01;
                year = year + 1;
            }
            else month = month + 1;
        }
        else previousDate = previousDate + 0x01;

        // add 0 if single digit no

        String formatedDate = String.format("%02d", previousDate);
        String formatedMonth = String.format("%02d", month);

        retunDate = formatedDate+"/"+formatedMonth+"/"+String.valueOf(year);


        return retunDate;
    }

    protected List<String> sortList(List<String> unsortedList)
    {
        List<String> datesList = new ArrayList<String>();
        for (int i = 0; i < unsortedList.size(); i++)
        {
            datesList.add(getEntryDate(unsortedList.get(i)));
        }

        String[] dates = new String[datesList.size()];
        dates = datesList.toArray(dates);

        Arrays.sort(dates, new Comparator<String>() {
            private SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            @Override
            public int compare(String o1, String o2) {
                int result = -1;

                try {
                    result = sdf.parse(o1).compareTo(sdf.parse(o2));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                return result;
            }
        });

        List<String> dateList2 = Arrays.asList(dates);
        List<String> finalList = new ArrayList<String>();
        for (int i = 0 ; i < dateList2.size(); i++)
        {
            for (int j = 0; j < dateList2.size(); j++)
            {
                String entryDate = getEntryDate(unsortedList.get(j));
                String entryKm = getEntryKm(unsortedList.get(j));
                if (dateList2.get(i).compareToIgnoreCase(entryDate)==0)
                {
                    finalList.add(entryDate+":"+entryKm+";");
                }
            }
        }

        return finalList;
    }

    protected String[] checkForCopy(List<String> list,String value)
    {
        String copyAtIndex="0";
        String isCopy="false";
        String[] result={isCopy,copyAtIndex};

        String date1 = getEntryDate(value);
        if (date1.equalsIgnoreCase("default getEntryDate as : not found"))
            date1=value;

        for (int i=0 ; i<list.size();i++)
        {
            if (date1.compareTo(getEntryDate(list.get(i)))==0)
            {
                result[1]= String.valueOf(i);
                result[0]= "true";

                return result;
            }
        }

        return result;
    }

    protected String getEntryDate(String entry)
    {
        int indexOfDelimiter = entry.indexOf(':');
        String date ="";
        if (indexOfDelimiter== -1) // i.e. : not found
            date = "default getEntryDate as : not found";
        else
            date = entry.substring(0,indexOfDelimiter);

        return date;
    }


}
package com.example.paras.transportmanagement;

/**
 * Created by Paras on 03/10/2017.
 */

public class DateHandling
{
    protected static String getYear(String date)
    {
        return date.substring(6);
    }
    protected static String getMonth(String date)
    {
        return date.substring(3,5);
    }
    protected static String getDate(String date)
    {
        return date.substring(0,2);
    }

}

package com.random.malay.expensetracker;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ViewExpense extends ActionBarActivity {

    EditText etWeekStart, etWeekEnd, etDay;
    int mYear, mMonth,mDay;
    Spinner dropdown;
    DBAdapter myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);
        dropdown = (Spinner)findViewById(R.id.spinnerMonth);
        String[] items = new String[]{
                "January","February","March","April","May","June","July","August","September","October","November","December"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        dropdown.setAdapter(adapter);
        etWeekStart = (EditText) findViewById(R.id.etWeekFilterStart);
        etWeekEnd = (EditText) findViewById(R.id.etWeekFilterEnd);
        etDay = (EditText) findViewById(R.id.etSortDay);

        etWeekStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog mDatePicker = new DatePickerDialog(ViewExpense.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
                        etWeekStart.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });
        etWeekEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog mDatePicker = new DatePickerDialog(ViewExpense.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
                        etWeekEnd.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });
        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog mDatePicker = new DatePickerDialog(ViewExpense.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
                        etDay.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });
        openDB();
    }
    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    public void goToMonth(View view){
        String monthNum;
        String month = dropdown.getSelectedItem().toString();
        if(month.equals("January")){
            monthNum="/01/";
        }else if (month.equals("February")) {
            monthNum="/02/";
        }else if (month.equals("March")) {
            monthNum="/03/";
        }else if (month.equals("April")) {
            monthNum="/04/";
        }else if (month.equals("May")) {
            monthNum="/05/";
        }else if (month.equals("June")) {
            monthNum="/06/";
        }else if (month.equals("July")) {
            monthNum="/07/";
        }else if (month.equals("August")) {
            monthNum="/08/";
        }else if (month.equals("September")) {
            monthNum="/09/";
        }else if (month.equals("October")) {
            monthNum="/10/";
        }else if (month.equals("November")) {
            monthNum="/11/";
        }else if (month.equals("December")) {
            monthNum="/12/";
        }else{
            monthNum="/00/";
        }
        Cursor cursor = myDB.getRowsFromMonth(monthNum);
        String[] fromFieldNames = new String[]{
                DBAdapter.KEY_DATE, DBAdapter.KEY_DESCRIPTION, DBAdapter.KEY_CATEGORY, DBAdapter.KEY_AMOUNT, DBAdapter.KEY_PAIDBY
        };
        int[] toViewIDs = new int[]{
                R.id.text_date, R.id.text_description,R.id.text_category, R.id.text_amount, R.id.tvPaidBy
        };
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.lvSortBy);
        myList.setAdapter(myCursorAdapter);
    }

    public void goToWeek(View view){
        String start = etWeekStart.getText().toString();
        String end = etWeekEnd.getText().toString();
        String startStringDay, startStringMonth,startStringYear;
        String endStringDay, endStringMonth,endStringYear;
        startStringDay = convertDateToDay(start);
        startStringMonth = convertDateToMonth(start);
        startStringYear = convertDateToYear(start);
        endStringDay = convertDateToDay(end);
        endStringMonth = convertDateToMonth(end);
        endStringYear = convertDateToYear(end);
        boolean checkStartIsGreaterThanEnd = checkDate(startStringDay,startStringMonth,startStringYear,endStringDay,endStringMonth,endStringYear);
        Toast.makeText(this,"The condition is " + checkStartIsGreaterThanEnd, Toast.LENGTH_LONG).show();
    }

    public void goToDay(View view){
        String day = etDay.getText().toString();
        Cursor cursor = myDB.getRowsFromDate(day);

        String[] fromFieldNames = new String[]{
                DBAdapter.KEY_DATE, DBAdapter.KEY_DESCRIPTION, DBAdapter.KEY_CATEGORY, DBAdapter.KEY_AMOUNT, DBAdapter.KEY_PAIDBY
        };
        int[] toViewIDs = new int[]{
                R.id.text_date, R.id.text_description,R.id.text_category, R.id.text_amount, R.id.tvPaidBy
        };
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.lvSortBy);
        Toast.makeText(this, "done"+day, Toast.LENGTH_LONG).show();
        myList.setAdapter(myCursorAdapter);
    }

    public String convertDateToDay(String string){
        String fString;
        int iNum,fNum;
        string = string.replace("/","");
        iNum = 0;
        try {
            iNum = Integer.parseInt(string);
        } catch(NumberFormatException nfe) {
            Toast.makeText(this,"showCould not parse " + nfe, Toast.LENGTH_SHORT).show();
        }
        fNum = iNum/10000;
        if(fNum<=9){
            fString = "0" + Integer.toString(fNum);
        }else{
            fString = Integer.toString(fNum);
        }
        return fString;
    }
    public String convertDateToMonth(String string){
        String fString;
        int iNum,fNum;
        string = string.replace("/","");
        iNum = 0;
        try {
            iNum = Integer.parseInt(string);
        } catch(NumberFormatException nfe) {
            Toast.makeText(this,"showCould not parse " + nfe, Toast.LENGTH_LONG).show();
        }
        fNum = (iNum/100)%100;
        if(fNum<=9){
            fString = "0" + Integer.toString(fNum);
        }else{
            fString = Integer.toString(fNum);
        }
        return fString;
    }
    public String convertDateToYear(String string){
        String fString;
        int iNum,fNum;
        string = string.replace("/","");
        iNum = 0;
        try {
            iNum = Integer.parseInt(string);
        } catch(NumberFormatException nfe) {
            Toast.makeText(this,"showCould not parse " + nfe, Toast.LENGTH_LONG).show();
        }
        fNum = iNum%100;
        if(fNum<=9){
            fString = "0" + Integer.toString(fNum);
        }else{
            fString = Integer.toString(fNum);
        }
        return fString;
    }

    public boolean checkDate(String sDay, String sMonth, String sYear, String eDay, String eMonth, String eYear){
        int sD,sM,sY,eD,eM,eY;
        sD = Integer.parseInt(sDay);
        sM = Integer.parseInt(sMonth);
        sY = Integer.parseInt(sYear);
        eD = Integer.parseInt(eDay);
        eM = Integer.parseInt(eMonth);
        eY = Integer.parseInt(eYear);
        boolean check = false;
        if(sY<=eY && sM<=eM && sD<=eD) {
            check = true;
        }
        return check;
    }

}

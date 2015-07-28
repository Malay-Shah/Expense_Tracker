package com.random.malay.expensetracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity {

    DBAdapter myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
        populateListView();
        listViewItemClick();

    }


    public void goToAddNewExpenseAct(View view){
        Intent intent = new Intent(this,AddNewExpense.class);
        startActivity(intent);
    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void populateListView(){
        Cursor cursor = myDB.getAllRows();
        String[] fromFieldNames = new String[]{
                DBAdapter.KEY_DATE, DBAdapter.KEY_DESCRIPTION, DBAdapter.KEY_CATEGORY, DBAdapter.KEY_AMOUNT
        };
        int[] toViewIDs = new int[]{
                R.id.text_date, R.id.text_description,R.id.text_category, R.id.text_amount
        };
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.lvMainAct);
        myList.setAdapter(myCursorAdapter);

    }
    public void refreshListView(View view){
        populateListView();
    }

    private void listViewItemClick(){
        ListView myList = (ListView) findViewById(R.id.lvMainAct);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDB.deleteRow(id);
                populateListView();
            }
        });
    }


}

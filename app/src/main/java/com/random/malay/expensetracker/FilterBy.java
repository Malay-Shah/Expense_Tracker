package com.random.malay.expensetracker;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class FilterBy extends ActionBarActivity {

    DBAdapter myDB;
    TextView displayTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_by);
        Spinner dropdown = (Spinner)findViewById(R.id.spinnerFilter);
        String[] items = new String[]{
                "Entertainment","Rent", "Transport", "Groceries","Travel", "Electricity", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        dropdown.setAdapter(adapter);
        openDB();
        populateListView();
        displayTotal = (TextView) findViewById(R.id.displayTotal);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerFilter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String selected = spinner.getSelectedItem().toString();
                filterBySelected(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }
    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    private void populateListView(){
        Cursor cursor = myDB.getAllRows();
        String[] fromFieldNames = new String[]{
                DBAdapter.KEY_DATE, DBAdapter.KEY_DESCRIPTION, DBAdapter.KEY_CATEGORY, DBAdapter.KEY_AMOUNT, DBAdapter.KEY_PAIDBY
        };
        int[] toViewIDs = new int[]{
                R.id.text_date, R.id.text_description,R.id.text_category, R.id.text_amount, R.id.tvPaidBy
        };
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.lvFilter);
        myList.setAdapter(myCursorAdapter);

    }
    public void filterBySelected(String category){
        Cursor cursor = myDB.getRowsThatContain(category);
        String[] fromFieldNames = new String[]{
                DBAdapter.KEY_DATE, DBAdapter.KEY_DESCRIPTION, DBAdapter.KEY_CATEGORY, DBAdapter.KEY_AMOUNT, DBAdapter.KEY_PAIDBY
        };
        int[] toViewIDs = new int[]{
                R.id.text_date, R.id.text_description,R.id.text_category, R.id.text_amount, R.id.tvPaidBy
        };
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.row_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.lvFilter);
        myList.setAdapter(myCursorAdapter);
        int total = myDB.getTotal(category);
        displayTotal.setText("Total = " + total);
    }


}

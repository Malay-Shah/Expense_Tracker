package com.random.malay.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    DBAdapter myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
        populateListView();
        listViewItemLongClick();

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

    private void listViewItemLongClick(){
        ListView myList = (ListView) findViewById(R.id.lvMainAct);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                myDB.deleteRow(id);
                populateListView();
                return false;
            }
        });
    }
    public void addAll(View view){

        int total = myDB.getTotal();
        Toast.makeText(this,"" +total,Toast.LENGTH_LONG).show();
    }

}

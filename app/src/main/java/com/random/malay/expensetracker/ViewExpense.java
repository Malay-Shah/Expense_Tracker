package com.random.malay.expensetracker;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ViewExpense extends ActionBarActivity {

    EditText etWeekStart, etWeekEnd, etDay;
    TextView tvTotal;
    int mYear, mMonth,mDay;
    Spinner dropdown;
    DBAdapter myDB;
    CheckBox cbCash, cbCredit, cbDebit;
    int recentClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);
        cbCash = (CheckBox) findViewById(R.id.cbCash);
        cbCredit = (CheckBox) findViewById(R.id.cbCredit);
        cbDebit = (CheckBox) findViewById(R.id.cbDebit);
        dropdown = (Spinner)findViewById(R.id.spinnerMonth);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        String[] items = new String[]{
                "January","February","March","April","May","June","July","August","September","October","November","December"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        dropdown.setAdapter(adapter);
        etDay = (EditText) findViewById(R.id.etSortDay);


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
                        String myFormat = "dd/MM/yy";
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
        listViewItemLongClick();
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
        String cash = "",credit = "",debit = "";
        if(cbCash.isChecked()){
            cash = "Cash";
        }
        if(cbCredit.isChecked()){
            credit = "Credit";
        }
        if(cbDebit.isChecked()){
            debit = "Debit";
        }

        Cursor cursor = myDB.getRowsFromMonth(monthNum,cash,credit,debit);
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
        recentClicked=0;
        int total = myDB.getTotalMonth(month, cash, credit, debit);
        tvTotal.setText("Total = " + total);
    }

    public void goToDay(View view){
        String cash = "",credit = "",debit = "";
        if(cbCash.isChecked()){
            cash = "Cash";
        }
        if(cbCredit.isChecked()){
            credit = "Credit";
        }
        if(cbDebit.isChecked()){
            debit = "Debit";
        }
        String day = etDay.getText().toString();
        Cursor cursor = myDB.getRowsFromDate(day, cash, credit, debit);

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
        recentClicked=1;
        int total = myDB.getTotalDate(day, cash, debit, credit);
        tvTotal.setText("Total = " + total);
    }

    public void bGoToPayment(View view){
        String cash = "",credit = "",debit = "";
        if(cbCash.isChecked()){
            cash = "Cash";
        }
        if(cbCredit.isChecked()){
            credit = "Credit";
        }
        if(cbDebit.isChecked()){
            debit = "Debit";
        }
        String day = etDay.getText().toString();
        Cursor cursor = myDB.getRowsFromPayment(cash, credit, debit);

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
        recentClicked=2;
        int total = myDB.getTotal(cash,credit,debit);
        tvTotal.setText("Total = " + total);
    }

    private void listViewItemLongClick(){
        ListView myList = (ListView) findViewById(R.id.lvSortBy);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                myDB.deleteRow(id);
                if(recentClicked==0){
                    Button button1 = (Button) findViewById(R.id.bGoMonth);
                    button1.performClick();
                }else if(recentClicked == 1){
                    Button button1 = (Button) findViewById(R.id.bGoDay);
                    button1.performClick();
                }else{
                    Button button1 = (Button) findViewById(R.id.bGoPayment);
                    button1.performClick();
                }
                return false;
            }
        });
    }
}

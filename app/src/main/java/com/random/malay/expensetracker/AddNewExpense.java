package com.random.malay.expensetracker;


import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddNewExpense extends ActionBarActivity {

    int mYear, mMonth,mDay;
    EditText date;
    DBAdapter myDB;
    EditText etDate, etDescription, etAmount;
    Spinner spinnerCategory;
    RadioButton radCredit,radDebit, radCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_expense);
        date = (EditText) findViewById(R.id.etDate);
        radCash = (RadioButton) findViewById(R.id.radCash);
        radCredit = (RadioButton) findViewById(R.id.radCredit);
        radDebit = (RadioButton) findViewById(R.id.radDebit);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog mDatePicker = new DatePickerDialog(AddNewExpense.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "dd/MM/yy"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
                        date.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        // Dropdown list for Category

        Spinner dropdown = (Spinner)findViewById(R.id.spinnerCategory);
        String[] items = new String[]{
                "Entertainment","Rent", "Transport", "Groceries","Travel", "Electricity", "Other"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        dropdown.setAdapter(adapter);

        // Initialize EditTexts
        etDate = (EditText) findViewById(R.id.etDate);
        etDescription = (EditText) findViewById(R.id.etDescription);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        etAmount = (EditText) findViewById(R.id.etAmount);

        openDB();
    }

    private void openDB(){
        myDB = new DBAdapter(this);
        myDB.open();
    }

    public void addExpenseToDb(View view){
        String dateIn = etDate.getText().toString();
        String descriptionIn = etDescription.getText().toString();
        String categoryIn = spinnerCategory.getSelectedItem().toString();
        Integer amountIn = 0;
        String paidBy;
        if(radDebit.isChecked()){
            paidBy = "Debit";
        }else if(radCredit.isChecked()){
            paidBy = "Credit";
        }else{
            paidBy = "Cash";
        }
        boolean check = false;
        if(etAmount.getText().length() != 0){
            amountIn = Integer.parseInt( etAmount.getText().toString() );
            check = true;
        }
        if(!TextUtils.isEmpty(dateIn) && !TextUtils.isEmpty(descriptionIn) && !TextUtils.isEmpty(categoryIn) && check) {
            myDB.insertRow(dateIn, descriptionIn, categoryIn, amountIn, paidBy);
            Toast.makeText(this, "Expense Created" ,Toast.LENGTH_SHORT).show();
            etAmount.setText("");
            etDescription.setText("");
        }else{
            Toast.makeText(this,"Fill in all information!",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(AddNewExpense.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radCash:
                if (checked)
                    break;
            case R.id.radCredit:
                if (checked)
                    break;
            case R.id.radDebit:
                if (checked)
                    break;
        }
    }
}

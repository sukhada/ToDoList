package com.example.skulkarni.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {
    Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String currValue = getIntent().getStringExtra("currValue");

        position = Integer.parseInt(getIntent().getStringExtra("position"));
        EditText val = (EditText) findViewById(R.id.editText);

        // update date of datepicker based on saved date
        String currDate = getIntent().getStringExtra("currDueDate");
        DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        Date currDueDate = new Date();
        try {
            currDueDate = df.parse(currDate);
        } catch (ParseException e) {
            currDueDate = new Date();
        }
        DatePicker dueDate = (DatePicker) findViewById(R.id.todo_item_edit_due_date);
        int year = currDueDate.getYear() + 1900;
        int month = currDueDate.getMonth();
        int day = currDueDate.getDate();
        dueDate.updateDate(year, month, day);

        val.setText(currValue);
        val.setSelection(val.getText().length());
    }

    public void onSaveItem(View v) {
        EditText editItem = (EditText) findViewById(R.id.editText);
        DatePicker dueDate = (DatePicker) findViewById(R.id.todo_item_edit_due_date);
        String selectedDate = new Date(dueDate.getYear() - 1900, dueDate.getMonth(), dueDate.getDayOfMonth()).toString();
        String itemText = editItem.getText().toString();

        Intent data = new Intent();
        data.putExtra("editedVal", editItem.getText().toString());
        data.putExtra("newDueDate", selectedDate);
        data.putExtra("position", position);
        if (!itemText.isEmpty()) {
            editItem.setText("");
        }
        setResult(200, data);
        finish();
    }
}

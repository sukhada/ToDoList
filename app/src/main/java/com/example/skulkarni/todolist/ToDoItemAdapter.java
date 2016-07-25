package com.example.skulkarni.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by skulkarni on 7/24/16.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    private ArrayList<ToDoItem> toDoItemList;

    public ToDoItemAdapter(Context context, ArrayList<ToDoItem> todoitems) {
        super(context, 0, todoitems);
        this.toDoItemList = todoitems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDoItem toDoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }
        // Lookup view for data population
        TextView editItem = (TextView) convertView.findViewById(R.id.todo_item_edit_text);
        TextView dueDate = (TextView) convertView.findViewById(R.id.todo_item_due_date);
        DateFormat df = new SimpleDateFormat("EEE MMM d yyyy");
        // Populate the data into the template view using the data object
        editItem.setText(toDoItem.text);
        dueDate.setText(df.format(toDoItem.dueDate).toString());
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public ToDoItem getItem(int position) {
        return toDoItemList.get(position);
    }

    @Override
    public int getCount() {
        return toDoItemList.size();
    }
}

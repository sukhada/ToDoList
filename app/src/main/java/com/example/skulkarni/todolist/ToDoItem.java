package com.example.skulkarni.todolist;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by skulkarni on 7/24/16.
 */
public class ToDoItem implements Serializable {
    public String text;
    public Date dueDate;

    public ToDoItem(String text, Date dueDate) {
        this.text = text;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return this.text + ',' + this.dueDate.toString();
    }

    public static ToDoItem fromString(String s) {
        String[] vals = s.split("\\s*,\\s*");
        DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        Date newDate = new Date();
        try {
            newDate = df.parse(vals[1]);
        } catch (ParseException e) {

        }

        ToDoItem parsedToDoItem = new ToDoItem(vals[0], newDate);
        return parsedToDoItem;
    }
}

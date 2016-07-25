package com.example.skulkarni.todolist;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class todolist extends AppCompatActivity {
    private ArrayList<ToDoItem> items;
    private ToDoItemAdapter itemsAdapter;
    private SwipeMenuListView listView;

    private static final String TAG = todolist.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_todolist);

        items = new ArrayList<ToDoItem>();
        readItems();
        itemsAdapter = new ToDoItemAdapter(this, items);
        listView = (SwipeMenuListView) findViewById(R.id.lvItems);


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(120);
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                openItem.setIcon(android.R.drawable.ic_menu_edit);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(120);
                // set a icon
                deleteItem.setIcon(android.R.drawable.ic_menu_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent i = new Intent(todolist.this, EditItemActivity.class);
                        i.putExtra("currValue", items.get(position).text.toString());
                        i.putExtra("currDueDate", items.get(position).dueDate.toString());
                        i.putExtra("position", String.valueOf(position));
                        startActivityForResult(i, 1); // brings up the second activity
                        break;
                    case 1:
                        // delyete
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        break;
                }
                return false;
            }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        Date now = new Date();

        if (!etNewItem.getText().toString().isEmpty()) {
            ToDoItem item = new ToDoItem(etNewItem.getText().toString(), now);
            items.add(item);
            itemsAdapter.notifyDataSetChanged();
            // itemsAdapter.add(item);
            etNewItem.setText("");
            writeItems();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == 200 && requestCode == 1) {
            // Extract name value from result extras

            Bundle b = data.getExtras();
            String edited = b.getString("editedVal");
            Integer pos = b.getInt("position");
            DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            String dueDateStr = b.getString("newDueDate");
            Date newDate = new Date();
            try {
                newDate = df.parse(dueDateStr);
            } catch(ParseException e) {

                newDate = new Date();
            }

            // TODO Update actual item instead of removing it and adding in a new one
            items.remove(pos.intValue());
            itemsAdapter.notifyDataSetChanged();
            ToDoItem item = new ToDoItem(edited, newDate);
            items.add(item);
            itemsAdapter.notifyDataSetChanged();
            writeItems();

            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateItemAtPosition(int position) {
        int visiblePosition = listView.getFirstVisiblePosition();
        View view = listView.getChildAt(position - visiblePosition);
        View posView = listView.getAdapter().getView(position, view, listView);
    }


    private void readItems() {
        File filesDir = getFilesDir();
        File toDoFile = new File(filesDir, "test.txt");
        try {
            items = new ArrayList<ToDoItem>();
            //items = new ArrayList<ToDoItem>(FileUtils.readLines(toDoFile));
            List<String> lines = FileUtils.readLines(toDoFile);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                ToDoItem newItem = ToDoItem.fromString(line);
                items.add(newItem);
            }
        } catch(IOException e) {
            items = new ArrayList<ToDoItem>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File toDoFile = new File(filesDir, "test.txt");
        try {
            ArrayList<String> lines = new ArrayList<String>();
            for (int i = 0; i < items.size(); i++) {
                ToDoItem item = items.get(i);
                String itemStr = item.toString();
                lines.add(itemStr);
            }
            FileUtils.writeLines(toDoFile, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

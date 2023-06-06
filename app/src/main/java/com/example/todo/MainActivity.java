// Copyright (c) Kosmas Temperekidis
// 
// This software is released under the MIT License.
// https://opensource.org/licenses/MIT

package com.example.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText inputText;
    ListView listView;
    ArrayList<String> list;
    SharedPreferences prefs;
    final String prefs_name = "my_prefs";
    final String key_list = "my_list";
    Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to the button, text input, and list view
        button = findViewById(R.id.addButton);
        inputText = findViewById(R.id.inputText);
        listView = findViewById(R.id.listView);
        clearButton = findViewById(R.id.clearButton);
        // Initialize an empty list to hold the items
        list = new ArrayList<>();

        // Create an adapter to display the list items in the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        // Load saved items from SharedPreferences
        prefs = getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        String savedList = prefs.getString(key_list,"" );
        if (!savedList.isEmpty()) {
            String[] savedItems = savedList.split("\n");
            list.addAll(Arrays.asList(savedItems));
            adapter.notifyDataSetChanged();
        }


        // Add a long-click listener to the list view items to prompt the user for deletion confirmation
        listView.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    // Create a new alert dialog to ask the user for confirmation
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this entry?");

                    // If the user confirms deletion, delete the item from the list
                    builder.setPositiveButton("Yes", (dialog, which) -> deleteItem(position));

                    // If the user cancels deletion, do nothing and close the dialog
                    builder.setNegativeButton("No", null);
                    builder.show();
                    return true;
                }
        );

        // Set an onClick listener to clear the list when the clear button is clicked
        clearButton.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Confirm Clear");
                    builder.setMessage("Are you sure you want to clear the list? This act is permanent and not reversible");

                    // If the user confirms deletion, delete the item from the list
                    builder.setPositiveButton("Yes", (dialog, which) -> clearList());

                    // If the user cancels deletion, do nothing and close the dialog
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
        );



    }

    // Method called when the user clicks the "Add" button
    public void onClickAdd(View v) {
        String text = inputText.getText().toString().trim();

        // If the input text is not empty, add it to the list and update the display
        if (!text.equals("")) {
            list.add(text);


            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
            inputText.setText("");

            // Save the updated list to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key_list, String.join("\n", list));
            editor.apply();
        }
    }

    // Method to delete an item from the list
    private void deleteItem(int position) {
        list.remove(position);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        // Save the updated list to SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key_list, String.join("\n", list));
        editor.apply();
    }

    public void clearList() {
        list.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        // Save the updated list to SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key_list, String.join("\n", list));
        editor.apply();

    }
}

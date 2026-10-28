package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    // =========================================
    // UI COMPONENTS
    // =========================================

    private EditText editItemName;
    private EditText editQuantity;

    private Spinner spinnerUnit;

    private Button btnAddItem;
    private Button btnUpdateItem;
    private Button btnDeleteItem;
    private Button btnBack;

    private RecyclerView recyclerPantry;
    private TextView txtEmptyPantry;


    // =========================================
    // DATABASE
    // =========================================

    private PantryDatabaseHelper databaseHelper;


    // =========================================
    // RECYCLER VIEW
    // =========================================

    private ArrayList<PantryItem> pantryItems;
    private PantryAdapter pantryAdapter;


    // =========================================
    // UNITS
    // =========================================

    private final String[] units = {
            "Select unit",
            "pieces",
            "g",
            "kg",
            "ml",
            "L",
            "cups",
            "tbsp",
            "tsp"
    };


    // =========================================
    // ON CREATE
    // =========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_pantry
        );


        // =====================================
        // CONNECT PANTRY VIEWS
        // =====================================

        editItemName =
                findViewById(
                        R.id.editItemName
                );

        editQuantity =
                findViewById(
                        R.id.editQuantity
                );

        spinnerUnit =
                findViewById(
                        R.id.spinnerUnit
                );

        btnAddItem =
                findViewById(
                        R.id.btnAddItem
                );

        btnUpdateItem =
                findViewById(
                        R.id.btnUpdateItem
                );

        btnDeleteItem =
                findViewById(
                        R.id.btnDeleteItem
                );

        btnBack =
                findViewById(
                        R.id.btnBack
                );

        recyclerPantry =
                findViewById(
                        R.id.recyclerPantry
                );

        txtEmptyPantry =
                findViewById(
                        R.id.txtEmptyPantry
                );


        // =====================================
        // DATABASE
        // =====================================

        databaseHelper =
                new PantryDatabaseHelper(this);


        // =====================================
        // SETUP
        // =====================================

        setupUnitSpinner();

        setupRecyclerView();

        loadPantryItems();


        // =====================================
        // ADD ITEM BUTTON
        // =====================================

        btnAddItem.setOnClickListener(v ->
                addItem()
        );


        // =====================================
        // UPDATE ITEM BUTTON
        // =====================================

        btnUpdateItem.setOnClickListener(v ->
                updateItem()
        );


        // =====================================
        // DELETE ITEM BUTTON
        // =====================================

        btnDeleteItem.setOnClickListener(v ->
                deleteItem()
        );


        // =====================================
        // BACK BUTTON
        // =====================================

        btnBack.setOnClickListener(v ->
                finish()
        );
    }


    // =========================================
    // SETUP UNIT SPINNER
    // =========================================

    private void setupUnitSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(
                adapter
        );
    }


    // =========================================
    // SETUP RECYCLER VIEW
    // =========================================

    private void setupRecyclerView() {

        pantryItems =
                new ArrayList<>();

        pantryAdapter =
                new PantryAdapter(
                        pantryItems
                );

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerPantry.setAdapter(
                pantryAdapter
        );
    }


    // =========================================
    // ADD ITEM
    // =========================================

    private void addItem() {

        String itemName =
                editItemName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                editQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                spinnerUnit
                        .getSelectedItem()
                        .toString();


        // Check ingredient name

        if (itemName.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter an ingredient name",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Check quantity

        if (quantityText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter a quantity",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Check unit

        if (unit.equals("Select unit")) {

            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Convert quantity text to number

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Quantity must be a valid number",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // Quantity must be positive

        if (quantity <= 0) {

            Toast.makeText(
                    this,
                    "Quantity must be greater than 0",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =====================================
        // INSERT INTO DATABASE
        // =====================================

        SQLiteDatabase db =
                databaseHelper.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                PantryDatabaseHelper.COLUMN_ITEM,
                itemName
        );

        values.put(
                PantryDatabaseHelper.COLUMN_QUANTITY,
                quantity
        );

        values.put(
                PantryDatabaseHelper.COLUMN_UNIT,
                unit
        );


        long result =
                db.insert(
                        PantryDatabaseHelper.TABLE_PANTRY,
                        null,
                        values
                );


        if (result != -1) {

            Toast.makeText(
                    this,
                    "Item added",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

            loadPantryItems();

        } else {

            Toast.makeText(
                    this,
                    "Could not add item",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================
    // UPDATE ITEM
    // =========================================

    private void updateItem() {

        String itemName =
                editItemName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                editQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                spinnerUnit
                        .getSelectedItem()
                        .toString();


        if (itemName.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter the item name to update",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        if (quantityText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter the new quantity",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        if (unit.equals("Select unit")) {

            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Quantity must be a valid number",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        if (quantity <= 0) {

            Toast.makeText(
                    this,
                    "Quantity must be greater than 0",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        // =====================================
        // UPDATE DATABASE
        // =====================================

        SQLiteDatabase db =
                databaseHelper.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                PantryDatabaseHelper.COLUMN_QUANTITY,
                quantity
        );

        values.put(
                PantryDatabaseHelper.COLUMN_UNIT,
                unit
        );


        int rows =
                db.update(
                        PantryDatabaseHelper.TABLE_PANTRY,
                        values,
                        PantryDatabaseHelper.COLUMN_ITEM +
                                " = ? COLLATE NOCASE",
                        new String[]{
                                itemName
                        }
                );


        if (rows > 0) {

            Toast.makeText(
                    this,
                    "Item updated",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

            loadPantryItems();

        } else {

            Toast.makeText(
                    this,
                    "Item not found",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================
    // DELETE ITEM
    // =========================================

    private void deleteItem() {

        String itemName =
                editItemName
                        .getText()
                        .toString()
                        .trim();


        if (itemName.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter the item name to delete",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        SQLiteDatabase db =
                databaseHelper.getWritableDatabase();


        int rows =
                db.delete(
                        PantryDatabaseHelper.TABLE_PANTRY,
                        PantryDatabaseHelper.COLUMN_ITEM +
                                " = ? COLLATE NOCASE",
                        new String[]{
                                itemName
                        }
                );


        if (rows > 0) {

            Toast.makeText(
                    this,
                    "Item deleted",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

            loadPantryItems();

        } else {

            Toast.makeText(
                    this,
                    "Item not found",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    // =========================================
    // LOAD PANTRY ITEMS
    // =========================================

    private void loadPantryItems() {

        pantryItems.clear();


        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();


        Cursor cursor =
                db.query(
                        PantryDatabaseHelper.TABLE_PANTRY,
                        null,
                        null,
                        null,
                        null,
                        null,
                        PantryDatabaseHelper.COLUMN_ID +
                                " DESC"
                );


        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(
                            cursor.getColumnIndexOrThrow(
                                    PantryDatabaseHelper.COLUMN_ID
                            )
                    );


            String itemName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    PantryDatabaseHelper.COLUMN_ITEM
                            )
                    );


            double quantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    PantryDatabaseHelper.COLUMN_QUANTITY
                            )
                    );


            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    PantryDatabaseHelper.COLUMN_UNIT
                            )
                    );


            PantryItem pantryItem =
                    new PantryItem(
                            id,
                            itemName,
                            quantity,
                            unit
                    );


            pantryItems.add(
                    pantryItem
            );
        }


        cursor.close();


        pantryAdapter.notifyDataSetChanged();


        // =====================================
        // EMPTY PANTRY FEEDBACK
        // =====================================

        if (pantryItems.isEmpty()) {

            txtEmptyPantry.setVisibility(
                    View.VISIBLE
            );

            recyclerPantry.setVisibility(
                    View.GONE
            );

        } else {

            txtEmptyPantry.setVisibility(
                    View.GONE
            );

            recyclerPantry.setVisibility(
                    View.VISIBLE
            );
        }
    }


    // =========================================
    // CLEAR INPUT FIELDS
    // =========================================

    private void clearFields() {

        editItemName.setText("");

        editQuantity.setText("");

        spinnerUnit.setSelection(0);
    }
}
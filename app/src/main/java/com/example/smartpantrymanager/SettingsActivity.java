package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    // UI components
    private EditText editUserName;
    private CheckBox checkVegetarian;
    private Button btnSaveSettings;
    private Button btnSettingsBack;

    // SharedPreferences
    private SharedPreferences preferences;

    // SharedPreferences file name
    private static final String PREFS_NAME = "SmartPantrySettings";

    // Keys
    private static final String KEY_NAME = "userName";
    private static final String KEY_VEGETARIAN = "vegetarian";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect Java to activity_settings.xml
        setContentView(R.layout.activity_settings);


        // -------------------------------------
        // CONNECT XML VIEWS
        // -------------------------------------

        editUserName = findViewById(R.id.editUserName);

        checkVegetarian = findViewById(R.id.checkVegetarian);

        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        btnSettingsBack = findViewById(R.id.btnSettingsBack);


        // -------------------------------------
        // OPEN SHARED PREFERENCES
        // -------------------------------------

        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );


        // Load previously saved settings
        loadSettings();


        // -------------------------------------
        // SAVE BUTTON
        // -------------------------------------

        btnSaveSettings.setOnClickListener(v -> saveSettings());


        // -------------------------------------
        // BACK BUTTON
        // -------------------------------------

        btnSettingsBack.setOnClickListener(v -> finish());
    }


    // =========================================
    // SAVE SETTINGS
    // =========================================

    private void saveSettings() {

        // Get name from EditText
        String userName = editUserName
                .getText()
                .toString()
                .trim();


        // Get checkbox value
        boolean vegetarianOnly =
                checkVegetarian.isChecked();


        // Save the values
        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putString(
                KEY_NAME,
                userName
        );

        editor.putBoolean(
                KEY_VEGETARIAN,
                vegetarianOnly
        );

        editor.apply();


        // Show confirmation
        Toast.makeText(
                this,
                "Settings saved successfully",
                Toast.LENGTH_SHORT
        ).show();
    }


    // =========================================
    // LOAD SETTINGS
    // =========================================

    private void loadSettings() {

        // Get saved name
        String savedName =
                preferences.getString(
                        KEY_NAME,
                        ""
                );


        // Get saved vegetarian setting
        boolean savedVegetarian =
                preferences.getBoolean(
                        KEY_VEGETARIAN,
                        false
                );


        // Display saved values
        editUserName.setText(savedName);

        checkVegetarian.setChecked(savedVegetarian);
    }
}
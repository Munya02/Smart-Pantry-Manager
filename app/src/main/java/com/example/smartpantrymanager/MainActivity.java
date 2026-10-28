package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private Button btnPantry;
    private Button btnRecipes;
    private Button btnSettings;

    private SharedPreferences preferences;

    private static final java.lang.String PREFS_NAME =
            "SmartPantrySettings";

    private static final java.lang.String KEY_NAME =
            "userName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtWelcome = findViewById(R.id.txtWelcome);
        btnPantry = findViewById(R.id.btnPantry);
        btnRecipes = findViewById(R.id.btnRecipes);
        btnSettings = findViewById(R.id.btnSettings);

        preferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        btnPantry.setOnClickListener(v -> {
            Intent intent =
                    new Intent(
                            MainActivity.this,
                            PantryActivity.class
                    );

            startActivity(intent);
        });

        btnRecipes.setOnClickListener(v -> {
            Intent intent =
                    new Intent(
                            MainActivity.this,
                            RecipeActivity.class
                    );

            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.menuHome) {
            return true;
        }

        if (itemId == R.id.menuPantry) {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            PantryActivity.class
                    )
            );

            return true;
        }

        if (itemId == R.id.menuRecipes) {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            RecipeActivity.class
                    )
            );

            return true;
        }

        if (itemId == R.id.menuSettings) {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    )
            );

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayUserName();
    }

    private void displayUserName() {

        java.lang.String userName =
                preferences.getString(
                        KEY_NAME,
                        ""
                );

        if (userName == null ||
                userName.trim().isEmpty()) {

            txtWelcome.setText(
                    R.string.welcome
            );

        } else {

            java.lang.String welcomeMessage =
                    "Welcome, " +
                            userName +
                            "!";

            txtWelcome.setText(
                    welcomeMessage
            );
        }
    }
}
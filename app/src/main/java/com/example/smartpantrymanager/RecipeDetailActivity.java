package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    // TextView that displays the recipe name
    private TextView txtRecipeName;

    // TextView that displays the ingredients
    private TextView txtIngredients;

    // TextView that displays the cooking instructions
    private TextView txtInstructions;

    // Back button
    private Button btnDetailBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect this Activity to activity_recipe_detail.xml
        setContentView(R.layout.activity_recipe_detail);


        // =====================================
        // CONNECT JAVA VARIABLES TO XML VIEWS
        // =====================================

        txtRecipeName =
                findViewById(
                        R.id.txtRecipeName
                );

        txtIngredients =
                findViewById(
                        R.id.txtIngredients
                );

        txtInstructions =
                findViewById(
                        R.id.txtInstructions
                );

        btnDetailBack =
                findViewById(
                        R.id.btnDetailBack
                );


        // =====================================
        // GET RECIPE INFORMATION
        // =====================================

        String recipeName =
                getIntent().getStringExtra(
                        "recipeName"
                );

        String ingredients =
                getIntent().getStringExtra(
                        "ingredients"
                );

        String instructions =
                getIntent().getStringExtra(
                        "instructions"
                );


        // =====================================
        // DISPLAY RECIPE INFORMATION
        // =====================================

        txtRecipeName.setText(
                recipeName
        );

        txtIngredients.setText(
                ingredients
        );

        txtInstructions.setText(
                instructions
        );


        // =====================================
        // BACK BUTTON
        // =====================================

        btnDetailBack.setOnClickListener(v ->
                finish()
        );
    }
}
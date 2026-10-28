package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {

    private LinearLayout recipeContainer;
    private TextView txtNoRecipes;
    private Button btnRecipeBack;

    private PantryDatabaseHelper databaseHelper;

    private static final String PREFS_NAME =
            "SmartPantrySettings";

    private static final String KEY_VEGETARIAN =
            "vegetarian";


    // =========================================
    // SMALL DATA CLASSES
    // =========================================

    private static class PantryIngredient {

        String name;
        double quantity;
        String unit;

        PantryIngredient(
                String name,
                double quantity,
                String unit
        ) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }


    private static class RecipeIngredient {

        String name;
        double quantity;
        String unit;

        RecipeIngredient(
                String name,
                double quantity,
                String unit
        ) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }


    // =========================================
    // ON CREATE
    // =========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe
        );

        recipeContainer =
                findViewById(
                        R.id.recipeContainer
                );

        txtNoRecipes =
                findViewById(
                        R.id.txtNoRecipes
                );

        btnRecipeBack =
                findViewById(
                        R.id.btnRecipeBack
                );

        databaseHelper =
                new PantryDatabaseHelper(this);

        btnRecipeBack.setOnClickListener(v ->
                finish()
        );

        loadSuggestedRecipes();
    }


    // =========================================
    // LOAD SUGGESTED RECIPES
    // =========================================

    private void loadSuggestedRecipes() {

        recipeContainer.removeAllViews();

        ArrayList<PantryIngredient> pantryIngredients =
                getPantryIngredients();


        // Read vegetarian setting
        SharedPreferences preferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        boolean vegetarianOnly =
                preferences.getBoolean(
                        KEY_VEGETARIAN,
                        false
                );


        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor recipeCursor =
                db.query(
                        PantryDatabaseHelper.TABLE_RECIPES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        PantryDatabaseHelper.COLUMN_RECIPE_NAME
                                + " ASC"
                );

        int matchingRecipeCount = 0;


        while (recipeCursor.moveToNext()) {

            int recipeId =
                    recipeCursor.getInt(
                            recipeCursor
                                    .getColumnIndexOrThrow(
                                            PantryDatabaseHelper
                                                    .COLUMN_RECIPE_ID
                                    )
                    );

            String recipeName =
                    recipeCursor.getString(
                            recipeCursor
                                    .getColumnIndexOrThrow(
                                            PantryDatabaseHelper
                                                    .COLUMN_RECIPE_NAME
                                    )
                    );

            String instructions =
                    recipeCursor.getString(
                            recipeCursor
                                    .getColumnIndexOrThrow(
                                            PantryDatabaseHelper
                                                    .COLUMN_INSTRUCTIONS
                                    )
                    );

            int vegetarianValue =
                    recipeCursor.getInt(
                            recipeCursor
                                    .getColumnIndexOrThrow(
                                            PantryDatabaseHelper
                                                    .COLUMN_VEGETARIAN
                                    )
                    );

            boolean recipeIsVegetarian =
                    vegetarianValue == 1;


            // =====================================
            // VEGETARIAN FILTER
            // =====================================

            if (vegetarianOnly &&
                    !recipeIsVegetarian) {

                continue;
            }


            // =====================================
            // GET RECIPE INGREDIENTS
            // =====================================

            ArrayList<RecipeIngredient> requiredIngredients =
                    getRecipeIngredients(
                            recipeId
                    );


            // =====================================
            // STRICT MATCHING
            // =====================================

            boolean canMakeRecipe =
                    hasAllIngredients(
                            pantryIngredients,
                            requiredIngredients
                    );

            if (!canMakeRecipe) {

                continue;
            }


            matchingRecipeCount++;


            // =====================================
            // CREATE RECIPE BUTTON
            // =====================================

            Button recipeButton =
                    new Button(this);

            recipeButton.setText(
                    recipeName
            );

            recipeButton.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )
            );

            LinearLayout.LayoutParams buttonParams =
                    (LinearLayout.LayoutParams)
                            recipeButton.getLayoutParams();

            buttonParams.setMargins(
                    0,
                    0,
                    0,
                    12
            );

            recipeButton.setLayoutParams(
                    buttonParams
            );


            // =====================================
            // RECIPE DETAIL INGREDIENT TEXT
            // =====================================

            String ingredientText =
                    createIngredientText(
                            requiredIngredients
                    );


            recipeButton.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                RecipeActivity.this,
                                RecipeDetailActivity.class
                        );

                intent.putExtra(
                        "recipeName",
                        recipeName
                );

                intent.putExtra(
                        "ingredients",
                        ingredientText
                );

                intent.putExtra(
                        "instructions",
                        instructions
                );

                startActivity(intent);
            });


            recipeContainer.addView(
                    recipeButton
            );
        }


        recipeCursor.close();


        // =====================================
        // ZERO MATCH FEEDBACK
        // =====================================

        if (matchingRecipeCount == 0) {

            txtNoRecipes.setVisibility(
                    View.VISIBLE
            );

            txtNoRecipes.setText(
                    R.string.no_matching_recipes
            );

        } else {

            txtNoRecipes.setVisibility(
                    View.GONE
            );
        }
    }


    // =========================================
    // GET PANTRY INGREDIENTS
    // =========================================

    private ArrayList<PantryIngredient>
    getPantryIngredients() {

        ArrayList<PantryIngredient> pantryIngredients =
                new ArrayList<>();

        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor cursor =
                db.query(
                        PantryDatabaseHelper.TABLE_PANTRY,

                        new String[]{
                                PantryDatabaseHelper.COLUMN_ITEM,
                                PantryDatabaseHelper.COLUMN_QUANTITY,
                                PantryDatabaseHelper.COLUMN_UNIT
                        },

                        null,
                        null,
                        null,
                        null,
                        null
                );


        while (cursor.moveToNext()) {

            String name =
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


            pantryIngredients.add(
                    new PantryIngredient(
                            normalizeIngredientName(name),
                            quantity,
                            normalizeUnit(unit)
                    )
            );
        }


        cursor.close();

        return pantryIngredients;
    }


    // =========================================
    // GET INGREDIENTS FOR ONE RECIPE
    // =========================================

    private ArrayList<RecipeIngredient>
    getRecipeIngredients(
            int recipeId
    ) {

        ArrayList<RecipeIngredient> ingredients =
                new ArrayList<>();

        SQLiteDatabase db =
                databaseHelper.getReadableDatabase();

        Cursor cursor =
                db.query(
                        PantryDatabaseHelper
                                .TABLE_RECIPE_INGREDIENTS,

                        new String[]{
                                PantryDatabaseHelper
                                        .COLUMN_INGREDIENT_NAME,

                                PantryDatabaseHelper
                                        .COLUMN_REQUIRED_QUANTITY,

                                PantryDatabaseHelper
                                        .COLUMN_REQUIRED_UNIT
                        },

                        PantryDatabaseHelper
                                .COLUMN_INGREDIENT_RECIPE_ID
                                + " = ?",

                        new String[]{
                                String.valueOf(recipeId)
                        },

                        null,
                        null,
                        null
                );


        while (cursor.moveToNext()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    PantryDatabaseHelper
                                            .COLUMN_INGREDIENT_NAME
                            )
                    );

            double quantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow(
                                    PantryDatabaseHelper
                                            .COLUMN_REQUIRED_QUANTITY
                            )
                    );

            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    PantryDatabaseHelper
                                            .COLUMN_REQUIRED_UNIT
                            )
                    );


            ingredients.add(
                    new RecipeIngredient(
                            normalizeIngredientName(name),
                            quantity,
                            normalizeUnit(unit)
                    )
            );
        }


        cursor.close();

        return ingredients;
    }


    // =========================================
    // STRICT RECIPE MATCHING
    // =========================================

    private boolean hasAllIngredients(
            ArrayList<PantryIngredient> pantryIngredients,
            ArrayList<RecipeIngredient> requiredIngredients
    ) {

        if (requiredIngredients.isEmpty()) {

            return false;
        }


        // Check EVERY ingredient required
        // by the recipe.
        for (RecipeIngredient required :
                requiredIngredients) {

            double totalAvailable = 0;

            boolean foundCompatibleIngredient =
                    false;


            // Search the user's pantry.
            for (PantryIngredient pantry :
                    pantryIngredients) {

                if (!pantry.name.equals(
                        required.name
                )) {

                    continue;
                }


                double convertedQuantity =
                        convertQuantity(
                                pantry.quantity,
                                pantry.unit,
                                required.unit
                        );


                // -1 means the units cannot
                // safely be compared.
                if (convertedQuantity < 0) {

                    continue;
                }


                foundCompatibleIngredient = true;

                totalAvailable +=
                        convertedQuantity;
            }


            // Ingredient missing completely.
            if (!foundCompatibleIngredient) {

                return false;
            }


            // Ingredient exists but there
            // is not enough of it.
            if (totalAvailable <
                    required.quantity) {

                return false;
            }
        }


        return true;
    }


    // =========================================
    // NORMALIZE INGREDIENT NAMES
    // =========================================

    private String normalizeIngredientName(
            String ingredient
    ) {

        if (ingredient == null) {

            return "";
        }


        String name =
                ingredient
                        .trim()
                        .toLowerCase();


        // Handle common plural forms used
        // by this application's recipes.

        if (name.equals("eggs")) {
            return "egg";
        }

        if (name.equals("tomatoes")) {
            return "tomato";
        }

        if (name.equals("potatoes")) {
            return "potato";
        }

        if (name.equals("breads")) {
            return "bread";
        }

        if (name.equals("chickens")) {
            return "chicken";
        }

        if (name.equals("cheeses")) {
            return "cheese";
        }

        if (name.equals("pastas")) {
            return "pasta";
        }

        if (name.equals("tunas")) {
            return "tuna";
        }

        if (name.equals("butters")) {
            return "butter";
        }

        if (name.equals("milks")) {
            return "milk";
        }

        if (name.equals("rices")) {
            return "rice";
        }


        return name;
    }


    // =========================================
    // NORMALIZE UNITS
    // =========================================

    private String normalizeUnit(
            String unit
    ) {

        if (unit == null) {

            return "";
        }


        String normalized =
                unit
                        .trim()
                        .toLowerCase();


        if (normalized.equals("piece")) {
            return "pieces";
        }

        if (normalized.equals("cup")) {
            return "cups";
        }

        if (normalized.equals("tablespoon")) {
            return "tbsp";
        }

        if (normalized.equals("tablespoons")) {
            return "tbsp";
        }

        if (normalized.equals("teaspoon")) {
            return "tsp";
        }

        if (normalized.equals("teaspoons")) {
            return "tsp";
        }

        if (normalized.equals("litre") ||
                normalized.equals("liter") ||
                normalized.equals("litres") ||
                normalized.equals("liters")) {

            return "l";
        }


        return normalized;
    }


    // =========================================
    // UNIT CONVERSION
    // =========================================

    private double convertQuantity(
            double quantity,
            String fromUnit,
            String toUnit
    ) {

        fromUnit =
                normalizeUnit(fromUnit);

        toUnit =
                normalizeUnit(toUnit);


        // Same unit
        if (fromUnit.equals(toUnit)) {

            return quantity;
        }


        // =====================================
        // MASS
        // =====================================

        // kg -> g
        if (fromUnit.equals("kg") &&
                toUnit.equals("g")) {

            return quantity * 1000;
        }


        // g -> kg
        if (fromUnit.equals("g") &&
                toUnit.equals("kg")) {

            return quantity / 1000;
        }


        // =====================================
        // VOLUME
        // =====================================

        // L -> ml
        if (fromUnit.equals("l") &&
                toUnit.equals("ml")) {

            return quantity * 1000;
        }


        // ml -> L
        if (fromUnit.equals("ml") &&
                toUnit.equals("l")) {

            return quantity / 1000;
        }


        // cups -> ml
        if (fromUnit.equals("cups") &&
                toUnit.equals("ml")) {

            return quantity * 250;
        }


        // ml -> cups
        if (fromUnit.equals("ml") &&
                toUnit.equals("cups")) {

            return quantity / 250;
        }


        // tbsp -> ml
        if (fromUnit.equals("tbsp") &&
                toUnit.equals("ml")) {

            return quantity * 15;
        }


        // ml -> tbsp
        if (fromUnit.equals("ml") &&
                toUnit.equals("tbsp")) {

            return quantity / 15;
        }


        // tsp -> ml
        if (fromUnit.equals("tsp") &&
                toUnit.equals("ml")) {

            return quantity * 5;
        }


        // ml -> tsp
        if (fromUnit.equals("ml") &&
                toUnit.equals("tsp")) {

            return quantity / 5;
        }


        // Units are incompatible.
        return -1;
    }


    // =========================================
    // CREATE INGREDIENT DISPLAY TEXT
    // =========================================

    private String createIngredientText(
            ArrayList<RecipeIngredient> ingredients
    ) {

        StringBuilder builder =
                new StringBuilder();


        for (RecipeIngredient ingredient :
                ingredients) {

            builder
                    .append("• ")
                    .append(
                            formatQuantity(
                                    ingredient.quantity
                            )
                    )
                    .append(" ")
                    .append(
                            ingredient.unit
                    )
                    .append(" ")
                    .append(
                            capitalizeFirstLetter(
                                    ingredient.name
                            )
                    )
                    .append("\n");
        }


        return builder
                .toString()
                .trim();
    }


    // =========================================
    // FORMAT QUANTITY
    // =========================================

    private String formatQuantity(
            double quantity
    ) {

        if (quantity ==
                Math.floor(quantity)) {

            return String.valueOf(
                    (int) quantity
            );
        }


        return String.valueOf(
                quantity
        );
    }


    // =========================================
    // CAPITALIZE INGREDIENT NAME
    // =========================================

    private String capitalizeFirstLetter(
            String text
    ) {

        if (text == null ||
                text.isEmpty()) {

            return "";
        }


        return text
                .substring(0, 1)
                .toUpperCase()
                +
                text.substring(1);
    }


    // =========================================
    // REFRESH SCREEN
    // =========================================

    @Override
    protected void onResume() {

        super.onResume();


        if (databaseHelper != null) {

            loadSuggestedRecipes();
        }
    }
}
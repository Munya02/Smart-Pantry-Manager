package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    // =========================================
    // DATABASE INFORMATION
    // =========================================

    private static final String DATABASE_NAME =
            "pantry.db";

    // Version 4 adds quantity and unit
    // requirements to recipe ingredients
    private static final int DATABASE_VERSION =
            4;


    // =========================================
    // PANTRY TABLE
    // =========================================

    public static final String TABLE_PANTRY =
            "pantry_items";

    public static final String COLUMN_ID =
            "id";

    public static final String COLUMN_ITEM =
            "item_name";

    public static final String COLUMN_QUANTITY =
            "quantity";

    public static final String COLUMN_UNIT =
            "unit";


    // =========================================
    // RECIPES TABLE
    // =========================================

    public static final String TABLE_RECIPES =
            "recipes";

    public static final String COLUMN_RECIPE_ID =
            "recipe_id";

    public static final String COLUMN_RECIPE_NAME =
            "recipe_name";

    public static final String COLUMN_INSTRUCTIONS =
            "instructions";

    public static final String COLUMN_VEGETARIAN =
            "vegetarian";


    // =========================================
    // RECIPE INGREDIENTS TABLE
    // =========================================

    public static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String COLUMN_RECIPE_INGREDIENT_ID =
            "recipe_ingredient_id";

    public static final String COLUMN_INGREDIENT_RECIPE_ID =
            "recipe_id";

    public static final String COLUMN_INGREDIENT_NAME =
            "ingredient_name";

    public static final String COLUMN_REQUIRED_QUANTITY =
            "required_quantity";

    public static final String COLUMN_REQUIRED_UNIT =
            "required_unit";


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public PantryDatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }


    // =========================================
    // CREATE DATABASE
    // =========================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        createPantryTable(db);

        createRecipeTables(db);

        seedRecipes(db);
    }


    // =========================================
    // CREATE PANTRY TABLE
    // =========================================

    private void createPantryTable(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " +
                        TABLE_PANTRY +
                        " (" +

                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_ITEM +
                        " TEXT NOT NULL, " +

                        COLUMN_QUANTITY +
                        " REAL NOT NULL, " +

                        COLUMN_UNIT +
                        " TEXT NOT NULL DEFAULT 'pieces'" +

                        ")";

        db.execSQL(createPantryTable);
    }


    // =========================================
    // CREATE RECIPE TABLES
    // =========================================

    private void createRecipeTables(SQLiteDatabase db) {

        String createRecipesTable =
                "CREATE TABLE " +
                        TABLE_RECIPES +
                        " (" +

                        COLUMN_RECIPE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_RECIPE_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_INSTRUCTIONS +
                        " TEXT NOT NULL, " +

                        COLUMN_VEGETARIAN +
                        " INTEGER NOT NULL DEFAULT 1" +

                        ")";

        db.execSQL(createRecipesTable);


        String createRecipeIngredientsTable =
                "CREATE TABLE " +
                        TABLE_RECIPE_INGREDIENTS +
                        " (" +

                        COLUMN_RECIPE_INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_INGREDIENT_RECIPE_ID +
                        " INTEGER NOT NULL, " +

                        COLUMN_INGREDIENT_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_REQUIRED_QUANTITY +
                        " REAL NOT NULL, " +

                        COLUMN_REQUIRED_UNIT +
                        " TEXT NOT NULL, " +

                        "FOREIGN KEY (" +
                        COLUMN_INGREDIENT_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" +
                        COLUMN_RECIPE_ID +
                        ")" +

                        ")";

        db.execSQL(createRecipeIngredientsTable);
    }


    // =========================================
    // DATABASE UPGRADE
    // =========================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        // Version 1 -> 2
        // Add unit to pantry
        if (oldVersion < 2) {

            db.execSQL(
                    "ALTER TABLE " +
                            TABLE_PANTRY +
                            " ADD COLUMN " +
                            COLUMN_UNIT +
                            " TEXT NOT NULL DEFAULT 'pieces'"
            );
        }


        // Version 2 -> 3
        // Create recipe tables
        if (oldVersion < 3) {

            createRecipeTablesVersion3(db);

            seedRecipesVersion3(db);
        }


        // Version 3 -> 4
        // Recreate ONLY recipe tables.
        // Pantry data remains untouched.
        if (oldVersion < 4) {

            db.execSQL(
                    "DROP TABLE IF EXISTS " +
                            TABLE_RECIPE_INGREDIENTS
            );

            db.execSQL(
                    "DROP TABLE IF EXISTS " +
                            TABLE_RECIPES
            );

            createRecipeTables(db);

            seedRecipes(db);
        }
    }


    // =========================================
    // VERSION 3 TABLES
    // Used only when upgrading very old DBs
    // =========================================

    private void createRecipeTablesVersion3(
            SQLiteDatabase db
    ) {

        String createRecipesTable =
                "CREATE TABLE " +
                        TABLE_RECIPES +
                        " (" +

                        COLUMN_RECIPE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_RECIPE_NAME +
                        " TEXT NOT NULL, " +

                        COLUMN_INSTRUCTIONS +
                        " TEXT NOT NULL, " +

                        COLUMN_VEGETARIAN +
                        " INTEGER NOT NULL DEFAULT 1" +

                        ")";

        db.execSQL(createRecipesTable);


        String createRecipeIngredientsTable =
                "CREATE TABLE " +
                        TABLE_RECIPE_INGREDIENTS +
                        " (" +

                        COLUMN_RECIPE_INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        COLUMN_INGREDIENT_RECIPE_ID +
                        " INTEGER NOT NULL, " +

                        COLUMN_INGREDIENT_NAME +
                        " TEXT NOT NULL" +

                        ")";

        db.execSQL(createRecipeIngredientsTable);
    }


    // =========================================
    // VERSION 3 SEED
    // Only needed for upgrade compatibility
    // =========================================

    private void seedRecipesVersion3(
            SQLiteDatabase db
    ) {

        // Nothing needs to remain here because
        // version 4 immediately recreates and
        // reseeds the recipe tables.
    }


    // =========================================
    // SEED VERSION 4 RECIPES
    // =========================================

    private void seedRecipes(SQLiteDatabase db) {

        // 1
        addRecipe(
                db,
                "Toast",
                new String[]{"bread"},
                new double[]{1},
                new String[]{"pieces"},
                "1. Place the bread in a toaster.\n" +
                        "2. Toast until golden brown.\n" +
                        "3. Serve while warm.",
                true
        );


        // 2
        addRecipe(
                db,
                "Egg Fried Rice",
                new String[]{"rice", "egg"},
                new double[]{1, 1},
                new String[]{"cups", "pieces"},
                "1. Cook the rice if needed.\n" +
                        "2. Heat a pan.\n" +
                        "3. Add the egg and scramble it.\n" +
                        "4. Add the rice.\n" +
                        "5. Mix well and serve.",
                true
        );


        // 3
        addRecipe(
                db,
                "Cheese Toast",
                new String[]{"bread", "cheese"},
                new double[]{1, 50},
                new String[]{"pieces", "g"},
                "1. Place cheese on the bread.\n" +
                        "2. Toast until the bread is golden.\n" +
                        "3. Allow the cheese to melt.\n" +
                        "4. Serve while warm.",
                true
        );


        // 4
        addRecipe(
                db,
                "Scrambled Eggs",
                new String[]{"egg", "milk"},
                new double[]{2, 50},
                new String[]{"pieces", "ml"},
                "1. Crack the eggs into a bowl.\n" +
                        "2. Add milk.\n" +
                        "3. Mix well.\n" +
                        "4. Cook in a pan while stirring.\n" +
                        "5. Serve when cooked.",
                true
        );


        // 5
        addRecipe(
                db,
                "Egg Toast",
                new String[]{"bread", "egg"},
                new double[]{1, 1},
                new String[]{"pieces", "pieces"},
                "1. Toast the bread.\n" +
                        "2. Cook the egg in a pan.\n" +
                        "3. Place the egg on the toast.\n" +
                        "4. Serve while warm.",
                true
        );


        // 6
        addRecipe(
                db,
                "Cheese Omelette",
                new String[]{"egg", "cheese", "milk"},
                new double[]{2, 50, 50},
                new String[]{"pieces", "g", "ml"},
                "1. Crack the eggs into a bowl.\n" +
                        "2. Add milk and mix.\n" +
                        "3. Pour into a heated pan.\n" +
                        "4. Add cheese.\n" +
                        "5. Fold the omelette and serve.",
                true
        );


        // 7
        addRecipe(
                db,
                "Cheesy Rice",
                new String[]{"rice", "cheese"},
                new double[]{1, 50},
                new String[]{"cups", "g"},
                "1. Cook the rice.\n" +
                        "2. Add cheese while the rice is hot.\n" +
                        "3. Mix until the cheese melts.\n" +
                        "4. Serve while warm.",
                true
        );


        // 8
        addRecipe(
                db,
                "French Toast",
                new String[]{"bread", "egg", "milk"},
                new double[]{2, 1, 100},
                new String[]{"pieces", "pieces", "ml"},
                "1. Crack the egg into a bowl.\n" +
                        "2. Add milk and mix.\n" +
                        "3. Dip the bread into the mixture.\n" +
                        "4. Cook both sides in a pan until golden.\n" +
                        "5. Serve while warm.",
                true
        );


        // 9
        addRecipe(
                db,
                "Chicken Fried Rice",
                new String[]{"rice", "chicken"},
                new double[]{1, 150},
                new String[]{"cups", "g"},
                "1. Cook the chicken and rice.\n" +
                        "2. Cut the chicken into pieces.\n" +
                        "3. Heat a pan.\n" +
                        "4. Add the chicken and rice.\n" +
                        "5. Mix well and serve.",
                false
        );


        // 10
        addRecipe(
                db,
                "Boiled Eggs",
                new String[]{"egg"},
                new double[]{2},
                new String[]{"pieces"},
                "1. Place the eggs in a pot.\n" +
                        "2. Cover them with water.\n" +
                        "3. Boil until cooked.\n" +
                        "4. Cool, peel and serve.",
                true
        );


        // 11
        addRecipe(
                db,
                "Rice and Eggs",
                new String[]{"rice", "egg"},
                new double[]{1, 2},
                new String[]{"cups", "pieces"},
                "1. Cook the rice.\n" +
                        "2. Cook the eggs in a pan.\n" +
                        "3. Serve the eggs with the rice.",
                true
        );


        // 12
        addRecipe(
                db,
                "Grilled Cheese Sandwich",
                new String[]{"bread", "cheese", "butter"},
                new double[]{2, 50, 10},
                new String[]{"pieces", "g", "g"},
                "1. Butter the bread.\n" +
                        "2. Place cheese between two slices.\n" +
                        "3. Grill in a pan until golden.\n" +
                        "4. Turn over and cook the other side.\n" +
                        "5. Serve warm.",
                true
        );


        // 13
        addRecipe(
                db,
                "Tomato Toast",
                new String[]{"bread", "tomato"},
                new double[]{1, 1},
                new String[]{"pieces", "pieces"},
                "1. Toast the bread.\n" +
                        "2. Slice the tomato.\n" +
                        "3. Place the tomato on the toast.\n" +
                        "4. Serve immediately.",
                true
        );


        // 14
        addRecipe(
                db,
                "Tomato Omelette",
                new String[]{"egg", "tomato"},
                new double[]{2, 1},
                new String[]{"pieces", "pieces"},
                "1. Crack and mix the eggs.\n" +
                        "2. Chop the tomato.\n" +
                        "3. Pour the eggs into a heated pan.\n" +
                        "4. Add the tomato.\n" +
                        "5. Cook until the eggs are set.",
                true
        );


        // 15
        addRecipe(
                db,
                "Chicken and Rice",
                new String[]{"chicken", "rice"},
                new double[]{150, 1},
                new String[]{"g", "cups"},
                "1. Cook the rice.\n" +
                        "2. Cook the chicken thoroughly.\n" +
                        "3. Serve the chicken with the rice.",
                false
        );


        // 16
        addRecipe(
                db,
                "Tuna Sandwich",
                new String[]{"bread", "tuna"},
                new double[]{2, 100},
                new String[]{"pieces", "g"},
                "1. Drain the tuna.\n" +
                        "2. Place the tuna on a slice of bread.\n" +
                        "3. Add another slice of bread.\n" +
                        "4. Cut and serve.",
                false
        );


        // 17
        addRecipe(
                db,
                "Mashed Potatoes",
                new String[]{"potato", "milk", "butter"},
                new double[]{2, 100, 20},
                new String[]{"pieces", "ml", "g"},
                "1. Peel and boil the potatoes.\n" +
                        "2. Drain the water.\n" +
                        "3. Add milk and butter.\n" +
                        "4. Mash until smooth.",
                true
        );


        // 18
        addRecipe(
                db,
                "Potato Omelette",
                new String[]{"potato", "egg"},
                new double[]{1, 2},
                new String[]{"pieces", "pieces"},
                "1. Cook the potato until soft.\n" +
                        "2. Cut it into small pieces.\n" +
                        "3. Mix with beaten egg.\n" +
                        "4. Cook the mixture in a pan until set.",
                true
        );


        // 19
        addRecipe(
                db,
                "Tomato Pasta",
                new String[]{"pasta", "tomato"},
                new double[]{100, 1},
                new String[]{"g", "pieces"},
                "1. Cook the pasta.\n" +
                        "2. Chop and cook the tomato.\n" +
                        "3. Add the cooked pasta.\n" +
                        "4. Mix together and serve.",
                true
        );


        // 20
        addRecipe(
                db,
                "Cheese Pasta",
                new String[]{"pasta", "cheese"},
                new double[]{100, 50},
                new String[]{"g", "g"},
                "1. Cook the pasta.\n" +
                        "2. Drain the water.\n" +
                        "3. Add cheese while the pasta is hot.\n" +
                        "4. Mix until the cheese melts.\n" +
                        "5. Serve warm.",
                true
        );
    }


    // =========================================
    // ADD ONE RECIPE
    // =========================================

    private void addRecipe(
            SQLiteDatabase db,
            String recipeName,
            String[] ingredients,
            double[] quantities,
            String[] units,
            String instructions,
            boolean vegetarian
    ) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put(
                COLUMN_RECIPE_NAME,
                recipeName
        );

        recipeValues.put(
                COLUMN_INSTRUCTIONS,
                instructions
        );

        recipeValues.put(
                COLUMN_VEGETARIAN,
                vegetarian ? 1 : 0
        );

        long recipeId =
                db.insert(
                        TABLE_RECIPES,
                        null,
                        recipeValues
                );

        if (recipeId == -1) {
            return;
        }


        for (int i = 0;
             i < ingredients.length;
             i++) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    COLUMN_INGREDIENT_RECIPE_ID,
                    recipeId
            );

            ingredientValues.put(
                    COLUMN_INGREDIENT_NAME,
                    ingredients[i]
                            .trim()
                            .toLowerCase()
            );

            ingredientValues.put(
                    COLUMN_REQUIRED_QUANTITY,
                    quantities[i]
            );

            ingredientValues.put(
                    COLUMN_REQUIRED_UNIT,
                    units[i]
            );

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    ingredientValues
            );
        }
    }
}
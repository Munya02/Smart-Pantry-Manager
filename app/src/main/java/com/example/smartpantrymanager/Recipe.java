package com.example.smartpantrymanager;

public class Recipe {

    // Recipe information
    private String name;
    private String[] ingredients;
    private String instructions;

    // Stores whether the recipe is vegetarian
    private boolean vegetarian;


    // Constructor
    public Recipe(
            String name,
            String[] ingredients,
            String instructions,
            boolean vegetarian
    ) {

        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.vegetarian = vegetarian;
    }


    // Return recipe name
    public String getName() {
        return name;
    }


    // Return ingredients
    public String[] getIngredients() {
        return ingredients;
    }


    // Return cooking instructions
    public String getInstructions() {
        return instructions;
    }


    // Return whether the recipe is vegetarian
    public boolean isVegetarian() {
        return vegetarian;
    }
}
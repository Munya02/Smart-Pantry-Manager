package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    // List of pantry items that will be displayed
    private final ArrayList<PantryItem> pantryItems;


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public PantryAdapter(ArrayList<PantryItem> pantryItems) {

        this.pantryItems = pantryItems;
    }


    // =========================================
    // CREATE EACH ROW
    // =========================================

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_pantry,
                                parent,
                                false
                        );


        return new PantryViewHolder(view);
    }


    // =========================================
    // PUT DATA INTO EACH ROW
    // =========================================

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position
    ) {

        // Get one pantry item
        PantryItem pantryItem =
                pantryItems.get(position);


        // Display ingredient name
        holder.txtItemName.setText(
                pantryItem.getItemName()
        );


        // Get quantity
        double quantity =
                pantryItem.getQuantity();


        // Format quantity
        String quantityText;

        if (quantity == Math.floor(quantity)) {

            quantityText =
                    String.valueOf(
                            (int) quantity
                    );

        } else {

            quantityText =
                    String.valueOf(quantity);
        }


        // Example:
        // Quantity: 2 cups

        String displayQuantity =
                "Quantity: " +
                        quantityText +
                        " " +
                        pantryItem.getUnit();


        holder.txtItemQuantity.setText(
                displayQuantity
        );
    }


    // =========================================
    // NUMBER OF ITEMS
    // =========================================

    @Override
    public int getItemCount() {

        return pantryItems.size();
    }


    // =========================================
    // VIEW HOLDER
    // =========================================

    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtItemName;
        TextView txtItemQuantity;


        public PantryViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);


            txtItemName =
                    itemView.findViewById(
                            R.id.txtItemName
                    );


            txtItemQuantity =
                    itemView.findViewById(
                            R.id.txtItemQuantity
                    );
        }
    }
}
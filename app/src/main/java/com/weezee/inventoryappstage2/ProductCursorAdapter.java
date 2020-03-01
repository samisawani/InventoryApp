package com.weezee.inventoryappstage2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry;

public class ProductCursorAdapter  extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {//this is a custom adapter that basically fill the list view and add a listener to the sell button
        View view=LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return view;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView productNameTextView = view.findViewById(R.id.product_name);
        TextView productPriceTextView = view.findViewById(R.id.price);
        TextView productQuantityTextView = view.findViewById(R.id.quantity);
        Button productSaleButton = view.findViewById(R.id.sell_button);

        final int columnIdIndex = cursor.getColumnIndex(ProductInventoryEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY);

        final int productID = cursor.getInt(columnIdIndex)-1;
        String productName = cursor.getString(productNameColumnIndex);
        int productPrice = cursor.getInt(productPriceColumnIndex);
        final int productQuantity = cursor.getInt(productQuantityColumnIndex);

        productSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) context;
                activity.sellAProduct(productID, productQuantity);
            }
        });

        productNameTextView.setText(productName);
        productPriceTextView.setText(String.valueOf(productPrice)+context.getString(R.string.unit_product_price));
        productQuantityTextView.setText(String.valueOf(productQuantity));
    }


}



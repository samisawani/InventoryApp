package com.weezee.inventoryappstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry.COLUMN_PRODUCT_NAME;
import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry.COLUMN_PRODUCT_PRICE;
import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY;
import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME;
import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER;
import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry.TABLE_NAME;
import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry._ID;

public class ProductInventoryDbHelper extends SQLiteOpenHelper {//this basically creates the database

    private static final String DATABASE_NAME = "bookStore.db";
    private static final int DATABASE_VERSION = 1;

    public ProductInventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_BOOKS_TABLE =  "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL , "
                + COLUMN_PRODUCT_SUPPLIER_NAME + " TEXT NOT NULL, "
                + COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER + " TEXT NOT NULL);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);    }
}

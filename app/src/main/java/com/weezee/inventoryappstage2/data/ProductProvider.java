package com.weezee.inventoryappstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Objects;

import static com.weezee.inventoryappstage2.data.ProductInventoryContract.*;

public class ProductProvider extends ContentProvider {// this is the intermediary layer between the database file and the main activity. it either acts directly  on the database because of a request from the MAIN ACTIVITY or give content from
    //the database to the loader which then give it to the cursor adapter and then to the list view
    private static final String TAG = "ProductProvider";
    private static final int PRODUCT_INVENTORY_TABLE = 100;

    private static final int PRODUCT_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCT_INVENTORY_TABLE, PRODUCT_INVENTORY_TABLE);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_PRODUCT_INVENTORY_TABLE + "/#", PRODUCT_ID);
    }

    private ProductInventoryDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ProductInventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PRODUCT_INVENTORY_TABLE:
                cursor = database.query(ProductInventoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductInventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(ProductInventoryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT_INVENTORY_TABLE:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertProduct(Uri uri, ContentValues values) {
        String productName = values.getAsString(ProductInventoryEntry.COLUMN_PRODUCT_NAME);
        if (productName == null|| productName.equals("")) {
            throw new IllegalArgumentException("Product requires a name");
        }

        Integer price = values.getAsInteger(ProductInventoryEntry.COLUMN_PRODUCT_PRICE);
        if (price == null ||price<=0) {
            throw new IllegalArgumentException("Product requires valid price");
        }

        Integer quantity = values.getAsInteger(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("product requires valid weight");
        }

        String supplierName = values.getAsString(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
        if (supplierName == null|| supplierName.equals("")) {
            throw new IllegalArgumentException("Product requires a name");
        }

        String supplierNumber = values.getAsString(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);
        if (supplierNumber == null|| supplierNumber.equals("")) {
            throw new IllegalArgumentException("Product requires a name");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ProductInventoryEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri.toString());
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT_INVENTORY_TABLE:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = ProductInventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(ProductInventoryEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductInventoryEntry.COLUMN_PRODUCT_NAME);
            if (name == null|| name.equals("")) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if (values.containsKey(ProductInventoryEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(ProductInventoryEntry.COLUMN_PRODUCT_PRICE);
            if (price == null || price <=0) {
                throw new IllegalArgumentException("Product requires valid price");
            }
        }

        if (values.containsKey(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer quantity = values.getAsInteger(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Product requires valid quantity");
            }
        }

        if (values.containsKey(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            if (supplierName == null|| supplierName.equals("")) {
                throw new IllegalArgumentException("Product requires a supplier name");
            }
        }

        if (values.containsKey(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER)) {
            String phoneNumber = values.getAsString(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);
            if (phoneNumber == null|| phoneNumber.equals("")) {
                throw new IllegalArgumentException("Product requires a supplier phone number");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(ProductInventoryEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT_INVENTORY_TABLE:
                rowsDeleted = database.delete(ProductInventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductInventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ProductInventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        if (rowsDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCT_INVENTORY_TABLE:
                return ProductInventoryEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductInventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    }


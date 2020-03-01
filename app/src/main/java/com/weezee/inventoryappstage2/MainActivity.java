package com.weezee.inventoryappstage2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import static com.weezee.inventoryappstage2.data.ProductInventoryContract.ProductInventoryEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PRODUCT_LOADER = 0;

    ProductCursorAdapter mCursorAdapter;//reads row by row and put them in the custom adapter of the listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        ListView productListView =  findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        mCursorAdapter = new ProductCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(ProductInventoryEntry.CONTENT_URI, id);// use content provider/ custom adapter to read the item's data from the database amd fill them in the EditorActivity
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }


    private void insertProduct() {//database-->content provider-->loader-->custom adapter-->list view
        ContentValues values = new ContentValues();
        values.put(ProductInventoryEntry.COLUMN_PRODUCT_NAME, "Lord of The Rings");
        values.put(ProductInventoryEntry.COLUMN_PRODUCT_PRICE, 13);
        values.put(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY,3);
        values.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Penguins");
        values.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, 78843654);
        //----------------------------------------------
        ContentValues values1 = new ContentValues();
        values1.put(ProductInventoryEntry.COLUMN_PRODUCT_NAME, "One Flew Over the Cuckoo's Nest");
        values1.put(ProductInventoryEntry.COLUMN_PRODUCT_PRICE, 10);
        values1.put(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY,22);
        values1.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "McGraw-Hill");
        values1.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, 79953125);
        //-----------------------------------------------
        ContentValues values2 = new ContentValues();
        values2.put(ProductInventoryEntry.COLUMN_PRODUCT_NAME, "Batman the Old G");
        values2.put(ProductInventoryEntry.COLUMN_PRODUCT_PRICE, 9999);
        values2.put(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY,1);
        values2.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, "Genesis Inspiration");
        values2.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, 79956127);
        //--------------------------------------------------


        Uri newUri = getContentResolver().insert(ProductInventoryEntry.CONTENT_URI, values);
        Uri newUri1 = getContentResolver().insert(ProductInventoryEntry.CONTENT_URI, values1);
        Uri newUri2 = getContentResolver().insert(ProductInventoryEntry.CONTENT_URI, values2);

    }


    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductInventoryEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertProduct();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProductInventoryEntry._ID,
                ProductInventoryEntry.COLUMN_PRODUCT_NAME,
                ProductInventoryEntry.COLUMN_PRODUCT_PRICE,
                ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY,
                ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER};

        return new CursorLoader(this,
                ProductInventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


    public void sellAProduct(int id, int quantity) {// if the product quantity is still greater than zero then check on the database and see if the row is updated. if so, then show qunatity changed else show error

        if (quantity > 0) {
            quantity--;
            ContentValues values = new ContentValues();
            values.put(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY, quantity);
            Uri updateUri = ContentUris.withAppendedId(ProductInventoryEntry.CONTENT_URI, id+1);
            if(getContentResolver().update(updateUri, values, null, null)>0)
            Toast.makeText(this, "Quantity  changed", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "Error changing quantity ", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "Out of stock!", Toast.LENGTH_SHORT).show();
        }
    }


}

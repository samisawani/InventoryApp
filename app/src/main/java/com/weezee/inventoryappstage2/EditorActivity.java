package com.weezee.inventoryappstage2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.weezee.inventoryappstage2.data.ProductInventoryContract.*;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private Uri mCurrentProductUri;

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private EditText mQuantityEditText;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneNumberEditText;


    private boolean mProductHasChanged = false;



    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };
    private  View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues values = new ContentValues();
            Integer currentQuantity;
            Button button=(Button)v;
            switch (button.getText().charAt(0)){
                case 'I':// increment case
                    if (mCurrentProductUri == null ) {
                        if (!TextUtils.isEmpty(mQuantityEditText.getText())){
                            currentQuantity=Integer.parseInt(mQuantityEditText.getText().toString());
                            currentQuantity++;
                            mQuantityEditText.setText(String.valueOf(currentQuantity) );
                        }
                        else {
                            currentQuantity=0;
                            mQuantityEditText.setText(String.valueOf(currentQuantity) );
                        }
                        break;
                    }
                    else{
                        if (!TextUtils.isEmpty(mQuantityEditText.getText())) {

                            currentQuantity = Integer.parseInt(mQuantityEditText.getText().toString());
                            currentQuantity++;
                            values.put(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY, currentQuantity);
                            mQuantityEditText.setText(String.valueOf(currentQuantity));

                            break;
                        } else {
                            currentQuantity = 0;
                            mQuantityEditText.setText(String.valueOf(currentQuantity));
                        }
                        break;
                    }

                case 'D': //decrement case
                    if (mCurrentProductUri == null ) {
                        if (!TextUtils.isEmpty(mQuantityEditText.getText())){
                            currentQuantity=Integer.parseInt(mQuantityEditText.getText().toString());
                            if (currentQuantity>0)currentQuantity--;
                            mQuantityEditText.setText(String.valueOf(currentQuantity) );
                        }
                        else {
                            currentQuantity=0;
                            mQuantityEditText.setText(String.valueOf(currentQuantity) );
                        }
                        break;
                    }
                    else{
                        if (!TextUtils.isEmpty(mQuantityEditText.getText())) {

                            currentQuantity = Integer.parseInt(mQuantityEditText.getText().toString());
                            if (currentQuantity>0)currentQuantity--;
                            values.put(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY, currentQuantity);
                            mQuantityEditText.setText(String.valueOf(currentQuantity));

                            break;
                        } else {
                            currentQuantity = 0;
                            mQuantityEditText.setText(String.valueOf(currentQuantity));
                        }
                        break;
                    }
                case 'C':   //call supplier case
                    if (TextUtils.isEmpty(mSupplierPhoneNumberEditText.getText())){
                        Toast.makeText(EditorActivity.this,"No number to call!" , Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+mSupplierPhoneNumberEditText.getText()));
                        startActivity(intent);}
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();


        if (mCurrentProductUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_product));


            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_product));


            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mNameEditText = findViewById(R.id.edit_product_name);
        mPriceEditText = findViewById(R.id.edit_product_price);
        mQuantityEditText = findViewById(R.id.edit_product_quantity);
        mSupplierNameEditText =  findViewById(R.id.edit_product_supplier_name);
        mSupplierPhoneNumberEditText =  findViewById(R.id.edit_product_supplier_phone_number);
        Button incrementButton= findViewById(R.id.increment_quantity);
        Button decrementButton= findViewById(R.id.decrement_quantity);
        Button callSupplierButton= findViewById(R.id.call_supplier);

        incrementButton.setOnClickListener(clickListener);
        decrementButton.setOnClickListener(clickListener);
        callSupplierButton.setOnClickListener(clickListener);
        incrementButton.setOnTouchListener(mTouchListener);
        decrementButton.setOnTouchListener(mTouchListener);
        callSupplierButton.setOnTouchListener(mTouchListener);
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);
    }



    private void saveProduct() {
        ContentValues values = new ContentValues();
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString= mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumber= mSupplierPhoneNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierNameString)||
                        TextUtils.isEmpty(supplierPhoneNumber)) {
            Toast.makeText(this, " make sure you fill all fields with valid values!", Toast.LENGTH_LONG).show();
            return;
        }

        else {
            values.put(ProductInventoryEntry.COLUMN_PRODUCT_NAME, nameString);
            values.put(ProductInventoryEntry.COLUMN_PRODUCT_PRICE, Integer.parseInt(priceString));
            values.put(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY, Integer.parseInt(quantityString));
            values.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierNameString);
            values.put(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);
        }
        if (mCurrentProductUri == null) {

            Uri newUri = getContentResolver().insert(ProductInventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveProduct();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }


                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }


        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
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
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(ProductInventoryEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierNumber = cursor.getString(supplierPhoneNumberColumnIndex);

            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneNumberEditText.setText(supplierNumber);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");
        mSupplierNameEditText.setSelection(0);
    }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (mCurrentProductUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}

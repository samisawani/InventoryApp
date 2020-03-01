package com.weezee.inventoryappstage2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
// this class basically defines wthe databsase path and it's name as well as the table name and the column names
public final class ProductInventoryContract {

    private ProductInventoryContract(){}

    static final String CONTENT_AUTHORITY = "com.weezee.inventoryappstage2.data";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_PRODUCT_INVENTORY_TABLE = "product_inventory";


    public static final class ProductInventoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCT_INVENTORY_TABLE);

        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_INVENTORY_TABLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCT_INVENTORY_TABLE;

        public final static String TABLE_NAME = "product_inventory";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_PRODUCT_NAME ="name";

        public final static String COLUMN_PRODUCT_PRICE = "price";

        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";


        public final static String COLUMN_PRODUCT_SUPPLIER_NAME = "supplier_name";

        public final static String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";



    }
}

package com.example.android.bookstoreapp.data;

import android.provider.BaseColumns;

/**
 * API Contract for the Pets app.
 */
public final class BookContract {

    // To prevent accidentally instantiating.
    private BookContract() {
    }

    /**
     * Inner class that defines constant values for the books database table.
     */
    public static final class BookEntry implements BaseColumns {

        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Product Name of the Book.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product_name";

        /**
         * Price of the pet.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Quantity of the Book.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Supplier Name of the Book.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_MAME = "supplier_name";

        /**
         * Supplier Phone Number of the Book.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

    }

}

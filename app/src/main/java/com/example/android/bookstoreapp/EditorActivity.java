package com.example.android.bookstoreapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookDbHelper;
import com.example.android.bookstoreapp.data.BookContract.BookEntry;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /**
     * EditText fields fromm books
     */
    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneNumberEditText;

    private boolean saveBook = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all views that we will need to read user input
        mProductNameEditText = (EditText) findViewById(R.id.edit_book_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_book_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_book_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_book_supplier_name);
        mSupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_book_supplier_phone_number);

    }

    /*
     * Get user input from editor and save new book in db
     */
    private void insertBook() {

        // Read from input fields
        // Use trim to eliminate white space
        String productNameString = mProductNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNumberString = mSupplierPhoneNumberEditText.getText().toString().trim();

        // Check if isn't no fill empty
        if (productNameString.matches("") || priceString.matches("") || quantityString.matches("") || supplierNameString.matches("") || supplierPhoneNumberString.matches("")) {
            Toast.makeText(this, getString(R.string.save_book_empty), Toast.LENGTH_SHORT).show();
        } else {
            int price = Integer.parseInt(priceString);
            int quantity = Integer.parseInt(quantityString);
            int supplierPhoneNumber = Integer.parseInt(supplierPhoneNumberString);

            // Create database helper
            BookDbHelper mDbHelper = new BookDbHelper(this);

            // Gets the database in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create ContentValues object
            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_PRODUCT_NAME, productNameString);
            values.put(BookEntry.COLUMN_PRICE, price);
            values.put(BookEntry.COLUMN_QUANTITY, quantity);
            values.put(BookEntry.COLUMN_SUPPLIER_MAME, supplierNameString);
            values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

            // Insert a new row in DB returning the ID.
            long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newRowId == -1) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.save_book_error), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getString(R.string.save_book_sucess, newRowId), Toast.LENGTH_SHORT).show();
                saveBook = true;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save Pet to the DB
                insertBook();
                // Exit activity if Book save
                if (saveBook) finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.android.bookstoreapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookDbHelper;
import com.example.android.bookstoreapp.data.BookContract.BookEntry;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;

    /**
     * Content URI for the existing book (null if it's a new book)
     */
    private Uri mCurrentBookUri;

    /**
     * EditText fields fromm books
     */
    private EditText mProductNameEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneNumberEditText;

    /**
     * Boolean flag of book changes (true) / (false)
     */
    private boolean mBookHasChanged = false;

    /**
     * OnTouchListener for modifying the view.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Check the intent used to launch this activity,
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();

        // If don't contain a book content URI, then create a new.
        if (mCurrentBookUri == null) {
            // New book, change the app bar to "Add a Book"
            setTitle(getString(R.string.editor_activity_title_new_book));

            // Invalidate the options menu, hidde "Delete" menu option.
            invalidateOptionsMenu();
        } else {
            // Change app bar to say "Edit Book"
            setTitle(getString(R.string.editor_activity_title_edit_book));

            // Initialize a loader to read the book data from the database
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }

        // Find views that we will need to read user input from
        mProductNameEditText = (EditText) findViewById(R.id.edit_book_product_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_book_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_book_quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_book_supplier_name);
        mSupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_book_supplier_phone_number);

        // Setup OnTouchListeners on all the input fields.
        mProductNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);

    }

    /**
     * Get user input and save book into database.
     */
    private void saveBook() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String sProductName = mProductNameEditText.getText().toString().trim();
        String sPrice = mPriceEditText.getText().toString().trim();
        String sQuantity = mQuantityEditText.getText().toString().trim();
        String sSupplierName = mSupplierNameEditText.getText().toString().trim();
        String sSupplierPhoneNumber = mSupplierPhoneNumberEditText.getText().toString().trim();

        // Check if all the fields in the editor are blank
        if (mCurrentBookUri == null &&
                TextUtils.isEmpty(sProductName) &&
                TextUtils.isEmpty(sPrice) &&
                TextUtils.isEmpty(sQuantity) &&
                TextUtils.isEmpty(sSupplierName) &&
                TextUtils.isEmpty(sSupplierPhoneNumber)) {
            // No fields were modified, return early without creating a new Book.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, sProductName);
        values.put(BookEntry.COLUMN_SUPPLIER_MAME, sSupplierName);
        // If the int is not provided don't parse use 0 by default.
        int iPrice = 0;
        int iQuantity = 0;
        int iSupplierPhoneNumber = 0;

        if (!TextUtils.isEmpty(sPrice)) {
            iPrice = Integer.parseInt(sPrice);
        }
        if (!TextUtils.isEmpty(sQuantity)) {
            iQuantity = Integer.parseInt(sQuantity);
        }
        if (!TextUtils.isEmpty(sSupplierPhoneNumber)) {
            iSupplierPhoneNumber = Integer.parseInt(sSupplierPhoneNumber);
        }

        values.put(BookEntry.COLUMN_PRICE, iPrice);
        values.put(BookEntry.COLUMN_QUANTITY, iQuantity);
        values.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, iSupplierPhoneNumber);

        // Check if is a new or existing book.
        if (mCurrentBookUri == null) {
            // This is a NEW book, insert into the provider,
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

            // Show a toast message depending successful.
            if (newUri == null) {
                // If the new content URI is null there was an error.
                Toast.makeText(this, getString(R.string.save_book_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful.
                Toast.makeText(this, getString(R.string.save_book_sucess),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // This is an EXISTING book, update it.
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);

            // Show a toast message depending successful.
            if (rowsAffected == 0) {
                // No rows were affected, then there was an error
                Toast.makeText(this, getString(R.string.update_book_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                // The update was successful.
                Toast.makeText(this, getString(R.string.update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method alows the menu updated.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If is a new book, hide the "Delete" item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option
        switch (item.getItemId()) {
            // Respond click on the "Save"
            case R.id.action_save:
                // Save book to database
                saveBook();
                // Exit activity
                finish();
                return true;
            // Respond click on the "Delete"
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond click on the "Up" arrow button
            case android.R.id.home:
                // If book no changed, continue navigating
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // If changes, setup dialog to warn the user
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show dialog notifies user for unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If no changes, continue
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // If there are unsaved changes, setup a dialog to warn the user.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Projection contains all columns
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_MAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        // Loader to execute ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentBookUri,        // Query the content URI
                projection,             // Columns to include
                null,          // No selection clause
                null,       // No selection arguments
                null);         // Default sort order
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Return if cursor is null or have less than 1 row
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Moving to the first row of the cursor
        if (cursor.moveToFirst()) {
            // Find the columns attributes
            int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_MAME);
            int supplierPhoneNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

            // Extract the value from the Cursor
            String productName = cursor.getString(productNameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            int supplierPhoneNumber = cursor.getInt(supplierPhoneNumberColumnIndex);

            // Update the views
            mProductNameEditText.setText(productName);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityEditText.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneNumberEditText.setText(Integer.toString(supplierPhoneNumber));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");
    }

    /**
     * Show dialog warns user for unsaved changes.
     *
     * @param discardButtonClickListener click listener what to do confirms discard changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create AlertDialog.Builder and set the message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // "Keep editing" dismiss the dialog and continue.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt user to confirm delete book.
     */
    private void showDeleteConfirmationDialog() {
        // Create AlertDialog.Builder, set the message and click listeners
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Delete".
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked "Cancel".
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Delete book from database.
     */
    private void deletePet() {
        // Check book exist.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the book.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            // Show toast message depending on successful.
            if (rowsDeleted == 0) {
                // No rows were deleted, error with the delete.
                Toast.makeText(this, getString(R.string.delete_book_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                // The delete was successful.
                Toast.makeText(this, getString(R.string.delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

}
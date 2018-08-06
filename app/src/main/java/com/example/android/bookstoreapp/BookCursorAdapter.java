package com.example.android.bookstoreapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookContract.BookEntry;

/**
 * {@link BookCursorAdapter}
 */
public class BookCursorAdapter extends CursorAdapter {

    private Context mContext;

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        mContext = context;
    }

    /**
     * Makes a new blank list item view..
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Binds book data to the given list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView productNameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        ImageView cart_btn = (ImageView) view.findViewById(R.id.cart_btn);

        // Find the attributes from columns
        int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
        final int id = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));

        // Read the attributes from the Cursor for the current book
        String bookProductName = cursor.getString(productNameColumnIndex);
        String bookQuantity = cursor.getString(quantityColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);

        final int iQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_QUANTITY));

        // Update the TextViews
        productNameTextView.setText(bookProductName);
        quantityTextView.setText(bookQuantity);
        priceTextView.setText(bookPrice);

        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (iQuantity > 0) {
                    int updateQuantity = iQuantity - 1;

                    // Get URI
                    Uri quantityUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);

                    // Update book
                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_QUANTITY, updateQuantity);
                    mContext.getContentResolver().update(quantityUri, values, null, null);
                    Toast.makeText(mContext, R.string.item_byed, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.out_stock, Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
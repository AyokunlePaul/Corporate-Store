package i.am.eipeks.corporatestore.welcome.activities;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.adapters.CartListAdapter;
import i.am.eipeks.corporatestore.welcome.classes.CartItemClass;
import i.am.eipeks.corporatestore.welcome.database.MyDatabaseHelper;

public class CartItems extends AppCompatActivity implements AdapterView.OnItemLongClickListener, View.OnClickListener {

    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<CartItemClass> cartItemClasses;
    private CartListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_items);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(this);
        sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        ListView cartListView = (ListView) findViewById(R.id.cart_items_list);
        cartListView.setLongClickable(true);

        ImageButton imageButton = (ImageButton) findViewById(R.id.print_receipt);
        imageButton.setOnClickListener(this);

        cartItemClasses = new ArrayList<>();

        adapter = new CartListAdapter(this, cartItemClasses);
        cartListView.setAdapter(adapter);

        Cursor cursor = sqLiteDatabase.query(
                MyDatabaseHelper.CART_TABLE_NAME,
                new String[]{MyDatabaseHelper.NAME_COLUMN, MyDatabaseHelper.TYPE_COLUMN,
                        MyDatabaseHelper.SIZE_COLUMN, MyDatabaseHelper.COLOR_COLUMN, MyDatabaseHelper.PRICE_COLUMN},
                null, null,null,null,null
        );

        if (cursor.moveToFirst()){
            do {
                try{
                    CartItemClass cartItems = new CartItemClass(
                            cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.NAME_COLUMN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.TYPE_COLUMN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLOR_COLUMN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.SIZE_COLUMN)),
                            cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.PRICE_COLUMN))
                    );
                    cartItemClasses.add(cartItems);
                    adapter.notifyDataSetChanged();
                } catch(SQLiteException e){
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cartListView.setOnItemLongClickListener(this);

        cursor.close();
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {

        final CartItemClass currentItem = cartItemClasses.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setTitle("Remove Item")
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {
                        cartItemClasses.remove(currentItem);
                        adapter.notifyDataSetChanged();

                        sqLiteDatabase.delete(MyDatabaseHelper.CART_TABLE_NAME,
                                MyDatabaseHelper.NAME_COLUMN + " = ? AND " + MyDatabaseHelper.ID + " = ?",
                                new String[]{currentItem.getName(), String.valueOf(position + 1)});

                        Snackbar.make(view, "Item removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cartItemClasses.add(position, currentItem);
                                adapter.notifyDataSetChanged();

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MyDatabaseHelper.NAME_COLUMN, currentItem.getName());
                                contentValues.put(MyDatabaseHelper.TYPE_COLUMN, currentItem.getType());
                                contentValues.put(MyDatabaseHelper.SIZE_COLUMN, currentItem.getSize());
                                contentValues.put(MyDatabaseHelper.COLOR_COLUMN, currentItem.getColor());
                                contentValues.put(MyDatabaseHelper.PRICE_COLUMN, currentItem.getPrice());

                                sqLiteDatabase.insert(MyDatabaseHelper.CART_TABLE_NAME, null, contentValues);

                            }
                        }).show();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.print_receipt:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Receipt");
//                        .setPositiveButton();
                break;
        }
    }
}

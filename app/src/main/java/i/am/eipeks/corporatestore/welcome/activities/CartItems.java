package i.am.eipeks.corporatestore.welcome.activities;


import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.adapters.CartListAdapter;
import i.am.eipeks.corporatestore.welcome.classes.CartItemClass;
import i.am.eipeks.corporatestore.welcome.database.MyDatabaseHelper;

public class CartItems extends AppCompatActivity{

    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<CartItemClass> cartItemClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_items);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(this);
        sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        ListView cartListView = (ListView) findViewById(R.id.cart_items_list);

        cartItemClasses = new ArrayList<>();

        CartListAdapter adapter = new CartListAdapter(this, cartItemClasses);
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

        cursor.close();
    }
}

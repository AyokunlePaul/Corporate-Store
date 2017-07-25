package i.am.eipeks.corporatestore.welcome.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.adapters.RecyclerAdapter;
import i.am.eipeks.corporatestore.welcome.classes.CorporateItem;
import i.am.eipeks.corporatestore.welcome.database.MyDatabaseHelper;

public class DisplayItems extends AppCompatActivity {

    String[] itemsTableColumnsToQuery = {
            MyDatabaseHelper.NAME_COLUMN, MyDatabaseHelper.COLOR_COLUMN, MyDatabaseHelper.CATEGORY_COLUMN,
            MyDatabaseHelper.PRICE_COLUMN, MyDatabaseHelper.QUANTITY_COLUMN, MyDatabaseHelper.SIZE_COLUMN,
            MyDatabaseHelper.DATE_RECEIVED, MyDatabaseHelper.TYPE_COLUMN, MyDatabaseHelper.SECTION_COLUMN
    };

    int childClicked;
    String categoryClicked = null, section = null;

    ArrayList<CorporateItem> corporateItemsAvailable, itemsToDisplay;
    ArrayList<String> category, sectionsList, nameList;

    MyDatabaseHelper myDatabaseHelper;
    SQLiteDatabase sqLiteDatabase;

    DatabaseReference mainDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_items_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        childClicked = getIntent().getIntExtra(Home.CHILD_CLICKED, 0);
        categoryClicked = getIntent().getExtras().getString(Home.CATEGORY_CLICKED);

        mainDatabase = FirebaseDatabase.getInstance().getReference();

        switch (categoryClicked){
            case "Double":
                switch (childClicked){
                    case 0:
                        section = "Bags and Clothes";
                        break;
                    case 1:
                        section = "Bags and Hats";
                        break;
                    case 2:
                        section = "Bags and Shoes";
                        break;
                    case 3:
                        section = "Bags and Slippers";
                        break;
                }
                break;
            case "Single":
                switch (childClicked){
                    case 0:
                        section = "Bags";
                        break;
                    case 1:
                        section = "Clothes";
                        break;
                    case 2:
                        section = "Hats";
                        break;
                    case 3:
                        section = "Jewelries";
                        break;
                    case 4:
                        section = "Shoes";
                        break;
                    case 5:
                        section = "Slippers";
                        break;
                }
        }

        corporateItemsAvailable = new ArrayList<>();
        itemsToDisplay = new ArrayList<>();
        category = new ArrayList<>();
        sectionsList = new ArrayList<>();
        nameList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, itemsToDisplay, getLayoutInflater());

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new GridSpacing(3, dpToPx(10), true));
        recyclerView.setAdapter(recyclerAdapter);

        myDatabaseHelper = new MyDatabaseHelper(this);
        sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        Cursor corporateItemsCursor = sqLiteDatabase.query(MyDatabaseHelper.TABLE_NAME, itemsTableColumnsToQuery,
                null,null,null,null,null);

        if (corporateItemsCursor.moveToFirst()){
            do {
                try{
                    CorporateItem newItem = new CorporateItem(
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.CATEGORY_COLUMN)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.SECTION_COLUMN)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.NAME_COLUMN)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.TYPE_COLUMN)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.COLOR_COLUMN)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.DATE_RECEIVED)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.QUANTITY_COLUMN)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.SIZE_COLUMN)),
                            corporateItemsCursor.getString(corporateItemsCursor.getColumnIndexOrThrow(MyDatabaseHelper.PRICE_COLUMN)));
                    corporateItemsAvailable.add(newItem);
                } catch (SQLiteException e){
                    e.printStackTrace();
                }
            } while (corporateItemsCursor.moveToNext());
        }

        for (CorporateItem corporateItem: corporateItemsAvailable){
            if (corporateItem.getCategory().equals(categoryClicked)){
                if (corporateItem.getSection().equals(section)){
                    itemsToDisplay.add(corporateItem);
                }
            }
        }
        corporateItemsCursor.close();
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_to_cart:
                Intent cartItemsActivity = new Intent(DisplayItems.this, CartItems.class);
                startActivity(cartItemsActivity);
                return true;
        }
        return false;
    }

    private class GridSpacing extends RecyclerView.ItemDecoration{

        private int spanCount, spacing;
        private boolean includeEdge;

        GridSpacing(int spanCount, int spacing, boolean includeEdge){
            this.spanCount = spanCount;
            this.includeEdge = includeEdge;
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
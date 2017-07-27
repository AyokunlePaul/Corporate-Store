package i.am.eipeks.corporatestore.welcome.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.adapters.SalesRecyclerAdapter;
import i.am.eipeks.corporatestore.welcome.classes.CorporateItem;
import i.am.eipeks.corporatestore.welcome.database.MyDatabaseHelper;

public class Sales extends AppCompatActivity {

    String[] itemsTableColumnsToQuery = {
            MyDatabaseHelper.NAME_COLUMN, MyDatabaseHelper.COLOR_COLUMN, MyDatabaseHelper.CATEGORY_COLUMN,
            MyDatabaseHelper.PRICE_COLUMN, MyDatabaseHelper.QUANTITY_COLUMN, MyDatabaseHelper.SIZE_COLUMN,
            MyDatabaseHelper.DATE_RECEIVED, MyDatabaseHelper.TYPE_COLUMN, MyDatabaseHelper.SECTION_COLUMN
    };

    private ArrayList<CorporateItem> corporateItemsAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        corporateItemsAvailable = new ArrayList<>();

        SalesRecyclerAdapter salesRecyclerAdapter = new SalesRecyclerAdapter(this, corporateItemsAvailable);

        RecyclerView recyclerViewSales = (RecyclerView) findViewById(R.id.recycler_view_sales);

        recyclerViewSales.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewSales.addItemDecoration(new GridSpacing(3, dpToPx(10), true));
        recyclerViewSales.setAdapter(salesRecyclerAdapter);

        MyDatabaseHelper helper = new MyDatabaseHelper(this);

        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

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
                    salesRecyclerAdapter.notifyDataSetChanged();
                } catch (SQLiteException e){
                    e.printStackTrace();
                }
            } while (corporateItemsCursor.moveToNext());
        }
        corporateItemsCursor.close();
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

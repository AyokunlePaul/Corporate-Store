package i.am.eipeks.corporatestore.welcome.activities;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.adapters.MyExpandableAdapter;
import i.am.eipeks.corporatestore.welcome.classes.CorporateItem;
import i.am.eipeks.corporatestore.welcome.classes.DataProvider;
import i.am.eipeks.corporatestore.welcome.database.MyDatabaseHelper;


public class Home extends AppCompatActivity
        implements ExpandableListView.OnChildClickListener,
        View.OnClickListener,
        DatePicker.OnDateChangedListener, SearchView.OnQueryTextListener {

    AutoCompleteTextView color;
    EditText quantity, size, price, productName;
    TextView dateTextView;
    Spinner category, type, section;
    DatePicker dateReceived;
    LinearLayout layout;

    TextInputLayout quantityTextInputLayout,
            sizeTextInputLayout, productNameTextInputLayout,
            colorTextInputLayout;

    MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    Cursor colorCursor, corporateItemsCursor;

    ArrayList<String> colorsAvailable;
    ArrayList<CorporateItem> corporateItemsAvailable;

    String[] itemsTableColumnsToQuery = {
            MyDatabaseHelper.NAME_COLUMN, MyDatabaseHelper.COLOR_COLUMN, MyDatabaseHelper.CATEGORY_COLUMN,
            MyDatabaseHelper.PRICE_COLUMN, MyDatabaseHelper.QUANTITY_COLUMN, MyDatabaseHelper.SIZE_COLUMN,
            MyDatabaseHelper.DATE_RECEIVED, MyDatabaseHelper.TYPE_COLUMN, MyDatabaseHelper.SECTION_COLUMN
    };

    ArrayAdapter colorAdapter;

    public static final String CATEGORY_CLICKED = "Category";
    public static final String CHILD_CLICKED = "Child";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        FirebaseApp.initializeApp(this);
//        mainDatabase = FirebaseDatabase.getInstance().getReference();

        colorsAvailable = new ArrayList<>();
        corporateItemsAvailable = new ArrayList<>();

        myDatabaseHelper = new MyDatabaseHelper(this);
        sqLiteDatabase = myDatabaseHelper.getWritableDatabase();

        colorCursor = sqLiteDatabase.query(MyDatabaseHelper.COLOR_TABLE_NAME,
                new String[]{MyDatabaseHelper.COLOR_TABLE_COLUMN},
        null, null, null, null, null);

        corporateItemsCursor = sqLiteDatabase.query(MyDatabaseHelper.TABLE_NAME,
                itemsTableColumnsToQuery,
                null, null, null, null, null);

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

        if (colorCursor.moveToFirst()){
            do {
                try{
                    String newColor = colorCursor.getString(colorCursor.getColumnIndexOrThrow(MyDatabaseHelper.COLOR_TABLE_COLUMN));
                    colorsAvailable.add(newColor);
                }catch (SQLiteException e){ e.printStackTrace(); }
            } while (colorCursor.moveToNext());
        }

        colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colorsAvailable);

        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
        HashMap<String, List<String>> header = DataProvider.getData();
        List<String> children = new ArrayList<>(header.keySet());

        FloatingActionButton addNewItem = (FloatingActionButton) findViewById(R.id.add_new_good);
        addNewItem.setOnClickListener(this);

        final MyExpandableAdapter adapter = new MyExpandableAdapter(this, header, children);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupExpandListener(
                new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) {
                        for (int i = 0; i < adapter.getGroupCount(); i++){
                            if (i != groupPosition){
                                expandableListView.collapseGroup(i);
                            }
                        }
//                        expandableListView.collapseGroup()
//                        RelativeLayout layout = (RelativeLayout) expandableListView.getChildAt(groupPosition);
//                        Log.i("Exception", Boolean.toString(layout == null));
//                        ImageView icon = (ImageView) layout.findViewById(R.id.icon);
//                        icon.setImageResource(R.drawable.ic_keyboard_arrow_up_48pt);
                        adapter.toggleIcon("up");
                    }
                });
        expandableListView.setOnGroupCollapseListener(
                new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        RelativeLayout layout = (RelativeLayout) expandableListView.getChildAt(groupPosition);
                        ImageView icon = (ImageView) layout.findViewById(R.id.icon);
                        icon.setImageResource(R.drawable.ic_keyboard_arrow_down_48pt);
                    }
                });

        colorCursor.close();
        corporateItemsCursor.close();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        switch (groupPosition){
            case 0:
                switch (childPosition){
                    case 0:
//                        Toast.makeText(this, "First item in double clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent0 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent0.putExtra(CATEGORY_CLICKED, "Double");
                        displayItemsIntent0.putExtra(CHILD_CLICKED, 0);
                        startActivity(displayItemsIntent0);
                        break;
                    case 1:
//                        Toast.makeText(this, "Second item in double clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent1 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent1.putExtra(CATEGORY_CLICKED, "Double");
                        displayItemsIntent1.putExtra(CHILD_CLICKED, 1);
                        startActivity(displayItemsIntent1);
                        break;
                    case 2:
//                        Toast.makeText(this, "Third item in double clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent2 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent2.putExtra(CATEGORY_CLICKED, "Double");
                        displayItemsIntent2.putExtra(CHILD_CLICKED, 2);
                        startActivity(displayItemsIntent2);
                        break;
                    case 3:
//                        Toast.makeText(this, "Fourth item in double clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent3 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent3.putExtra(CATEGORY_CLICKED, "Double");
                        displayItemsIntent3.putExtra(CHILD_CLICKED, 3);
                        startActivity(displayItemsIntent3);
                        break;
                }
                break;
            case 1:
                switch (childPosition) {
                    case 0:
//                        Toast.makeText(this, "First item in single clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent0 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent0.putExtra(CATEGORY_CLICKED, "Single");
                        displayItemsIntent0.putExtra(CHILD_CLICKED, 0);
                        startActivity(displayItemsIntent0);
                        break;
                    case 1:
//                        Toast.makeText(this, "Second item in single clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent1 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent1.putExtra(CATEGORY_CLICKED, "Single");
                        displayItemsIntent1.putExtra(CHILD_CLICKED, 1);
                        startActivity(displayItemsIntent1);
                        break;
                    case 2:
//                        Toast.makeText(this, "Third item in single clicked", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this, "First item in single clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent2 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent2.putExtra(CATEGORY_CLICKED, "Single");
                        displayItemsIntent2.putExtra(CHILD_CLICKED, 2);
                        startActivity(displayItemsIntent2);
                        break;
                    case 3:
//                        Toast.makeText(this, "Fourth item in single clicked", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this, "First item in single clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent3 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent3.putExtra(CATEGORY_CLICKED, "Single");
                        displayItemsIntent3.putExtra(CHILD_CLICKED, 3);
                        startActivity(displayItemsIntent3);
                        break;
                    case 4:
//                        Toast.makeText(this, "Fifth item in single clicked", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this, "First item in single clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent4 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent4.putExtra(CATEGORY_CLICKED, "Single");
                        displayItemsIntent4.putExtra(CHILD_CLICKED, 4);
                        startActivity(displayItemsIntent4);
                        break;
                    case 5:
//                        Toast.makeText(this, "Sixth item in single clicked", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this, "First item in single clicked", Toast.LENGTH_SHORT).show();
                        Intent displayItemsIntent5 = new Intent(Home.this, DisplayItems.class);
                        displayItemsIntent5.putExtra(CATEGORY_CLICKED, "Single");
                        displayItemsIntent5.putExtra(CHILD_CLICKED, 5);
                        startActivity(displayItemsIntent5);
                        break;
                }
        }
        return false;
    }

    @Override
    public void onClick(final View v) {

        Calendar calendar;

        switch (v.getId()){
            case R.id.add_new_good:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add new good")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", null);

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_add_new_item, null);
                builder.setView(view);

                quantity = (EditText) view.findViewById(R.id.quantity_edit_text);
                size = (EditText) view.findViewById(R.id.size_edit_text);
                price = (EditText) view.findViewById(R.id.price_edit_text);
                productName = (EditText) view.findViewById(R.id.product_name);

                color = (AutoCompleteTextView) view.findViewById(R.id.color_edit_text);
                color.setAdapter(colorAdapter);

                dateTextView = (TextView) view.findViewById(R.id.date_received_text_view);

                category = (Spinner) view.findViewById(R.id.category_spinner);
                type = (Spinner) view.findViewById(R.id.type_spinner);
                section = (Spinner) view.findViewById(R.id.section_spinner);

                quantityTextInputLayout = (TextInputLayout) view.findViewById(R.id.quantity_text_input_layout);
//                priceTextInputLayout = (TextInputLayout) view.findViewById(R.id.price_text_input_layout);
                productNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.product_name_text_input_layout);
                colorTextInputLayout = (TextInputLayout) view.findViewById(R.id.color_text_input_layout);
                sizeTextInputLayout = (TextInputLayout) view.findViewById(R.id.size_text_input_layout);

                quantityTextInputLayout.setHint(getResources().getString(R.string.quantity_hint));
//                priceTextInputLayout.setHint(getResources().getString(R.string.price_hint));
                productNameTextInputLayout.setHint(getResources().getString(R.string.product_name));
                colorTextInputLayout.setHint(getResources().getString(R.string.color_hint));
                sizeTextInputLayout.setHint(getResources().getString(R.string.size_hint));

                spinnerAdapters();

                layout = (LinearLayout) view.findViewById(R.id.sixth_layout);
                layout.setOnClickListener(this);

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(
                        new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(final DialogInterface dialog) {
                                Button positive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                positive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String productNameString = productName.getText().toString();
                                        String colorString = color.getText().toString();
                                        String quantityString = quantity.getText().toString();
                                        String sizeString = size.getText().toString();
//                                        String priceString = price.getText().toString();

                                        String categoryString = category.getSelectedItem().toString();
                                        String typeString = type.getSelectedItem().toString();
                                        String sectionString = section.getSelectedItem().toString();
                                        String dateString = dateTextView.getText().toString();

                                        if (productNameString.isEmpty() || colorString.isEmpty()){
                                            if (colorString.isEmpty()){
                                                Snackbar.make(v, "Please enter product color", Snackbar.LENGTH_SHORT).show();
                                            }
                                            if (productNameString.isEmpty()){
                                                Snackbar.make(v, "Please enter product name", Snackbar.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            dialog.dismiss();

                                            CorporateItem newItem = new CorporateItem(
                                                    categoryString, sectionString, productNameString,
                                                    typeString, colorString, dateString,
                                                    quantityString, sizeString);
                                            Snackbar.make(v, "Done", Snackbar.LENGTH_LONG)
                                                    .setAction("UND0", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                        }
                                                    }).setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                                    .show();

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(MyDatabaseHelper.NAME_COLUMN, productNameString);
                                            contentValues.put(MyDatabaseHelper.COLOR_COLUMN, colorString);
                                            contentValues.put(MyDatabaseHelper.QUANTITY_COLUMN, quantityString);
                                            contentValues.put(MyDatabaseHelper.SIZE_COLUMN, sizeString);
                                            contentValues.put(MyDatabaseHelper.PRICE_COLUMN, 0.00);
                                            contentValues.put(MyDatabaseHelper.CATEGORY_COLUMN, categoryString);
                                            contentValues.put(MyDatabaseHelper.TYPE_COLUMN, typeString);
                                            contentValues.put(MyDatabaseHelper.SECTION_COLUMN, sectionString);
                                            contentValues.put(MyDatabaseHelper.DATE_RECEIVED, dateString);

                                            sqLiteDatabase.insert(MyDatabaseHelper.TABLE_NAME, null, contentValues);

                                            corporateItemsAvailable.add(newItem);

                                            if (!colorsAvailable.contains(colorString)){
                                                ContentValues colorValue = new ContentValues();
                                                colorValue.put(MyDatabaseHelper.COLOR_TABLE_COLUMN, colorString);
                                                sqLiteDatabase.insert(MyDatabaseHelper.COLOR_TABLE_NAME, null, colorValue);
                                            }

//                                            Snackbar.make(v, "Done", Snackbar.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                );
                dialog.show();
                break;
            case R.id.sixth_layout:

                //AlertDialog.Builder setup
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflaterLayout = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View datePickerView = inflaterLayout.inflate(R.layout.date_picker, null);
                dialogBuilder.setView(datePickerView);
                dialogBuilder.setTitle("Date Received");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("DONE", null);
                dialogBuilder.setNegativeButton("CANCEL", null);

                dateReceived = (DatePicker) datePickerView.findViewById(R.id.date_picker);
                calendar = Calendar.getInstance();

                dateReceived.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), this);

                //AlertDialog
                AlertDialog dialogDatePicker = dialogBuilder.create();
                dialogDatePicker.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button positive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String datePicked = String.valueOf(dateReceived.getDayOfMonth());
                                String monthPicked = String.valueOf(dateReceived.getMonth());
                                String yearPicked = String.valueOf(dateReceived.getYear());

                                dateTextView = (TextView) layout.findViewById(R.id.date_received_text_view);
                                String date = datePicked.concat("/").concat(monthPicked).concat("/").concat(yearPicked);
                                dateTextView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                                dateTextView.setText(date);

                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialogDatePicker.show();
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    public void spinnerAdapters(){
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_item);
        categoryAdapter.setDropDownViewResource(R.layout.simple_spinner_drop_down);
        category.setAdapter(categoryAdapter);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        ArrayAdapter<CharSequence> singleSectionsAdapter = ArrayAdapter.createFromResource(Home.this, R.array.single_section, R.layout.spinner_item);
                        singleSectionsAdapter.setDropDownViewResource(R.layout.simple_spinner_drop_down);
                        section.setAdapter(singleSectionsAdapter);
                        break;
                    case 1:
                        ArrayAdapter<CharSequence> doubleSectionsAdapter = ArrayAdapter.createFromResource(Home.this, R.array.double_section, R.layout.spinner_item);
                        doubleSectionsAdapter.setDropDownViewResource(R.layout.simple_spinner_drop_down);
                        section.setAdapter(doubleSectionsAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> typesAdapter = ArrayAdapter.createFromResource(this, R.array.types, R.layout.spinner_item);
        typesAdapter.setDropDownViewResource(R.layout.simple_spinner_drop_down);
        type.setAdapter(typesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.search:

                return true;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
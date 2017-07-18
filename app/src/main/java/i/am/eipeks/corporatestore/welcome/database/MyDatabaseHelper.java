package i.am.eipeks.corporatestore.welcome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    //variables
    private Context context;

    //Database information
    private static final String DB_NAME = "corporateItems.db";
    private static final int DB_VERSION = 1;

    //Table Information
    public static final String TABLE_NAME = "GoodsTable";
    private static final String ID = "_id";
    public static final String CATEGORY_COLUMN = "GoodsTable";
    public static final String SECTION_COLUMN = "Section";
    public static final String NAME_COLUMN = "Name";
    public static final String TYPE_COLUMN = "Type";
    public static final String COLOR_COLUMN = "Color";
    public static final String DATE_RECEIVED = "Date";
    public static final String QUANTITY_COLUMN = "Quantity";
    public static final String SIZE_COLUMN = "Size";
    public static final String PRICE_COLUMN = "Price";

    //ColorTable information
    public static final String COLOR_TABLE_NAME = "Colors";
    public static final String COLOR_TABLE_COLUMN = "Colors";

    //CartTable information
    public static final String CART_TABLE_NAME = "CartTable";

    //Create database
    private static final String createTable =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    CATEGORY_COLUMN + " TEXT, " +
                    SECTION_COLUMN + " TEXT, " +
                    NAME_COLUMN + " TEXT, " +
                    TYPE_COLUMN + " TEXT, " +
                    COLOR_COLUMN + " TEXT, " +
                    DATE_RECEIVED + " TEXT, " +
                    QUANTITY_COLUMN + " TEXT, " +
                    SIZE_COLUMN + " TEXT, " +
                    PRICE_COLUMN + " TEXT);";

    //create color table
    private static final String createColorTable = "CREATE TABLE " + COLOR_TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY, " +
            COLOR_TABLE_COLUMN + " TEXT);";

    //create cartTable
    private static final String createCartTable =
            "CREATE TABLE " + CART_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY, " +
                    CATEGORY_COLUMN + " TEXT, " +
                    SECTION_COLUMN + " TEXT, " +
                    NAME_COLUMN + " TEXT, " +
                    TYPE_COLUMN + " TEXT, " +
                    COLOR_COLUMN + " TEXT, " +
                    DATE_RECEIVED + " TEXT, " +
                    QUANTITY_COLUMN + " TEXT, " +
                    SIZE_COLUMN + " TEXT, " +
                    PRICE_COLUMN + " TEXT);";

    public MyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
        Toast.makeText(context, "Database successfully created", Toast.LENGTH_SHORT).show();

        ContentValues itemsContentValues = new ContentValues();

        itemsContentValues.put(CATEGORY_COLUMN, "Double");
        itemsContentValues.put(SECTION_COLUMN, "Bags and Shoes");
        itemsContentValues.put(NAME_COLUMN, "Louis David");
        itemsContentValues.put(TYPE_COLUMN, "FL");
        itemsContentValues.put(COLOR_COLUMN, "Navy Blue");
        itemsContentValues.put(DATE_RECEIVED, "16/7/2017");
        itemsContentValues.put(QUANTITY_COLUMN, "1");
        itemsContentValues.put(SIZE_COLUMN, "46");
        itemsContentValues.put(PRICE_COLUMN, "#23,000.00");

        db.insert(TABLE_NAME, null, itemsContentValues);

        db.execSQL(createColorTable);
        Toast.makeText(context, "Color table successfully created", Toast.LENGTH_SHORT).show();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOR_TABLE_COLUMN, "Orange");
        contentValues.put(COLOR_TABLE_COLUMN, "Purple");
        contentValues.put(COLOR_TABLE_COLUMN, "Navy Blue");
        contentValues.put(COLOR_TABLE_COLUMN, "Pink");
        contentValues.put(COLOR_TABLE_COLUMN, "Gold");
        contentValues.put(COLOR_TABLE_COLUMN, "Aqua");
        contentValues.put(COLOR_TABLE_COLUMN, "Coral");
        contentValues.put(COLOR_TABLE_COLUMN, "Lilac");
        contentValues.put(COLOR_TABLE_COLUMN, "Teal Green");
        contentValues.put(COLOR_TABLE_COLUMN, "Coffee Brown");
        contentValues.put(COLOR_TABLE_COLUMN, "Royal Blue");
        contentValues.put(COLOR_TABLE_COLUMN, "Yellow");
        contentValues.put(COLOR_TABLE_COLUMN, "Emerald Green");
        contentValues.put(COLOR_TABLE_COLUMN, "Wine");
        contentValues.put(COLOR_TABLE_COLUMN, "Lemon");
        contentValues.put(COLOR_TABLE_COLUMN, "Champagne Gold");

        db.insert(COLOR_TABLE_NAME, null, contentValues);

        db.execSQL(createCartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgrade = "DROP IF EXISTS " + TABLE_NAME;
        db.execSQL(upgrade);
        onCreate(db);
    }
}

package i.am.eipeks.corporatestore.welcome.login.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class PasswordsDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "passwords.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "PasswordsTable";
    private static final String ID = "_id";
    public static final String COLUMN_USER_NAME = "UserName";
    public static final String COLUMN_USER_PASSWORD = "Password";
    public static final String COLUMN_USER_NUMBER = "Number";
    public static final String COLUMN_USER_EMAIL = "Email";
    public static final String COLUMN_POSITION = "Position";

    private Context context;

    private static final String createDatabase =
            "CREATE TABLE " + TABLE_NAME
            + " (" + ID + " INTEGER PRIMARY KEY, "
            + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_PASSWORD + " TEXT, "
            + COLUMN_USER_NUMBER + " TEXT, "
            + COLUMN_USER_EMAIL + " TEXT"
            + COLUMN_POSITION + " TEXT);";

    public PasswordsDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDatabase);
        Toast.makeText(context, "Passwords database created successfully", Toast.LENGTH_SHORT).show();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, "Admin");
        contentValues.put(COLUMN_USER_PASSWORD, "Admin");
        contentValues.put(COLUMN_USER_NUMBER, "07067274441");
        contentValues.put(COLUMN_USER_EMAIL, "apexzdgr8est@gmail.com");

        db.insert(TABLE_NAME, null, contentValues);
        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
    }
}

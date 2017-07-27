package i.am.eipeks.corporatestore.welcome.login.activities_fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import i.am.eipeks.corporatestore.R;
import i.am.eipeks.corporatestore.welcome.activities.Home;
import i.am.eipeks.corporatestore.welcome.activities.Sales;
import i.am.eipeks.corporatestore.welcome.login.classes.Session;
import i.am.eipeks.corporatestore.welcome.login.database.PasswordsDB;

public class MasterLogin extends Activity implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private EditText password, username;
    private ImageView login_image;
    private Session session;
    private SQLiteDatabase database;
//    public static final String CURRENT_USER = "Current User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_login_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        spinner = (Spinner) findViewById(R.id.login_as);
        password = (EditText) findViewById(R.id.password);
        login_image = (ImageView) findViewById(R.id.login_picture);
        username = (EditText) findViewById(R.id.username);
        Button login = (Button) findViewById(R.id.login);

        session = new Session(this);

        PasswordsDB passwordsDB = new PasswordsDB(this);
        database = passwordsDB.getReadableDatabase();
//        passwordsDB.addInitialEntries(database);

        if (session.loggedIn()){
            switch (session.getUserLoggedIn()){
                case "Admin":
                    startActivity(new Intent(MasterLogin.this, Home.class));
                    finish();
                    break;
                case "Sales Manager":
                    startActivity(new Intent(MasterLogin.this, Sales.class));
                    break;
            }
        }
//        SQLiteDatabase loginDatabase = passwordsDB.getWritableDatabase();

        List<String> spinnerItems = Arrays.asList(getResources().getStringArray(R.array.login_as));

        ArrayList<String> username = new ArrayList<>();
        ArrayList<String> password = new ArrayList<>();

        get(username, password);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner, spinnerItems);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(this);
        login.setOnClickListener(this);

//        Toast.makeText(this, username.toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, password.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.login:
//                String login_as = spinner.getSelectedItem().toString();
                String passwordText = password.getText().toString();
                String usernameText = username.getText().toString();

                boolean notEmpty = !(passwordText.isEmpty() && usernameText.isEmpty());
                boolean isUserRegistered = getUserLogInInfo(usernameText, passwordText);

                if (isUserRegistered){
                    if (notEmpty){
                        switch ((String)spinner.getSelectedItem()){
                            case "Admin":
                                startActivity(new Intent(MasterLogin.this, Home.class));
                                session.setLoggedIn(true);
                                session.setUserLoggedIn((String)spinner.getSelectedItem());
                                finish();
                                break;
                            case "Sales Manager":
                                startActivity(new Intent(MasterLogin.this, Sales.class));
                                session.setLoggedIn(true);
                                session.setUserLoggedIn((String)spinner.getSelectedItem());
                                finish();
//                                Toast.makeText(this, "Sales manager", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    } else {
                        Toast.makeText(this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Not a registered user", Toast.LENGTH_SHORT).show();
                }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (spinner.getSelectedItemPosition()){
            case 0:
                login_image.setImageResource(R.drawable.admin);
                break;
            case 1:
                login_image.setImageResource(R.drawable.retail);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void get(ArrayList<String> username, ArrayList<String> password){

        Cursor cursor = database.query(
                PasswordsDB.TABLE_NAME,
                new String[]{PasswordsDB.COLUMN_USER_NAME, PasswordsDB.COLUMN_USER_PASSWORD},
                null, null, null, null, null, null
        );

        if (cursor.moveToFirst()){
            do {
                username.add(cursor.getString(cursor.getColumnIndexOrThrow(PasswordsDB.COLUMN_USER_NAME)));
                password.add(cursor.getString(cursor.getColumnIndexOrThrow(PasswordsDB.COLUMN_USER_PASSWORD)));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public boolean getUserLogInInfo(String username, String password){
        String databaseQueryString = "SELECT *  FROM " + PasswordsDB.TABLE_NAME
                + " WHERE " + PasswordsDB.COLUMN_USER_NAME + " = ? AND "
                + PasswordsDB.COLUMN_USER_PASSWORD + " = ? ";
        Cursor cursor = database.rawQuery(databaseQueryString, new String[]{username, password});
        cursor.moveToFirst();

        Toast.makeText(this, cursor.toString(), Toast.LENGTH_SHORT).show();

        if (cursor.getCount() > 0){
            return true;
        }

        cursor.close();
//        database.close();

        return false;
    }
}

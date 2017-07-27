package i.am.eipeks.corporatestore.welcome.login.classes;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Intent intent;

    private int PRIVATE_MODE = 0;
    private static final String PREFERENCE_NAME = "EipeksPref";
    private static final String LOGGED_IN = "Logged In";
    public static final String KEY_NAME = "name";
    public static final String EMAIL = "email";

    

}

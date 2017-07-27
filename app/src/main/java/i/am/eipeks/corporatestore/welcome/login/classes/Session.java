package i.am.eipeks.corporatestore.welcome.login.classes;


import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private String userLoggedIn;

    public Session(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLoggedIn(boolean loggedIn){
        editor.putBoolean("loggedInMode", loggedIn);
        editor.commit();
    }

    public void setUserLoggedIn(String user){
        userLoggedIn = user;
    }

    public String getUserLoggedIn(){
        return this.userLoggedIn;
    }

    public boolean loggedIn(){
        return preferences.getBoolean("loggedInMode", false);
    }

}

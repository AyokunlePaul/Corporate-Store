package i.am.eipeks.corporatestore.welcome.classes;

import android.app.Application;

import com.google.firebase.FirebaseApp;


public class CorporateStore extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}

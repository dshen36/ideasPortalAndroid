package release.mobileideasportal;

import android.content.Context;

/**
 * Created by brich200 on 7/15/15.
 */
public class CredentialHolder {
    private static CredentialHolder instance;
    private String email, password;
    private Context context;

    public static synchronized CredentialHolder getInstance(Context context) {
        if(instance == null) {
            instance = new CredentialHolder(context);
        }

        return instance;
    }

    public CredentialHolder(Context context) {
        this.context = context;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail(){return email;}
    public String getPassword(){return password;}
}

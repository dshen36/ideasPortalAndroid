package release.mobileideasportal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends ActionBarActivity {
    CredentialHolder credentialHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        credentialHolder = CredentialHolder.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void LogIn(View view) {
        EditText email = (EditText) findViewById(R.id.edit_email);
        EditText password = (EditText) findViewById(R.id.edit_password);
        if(!email.getText().toString().equals("") && testEmail(email.getText().toString())) {
            credentialHolder.setUserEmail(email.getText().toString());
            startActivity(new Intent(this, MyActivity.class));
        } else if (!testEmail(email.getText().toString())) {
            Toast.makeText(this,"Please Enter a Comcast Email",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Please Enter an Email",Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean testEmail(String email){
        return email.matches("[a-zA-Z]+(((\\-)|[._a-zA-Z0-9])*)@cable.comcast.com")||email.matches("");
    }
}

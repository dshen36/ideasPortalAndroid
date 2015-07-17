package release.mobileideasportal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MyActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "release.myfirstapp.MESSAGE";
    String asynchTaskType;
    int [] availableIds;
    Intent intent;
    String urlString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        urlString = "http://rossette9-001-site1.mywindowshosting.com/api/idea";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.menu_my, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        switch(item.getItemId()) {
            case R.id.action_search:
                //openSettings();
        }
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("In do in background");
            System.out.println(asynchTaskType);

            if(asynchTaskType.equals("Ideas")){
                URL url = null;
                try {

                    url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");


                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String output;
                    String jsonText = "";
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    System.out.println("Output from Server .... \n");
                    while ((output = br.readLine()) != null) {
                        jsonText = jsonText + output;
                    }
                    //jsonText= jsonText.substring(1, jsonText.length()-1);
                    System.out.println(jsonText);
                    JSONArray jsonArray = new JSONArray(jsonText);
                    availableIds = new int[jsonArray.length()];
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        availableIds[i] = jsonObject.getInt("Id");
                    }
                    intent = new Intent(MyActivity.this, Directory.class);
                    //intent = new Intent(MyActivity.this, DisplayMessageActivity.class);
                    intent.putExtra("url","");
                    intent.putExtra("availableIds", availableIds);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "Searched";
            }
            return "LoadDone";

        }

        protected void onPostExecute(String result) {
            if(result.equals("Searched")) {
                startActivity(intent);
            }
        }
    }

    public void createIdea(View view) {
        startActivity(new Intent(MyActivity.this, SubmitActivity.class));
    }

    public void viewIdeas(View view) {
        asynchTaskType = "Ideas";
        new CallAPI().execute("Ideas");
        //startActivity(new Intent(MyActivity.this, DisplayMessageActivity.class));
    }
    public void startWelcome(View view) {startActivity(new Intent(this, MyActivity.class));}


    public void startLabWeek(View view) {startActivity(new Intent(this, LabWeekDirectory.class));}
}

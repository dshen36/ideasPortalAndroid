package com.example.brich200.mobileideasportal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.brich200.mobileideasportal.R;

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

public class Directory extends ActionBarActivity {

    String asynchTaskType;
    int[] availableIds;
    Spinner dropDownSpinner;
    String[] subMenus = {"(Select Page)","Ideas","Lab Weeks","Challenges","Partners","Success Stories"};
    GridLayout singleIdea;
    LinearLayout ideaLayout;
    Idea idea;
    URL url;
    InflateIdeaView [] inflatedIdeas;
    boolean vote, canVote;

    int currentIdea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        singleIdea = (GridLayout) findViewById(R.id.singleIdea);
        ideaLayout = (LinearLayout) findViewById(R.id.singleIdeaLayout);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,subMenus);

        dropDownSpinner = (Spinner) findViewById(R.id.spinner);
        dropDownSpinner.setAdapter(adapter);
        dropDownSpinner.setOnItemSelectedListener(spinnerListener);
        idea = new Idea();

        asynchTaskType = "Load";
        new CallAPI().execute("Load");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_directory, menu);
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

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(parent.getItemAtPosition(position).toString());
            if (parent.getItemAtPosition(position).toString().equals("Ideas")) {
                startActivity(new Intent(Directory.this, Directory.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Partners")) {
                startActivity(new Intent(Directory.this, Partners.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Success Stories")) {
                startActivity(new Intent(Directory.this, SuccessStoriesMain.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Challenges")) {
                startActivity(new Intent(Directory.this, Challenges.class));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            dropDownSpinner.setOnItemSelectedListener(spinnerListener);
        }
    };


    public void cxInnovationsClick(View view) {
        startActivity(new Intent(this, CxInnovationMain.class));
    }

    public void createIdea(View view) {
        startActivity(new Intent(this, SubmitActivity.class));
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... string) {
            /*String urlString = params[0]; // URL to call

            String resultToDisplay = "";

            InputStream in = null;*/

            // HTTP Get
            if (asynchTaskType.equals("Load")) {
                System.out.println("Asynch Task: Loading");
                try {
                    url = new URL("http://comcastideas-interns.azurewebsites.net/api/idea");
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
                    inflatedIdeas = new InflateIdeaView[jsonArray.length()];
                    System.out.println("Inflating");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        availableIds[i] = jsonObject.getInt("Id");
                        if(inflatedIdeas[i] == null) {
                            inflatedIdeas[i] = new InflateIdeaView();
                        }
                        inflatedIdeas[i].setIdea(new Idea(jsonObject.getString("Title"), jsonObject.getString("Tags"),jsonObject.getString("Issue"),
                                jsonObject.getString("Description"),jsonObject.getString("CustomerExperienceImpact"),
                                jsonObject.getString("MetricsImpact"),jsonObject.getInt("Status"),jsonObject.getInt("IntellectualPropertyStatus"),
                                jsonObject.getString("Email"),jsonObject.getString("AdditionalTeamMemberEmail"),jsonObject.getInt("Id"),
                                jsonObject.getInt("Votes"),jsonObject.getString("LastModified"), jsonObject.getInt("Views"),jsonObject.getString("ImageIDs")));
                        System.out.println(i);
                    }
                    try {
                        JSONObject jsonObject;
                        if (jsonText.charAt(0) == '[') {
                            JSONArray jsonArray2 = new JSONArray(jsonText);
                            jsonObject = jsonArray.getJSONObject(0);
                        } else {
                            jsonObject = new JSONObject(jsonText);
                        }
                        idea.setTitle(jsonObject.getString("Title"));
                        idea.setTags(jsonObject.getString("Tags"));
                        idea.setIssue(jsonObject.getString("Issue"));
                        idea.setDescription(jsonObject.getString("Description"));
                        idea.setCustomerExperienceImpact(jsonObject.getString("CustomerExperienceImpact"));
                        idea.setMetricsImpact(jsonObject.getString("MetricsImpact"));
                        idea.setStatus(jsonObject.getInt("Status"));
                        idea.setIntelectualPropertyStatus(jsonObject.getInt("IntellectualPropertyStatus"));
                        idea.setEmail(jsonObject.getString("Email"));
                        idea.setAdditionalTeamMemberEmail(jsonObject.getString("AdditionalTeamMemberEmail"));
                        idea.setId(jsonObject.getInt("Id"));
                        idea.setUpvotes((jsonObject.getInt("Votes")));
                        idea.setLastModified(jsonObject.getString("LastModified"));
                        idea.setImageIds(jsonObject.getString("ImageIDs"));
                        System.out.println(idea.getImageIds());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (asynchTaskType.equals("Vote")) {
                System.out.println("Asynch Task: Voting");
                try {
                    url = new URL("http://comcastideas-interns.azurewebsites.net/api/idea/" + currentIdea + "?voteUp=" + vote);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String output;
                    System.out.println("Output from Server .... \n");
                    while ((output = br.readLine()) != null) {
                        System.out.println(output);
                    }

                    conn.disconnect();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                asynchTaskType = "Load";
                new CallAPI().execute("value");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            inflateIdeas();
            canVote = true;
        }
    }

    private void inflateIdeas() {

        LinearLayout control = (LinearLayout) findViewById(R.id.singleIdeaLayout);
        control.removeAllViews();
        for(int i = 0; i < inflatedIdeas.length; ++i) {
            LayoutInflater l = getLayoutInflater();
            View v = l.inflate(R.layout.single_idea, control, false);
            TextView day = (TextView) v.findViewById(R.id.day);
            TextView month = (TextView) v.findViewById(R.id.month);
            TextView year = (TextView) v.findViewById(R.id.year);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView votes = (TextView) v.findViewById(R.id.votes);
            TextView description = (TextView) v.findViewById(R.id.description);
            TextView tags = (TextView) v.findViewById(R.id.tags);
            TextView id = (TextView) v.findViewById(R.id.idea_id);
            day.setText(getDay(inflatedIdeas[i].getIdea().getLastModified()));
            month.setText(getMonth(inflatedIdeas[i].getIdea().getLastModified()));
            year.setText(getYear(inflatedIdeas[i].getIdea().getLastModified()));
            id.setText(inflatedIdeas[i].getIdea().getId() + "");
            title.setText(inflatedIdeas[i].getIdea().getTitle());
            votes.setText(inflatedIdeas[i].getIdea().getUpVotes() + "");
            description.setText(inflatedIdeas[i].getIdea().getDescription());
            tags.setText(inflatedIdeas[i].getIdea().getTags());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
            v.setLayoutParams(params);
            control.addView(v);
            inflatedIdeas[i].setView(v);
        }
    }

    public void upVote(View view) {
        System.out.println("Up Clicked");
        canVote = false;
        LinearLayout control = (LinearLayout) findViewById(R.id.singleIdeaLayout);
        View parentView = view;
        while (!parentView.getParent().equals(control)) {
            parentView = (View) parentView.getParent();
            System.out.println("Going up");
        }
        System.out.println("Found");
        TextView id = (TextView) parentView.findViewById(R.id.idea_id);
        id.getText();
        String numberString = id.getText().toString();

        currentIdea = Integer.parseInt(numberString);
        vote = true;
        asynchTaskType = "Vote";
        new CallAPI().execute("Vote");
    }

    public void downVote(View view) {
        System.out.println("Up Clicked");
        canVote = false;
        LinearLayout control = (LinearLayout) findViewById(R.id.singleIdeaLayout);
        View parentView = view;
        while (!parentView.getParent().equals(control)) {
            parentView = (View) parentView.getParent();
            System.out.println("Going up");
        }
        System.out.println("Found");
        TextView id = (TextView) parentView.findViewById(R.id.idea_id);
        id.getText();
        String numberString = id.getText().toString();

        currentIdea = Integer.parseInt(numberString);
        vote = false;
        asynchTaskType = "Vote";
        new CallAPI().execute("Vote");
    }

    public void openIdea(View view) {
        LinearLayout control = (LinearLayout) findViewById(R.id.singleIdeaLayout);
        View parentView = view;
        while (!parentView.getParent().equals(control)) {
            parentView = (View) parentView.getParent();
            System.out.println("Going up");
        }
        System.out.println("Found");
        TextView id = (TextView) parentView.findViewById(R.id.idea_id);
        id.getText();
        String numberString = id.getText().toString();

        currentIdea = Integer.parseInt(numberString);
        if (availableIds != null){
            while (availableIds[0] != currentIdea) {
                int temp = availableIds[0];
                for(int i = 0; i < availableIds.length - 1; i++) {
                    availableIds[i] = availableIds[i + 1];
                    System.out.println(availableIds[i]);
                }
                availableIds[availableIds.length - 1] = temp;
                System.out.println(availableIds[availableIds.length - 1]);
            }
        }
        Intent intent = new Intent(Directory.this, DisplayMessageActivity.class);
        intent.putExtra("Available Ids", availableIds);
        startActivity(intent);
    }

    public static String getDay(String timestamp)
    {
        String day = "";
        day = timestamp.substring(8,10);
        return day;
    }

    public static String getMonth(String timestamp)
    {
        String month = "";
        month = timestamp.substring(5,7);
        return month;
    }

    public static String getYear(String timestamp)
    {
        String year = "";
        year = timestamp.substring(0,4);
        return year;
    }
}

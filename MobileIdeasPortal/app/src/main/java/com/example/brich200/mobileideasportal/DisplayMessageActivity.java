package com.example.brich200.mobileideasportal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class DisplayMessageActivity extends ActionBarActivity {

    TextView titleText, tagsText, issueText, descriptionText, customerExperienceImpactText, metricsText,
            statusText, intellectualPropertyStatusText, emailText, teammatesEmailText, idText, votesText,
            lastModifiedText;

    String title, tags, issue, description, customerExperienceImpact, metrics, email, teammatesEmail, lastModified;

    int status, intellectualPropertyStatus, id, votes;

    int currentIdea;

    Idea idea;

    String asychTaskType;

    int[] availableIds;

    ProgressDialog progressDialog;
    URL url;

    private Handler uIHandler = new Handler();
    LinearLayout layout;

    boolean vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(this, "Retrieving Idea", "Please Wait While We Retrieve The Idea");
        setContentView(R.layout.activity_display_message);
        layout =(LinearLayout) findViewById(R.id.layout);
        layout.setFocusable(true);
        idea = new Idea();




        titleText = (TextView) findViewById(R.id.title);
        tagsText = (TextView) findViewById(R.id.tags);
        issueText = (TextView) findViewById(R.id.current_issue);
        descriptionText = (TextView) findViewById(R.id.description);
        customerExperienceImpactText = (TextView) findViewById(R.id.customer_exp_imp);
        metricsText = (TextView) findViewById(R.id.metrics);
        statusText = (TextView) findViewById(R.id.status);
        intellectualPropertyStatusText = (TextView) findViewById(R.id.intellectual_property_status);
        emailText = (TextView) findViewById(R.id.email);
        teammatesEmailText = (TextView) findViewById(R.id.teammates_email);
        idText = (TextView) findViewById(R.id.id);
        votesText = (TextView) findViewById(R.id.votes);
        lastModifiedText = (TextView) findViewById(R.id.last_modified);



        /*title.setText(idea.getTitle());
        tags.setText(idea.getTags());
        issue.setText(idea.getIssue());
        description.setText(idea.getDescription());
        customerExperienceImpact.setText(idea.getCustomerExperienceImpact());
        metrics.setText(idea.getMetricsImpact());
        email.setText(idea.getEmail());
        teammatesEmail.setText(idea.getAdditionalTeamMemberEmail());*/

        /*//get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        //create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        //Set the text view as the activity layout
        setContentView(textView);*/
        asychTaskType = "Load";
        new CallAPI().execute("value");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 & resultCode == RESULT_OK) {
            new CallAPI().execute("value");
        }
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            /*String urlString = params[0]; // URL to call

            String resultToDisplay = "";

            InputStream in = null;*/

            // HTTP Get
            if(asychTaskType.equals("Load")) {
                System.out.println("Asynch Task: Loading");
                try {
                    if (getIntent().getStringExtra("url") != null) {
                        url = new URL(getIntent().getStringExtra("url"));
                    } else if (getIntent().getIntArrayExtra("availableIds") != null) {
                        availableIds = getIntent().getIntArrayExtra("availableIds");
                        currentIdea = availableIds[0];
                        url = new URL("http://comcastideas-interns.azurewebsites.net/api/idea/" + currentIdea);
                    } else {
                        url = new URL("http://comcastideas-interns.azurewebsites.net/api/idea/10");
                        availableIds = new int[]{10};
                        currentIdea = 10;
                    }
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");


                    if (conn.getResponseCode() != 200) {
                        System.out.println("Url:  " + url.toString());
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
                    try {
                        JSONObject jsonObject = new JSONObject(jsonText);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            } else if (asychTaskType.equals("Next")){

                System.out.println("Asynch Task: Next");
                try {
                    currentIdea = availableIds[0];
                    url = new URL("http://comcastideas-interns.azurewebsites.net/api/idea/" + currentIdea);
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
                    try {
                        JSONObject jsonObject = new JSONObject(jsonText);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            } else if (asychTaskType.equals("Vote")){
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
                asychTaskType = "Load";
                new CallAPI().execute("value");
            }

            return null/*resultToDisplay*/;
        }

        protected void onPostExecute(String result) {
            System.out.println("PostExecute");
            updateUI();

        }

    }

    private void updateUI() {

        titleText.setText(idea.getTitle());
        System.out.println(titleText.getText().toString());
        tagsText.setText(idea.getTags());
        issueText.setText(idea.getIssue());
        descriptionText.setText(idea.getDescription());
        customerExperienceImpactText.setText(idea.getCustomerExperienceImpact());
        metricsText.setText(idea.getMetricsImpact());
        statusText.setText(idea.getStatus() + "");
        intellectualPropertyStatusText.setText(idea.getIntelectualPropertyStatus() + "");
        emailText.setText(idea.getEmail());
        teammatesEmailText.setText(idea.getAdditionalTeamMemberEmail());
        idText.setText(idea.getId() + "");
        votesText.setText(idea.getUpVotes() + "");
        lastModifiedText.setText(idea.getLastModified());
        layout.setFocusable(true);
        progressDialog.hide();
    }

    public void editContents(View view) {
        Intent intent = new Intent(DisplayMessageActivity.this, EditActivity.class);
        intent.putExtra("id", idea.getId());
        //startActivityForResult(intent,1);
        startActivity(intent);
    }

    public void getNextIdea(View view) {
        if (availableIds != null){
            int temp = availableIds[0];
            for(int i = 0; i < availableIds.length - 1; i++) {
                availableIds[i] = availableIds[i + 1];
                System.out.println(availableIds[i]);
            }
            availableIds[availableIds.length - 1] = temp;
            System.out.println(availableIds[availableIds.length - 1]);
        }
        asychTaskType = "Next";
        new CallAPI().execute("value");
    }

    public void upVote(View view) {
        asychTaskType = "Vote";
        vote = true;
        new CallAPI().execute("value");
    }

    public void downVote(View view) {
        asychTaskType = "Vote";
        vote = false;
        new CallAPI().execute("value");
    }
}

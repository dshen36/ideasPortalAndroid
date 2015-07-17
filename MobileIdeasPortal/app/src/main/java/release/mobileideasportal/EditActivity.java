package release.mobileideasportal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;


public class EditActivity extends ActionBarActivity {

    EditText title, tags, issue, description, customerExperience, editOther, email, teamEmail;
    CheckBox self_service, call_deflection, agent_contact, call_resolution, rework, avoidable_truck, upstream_downstream, cost_savings, other;

    Spinner dropDownSpinner;
    String[] subMenus = {"Menu", "Ideas","Lab Weeks","Challenges","Partners","Success Stories"};

    private static int RESULT_LOAD_IMAGE = 1;

    String asynchTaskType, urlString;

    Idea idea;

    int[] availableIds;
    SearchView searchIdeas;
    Intent intent;

    URL url;

    Button delete, cancel;



    private String baseUrl = "http://rossette9-001-site1.mywindowshosting.com/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        title = (EditText) findViewById(R.id.edit_title);
        tags = (EditText) findViewById(R.id.edit_tags);
        issue = (EditText) findViewById(R.id.edit_issue);
        description = (EditText) findViewById(R.id.edit_description);
        customerExperience = (EditText) findViewById(R.id.edit_customer_exp_impact);
        editOther = (EditText) findViewById(R.id.edit_other);
        email = (EditText) findViewById(R.id.edit_email);
        teamEmail = (EditText) findViewById(R.id.edit_team_email);

        self_service = (CheckBox) findViewById(R.id.self_Service);
        call_deflection = (CheckBox) findViewById(R.id.call_deflection);
        agent_contact = (CheckBox) findViewById(R.id.agent_contact);
        call_resolution = (CheckBox) findViewById(R.id.call_resolution);
        rework = (CheckBox) findViewById(R.id.rework);
        avoidable_truck = (CheckBox) findViewById(R.id.avoidable_truck);
        upstream_downstream = (CheckBox) findViewById(R.id.upsteam_downstream);
        cost_savings = (CheckBox) findViewById(R.id.cost_savings);
        other = (CheckBox) findViewById(R.id.other);

        delete = (Button) findViewById(R.id.deleteIdea);
        delete.setVisibility(View.VISIBLE);
        cancel = (Button) findViewById(R.id.cancel_button);
        cancel.setVisibility(View.VISIBLE);


        idea = new Idea();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,subMenus);

        dropDownSpinner = (Spinner) findViewById(R.id.spinner);
        dropDownSpinner.setAdapter(adapter);
        dropDownSpinner.setOnItemSelectedListener(spinnerListener);

        idea.setId(getIntent().getIntExtra("id", -1));
        System.out.println("Id: " + idea.getId());
        if(idea.getId() == -1) {
            finish();
        } else {
            System.out.println("Start API");
            asynchTaskType = "Load";
            new CallAPI().execute("Load");
        }

        searchIdeas = (SearchView) findViewById(R.id.ideaSearch);
        searchIdeas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                asynchTaskType = "Search";
                urlString = "http://rossette9-001-site1.mywindowshosting.com/api/idea?searchQuery=" + query + "&searchParamater=Title";
                Intent intent = new Intent(EditActivity.this, Directory.class);
                intent.putExtra("url", urlString);
                startActivity(intent);
                /*System.out.println(urlString);
                new CallAPI().execute("value");*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_submit, menu);
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

    public void submitIdea(View view) {
        if(checkEmails(email.getText().toString(), teamEmail.getText().toString())) {
            idea.setTitle(title.getText().toString());
            System.out.println("New Title: " + idea.getTitle());
            idea.setTags(tags.getText().toString());
            idea.setIssue(issue.getText().toString());
            idea.setDescription(description.getText().toString());
            idea.setCustomerExperienceImpact(customerExperience.getText().toString());
            idea.setMetricsImpact(checkCheckboxes());
            idea.setEmail(email.getText().toString());
            idea.setAdditionalTeamMemberEmail(teamEmail.getText().toString());
            idea.setStatus(1);
            idea.setIntelectualPropertyStatus(1);

            asynchTaskType = "Submit";
            new CallAPI().execute("Submit");
        } else {
            Toast.makeText(EditActivity.this, "Invalid Email or Additional Email, Please Ensure They Are Correct @cable.comcast.com Emails", Toast.LENGTH_LONG).show();
        }
        //startActivity(new Intent(SubmitActivity.this, DisplayMessageActivity.class));
    }

    private String checkCheckboxes() {
        String metricsImpact = "";
        if (self_service.isChecked()) {
            metricsImpact = metricsImpact.concat(self_service.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (call_deflection.isChecked()) {
            metricsImpact = metricsImpact.concat(call_deflection.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (agent_contact.isChecked()) {
            metricsImpact = metricsImpact.concat(agent_contact.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (call_resolution.isChecked()) {
            metricsImpact = metricsImpact.concat(call_resolution.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (rework.isChecked()) {
            metricsImpact = metricsImpact.concat(rework.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (avoidable_truck.isChecked()) {
            metricsImpact = metricsImpact.concat(avoidable_truck.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (upstream_downstream.isChecked()) {
            metricsImpact = metricsImpact.concat(upstream_downstream.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (cost_savings.isChecked()) {
            metricsImpact = metricsImpact.concat(cost_savings.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (other.isChecked()) {
            metricsImpact = metricsImpact.concat(editOther.getText().toString() + " ");
            System.out.println("Metrics Impact:" + metricsImpact);
        }
        if (metricsImpact.length() > 0) {
            metricsImpact.substring(0, metricsImpact.length() - 2);
        }
        return metricsImpact;
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("In do in background");
            System.out.println(asynchTaskType);

            if(asynchTaskType.equals("Submit")) {

                String ideaString = "";
                if (!idea.getTitle().equals("") && !idea.getMetricsImpact().equals("") && idea.getStatus() != -1 && idea.getIntelectualPropertyStatus() != -1 && !idea.getEmail().equals("")) {
                    ideaString = ideaString.concat(String.format("{\"Title\":\"%s\"", idea.getTitle()));
                    if (!idea.getTags().equals("")) {
                        ideaString = ideaString.concat(String.format(",\"Tags\":\"%s\"", idea.getTags()));
                    }
                    if (!idea.getIssue().equals("")) {
                        ideaString = ideaString.concat(String.format(",\"Issue\":\"%s\"", idea.getIssue()));
                    }
                    if (!idea.getDescription().equals("")) {
                        ideaString = ideaString.concat(String.format(",\"Description\":\"%s\"", idea.getDescription()));
                    }
                    if (!idea.getCustomerExperienceImpact().equals("")) {
                        ideaString = ideaString.concat(String.format(",\"CustomerExperienceImpact\":\"%s\"", idea.getCustomerExperienceImpact()));
                    }
                    ideaString = ideaString.concat(String.format(",\"MetricsImpact\":\"%s\"", idea.getMetricsImpact()));
                    ideaString = ideaString.concat(String.format(",\"Status\":%d", idea.getStatus()));
                    ideaString = ideaString.concat(String.format(",\"IntellectualPropertyStatus\":%d", idea.getIntelectualPropertyStatus()));
                    ideaString = ideaString.concat(String.format(",\"Email\":\"%s\"", idea.getEmail()));
                    if (!idea.getAdditionalTeamMemberEmail().equals("")) {
                        ideaString = ideaString.concat(String.format(",\"AdditionalTeamMemberEmail\":\"%s\"", idea.getAdditionalTeamMemberEmail()));
                    }
                    ideaString = ideaString.concat("}");
                    System.out.println("Idea printed: " + ideaString);

                    try {

                        Intent intent = getIntent();
                        url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/" + intent.getIntExtra("id",-1));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("PUT");
                        conn.setRequestProperty("Content-Type", "application/json");

                        OutputStream os = conn.getOutputStream();
                        os.write(ideaString.getBytes());
                        os.flush();

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

                    } catch (IOException e) {

                        e.printStackTrace();

                    }
                    return "Continue";
                } else {
                    return "Fail";
                }
            } else if (asynchTaskType.equals("Load")){
                try {
                    System.out.println("Loading Edit Activity");
                    Intent intent = getIntent();
                    url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/" + intent.getIntExtra("id",-1));
                    //url = new URL(url, "/" + idea.getId());
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
            } else if (asynchTaskType.equals("Delete")){
                try {
                    Intent intent = getIntent();
                    url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/" + intent.getIntExtra("id",-1));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("DELETE");
                    conn.setRequestProperty("Content-Type", "application/json");
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }
                    conn.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return ("Deleted");
            } else if (asynchTaskType.equals("Cancel")){
                return "Canceled";
            }else if(asynchTaskType.equals("Search")){
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
                    intent = new Intent(EditActivity.this, DisplayMessageActivity.class);
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
            System.out.println("PostExecute");
            if (result.equals("Continue")){
                System.out.println("Continued");
                Toast.makeText(EditActivity.this, "Idea Submitted", Toast.LENGTH_SHORT).show();
                //setResult(RESULT_OK, new Intent(EditActivity.this, DisplayMessageActivity.class));
                //finish();
                startActivity(new Intent(EditActivity.this, DisplayMessageActivity.class));
            } else if (result.equals("Fail")) {
                System.out.println("Failed");
                Toast.makeText(EditActivity.this,"Missing Fields, Please Give Your Idea A Title and Provide Your Email, and Check At Least One Metrics Impact", Toast.LENGTH_LONG).show();
                //setResult(RESULT_CANCELED, new Intent(EditActivity.this, DisplayMessageActivity.class));
                //finish();
                return;
            } else if (result.equals("LoadDone")) {
                System.out.println("LoadDone");
                updateEditTexts();
            } else if (result.equals("Deleted")){
                System.out.println("Deleted");
                startActivity(new Intent(EditActivity.this, MyActivity.class));
            } else if (result.equals("Canceled")){
                Intent intent = new Intent(EditActivity.this, DisplayMessageActivity.class);
                intent.putExtra("url", url.toString());
                startActivity(intent);
            } else if(result.equals("Searched")) {
                startActivity(intent);
            }

        }

    }

    private void updateEditTexts() {
        title.setText(idea.getTitle());
        tags.setText(idea.getTags());
        issue.setText(idea.getIssue());
        description.setText(idea.getDescription());
        customerExperience.setText(idea.getCustomerExperienceImpact());
        setCheckboxes();
        email.setText(idea.getEmail());
        teamEmail.setText(idea.getAdditionalTeamMemberEmail());

    }

    private void setCheckboxes() {
        String metrics = idea.getMetricsImpact();
        if(metrics.contains("Improved Self-Service")){
            metrics = metrics.replace("Improved Self-Service", "");
            self_service.setChecked(true);
        }
        if(idea.getMetricsImpact().contains("Call Deflection/Avoidance")){
            metrics = metrics.replace("Call Deflection/Avoidance","");
            call_deflection.setChecked(true);
        }
        if(idea.getMetricsImpact().contains("Agent Contact Rate")){
            metrics = metrics.replace("Agent Contact Rate","");
            agent_contact.setChecked(true);
        }
        if(idea.getMetricsImpact().contains("First Call Resolution")){
            metrics = metrics.replace("First Call Resolution","");
            call_resolution.setChecked(true);
        }
        if(idea.getMetricsImpact().contains("Rework")){
            metrics = metrics.replace("Rework","");
            rework.setChecked(true);
        }
        if(idea.getMetricsImpact().contains("Avoidable Truck Rolls")){
            metrics = metrics.replace("Avoidable Truck Rolls","");
            avoidable_truck.setChecked(true);
        }
        if(idea.getMetricsImpact().contains("Upstream/Downstream Transmit")){
            metrics = metrics.replace("Upstream/Downstream Transmit","");
            upstream_downstream.setChecked(true);
        }
        if(idea.getMetricsImpact().contains("Cost Savings")){
            metrics = metrics.replace("Cost Savings","");
            cost_savings.setChecked(true);
        }
        if(!metrics.isEmpty()){
            while(metrics.length() != 0 && metrics.charAt(0)==' '){
                metrics = metrics.substring(1);
            }
            if(!metrics.isEmpty()) {
                other.setChecked(true);
                editOther.setText(metrics);
            }
        }
    }

    public void deleteIdea(View view) {
        asynchTaskType = "Delete";
        new CallAPI().execute("Delete");
    }

    public void createIdea(View view) {
        startActivity(new Intent(EditActivity.this, SubmitActivity.class));
    }

    public void cancel(View view) {
        asynchTaskType = "Cancel";
        new CallAPI().execute("Cancel");
    }

    public boolean checkEmails(String email, String teamEmails){
        boolean valid = true;
        email = email.trim();
        valid = testEmail(email);
        String[] emailArray = strip(teamEmails);
        int i = 0;
        while(i < emailArray.length && valid){
            valid = testEmail(emailArray[i]);
            i++;
        }
        return valid;
    }

    public static boolean testEmail(String email){
        return email.matches("[a-zA-Z]+(((\\-)|[._a-zA-Z0-9])*)@cable.comcast.com")||email.matches("");
    }

    public String[] strip(String unStripped) {
        String[] stripped = unStripped.split(",");
        for (int i = 0; i < stripped.length; i++) {
            stripped[i] = stripped[i].trim();
        }
        return stripped;
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(parent.getItemAtPosition(position).toString());
            if (parent.getItemAtPosition(position).toString().equals("Ideas")) {
                startActivity(new Intent(EditActivity.this, Directory.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Partners")) {
                startActivity(new Intent(EditActivity.this, Partners.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Success Stories")) {
                startActivity(new Intent(EditActivity.this, SuccessStoriesMain.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Challenges")) {
                startActivity(new Intent(EditActivity.this, Challenges.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Lab Weeks")) {
                startActivity(new Intent(EditActivity.this, LabWeekDirectory.class));
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

    public void startWelcome(View view) {startActivity(new Intent(this, MyActivity.class));}


}

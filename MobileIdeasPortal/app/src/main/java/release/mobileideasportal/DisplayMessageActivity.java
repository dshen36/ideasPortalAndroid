package release.mobileideasportal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

    ImageView image;
    Bitmap bmp;
    ScrollView scrollView;

    LinearLayout ideaHolder;

    boolean showImage = true, firstTime = true;

    Spinner dropDownSpinner;
    String[] subMenus = {"Menu","Ideas","Lab Weeks","Challenges","Partners","Success Stories"};

    String title, tags, issue, description, customerExperienceImpact, metrics, email, teammatesEmail, lastModified;

    int status, intellectualPropertyStatus, id, votes;

    Button editContents;
    LinearLayout buttons;

    int currentIdea;

    Idea idea;

    String asynchTaskType;

    int[] availableIds;

    ProgressDialog progressDialog;
    URL url;

    private Handler uIHandler = new Handler();
    RelativeLayout layout;

    boolean vote;

    CredentialHolder credentialHolder;
    String urlString;
    SearchView searchIdeas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = ProgressDialog.show(this, "Retrieving Idea", "Please Wait While We Retrieve The Idea");
        setContentView(R.layout.activity_display_message);
        layout =(RelativeLayout) findViewById(R.id.layout);
        layout.setFocusable(true);
        idea = new Idea();

        credentialHolder = CredentialHolder.getInstance(this);

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

        scrollView = (ScrollView) findViewById(R.id.scrollView2);
        ideaHolder = (LinearLayout) findViewById(R.id.ideaHolder);

        image = (ImageView) findViewById(R.id.image);
        editContents = (Button) findViewById(R.id.edit_contents);
        buttons = (LinearLayout) findViewById(R.id.buttons);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,subMenus);

        dropDownSpinner = (Spinner) findViewById(R.id.spinner);
        dropDownSpinner.setAdapter(adapter);
        dropDownSpinner.setOnItemSelectedListener(spinnerListener);

        searchIdeas = (SearchView) findViewById(R.id.ideaSearch);
        searchIdeas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                asynchTaskType = "Search";
                urlString = "http://rossette9-001-site1.mywindowshosting.com/api/idea?searchQuery=" + query + "&searchParamater=Title";
                Intent intent = new Intent(DisplayMessageActivity.this, Directory.class);
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


        availableIds = getIntent().getIntArrayExtra("Available Ids");

        asynchTaskType = "Load";
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

    public void cxInnovationsClick(View view) {
        startActivity(new Intent(this, CxInnovationMain.class));
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            /*String urlString = params[0]; // URL to call

            String resultToDisplay = "";

            InputStream in = null;*/

            // HTTP Get
            if(asynchTaskType.equals("Load")) {
                System.out.println("Asynch Task: Loading");
                try {
                    if (getIntent().getStringExtra("url") != null) {
                        url = new URL(getIntent().getStringExtra("url"));
                    } else if (availableIds != null) {
                        currentIdea = availableIds[0];
                        url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/" + currentIdea);
                    } else if (getIntent().getIntArrayExtra("availableIds") != null) {
                        availableIds = getIntent().getIntArrayExtra("availableIds");
                        currentIdea = availableIds[0];
                        url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/" + currentIdea);
                    } else {
                        url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea");
                        /*url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/10");
                        availableIds = new int[]{10};
                        currentIdea = 10;*/
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
                        JSONObject jsonObject;
                        if(jsonText.charAt(0) == '[') {
                            JSONArray jsonArray = new JSONArray(jsonText);
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

                    conn.disconnect();
                    System.out.println(idea.getImageIds());
                    if(!idea.getImageIds().equals("null") || showImage) {/*
                        System.out.println("getting images");
                        String [] nums = idea.getImageIds().split("/^[1-9][0-9]*$/");
                        int[] numbers = new int[nums.length];
                        for(int i = 0; i < nums.length; ++i){
                            numbers[i] = Integer.parseInt(nums[i].trim());
                        }*/
                        url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/Image/1021?imageId=16");
/*
                        url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/Image/" + (availableIds != null ? availableIds[0]:idea.getId()) +"?imageId=" + numbers[0] );
*/
                        conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");

                            if (conn.getResponseCode() != 200) {
                                System.out.println("Url:  " + url.toString());
                                throw new RuntimeException("Failed : HTTP error code : "
                                        + conn.getResponseCode());
                            }
                        InputStream in = new BufferedInputStream(url.openStream());
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int n = 0;
                        while (-1!=(n=in.read(buf))){
                            out.write(buf, 0, n);
                        }
                        out.close();
                        in.close();
                        byte[] response = out.toByteArray();
                        bmp = BitmapFactory.decodeByteArray(response,0,response.length);
                        //image.setImageBitmap(bmp);
/*
                        br = new BufferedReader(new InputStreamReader(
                                (conn.getInputStream())));

                        String byteArrayText = "";
                        System.out.println("Output from Server .... \n");
                        while ((output = br.readLine()) != null) {
                            byteArrayText = byteArrayText + output;
                        }


                        System.out.println(byteArrayText);

                        InputStream inputStream = conn.getInputStream();
                        byte [] imageArray = IOUtils.toByteArray(inputStream);
                        System.out.println(imageArray.toString());
                        imageArray = byteArrayText.getBytes();
                        //IdeasImages ideasImages = new IdeasImages
                        *//*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
                            baos.write(buffer, 0, read);
                        }
                        baos.flush();
                        byte [] imageArray = baos.toByteArray();*//*
                        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                        //Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);*/
                        /*image = (ImageView) findViewById(R.id.image);
                        image.setImageBitmap(bmp);*/
                        conn.disconnect();
                    }

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            } else if (asynchTaskType.equals("Next")){

                System.out.println("Asynch Task: Next");
                try {
                    currentIdea = availableIds[0];
                    url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/" + currentIdea);
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
                        idea.setImageIds(jsonObject.getString("ImageIDs"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();
                    System.out.println(idea.getImageIds());
                    if(!idea.getImageIds().equals("null") && firstTime) {
                        showImage = true;
                        System.out.println("getting images");
                        String [] nums = idea.getImageIds().split("/^[1-9][0-9]*$/");
                        int[] numbers = new int[nums.length];
                        for(int i = 0; i < nums.length; ++i){
                            numbers[i] = Integer.parseInt(nums[i].trim());
                        }
                        System.out.println("http://rossette9-001-site1.mywindowshosting.com/api/Image/" + availableIds[0] +"?imageId=" + numbers[0]);
                        url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/Image/" + availableIds[0] +"?imageId=" + numbers[0]);
                        HttpURLConnection conn1 = (HttpURLConnection) url.openConnection();
                        conn1.setRequestMethod("GET");
                        conn1.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        conn1.setRequestProperty("charset", "UTF-16");


                        if (conn1.getResponseCode() != 200) {
                            System.out.println("Url:  " + url.toString());
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + conn.getResponseCode());
                        }


                        /*InputStream inputStream = conn1.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int read = 0;
                        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
                            baos.write(buffer, 0, read);
                        }
                        baos.flush();
                        byte [] imageArray = baos.toByteArray();
                        Bitmap bmp = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                        image.setImageBitmap(bmp);*/
                        br = new BufferedReader(new InputStreamReader(
                                (conn1.getInputStream())));

                        String byteArrayText = "";
                        System.out.println("Output from Server .... \n");
                        while ((output = br.readLine()) != null) {
                            byteArrayText = byteArrayText + output;
                        }

                        System.out.println(byteArrayText);

                        conn.disconnect();
                    } else if(!idea.getImageIds().equals("null")) {
                        showImage = true;
                    }

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            } else if (asynchTaskType.equals("Vote")){
                System.out.println("Asynch Task: Voting");
                try {
                    url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea/" + currentIdea + "?voteUp=" + vote);
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
            } else if(asynchTaskType.equals("Ideas")) {
                URL url = null;
                try {

                    url = new URL("http://rossette9-001-site1.mywindowshosting.com/api/idea");
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        availableIds[i] = jsonObject.getInt("Id");
                        System.out.println(availableIds[i]);
                    }
                    url = null;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                asynchTaskType = "Load";
                new CallAPI().execute("value");
            }

            return null/*resultToDisplay*/;
        }

        protected void onPostExecute(String result) {
            System.out.println("PostExecute");
            if(asynchTaskType.equals("Next")) {
                scrollView.scrollTo(0,0);
            }
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
        if(bmp != null && firstTime) {
            image = (ImageView) findViewById(R.id.image);
            image.setImageBitmap(bmp);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            System.out.println(byteArray);
            firstTime = false;
            ideaHolder.removeView(image);
        }
        if (showImage && image.getParent() == null) {
            ideaHolder.addView(image, 1);
        }else {
            ideaHolder.removeView(image);
        }
        if(!credentialHolder.getUserEmail().equals(idea.getEmail())) {
            if(editContents.getParent()!= null && editContents.getParent().equals(buttons)) {
                buttons.removeView(editContents);
            }
        } else {
            if(editContents.getParent()==null) {
                buttons.addView(editContents,0);
            }
        }
        showImage = false;
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
        progressDialog = ProgressDialog.show(this, "Loading Next Idea", "Please Wait");
        asynchTaskType = "Next";
        new CallAPI().execute("value");
    }

    public void upVote(View view) {
        asynchTaskType = "Vote";
        vote = true;
        new CallAPI().execute("value");
    }

    public void downVote(View view) {
        asynchTaskType = "Vote";
        vote = false;
        new CallAPI().execute("value");
    }

    public void createIdea(View view) {
        startActivity(new Intent(DisplayMessageActivity.this, SubmitActivity.class));
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(parent.getItemAtPosition(position).toString());
            if (parent.getItemAtPosition(position).toString().equals("Ideas")) {
                /*asynchTaskType = "Ideas";
                new CallAPI().execute("Ideas");*/
                startActivity(new Intent(DisplayMessageActivity.this, Directory.class));
//                dropDownSpinner.setSelection(0);
                dropDownSpinner.setOnItemSelectedListener(spinnerListener);
            } else if (parent.getItemAtPosition(position).toString().equals("Partners")) {
                startActivity(new Intent(DisplayMessageActivity.this, Partners.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Success Stories")) {
                startActivity(new Intent(DisplayMessageActivity.this, SuccessStoriesMain.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Challenges")) {
                startActivity(new Intent(DisplayMessageActivity.this, Challenges.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Lab Weeks")) {
                startActivity(new Intent(DisplayMessageActivity.this, LabWeekDirectory.class));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            dropDownSpinner.setOnItemSelectedListener(spinnerListener);
        }
    };

    public void startWelcome(View view) {startActivity(new Intent(this, MyActivity.class));}
}

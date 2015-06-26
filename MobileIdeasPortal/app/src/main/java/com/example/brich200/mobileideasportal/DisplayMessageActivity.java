package com.example.brich200.mobileideasportal;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMessageActivity extends ActionBarActivity {

    TextView title, tags, issue, description, customerExperienceImpact, metrics, email, teammatesEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        title = (TextView) findViewById(R.id.title);
        tags = (TextView) findViewById(R.id.tags);
        issue = (TextView) findViewById(R.id.current_issue);
        description = (TextView) findViewById(R.id.description);
        customerExperienceImpact = (TextView) findViewById(R.id.customer_exp_imp);
        metrics = (TextView) findViewById(R.id.metrics);
        email = (TextView) findViewById(R.id.email);
        teammatesEmail = (TextView) findViewById(R.id.teammates_email);

        Idea idea = Idea.getInstance(this);

        title.setText(idea.getTitle());
        tags.setText(idea.getTags());
        issue.setText(idea.getIssue());
        description.setText(idea.getDescription());
        customerExperienceImpact.setText(idea.getCustomerExperienceImpact());
        metrics.setText(idea.getMetricsImpact());
        email.setText(idea.getEmail());
        teammatesEmail.setText(idea.getAdditionalTeamMemberEmail());

        /*//get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        //create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        //Set the text view as the activity layout
        setContentView(textView);*/
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
}

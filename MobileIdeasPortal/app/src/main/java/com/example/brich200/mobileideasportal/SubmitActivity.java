package com.example.brich200.mobileideasportal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;


public class SubmitActivity extends ActionBarActivity {

    EditText title, tags, issue, description, customerExperience, editOther, email, teamEmail;
    CheckBox self_service, call_deflection, agent_contact, call_resolution, rework, avoidable_truck, upstream_downstream, cost_savings, other;

    Spinner dropDownSpinner;
    String[] subMenus = {"Ideas","Lab Weeks","Challenges","Partners","Success Stories"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,subMenus);

        dropDownSpinner = (Spinner) findViewById(R.id.spinner);
        dropDownSpinner.setAdapter(adapter);
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
        Idea idea = Idea.getInstance(this);
        idea.setTitle(title.getText().toString());
        idea.setTags(tags.getText().toString());
        idea.setIssue(issue.getText().toString());
        idea.setDescription(description.getText().toString());
        idea.setCustomerExperienceImpact(customerExperience.getText().toString());
        idea.setMetricsImpact(checkCheckboxes());
        idea.setEmail(email.getText().toString());
        idea.setAdditionalTeamMemberEmail(teamEmail.getText().toString());
        Toast.makeText(this, "Idea Submitted",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SubmitActivity.this, DisplayMessageActivity.class));
    }

    private String checkCheckboxes() {
        String metricsImpact = "";
        if (self_service.isChecked()){
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
        if(metricsImpact.length() > 0) {
            metricsImpact.substring(0, metricsImpact.length() - 1);
        }
        return metricsImpact;
    }
}

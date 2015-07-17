package release.mobileideasportal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

public class Challenges extends ActionBarActivity {

    String asynchTaskType;
    int[] availableIds;
    Spinner dropDownSpinner;
    String[] subMenus = {"Menu","Ideas","Lab Weeks","Challenges","Partners","Success Stories"};
    String urlString;
    SearchView searchIdeas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

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
                Intent intent = new Intent(Challenges.this, Directory.class);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_partners, menu);
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
                startActivity(new Intent(Challenges.this, Directory.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Partners")) {
                startActivity(new Intent(Challenges.this, Partners.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Success Stories")) {
                startActivity(new Intent(Challenges.this, SuccessStoriesMain.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Challenges")) {
                startActivity(new Intent(Challenges.this, Challenges.class));
            } else if (parent.getItemAtPosition(position).toString().equals("Lab Weeks")) {
                startActivity(new Intent(Challenges.this, LabWeekDirectory.class));
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

    public void startWelcome(View view) {startActivity(new Intent(this, MyActivity.class));}
}

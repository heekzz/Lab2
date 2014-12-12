package freha_tddd13.lab2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.view.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {
    private ListAdapter listAdapter;
    private ExpandableListView exListView;
    private EditText searchField;
    private List<String> listHeader;
    private HashMap<Integer, List<String>> listChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addSomeData();

        exListView = (ExpandableListView) findViewById(R.id.expandableListView);
        searchField = (EditText) findViewById(R.id.textField);

        listAdapter = new ListAdapter(this, listHeader, listChild);
        exListView.setAdapter(listAdapter);
        exListView.setOnChildClickListener(childClickListener);
        exListView.setOnGroupExpandListener(groupExpandListener);
        exListView.setOnGroupCollapseListener(groupCollapseListener);
        exListView.setOnGroupClickListener(groupClickListener);


        searchField.addTextChangedListener(watcher);
        searchField.setHint("€/€");
        searchField.setText("/");
        searchField.setSelection(searchField.getText().length());

    }

    private ExpandableListView.OnGroupExpandListener groupExpandListener = new ExpandableListView.OnGroupExpandListener() {
        @Override
        public void onGroupExpand(int i) {
            String tmpText = "/" + listHeader.get(i) + "/";
            if(!searchField.getText().toString().equals(tmpText))
                searchField.setText(tmpText);

            searchField.setSelection(searchField.getText().length());

        }
    };

    private ExpandableListView.OnGroupCollapseListener groupCollapseListener = new ExpandableListView.OnGroupCollapseListener() {
        @Override
        public void onGroupCollapse(int i) {
            if(!searchField.getText().toString().equals("/"))
                searchField.setText("/");
            searchField.setSelection(searchField.getText().length());
        }
    };


    private ExpandableListView.OnChildClickListener childClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
            String tmpText = "/" + listHeader.get(groupPosition) + "/" + listChild.get(groupPosition).get(childPosition);
            if(!searchField.getText().toString().equals(tmpText)) {
                searchField.setText(tmpText);
            }
            searchField.setSelection(searchField.getText().length());

            int index = exListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));
            exListView.setItemChecked(index, true);

            return false;
        }
    };
    private ExpandableListView.OnGroupClickListener groupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            exListView.clearChoices();
            return false;
        }
    };
    // Listening to any changes in the textfield
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            searchDetector(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    // Matches the input in the searchfield towards the ExpandableListView
    private void searchDetector(CharSequence charSequence) {
        String phrase = charSequence.toString();
        String[] splitPhrase = phrase.split("/");
        String group = null, child = null;
        int childID = 0;
        searchField.setBackgroundColor(Color.WHITE);

        // Devide the searchphrase in two strings to separate group and children matches in the list
        ArrayList<Integer> groupIDs = new ArrayList<Integer>();
        if (splitPhrase.length > 1) {
            group = splitPhrase[1];

            // Loop through possible group duplicates, if we have more groups
            // with the same name we put them in a ArrayList to be able to loop
            // through all the group's children
            for(int i=0;i<listHeader.size();i++){
                if(group.equalsIgnoreCase(listHeader.get(i))){
                    groupIDs.add(i);
                }
            }
        }

        if (splitPhrase.length > 2) {
            child = splitPhrase[2];
            for (int i = 0; i < groupIDs.size(); i++) {
                if (listChild.get(groupIDs.get(i)).contains(child)) {
                    childID = listAdapter.getChildPosition(groupIDs.get(i),child);

                }
            }
        }

        // If the substring in the search field matches something in the eXListView, set color to white, else red
        if(!matchSubstring(group, child, groupIDs)) {
            if(!charSequence.toString().equalsIgnoreCase("/"))
                searchField.setBackgroundColor(Color.RED);
            exListView.clearChoices();
            listAdapter.notifyDataSetChanged();
        }


        // If we match a children we highlight it.
        for(int i = 0; i < groupIDs.size(); i++){
            if(child != null && group != null && listChild.get(groupIDs.get(i)).contains(child)) {
                int index = exListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupIDs.get(i), childID));
                exListView.setItemChecked(index, true);

            }
            if (!exListView.isGroupExpanded(groupIDs.get(i))) {
                exListView.expandGroup(groupIDs.get(i));
            }
        }
    }


    // Checks if the typed string in searchField matches a possible result
    private boolean matchSubstring(String group, String child, ArrayList groupIDs) {
        boolean G = false, C = false;

        if(group != null) {
            // Looking for match in group titles
            for (String word : listHeader) {
                if (word.startsWith(group))
                    G = true;
            }

            if(child != null) {


                for(int i = 0; i < groupIDs.size(); i++){
                    List<String> tmp = listChild.get(groupIDs.get(i));
                    for(int j = 0; j < tmp.size() ; j++) {
                        if (tmp.get(j).startsWith(child))
                            C = true;
                    }
                }
            }

            else
                return G; // if there only is a group typed we only look for groups
        }

        return C && G;
    }

    // Fills the table with data
    // very nice
    private void addSomeData () {
        listHeader = new ArrayList<String>();
        listChild = new HashMap<Integer, List<String>>();

        listHeader.add("dennis");
        listHeader.add("dennis");
        listHeader.add("fredrik");
        listHeader.add("anton");
        listHeader.add("alex");

        List<String> deno = new ArrayList<String>();
        deno.add("king");
        deno.add("boss");

        List<String> deno2 = new ArrayList<String>();
        deno2.add("nice");
        deno2.add("guy");
        deno2.add("nice");


        List<String> fredrik = new ArrayList<String>();
        fredrik.add("boo");
        fredrik.add("loser");
        fredrik.add("mamasboy");

        List<String> anton = new ArrayList<String>();
        anton.add("lol");
        anton.add("thisguy");
        anton.add("idonteven");
        anton.add("gohome");

        List<String> alex = new ArrayList<String>();
        alex.add("tryhard");
        alex.add("weebaby");
        alex.add("diaperboy");

        listChild.put(0, deno);
        listChild.put(1, deno2);
        listChild.put(2, fredrik);
        listChild.put(3, anton);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
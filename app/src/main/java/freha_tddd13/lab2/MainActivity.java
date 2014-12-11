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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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


        searchField.addTextChangedListener(watcher);
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
            if(!searchField.getText().toString().equals(tmpText))
                searchField.setText(tmpText);
            searchField.setSelection(searchField.getText().length());

            int index = exListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition,childPosition));
            exListView.setItemChecked(index, true);
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
        int groupID = 0, childID = 0;
        searchField.setBackgroundColor(Color.WHITE);

        // Devide the searchphrase in two strings to separate group and children matches in the list
        if (splitPhrase.length > 1) {
            group = splitPhrase[1];
            groupID = listAdapter.getGroupPosition(group);
        }
        if (splitPhrase.length > 2) {
            child = splitPhrase[2];
            childID = listAdapter.getChildPosition(group, child);
        }

        // If the substring in the searchfield matches something in the eXListView, set color to white, else red
        if(!matchSubstring(group, child)) {
            searchField.setBackgroundColor(Color.RED);
        }
        // If we match something from our list we expand it
        if (listHeader.contains(group) && !exListView.isGroupExpanded(listAdapter.getGroupPosition(group))) {
            exListView.expandGroup(groupID);
        }

        // If we match a childen we highlight it.
        if(child != null && group != null && listChild.get(groupID).contains(child)){
           final int index = exListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupID,childID));
            exListView.setItemChecked(index, true);
        }
    }


    // Checks if the typed string in searchField matches a possible result
    private boolean matchSubstring(String group, String child) {
        boolean G = false, C = false;

        if(group != null) {
            // Looking for match in group titles
            for (String word : listHeader) {
                if (word.startsWith(group))
                    G = true;
            }

            List<String> tmp = listChild.get(listAdapter.getGroupPosition(group));

            if(child != null) {
                for (String word : tmp) {
                    if (word.startsWith(child))
                        C = true;
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

        listHeader.add("light");
        listHeader.add("light");
        listHeader.add("medium");
        listHeader.add("dark");

        List<String> light = new ArrayList<String>();
        light.add("blue");
        light.add("red");

        List<String> light2 = new ArrayList<String>();
        light2.add("yellow");
        light2.add("green");


        List<String> medium = new ArrayList<String>();
        medium.add("blue");
        medium.add("red");
        medium.add("yellow");
        medium.add("green");

        List<String> dark = new ArrayList<String>();
        dark.add("blue");
        dark.add("red");
        dark.add("yellow");
        dark.add("green");

        listChild.put(0, light);
        listChild.put(1, light2);
        listChild.put(2, medium);
        listChild.put(3, dark);


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

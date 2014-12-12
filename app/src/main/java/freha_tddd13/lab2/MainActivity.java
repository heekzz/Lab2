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
            //on expand, we set the name of the group header in the search field and surround the text with forward slashes.
            String tmpText = "/" + listHeader.get(i) + "/";
            if(!searchField.getText().toString().equals(tmpText))
                searchField.setText(tmpText);

            // this makes sure the text cursor is at the end
            searchField.setSelection(searchField.getText().length());

        }
    };

    private ExpandableListView.OnGroupCollapseListener groupCollapseListener = new ExpandableListView.OnGroupCollapseListener() {
        @Override
        public void onGroupCollapse(int i) {
            // on collapse we set the text as only a forward slash
            if(!searchField.getText().toString().equals("/"))
                searchField.setText("/");
            searchField.setSelection(searchField.getText().length());
        }
    };


    private ExpandableListView.OnChildClickListener childClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
            // this sets the name of the group and the child in the search field
            String tmpText = "/" + listHeader.get(groupPosition) + "/" + listChild.get(groupPosition).get(childPosition);
            if(!searchField.getText().toString().equals(tmpText)) {
                searchField.setText(tmpText);
            }
            searchField.setSelection(searchField.getText().length());

            // uses flatlistposition to get the index of the child we want to highlight
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
    // Listens to any changes in the textfield
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
        ArrayList<Integer> groupIDs = new ArrayList<>();

        // Divide the search phrase in two strings to separate group and children matches in the list
        if (splitPhrase.length > 1) {
            group = splitPhrase[1];

            // Loop through possible group duplicates, if we have more groups
            // with the same name we put them in a ArrayList to be able to loop
            // through all the different group's children
            for(int i=0;i<listHeader.size();i++){
                if(group.equalsIgnoreCase(listHeader.get(i))){
                    groupIDs.add(i);
                }
            }
        }

        // Use a if-function to avoid ArrayIndexOutOfBounds Exception if
        // we dont have a child typed in the searchfield
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
            exListView.clearChoices(); // Clear previous marked item in the ExpListView
            listAdapter.notifyDataSetChanged();
        } else {
            searchField.setBackgroundColor(Color.WHITE);
        }

        // If we match a children we highlight it.
        // Loop through possible multiple group matches to see where the child is located
        for(int i = 0; i < groupIDs.size(); i++){
            if(child != null && group != null && listChild.get(groupIDs.get(i)).contains(child)) {
                int index = exListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupIDs.get(i), childID));
                exListView.setItemChecked(index, true); // Highlight the child
            }
            if (!exListView.isGroupExpanded(groupIDs.get(i))) {
                exListView.expandGroup(groupIDs.get(i)); // Expand the group where the child is located
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
                // Loop through possible multiple group matches and looking for
                // a child in the different groups which matches the substring
                for(int i = 0; i < groupIDs.size(); i++){
                    List<String> tmp = listChild.get(groupIDs.get(i));
                    for(int j = 0; j < tmp.size() ; j++) {
                        if (tmp.get(j).startsWith(child))
                            C = true;
                    }
                }
            }else
                return G; // if there only is a group typed we only look for groups
        }

        // return true if we match both a group and a child
        return C && G;
    }

    // Fills the table with data
    // very nice
    private void addSomeData () {
        listHeader = new ArrayList<>();
        listChild = new HashMap<>();

        listHeader.add("europe");
        listHeader.add("europe");
        listHeader.add("asia");
        listHeader.add("africa");

        List<String> europe = new ArrayList<>();
        europe.add("germany");
        europe.add("italy");
        europe.add("greece");

        List<String> europe2 = new ArrayList<>();
        europe2.add("sweden");
        europe2.add("norway");
        europe2.add("denmark");


        List<String> asia = new ArrayList<>();
        asia.add("china");
        asia.add("india");
        asia.add("japan");

        List<String> africa = new ArrayList<>();
        africa.add("kenya");
        africa.add("kamerun");
        africa.add("lybia");
        africa.add("gambia");

        listChild.put(0, europe);
        listChild.put(1, europe2);
        listChild.put(2, asia);
        listChild.put(3, africa);
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
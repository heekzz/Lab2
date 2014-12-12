package freha_tddd13.lab2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


public class ListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<String> headerList; // header titles
    // child data in format of header title, child title
    private HashMap<Integer, List<String>> childList;

    public ListAdapter(Context c, List<String> header,
                                 HashMap<Integer, List<String>> children) {
        this.context = c;
        this.headerList = header;
        this.childList = children;
    }

    @Override
    public String getChild(int groupPosition, int childPosititon) {
        return childList.get(groupPosition).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String childText = childList.get(groupPosition).get(childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.listItem);

        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childList.get(groupPosition).size();
    }

    @Override
    public List<String> getGroup(int groupPosition) {
        return childList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.headerList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = headerList.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.listHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getChildPosition(int group, String child) {
        int count = childList.get(group).size();
        for(int i = 0; i < count; i++) {
            if(child.equals(getChild(group, i)))
                return i;
        }
        return 0;
    }
}

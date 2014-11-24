package freha_tddd13.lab2;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fredrik on 14-11-24.
 */
public class ListAdapter implements ExpandableListAdapter{

    ArrayList<Lightness> data = new ArrayList<Lightness>();
    ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();
    Context context;

    public ListAdapter(ArrayList<Lightness> data, Context c) {
        this.data = data;
        this.context = c;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.observers.add(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.observers.remove(dataSetObserver);
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return data.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return data.get(i).getColorList().get(i2);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i2) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Lightness lightness = (Lightness) getGroup(i);
        String title = lightness.getName();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_view, null);
        }

        TextView header =


        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l2) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
}

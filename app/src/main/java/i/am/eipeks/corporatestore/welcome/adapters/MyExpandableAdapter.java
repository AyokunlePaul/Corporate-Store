package i.am.eipeks.corporatestore.welcome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import i.am.eipeks.corporatestore.R;


public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private HashMap<String, List<String>> childrenTitles;
    private List<String> headerTitles;

    public MyExpandableAdapter(Context context, HashMap<String, List<String>> childrenTitles, List<String> headerTitles){
        this.context = context;
        this.childrenTitles = childrenTitles;
        this.headerTitles = headerTitles;
    }

    @Override
    public int getGroupCount() {
        return headerTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenTitles.get(headerTitles.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return headerTitles.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return childrenTitles.get(headerTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_header, parent, false);
        }

        String title = getGroup(groupPosition);
        TextView header = (TextView) convertView.findViewById(R.id.header);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        header.setText(title);

        if (isExpanded){
            icon.setImageResource(R.drawable.ic_keyboard_arrow_down_48pt);
        } else {
            icon.setImageResource(R.drawable.ic_keyboard_arrow_up_48pt);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.home_children, parent, false);
        }

        String child = getChild(groupPosition, childPosition);
        TextView textView = (TextView) convertView.findViewById(R.id.children_view);
        textView.setText(child);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

package andiag.coru.es.welegends.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.entities.Group;
import andiag.coru.es.welegends.entities.League;
import andiag.coru.es.welegends.utils.static_data.ImagesHandler;

/**
 * Created by andyq on 17/07/2015.
 */
public class AdapterLeagues extends BaseExpandableListAdapter {

    private ActivityMain context;
    private List<Group> groups = new ArrayList<>();

    public AdapterLeagues(ActivityMain context) {
        this.context = context;
    }

    public void updateGroups(ArrayList<Group> list){
        this.groups=list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getLeagues().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getLeagues().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String groupName = ((Group) getGroup(groupPosition)).getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.textGroup);
        item.setText(groupName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        League l = (League) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_league, null);
        }

        TextView textName = (TextView) convertView.findViewById(R.id.textTeamName);
        TextView textRankeds = (TextView) convertView.findViewById(R.id.textRanked);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageRanked);

        textName.setText(l.getEntries().get(0).getPlayerOrTeamName());

        String imres = l.getTier() + l.getEntries().get(0).getDivision();
        int id = context.getResources().getIdentifier(imres.toLowerCase(),"drawable",context.getPackageName());
        image.setImageResource(id);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

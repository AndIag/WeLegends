package andiag.coru.es.welegends.adapters;

/**
 * Created by andyq on 17/07/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import andiag.coru.es.welegends.R;
import andiag.coru.es.welegends.activities.ActivityMain;
import andiag.coru.es.welegends.entities.Item;
import andiag.coru.es.welegends.entities.ItemLeague;
import andiag.coru.es.welegends.entities.ItemSection;

public class AdapterListHeader extends BaseAdapter {

    private ActivityMain context;
    private ArrayList<Item> items = new ArrayList();
    private LayoutInflater vi;

    public AdapterListHeader(ActivityMain context) {
        this.context = context;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateItems(ArrayList<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final Item i = items.get(position);
        if (i != null) {
            if(i.isSection()){
                ItemSection si = (ItemSection)i;
                v = vi.inflate(R.layout.group_item, null);
                TextView sectionView =
                        (TextView) v.findViewById(R.id.textGroup);
                sectionView.setText(si.getName());
            } else {
                ItemLeague ei = (ItemLeague)i;
                v = vi.inflate(R.layout.item_league, null);
                TextView textName = (TextView) v.findViewById(R.id.textTeamName);
                TextView textRankeds = (TextView) v.findViewById(R.id.textRanked);
                ImageView image = (ImageView) v.findViewById(R.id.imageRanked);

                textName.setText(ei.getLeague().getEntries().get(0).getPlayerOrTeamName());

                String imres = ei.getLeague().getTier() + ei.getLeague().getEntries().get(0).getDivision();
                int id = context.getResources().getIdentifier(imres.toLowerCase(),"drawable",context.getPackageName());
                image.setImageResource(id);

            }
        }
        v.setOnClickListener(null);
        v.setOnLongClickListener(null);
        v.setLongClickable(false);
        return v;
    }
}
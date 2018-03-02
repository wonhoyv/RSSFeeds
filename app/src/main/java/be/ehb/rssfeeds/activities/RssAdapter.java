package be.ehb.rssfeeds.activities;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.ehb.rssfeeds.R;
import be.ehb.rssfeeds.customviews.NonClickableWebview;
import be.ehb.rssfeeds.model.RssItem;


public class RssAdapter extends BaseAdapter {

    private class ViewHolder
    {
        private NonClickableWebview descriptionWV;
        private TextView titleTV;
    }

    private ArrayList<RssItem> items;
    private Activity activity;

    public RssAdapter(Activity activity) {
        this.items = new ArrayList<>();
        this.activity= activity;
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

        ViewHolder holder;

        //is er al een view aangemaakt?
        if(convertView == null)
        {
            holder = new ViewHolder();

            convertView = activity.getLayoutInflater().inflate(R.layout.rss_row, parent, false);
            holder.descriptionWV = (NonClickableWebview) convertView.findViewById(R.id.wv_description);
            holder.titleTV = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();

        RssItem currentItem = items.get(position);

        holder.titleTV.setText(currentItem.getTitle());
        holder.descriptionWV.loadData(currentItem.getDescription(), "text/html", "UTF-8");
        holder.descriptionWV.getSettings().setTextZoom(85);

        return convertView;
    }

    public void addAll(ArrayList<RssItem> items)
    {
        if(items == null)
        {
            Toast.makeText(activity, "No new items found, please check your connection", Toast.LENGTH_LONG).show();
            return;
        }
        this.items.addAll(items);
    }
}

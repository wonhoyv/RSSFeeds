package be.ehb.rssfeeds.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.net.URL;

import be.ehb.rssfeeds.R;
import be.ehb.rssfeeds.model.RssItem;
import be.ehb.rssfeeds.parser.RSSParserTask;


public class MainActivity extends AppCompatActivity {

    private ListView feedsLV;
    private RssAdapter adapter;
    private ProgressBar waitPB;

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RssItem item = (RssItem) adapter.getItem(position);
            Intent articleIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
            startActivity(articleIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waitPB = findViewById(R.id.pb_wait);

        adapter = new RssAdapter(this);

        feedsLV = findViewById(R.id.lv_feeds);
        feedsLV.setAdapter(adapter);
        feedsLV.setOnItemClickListener(listener);

        try {
            URL urlEUResearchNanoTech = new URL("http://ec.europa.eu/research/rss/whatsnew-54.xml");
            URL urlKMI = new URL("http://www.meteo.be/meteo/view/nl/65679-Nieuws.html?rss=true");
            RSSParserTask parserTask = new RSSParserTask(adapter, waitPB);
            parserTask.execute(urlEUResearchNanoTech, urlKMI);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Parse", "No good url senior");
        }
    }
}

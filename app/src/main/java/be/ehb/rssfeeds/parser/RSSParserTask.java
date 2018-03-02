package be.ehb.rssfeeds.parser;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import be.ehb.rssfeeds.activities.RssAdapter;
import be.ehb.rssfeeds.model.RssItem;


//zie developer blog => http://android-developers.blogspot.be/2011/12/watch-out-for-xmlpullparsernexttext.html
public class RSSParserTask extends AsyncTask<URL, Void, ArrayList<RssItem>> {

    private RssAdapter adapter;
    private WeakReference<ProgressBar> waitPB;

    public RSSParserTask(RssAdapter adapter, ProgressBar waitPB) {
        this.adapter = adapter;
        this.waitPB = new WeakReference<ProgressBar>(waitPB);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        waitPB.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<RssItem> doInBackground(URL... params) {
        ArrayList<RssItem> rssItems = new ArrayList<>();

        for (URL url : params) {
            try {
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(url.openStream(), null);

                String currentTag;
                String title = null;
                String link = null;
                String description= null;

                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        currentTag = xmlPullParser.getName();

                        eventType = xmlPullParser.next();
                        //reden dat dit meteen na de starttag wordt uitgevoerd is dat het anders door lege tekst zal worden overschreven bij de eindtag
                        //na de eindtag volgt er nog lege tekst en de parser ziet </link> als identiek aan <link>
                        if (eventType == XmlPullParser.TEXT) {
                            if ("title".equals(currentTag)) {
                                title = xmlPullParser.getText();
                            }
                            if ("link".equals(currentTag)) {
                                link = xmlPullParser.getText();
                            }
                            if ("description".equals(currentTag)) {
                                description = xmlPullParser.getText();
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if ("item".equals(xmlPullParser.getName())) {
                            rssItems.add(new RssItem(description, link, title));
                        }
                    }
                    eventType = xmlPullParser.next();
                }
                //first item = channel, removed
                rssItems.remove(0);
            } catch (XmlPullParserException | IOException e) {
                //returning null when failed, adapter will handle null arrays
                Log.d("Parse", "Fault while parsing:" + e.getLocalizedMessage());
            }
        }
        return rssItems;
    }

    @Override
    protected void onPostExecute(ArrayList<RssItem> rssItems) {
        super.onPostExecute(rssItems);
        adapter.addAll(rssItems);
        adapter.notifyDataSetChanged();

        waitPB.get().setVisibility(View.INVISIBLE);
    }
}

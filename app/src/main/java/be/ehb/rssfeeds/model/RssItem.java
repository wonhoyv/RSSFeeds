package be.ehb.rssfeeds.model;


public class RssItem {

	private String description;
	private String link;
	private String title;

	public RssItem(String description, String link, String title) {
		this.description = description;
		this.link = link;
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public String getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}
}

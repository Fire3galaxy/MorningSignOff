package app.morningsignout.com.morningsignoff;

public class Article {
    // Planned to use for Category page (you don't have to)
	String title;
    String description;
    String link;

    // Used for Article page
    String article_body;
    String image_link;
    String article_title;

    public String getArticle_body() {
        return article_body;
    }
    public void setArticle_body(String article_body) {
        this.article_body = article_body;
    }

    public String getImage_link() {
        return image_link;
    }
    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getArticle_title() {
        return article_title;
    }
    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}

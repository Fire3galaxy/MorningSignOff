package app.morningsignout.com.morningsignoff;

import android.graphics.Bitmap;

public class Article {
    private String title;
    private String description;
    private String link;
    private String imageURL;
    private int id;
    private Bitmap image;

    public Article() {
    }

    public Article(String title, String description, Bitmap image, int id) {
        this.title = title;
        this.description = description;
        this.image =image;
        this.id = id;
    }

    // testing
    public Article(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    Bitmap bitmap;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

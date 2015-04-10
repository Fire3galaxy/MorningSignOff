package app.morningsignout.com.morningsignoff;

import android.graphics.Bitmap;

public class Article {
	private String title;
    private String description;
    private String link;
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

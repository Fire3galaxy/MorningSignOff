package app.morningsignout.com.morningsignoff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

// This class is called in Category Activity to fetch articles online and fead it to CategoryAdapter
// do in background gets the article objects from morningsignout, and convets imageURL to bitmap
// getArticels() is the method that connects to the website and html
// articles are sent to onPostExecute, where I set arrayAdapter.
public class FetchListArticlesTask extends AsyncTask<String, Void, List<Article>> {
    // Assigned value keeps logString in sync with class name if class name changed (Udacity)
    private final String logString = FetchListArticlesTask.class.getSimpleName();

    private int numOfArtilesLoadAtOnce = 12;
    // Used for creating CategoryAdapter and onItemClick listener
    private ListView listView;
    private ProgressBar progressBar;
    private Context c;
    private String category;

    // The dimension of the rescaled bitmap
    private static final int bitmapDimension = 100;

    public FetchListArticlesTask(Context c, ListView listView, ProgressBar progressBar) {
        this.c = c;
        this.listView = listView;
        this.progressBar = progressBar;
    }

    public FetchListArticlesTask() {
    }

    // takes in the category name as a sufix to the URL, ex. healthcare/  and call getArticles()
    @Override
    protected List<Article> doInBackground(String... params) {
        try {
            // pass the category name as a string
            category = params[0];
            return getArticles(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Articles retrived online are being sent here, and we pass the info to the CategoryAdapter
    protected void onPostExecute(final List<Article> articles) {
        progressBar.setVisibility(ProgressBar.GONE);

        // Setup the adapter using the CategoryAdapter class
        if(listView.getAdapter() == null)
            listView.setAdapter(new CategoryAdapter(c, articles));

        // Setup the click listener for the listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // test show its clicked
                String articleTitle = articles.get(position).getTitle();
                Toast toast = Toast.makeText(c.getApplicationContext(),
                        "Loading Article: " + articleTitle, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                // Create new activity for the selected page here
                // feed the new activity with the URL of the page
                String articleLink = articles.get(position).getLink();
                Intent articleActivity = new Intent(c, ArticleActivity.class);
                articleActivity.putExtra(Intent.EXTRA_HTML_TEXT, articleLink);
                articleActivity.putExtra(Intent.EXTRA_SHORTCUT_NAME, articleTitle);
                articleActivity.putExtra(Intent.EXTRA_TITLE, category);

                c.startActivity(articleActivity);
            }
        });

    }

    // input an image URL, get its bitmap
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // rescale the bitmap
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmapDimension, bitmapDimension, true);
                return scaledBitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    // Go to MorningSignOut.com and get a list of articles
    // get 12 articles and sent to onPostExecute()
	List<Article> getArticles(String arg) {
        // String arg is "research", "wellness", "humanities", etc.
        // For getting article titles, descriptions, and images. See class Article
        List<Article> articlesList;
        Parser p = new Parser();
        String urlPath = "http://morningsignout.com/category/" + arg;

        BufferedReader in = null;
        HttpURLConnection c = null; // Done because of tutorial

        try {
            // Open connection to
            URL url = new URL(urlPath);
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();

            if (c.getInputStream() == null) return null; // Stream was null, why?

            in = new BufferedReader(new InputStreamReader(c.getInputStream() ) );
            String inputLine;

            // For parsing the html
            boolean inContent = false; // If in <h1> tags, need to wait 2 tags before
            int closeDiv = 0, ind = 0; // counts </div> tags, ind is index of articlesList

            articlesList = new ArrayList<Article>();

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("<div class=\"content__post__info\">")) {
                    articlesList.add(new Article());
                    inContent = true;
                }

                if (inContent) {
                    // Title & Link of article
                    if (inputLine.contains("<h1>")) {
                        String title = p.getTitle(inputLine),
                                link = p.getLink(inputLine);

                        articlesList.get(ind).setTitle(title);
                        articlesList.get(ind).setLink(link);
                    } // Image URL
                    else if (inputLine.trim().contains("<img")) {
                        String imageURL = p.getImageURL(inputLine);

                        // convert string to bitmap then feed to each article
                        Bitmap image = downloadBitmap(imageURL);
                        articlesList.get(ind).setImage(image);
                    }
                    // Description of article
                    else if (inputLine.contains("<p>")) {
                        String description = p.getDescription(inputLine);
                        articlesList.get(ind).setDescription(description);
                    }

                    if (inputLine.trim().equals("</div>")) closeDiv++;
                    if (closeDiv == 2) {
                        closeDiv = 0;
                        inContent = false;
                        ind++;
                        // System.out.println();
                    }
                }

                //        		System.out.println(inputLine.trim());

            }
            in.close();
//	        for (int i = 0; i < articles.size(); i++)
//	        	System.out.println(articles.get(i).getDescription());

            // If buffer was empty, no items in list, so website has no articles for some reason.
            return articlesList.isEmpty() ? null : articlesList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(logString, "error closing stream", e);
                }
            }
        }

        return null; // Exiting try/catch likely means error occurred.
    }
}

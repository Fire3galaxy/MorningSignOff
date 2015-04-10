package app.morningsignout.com.morningsignoff;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
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

    // Used for creating CategoryAdapter and onItemClick listener
    private ListView listView;
    private ProgressBar progressBar;
    private Context c;
    private String category;

    private List<Article> articlesList;
    private int pageNum;

    // The dimension of the rescaled bitmap
    private static final int bitmapDimension = 100;

    public FetchListArticlesTask(Context c, ListView listView, ProgressBar progressBar, int pageNum) {
        this.c = c;
        this.listView = listView;
        this.progressBar = progressBar;
        this.pageNum = pageNum;
    }

    public FetchListArticlesTask() {
    }

    @Override
    protected void onPreExecute() {
        // initialize the batch number to be 1 when no articles are loaded


    }

    // takes in the category name as a sufix to the URL, ex. healthcare/  and call getArticles()
    @Override
    protected List<Article> doInBackground(String... params) {
        try {
            // pass the category name as a string
            category = params[0];
            return getArticles(params[0], pageNum);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Articles retrived online are being sent here, and we pass the info to the CategoryAdapter
    protected void onPostExecute(final List<Article> articles) {
        progressBar.setVisibility(ProgressBar.GONE);

        progressBar.setVisibility(ProgressBar.GONE);

        // Setup the adapter using the CategoryAdapter class
        // If the adapter is not set, then create the adapter and add the articles
        // If the adapter is set, then add more articles to the list then notify the data change
        if(listView.getAdapter() == null) {
            listView.setAdapter(new CategoryAdapter(c, articles));
        }else {
            CategoryAdapter categoryAdapter = (CategoryAdapter) listView.getAdapter();
            categoryAdapter.loadMoreItems(articles, pageNum);
            categoryAdapter.notifyDataSetChanged();
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean isScrolling = false;
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0) {
                    boolean atStart = true;
                    boolean atEnd = true;

                    View firstView = view.getChildAt(0);
                    if (firstVisibleItem > 0) {
                        // not at start
                        atStart = false;
                    }

                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    View lastView = view.getChildAt(visibleItemCount - 1);
                    if (lastVisibleItem < totalItemCount) {
                        // not at end
                        atEnd = false;
                    }

                    // now use atStart and atEnd to do whatever you need to do
                    if(atEnd && isScrolling) {
                        // The articltList only returns null if the last page has 12 items
                        // otherwise it returns random articles
                        if(articlesList == null || articlesList.size() < 12){
                            Toast toast = Toast.makeText(c.getApplicationContext(),
                                    "Bottom", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }else {
                            Toast toast = Toast.makeText(c.getApplicationContext(),
                                    "Loading ...........", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();

                            new FetchListArticlesTask(c, listView, progressBar, ++pageNum).execute(category);
                        }
                        isScrolling = false;
                    }

                }
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScrolling = true;
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
    List<Article> getArticles(String arg, int pageNum) {
        // String arg is "research", "wellness", "humanities", etc.
        // For getting article titles, descriptions, and images. See class Article
        Parser p = new Parser();
        String urlPath;
        if(arg.equals("latest/")){
            urlPath = "http://morningsignout.com/latest/page/" + pageNum;
        }else {
            urlPath = "http://morningsignout.com/category/" + arg +"page/" + pageNum;
        }

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


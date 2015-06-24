package app.morningsignout.com.morningsignoff;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.R.*;

// Category page activity
public class CategoryActivity extends ActionBarActivity {
    ListView list;
    ProgressBar progressBar, progressBar2;
    List<Article> articles;
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use the ListView layout from fragmment_category_main.xml,
        setContentView(R.layout.fragment_category_main);
        // setContentView(R.layout.activity_category);
        list = (ListView)findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        // Fetch the category name from Intent, which is set in MainPageFragment
        Intent intent = getIntent();

        if (intent != null && category.isEmpty()) {
            // Set the title for the Category Activity
            String title = intent.getStringExtra(Intent.EXTRA_TITLE);
            String title_firstCharCap =
                    Character.toString(Character.toUpperCase(title.charAt(0))) + title.substring(1);
            setTitle(title_firstCharCap);
            category = title + "/";
        } else {
            setTitle("Error");
        }

        // Use Asynctask to fetch article from the given category
        new FetchListArticlesTask(this, list, progressBar, progressBar2, 1).execute(category);

        // Setup the click listener for the listView
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryAdapter categoryAdapter = (CategoryAdapter) parent.getAdapter();

                // test show its clicked
                Log.e("position: "+position, "cccccccccccccccccc");
                Log.e("Article size" + categoryAdapter.getItem(position), "cccccccccccccccccc");
                SingleRow rowTemp= (SingleRow) categoryAdapter.getItem(position);
                String articleTitle = rowTemp.title;

//                Toast toast = Toast.makeText(c.getApplicationContext(),
//                        "Loading Article: " + articleTitle, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
//                toast.show();

                // Create new activity for the article here
                // feed the new activity with the URL of the page
                String articleLink = rowTemp.link;
                Intent articleActivity = new Intent(list.getContext(), ArticleActivity.class);
                // EXTRA_HTML_TEXT holds the html link for the article
                articleActivity.putExtra(Intent.EXTRA_HTML_TEXT, articleLink);
                // EXTRA_SHORTCUT_NAME holds the name of the article, e.g. "what life sucks in hell"
                articleActivity.putExtra(Intent.EXTRA_SHORTCUT_NAME, articleTitle);
                // EXTRA_TITLE holds the category name, e.g. "wellness/"
                articleActivity.putExtra(Intent.EXTRA_TITLE, category);

                list.getContext().startActivity(articleActivity);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

// Defines the row item xml
class SingleRow{
    String title;
    String description;
    String link;
    String imageURL;
    Bitmap image;

    SingleRow(String title, String description, Bitmap image, String link) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.link = link;

        this.imageURL = null;
    }

    // For fetch list articles, imageURL is set to download image later (fetch cat. image task)
    SingleRow(String title, String description, String imageURL, String link) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.link = link;

        this.image = null;
    }

    static SingleRow newInstance(Article article) {
        return new SingleRow(article.getTitle(),
                article.getDescription(),
                article.getImageURL(),
                article.getLink());
    }
}

// CategoryAdapter takes in a list of Articles and displays the titles, descriptions, images
// of those articles in the category page as row items
// It is created in the FetchListArticlesTask which is called in CategoryActivity
class CategoryAdapter extends BaseAdapter{
    ArrayList<SingleRow> list;
    Context context;
    private int pageNum;
    private int maxStoredArticles = 24;

    CategoryAdapter(Context c, List<Article> articles){
        list = new ArrayList<SingleRow>();
        // the context is needed for creating LayoutInflater
        context = c;
//        Resources res = c.getResources();

        for(int i = 0; i < articles.size(); ++i){
            String title = articles.get(i).getTitle();
            String description = articles.get(i).getDescription();
            String link = articles.get(i).getLink();
            String imageURL = articles.get(i).getImageURL();

            list.add(new SingleRow(title, description, imageURL, link));
        }
    }

    // Get the number of row items
    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int i){
        return list.get(i);
    }

    // Get the item id, since no database, the id is its assignment
    @Override
    public long getItemId(int i){
        return i;
    }

    // Get the View route of a single row by id
    @Override
    public View getView(int i, View view, final ViewGroup viewGroup){
        // crate a new rowItem object here
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row, viewGroup, false);

        // Get the description, image and title of the row item
        TextView title = (TextView) row.findViewById(R.id.textView);
        TextView description = (TextView) row.findViewById(R.id.textView2);
        ImageView image = (ImageView) row.findViewById(R.id.imageView);
        ProgressBar pb = (ProgressBar) row.findViewById(R.id.progressBarSingleRow);

        // Load data into row element
        if (list.get(i).image == null)
            new FetchCategoryImageTask(list.get(i), title, description, image, pb).execute();
        else {
            // Set the values of the rowItem
            SingleRow rowTemp = list.get(i);
            title.setText(rowTemp.title);
            description.setText(rowTemp.description);
            image.setImageBitmap(rowTemp.image);
        }

        return row;
    }

    // This function is called along with .notifyDataSetChange() in Asynctask's onScrollListener function
    // when the viewers scroll to the bottom of the list
    public void loadMoreItems(List<Article> moreArticles, int pageNum){
        // If there are more than 24 articles, empty first 12 first then add 12 more articles in
//        int numOfArticles = list.size();
//        if(numOfArticles >= maxStoredArticles){
//            for(int i = 0; i < moreArticles.size(); i++){
//                list.remove(i);
//            }
//        }

        // if prevent the late page from loading twice
        if(moreArticles!= null && this.pageNum != pageNum){
            this.pageNum = pageNum;

            // Testing CategoryAdapter
            Log.e("loading more" + this.pageNum, "eeeeeeee");
            Log.e("Moresize" + moreArticles.size(), "eeeeeeee");
            Log.e("row" + list.size(), "eeeeeeee");

            for (int i = 0; i < moreArticles.size(); ++i) {
                String title = moreArticles.get(i).getTitle();
                String description = moreArticles.get(i).getDescription();
                String link = moreArticles.get(i).getLink();
                Bitmap image = moreArticles.get(i).getImage();
                list.add(new SingleRow(title, description, image, link));
            }
        }
    }

    public ArrayList<SingleRow> getList() {
        return list;
    }
}

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// Category page activity
public class CategoryActivity extends ActionBarActivity {
    ListView list;
    List<Article> articles;
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Testing CategoryAdapter
//        Article article1 = new Article("Are Philosophers Crazy?", "Nietzche, Godel all went crazy");
//        Article article2 = new Article("Bible", "Jesus is immportal");
//        articles = new ArrayList<Article>();
//        articles.add(article1);
//        articles.add(article2);

        // Use the ListView layout from fragmment_category_main.xml,
        setContentView(R.layout.fragment_category_main);
        // setContentView(R.layout.activity_category);
        list = (ListView)findViewById(R.id.listView);

        // Testing
        // Set up the adapter using the CategoryAdapter class
        //list.setAdapter(new CategoryAdapter(this, articles));
        // Set up the click listener for the listView
        //list.setOnItemClickListener(this);

        // Fetch the category name from Intent, which is set in MainPageFragment
        Intent intent = getIntent();
        Log.e(intent.getStringExtra(Intent.EXTRA_TITLE), "cccccccccccccccccc");

        if (intent != null && category.isEmpty()) {
            // Set the title for the Category Activity
            String title = intent.getStringExtra(Intent.EXTRA_TITLE);
            setTitle(title.toUpperCase());
            category = title + "/";
        } else {
            setTitle("Error");
        }

        // Use Asynctask to fetch article from the given category
        new FetchListArticlesTask(this, list).execute(category);


//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_category_main, container, false);
            return rootView;
        }
    }
}

// Defines the row item xml
class SingleRow{
    String title;
    String description;
    Bitmap image;

    SingleRow(String title, String description, Bitmap image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }
}

// CategoryAdapter takes in a list of Articles and displays the titles, descriptions, images
// of those articles in the category page as row items
// It is created in the FetchListArticlesTask which is called in CategoryActivity
class CategoryAdapter extends BaseAdapter{
    ArrayList<SingleRow> list;
    Context context;

    CategoryAdapter(Context c, List<Article> articles){
        list = new ArrayList<SingleRow>();
        // the context is needed for creating LayoutInflater
        context = c;
        Resources res = c.getResources();

        // Testing
//        String[] titles = res.getStringArray(R.array.titles);
//        String[] descriptions = res.getStringArray(R.array.descriptions);
//        int[] images = {R.drawable.list_item_1,
//                        R.drawable.list_item_1,
//                        R.drawable.list_item_1,
//                        R.drawable.list_item_1};

//        String[] titles = {"Title_1"};
//        String[] descriptions = {"description _1"};

        for(int i = 0; i < articles.size(); ++i){
            String title = articles.get(i).getTitle();
            String description = articles.get(i).getDescription();
            Bitmap image = articles.get(i).getImage();
            list.add(new SingleRow(title, description, image));

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

        // Set the values of the rowItem
        SingleRow rowTemp = list.get(i);
        title.setText(rowTemp.title);
        description.setText(rowTemp.description);
        image.setImageBitmap(rowTemp.image);

        // Testing
//        Bitmap bmp = null;
//        try {
//            URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            image.setImageBitmap(bmp);
//        } catch (IOException e) {
//            // TODO: handle exception
//            Log.e("error", "Failed to load image");
//            // failed to load warning image
//            image.setImageResource(R.drawable.list_item_1);
//        }
//        image.setImageResource(R.drawable.list_item_1);

//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.list_item_1);
//        bitmap = Bitmap.createScaledBitmap(bitmap, 5, 5, true);




        return row;
    }



}

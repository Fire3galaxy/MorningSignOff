package app.morningsignout.com.morningsignoff;

/**
 * Created by Daniel on 3/1/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainPageFragment extends Fragment {
    public MainPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // changing menu through fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Action bar (Home and Up button clicks have default to parent activity in Andr...Manifest
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String PLACEHOLDER = "PLACEHOLDER";

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String array[] = {
                "Latest",
                "Research",
                "Wellness",
                "Humanities",
                "Medicine",
                "Public Health",
                "Healthcare"
        };
        ArrayList<String> articles = new ArrayList<String>(Arrays.asList(array));

        // Adapter that will send array's data to list view
        final ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_items_mainpage,
                R.id.list_item_button,
                articles);

        ListView lv_frag = (ListView) rootView.findViewById(R.id.listview_articles);
        lv_frag.setAdapter(stringAdapter);
        lv_frag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Glorious toast example before learning how to launch CategoryActivity!
//            Toast toast = Toast.makeText(getActivity(), stringAdapter.getItem(position),
//                Toast.LENGTH_SHORT);
//            toast.show();

                // Add article link to new activity to download article
                String article_test = "http://morningsignout.com/growth-factor-shows-" +
                        "regenerative-effects-in-patients-with-parkinsons-disease/";
                Intent articlePageIntent = new Intent(getActivity(), ArticleActivity.class)
                        .putExtra(Intent.EXTRA_HTML_TEXT, article_test);
                        //.putExtra(Intent.EXTRA_TITLE, stringAdapter.getItem(position));
                startActivity(articlePageIntent);
            }
        });

        return rootView;
    }
}

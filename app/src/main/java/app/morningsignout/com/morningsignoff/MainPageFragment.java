package app.morningsignout.com.morningsignoff;

/**
 * Created by Daniel on 3/1/2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainPageFragment extends Fragment {
    FetchListArticlesTask fetchListTask = new FetchListArticlesTask();

    public MainPageFragment() {
    }


    // FIXME: All onCreate stuff besides onCreateView is for practice w/ internet. Not for this fragment!
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // changing menu through fragment
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Action bar (Home and Up button clicks have default to parent activity in Andr...Manifest
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            fetchListTask.execute("research");
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
                Toast toast = Toast.makeText(getActivity(), stringAdapter.getItem(position),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return rootView;
    }
}

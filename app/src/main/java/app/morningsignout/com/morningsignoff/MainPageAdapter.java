package app.morningsignout.com.morningsignoff;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Daniel on 3/2/2015.
 */
public class
        MainPageAdapter extends ArrayAdapter<String> {
    public MainPageAdapter(Context context, final int resource, int textViewResourceId,
                           List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

}

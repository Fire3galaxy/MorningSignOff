package app.morningsignout.com.morningsignoff;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Daniel on 3/2/2015.
 */
public class HeadlineFragment extends Fragment {
    final static String IMAGE_NUMBER = "image number";
    private int page_number;

    public HeadlineFragment() {
    }

    // A "Factory" method that creates different images when scrolled through
    public static HeadlineFragment create(int pageNumber) {
        HeadlineFragment newImage = new HeadlineFragment();
        Bundle args = new Bundle();
        args.putInt(IMAGE_NUMBER, pageNumber);
        newImage.setArguments(args);
        return newImage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page_number = getArguments().getInt(IMAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String HEADLINE = "HEADLINE"; // for log cat

        View rootView = inflater.inflate(R.layout.fragment_headline, container, false);
        ImageButton ib = (ImageButton) rootView.findViewById(R.id.imageButton_headline);

        if (page_number == 0)
            ib.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.gravityfallsteaser));
        else if (page_number == 1)
            ib.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.the_flash));
        else if (page_number == 2)
            ib.setImageResource(R.drawable.steven_universe_by_flafly_d6zv94s);


// When I thought this was a sideways list (it's a series of pages...)
//        ArrayList<Bitmap> heads = new ArrayList<Bitmap>();
//        heads.add(BitmapFactory.decodeResource(getResources(), R.drawable.gravityfallsteaser));
//        heads.add(BitmapFactory.decodeResource(getResources(), R.drawable.heartrate));
//        heads.add(BitmapFactory.decodeResource(getResources(), R.drawable.the_flash));
//
//        final ArrayAdapter<Bitmap> bitmapAdapter = new ArrayAdapter<Bitmap>(
//                getActivity(),
//                R.layout.list_items_headline,
//                R.id.container_headline,
//                heads
//        );
//
//        ListView lv_frag = (ListView) rootView.findViewById(R.id.listview_headline);

        return rootView;
    }
}

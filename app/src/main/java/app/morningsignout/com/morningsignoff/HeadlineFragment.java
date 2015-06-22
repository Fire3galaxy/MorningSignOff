package app.morningsignout.com.morningsignoff;

import android.support.v4.app.Fragment;
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
        final String logString = "HEADLINE"; // for log cat

        // Set up view with container (layout) and set up imageButton
        View rootView = inflater.inflate(R.layout.fragment_headline, container, false);
        ImageButton ib = (ImageButton) rootView.findViewById(R.id.imageButton_headline);

        // Decide image to show based on index in headline images
        new DownloadImageTask(this, getActivity(), rootView, ib).execute(page_number);

        return rootView;
    }
}

package app.morningsignout.com.morningsignoff;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
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
    public HeadlineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String HEADLINE = "HEADLINE"; // for log cat

        View rootView = inflater.inflate(R.layout.fragment_headline, container, false);

        ImageButton imgButton_frag = (ImageButton) rootView.findViewById(R.id.container_headline);
        Bitmap head = BitmapFactory.decodeResource(getResources(), R.drawable.gravityfallsteaser);
        imgButton_frag.setImageBitmap(head);

        return rootView;
    }
}

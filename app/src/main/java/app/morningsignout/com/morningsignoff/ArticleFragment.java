package app.morningsignout.com.morningsignoff;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Daniel on 3/2/2015.
 */
public class ArticleFragment extends Fragment {
    public ArticleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String ARTICLE = "ARTICLE"; // for log cat

        View rootView = inflater.inflate(R.layout.fragment_article, container, false);

        String title = "My Greatest Masterpiece",
                author = "By Alex Hirsch";

        TextView textViewTitle_frag = (TextView) rootView.findViewById(R.id.textView_title);
        textViewTitle_frag.setText(title);

        ImageView imgView_frag = (ImageView) rootView.findViewById(R.id.imageView_article);
        Bitmap head = BitmapFactory.decodeResource(getResources(), R.drawable.gravityfallsteaser);
        imgView_frag.setImageBitmap(head);

        TextView textViewAuthor_frag = (TextView) rootView.findViewById(R.id.textView_author);
        textViewAuthor_frag.setText(author);

        TextView textViewArticle_frag = (TextView) rootView.findViewById(R.id.textView_article);

        return rootView;
    }
}

package app.morningsignout.com.morningsignoff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

        // Set up view with container (layout) and set up imageButton
        View rootView = inflater.inflate(R.layout.fragment_headline, container, false);
        ImageButton ib = (ImageButton) rootView.findViewById(R.id.imageButton_headline);

        // Decide image to show based on index in headline images
        new DownloadImageTask(rootView, ib).execute(page_number);

        return rootView;
    }

    // For downloading images from latest articles. Code partially from
    // http://developer.android.com/guide/components/processes-and-threads.html
    // http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadImageTask extends AsyncTask<Integer, Void, Article> {
        ImageButton i;
        ProgressBar progressBar;
        TextView textView;

        DownloadImageTask(View view, ImageButton imageButton) {
            i = imageButton;
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar_headline);
            textView = (TextView) view.findViewById(R.id.textView_headline);
        }

        // Displays progress bar
        protected void onPreExecute() {
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        /** The system calls this to perform work in a worker thread and
         * delivers it the parameters given to AsyncTask.execute() */
        protected Article doInBackground(Integer... headlinePageNumber) {
            Article article = FetchHeadlineArticles.getArticles("featured",
                    headlinePageNumber[0]);

            try {
                InputStream in = new URL(article.getImageURL()).openStream();
                article.setBitmap(BitmapFactory.decodeStream(in));
            } catch (IOException e) {
                Log.e("HEADLINE IMAGE DOWNLOAD", e.getMessage());
            }

            return article;
        }

        /** The system calls this to perform work in the UI thread and delivers
         * the result from doInBackground() */
        protected void onPostExecute(final Article result) {
            // Set downloaded bitmap
            i.setImageBitmap(result.getBitmap());

            // When clicked, should open webview to article
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent articlePageIntent = new Intent(getActivity(), ArticleActivity.class)
                            .putExtra(Intent.EXTRA_HTML_TEXT, result.getLink())
                            .putExtra(Intent.EXTRA_SHORTCUT_NAME, result.getTitle());
                    startActivity(articlePageIntent);
                }
            });

            // Title
            textView.setText(result.getTitle());

            // Remove loading bar
            progressBar.setVisibility(ProgressBar.GONE);

            // Make title visibile
            textView.setVisibility(TextView.VISIBLE);
        }
    }
}

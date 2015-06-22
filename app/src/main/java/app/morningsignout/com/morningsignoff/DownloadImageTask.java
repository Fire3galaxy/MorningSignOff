package app.morningsignout.com.morningsignoff;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.IOUtils.*;

import app.morningsignout.com.morningsignoff.HeadlineArtContract.*;

/**
 * Created by Daniel on 6/22/2015.
 */
// For downloading images from latest articles. Code partially from
// http://developer.android.com/guide/components/processes-and-threads.html
// http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
public class DownloadImageTask extends AsyncTask<Integer, Void, Article> {
    Context mContext;
    Fragment mFragment;
    ImageButton i;
    ProgressBar progressBar;
    TextView textView;

    DownloadImageTask(Fragment fragment, Context context, View view, ImageButton imageButton) {
        mContext = context;
        mFragment = fragment;
        i = imageButton;
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_headline);
        textView = (TextView) view.findViewById(R.id.textView_headline);
    }

    private long addH_article(String title, String link, byte[] imageByteStream) {
        long _id;

        // Query database to see if article exists
        Cursor c = mContext.getContentResolver().query(
                HeadlineArtContract.BASE_CONTENT_URI,
                new String[]{H_articleEntry._ID},
                "? = " + link,
                new String[]{H_articleEntry.COLUMN_LINK},
                null);

        try {
            // Insert H_articleEntry if not found
            if (c.getCount() == 0) {
                ContentValues articleValues = new ContentValues();
                articleValues.put(H_articleEntry.COLUMN_TITLE, title);
                articleValues.put(H_articleEntry.COLUMN_LINK, link);
                articleValues.put(H_articleEntry.COLUMN_IMAGEBYTESTREAM, imageByteStream);

                _id = H_articleEntry.getIndexFromUri(
                        mContext.getContentResolver().insert(H_articleEntry.CONTENT_URI,
                                articleValues));
            }

            // Or just return _id of existing row
            else {
                _id = c.getLong(c.getColumnIndex(H_articleEntry._ID));
            }
        } finally {
            c.close();
        }

        return _id;
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
            // Lowers resolution of images by subsampling image, saves memory & time
            BitmapFactory.Options a = new BitmapFactory.Options();
            a.inSampleSize = 2;

            InputStream in = new URL(article.getImageURL()).openStream();

            // Store inputStream buffer contents in byte array through bytearrayoutputstream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(in, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            // Save bitmap here
            article.setBitmap(BitmapFactory.decodeStream(bais, null, a));

            // Insert into database for quick loading later (_id starts with 1)
            ContentValues articleValues = new ContentValues();
            articleValues.put(H_articleEntry.COLUMN_TITLE, article.getTitle());
            articleValues.put(H_articleEntry.COLUMN_LINK, article.getLink());
            articleValues.put(H_articleEntry.COLUMN_IMAGEBYTESTREAM, baos.toByteArray());
        } catch (IOException e) {
            Log.e("HEADLINE IMAGE DOWNLOAD", e.getMessage());
        }

        return article;
    }

    /** The system calls this to perform work in the UI thread and delivers
     * the result from doInBackground() */
    protected void onPostExecute(final Article result) {
        // Preserve aspect ratio of image
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setCropToPadding(true);

        // Set downloaded bitmap
        i.setImageBitmap(result.getBitmap());

        // When clicked, should open webview to article
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent articlePageIntent = new Intent(mContext, ArticleActivity.class)
                        .putExtra(Intent.EXTRA_HTML_TEXT, result.getLink())
                        .putExtra(Intent.EXTRA_SHORTCUT_NAME, result.getTitle());
                mFragment.startActivity(articlePageIntent);
            }
        });

        // Title
        textView.setText(result.getTitle());

        // Remove loading bar
        progressBar.setVisibility(ProgressBar.GONE);

        // Make title visible
        textView.setVisibility(TextView.VISIBLE);
    }
}

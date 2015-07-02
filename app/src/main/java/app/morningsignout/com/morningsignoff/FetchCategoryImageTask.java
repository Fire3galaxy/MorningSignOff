package app.morningsignout.com.morningsignoff;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Daniel on 6/24/2015.
 */
public class FetchCategoryImageTask extends AsyncTask<Void, Void, Bitmap> {
    SingleRow sr;
    TextView title;
    TextView description;
    ImageView image;
    ProgressBar pb;

    public FetchCategoryImageTask(SingleRow singleRow, TextView title, TextView description,
                                  ImageView image, ProgressBar pb) {
        sr = singleRow;
        this.title = title;
        this.description = description;
        this.image = image;
        this.pb = pb;
    }

    @Override
    protected void onPreExecute() {
        // Set loading progress bar and make other elements invisible
        pb.setVisibility(ProgressBar.VISIBLE);

        title.setVisibility(TextView.INVISIBLE);
        description.setVisibility(TextView.INVISIBLE);
        image.setVisibility(ImageView.INVISIBLE);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return (sr.imageURL != null) ? downloadBitmap(sr.imageURL) : null;
    }

    @Override
    protected void onPostExecute(final Bitmap b) {
        // textView title
        title.setText(sr.title);

        // textView description
        description.setText(sr.description);

        // imageView image
        // Preserve aspect ratio of image
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        image.setCropToPadding(true);
        image.setImageBitmap(b);

        // Remove progressBar and make article-related elements visible
        pb.setVisibility(ProgressBar.GONE);

        title.setVisibility(TextView.VISIBLE);
        description.setVisibility(TextView.VISIBLE);
        image.setVisibility(ImageView.VISIBLE);

        // Save image to SingleRow object for categoryAdapter's getView()
        sr.image = b;
    }

    // The dimension of the rescaled bitmap
    private static final int bitmapDimension = 100;

    // input an image URL, get its bitmap
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                // Lowers resolution of images by subsampling image, saves memory & time
                BitmapFactory.Options a = new BitmapFactory.Options();
                a.inSampleSize = 1;

                // Create bitmap from stream
                return BitmapFactory.decodeStream(inputStream, null, a);
            }
        } catch (Exception e) {
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

}

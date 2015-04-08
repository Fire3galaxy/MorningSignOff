package app.morningsignout.com.morningsignoff;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;


public class ArticleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArticleFragment newArticle = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString("HTML_LINK", getIntent().getStringExtra(Intent.EXTRA_HTML_TEXT));
        newArticle.setArguments(args);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true); //made back arrow in top left corner

        // Possibility 2, A webview that directly shows the article page
        WebView article = (WebView) findViewById(R.id.webView_article);
        article.loadUrl(getIntent().getStringExtra(Intent.EXTRA_HTML_TEXT));

        // Possibility 1, parsing the article page to show later
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container_article, newArticle)
//                    .commit();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

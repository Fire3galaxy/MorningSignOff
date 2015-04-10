package app.morningsignout.com.morningsignoff;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.WebView;

// Activity class created in FetchListArticleTask when user clicks on an article from the ListView
public class ArticleActivity extends ActionBarActivity {
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        super.getSupportActionBar().setDisplayHomeAsUpEnabled(true); //made back arrow in top left corner

        // Set the title for this activity to the article title
        Intent intent = getIntent();
        if (intent != null) {
            category = getIntent().getStringExtra(intent.EXTRA_TITLE);
            setTitle(getIntent().getStringExtra(intent.EXTRA_SHORTCUT_NAME));
            // Possibility 2, A webview that directly shows the article page
            WebView article = (WebView) findViewById(R.id.webView_article);
            article.loadUrl(getIntent().getStringExtra(intent.EXTRA_HTML_TEXT));

        }
        // Possibility 2, A webview that directly shows the article page
        WebView article = (WebView) findViewById(R.id.webView_article);
        article.loadUrl(getIntent().getStringExtra(Intent.EXTRA_HTML_TEXT));

        // Possibility 1, parsing the article page to show later
//        ArticleFragment newArticle = new ArticleFragment();
//        Bundle args = new Bundle();
//        args.putString("HTML_LINK", getIntent().getStringExtra(Intent.EXTRA_HTML_TEXT));
//        newArticle.setArguments(args);

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


    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Log.e(category, "yyyyyyyyyyyyy");
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(Intent.EXTRA_TITLE, category);
                NavUtils.navigateUpTo(this, intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

package efrei.android.project;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Object;

public class MainActivity extends AppCompatActivity {
    private static List<Article> articleDefList = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArticleAdapter mAdapter;
    public SharedPreferences sharedPreferences;
    private static final String PREFS = "PREFS";
    private static final String USERNAME = "USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        articleDefList.clear();
        mAdapter = new ArticleAdapter(articleDefList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        if (sharedPreferences.contains(USERNAME)) {
            String name = sharedPreferences.getString(USERNAME, null);
            Toast.makeText(this, "Bonjour " + name, Toast.LENGTH_SHORT).show();
        }



        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Article articleDef = articleDefList.get(position);
                String link = articleDef.getLink();

                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(link));
                    startActivity(i);
                }
                catch(ActivityNotFoundException e) {
                    // Chrome is not installed
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(i);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                urls = FavoryActivity.getStringArrayPref(getApplicationContext(), "urls");
                urls.add(articleDefList.get(position).getLink());
                ArrayList<String> list = new ArrayList<String>((urls));
                FavoryActivity.setStringArrayPref(getApplicationContext(), "urls", list);
                String myString = "Added to favorites";
                Toast.makeText(getApplicationContext(), myString, Toast.LENGTH_SHORT).show();
            }
        }));

        new download_news(this).execute();
    }

    public static Article getArticle (String newLink){
        for (Article myArticle: articleDefList) {
            if(myArticle.getLink().equals(newLink)){
                return(myArticle);
            }
        }
        return(null);
    }

    /**
     * Prepares sample data to provide data set to adapter
     */
    public void prepareData(ArrayList<String> res, ArrayList<String>res2) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean gorafi = sharedPreferences.getBoolean("gorafi", false);
        boolean onion = sharedPreferences.getBoolean("onion", false);
        System.out.println(gorafi);
        System.out.println(onion);

        if(gorafi){
            for(String a : res){
                Article b = new Article(a,"gorafi");
                articleDefList.add(b);
            }
        }

        if(onion){
            for(String a : res2){
                Article b = new Article(a,"onion");
                articleDefList.add(b);
            }
        }


        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_login)
        {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        if (id == R.id.action_favs)
        {
            startActivity(new Intent(MainActivity.this, FavoryActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

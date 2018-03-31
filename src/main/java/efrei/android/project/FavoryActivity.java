package efrei.android.project;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import efrei.android.project.R;

public class FavoryActivity extends Activity {
    /** Called when the activity is first created. */
    private List<Article> articleDefList = new ArrayList<>();
    private Article myArticle;
    private List<String> urls = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArticleAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favory);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ArticleAdapter(articleDefList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ArrayList<String> list;
        String urls[];
        list = getStringArrayPref(this, "urls");

        if(list != null){
            urls = list.toArray(new String[list.size()]);
            for(int i = 0 ; i < urls.length; i++){
                myArticle = MainActivity.getArticle(urls[i]);
                if(myArticle != null){
                    articleDefList.add(myArticle);
                }
            }
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
                articleDefList.remove(articleDefList.get(position));
                ArrayList<String> list;
                list = getStringArrayPref(getApplicationContext(), "urls");
                list.remove(articleDefList.get(position).getLink());
                setStringArrayPref(getApplicationContext(),"urls" , list);
                Toast.makeText(getApplicationContext(), "Favorite deleted", Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        }));

        Button deletenButton = (Button) findViewById(R.id.delFavButton);
        deletenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                del_favs();
            }
        });
    }

    public static void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public void del_favs(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefs.edit().remove("urls").commit();
        Toast.makeText(getApplicationContext(), "All favorites deleted", Toast.LENGTH_SHORT).show();
    }
}
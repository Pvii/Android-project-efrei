package efrei.android.project;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Pvii on 29/03/2018.
 */

public class download_news extends AsyncTask<Void, Void, ArrayList<ArrayList<String>>> {

    public volatile MainActivity m;

    download_news(MainActivity ma){
        m  = ma;
    }


    @Override
    protected ArrayList<ArrayList<String>> doInBackground(Void... voids) {
        ArrayList<ArrayList<String>> a = new ArrayList<>();
        try {
            a.add(founder.found_articles("http://www.legorafi.fr/feed/"));
            a.add(founder.found_articles("https://www.theonion.com/rss"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }



    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> result){

        m.prepareData(result.get(0), result.get(1));

    }
}

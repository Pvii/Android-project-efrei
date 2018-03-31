package efrei.android.project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {

    private List<Article> ArticleDefList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, link, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            //genre = (TextView) view.findViewById(R.id.genre);
            //link = (TextView) view.findViewById(R.id.link);
        }
    }


    public ArticleAdapter(List<Article> ArticleDefList) {
        this.ArticleDefList = ArticleDefList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Article articleDef = ArticleDefList.get(position);
        holder.title.setText(articleDef.getTitre());
        //holder.genre.setText(articleDef.getCategory());
        //holder.link.setText(articleDef.getLink());
    }

    @Override
    public int getItemCount() {
        return ArticleDefList.size();
    }
}

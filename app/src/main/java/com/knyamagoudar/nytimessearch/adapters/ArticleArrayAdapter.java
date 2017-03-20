package com.knyamagoudar.nytimessearch.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.knyamagoudar.nytimessearch.R;
import com.knyamagoudar.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.knyamagoudar.nytimessearch.R.id.tvTitle;

/**
 * Created by knyamagoudar on 3/14/17.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    private static class ViewHolder{
        TextView title;
        ImageView thumbNail;
    }

    public ArticleArrayAdapter(Context context, ArrayList<Article> articles){
        super(context,0,articles);
    }



    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        Article article = this.getItem(position);
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result,parent,false);

            viewHolder.title = (TextView) convertView.findViewById(tvTitle);
            viewHolder.thumbNail = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.thumbNail.setImageResource(0);

            convertView.setTag(viewHolder);
        }else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //get the text view reference and set it to headline

        viewHolder.title.setText(article.getHeadLine());
        String thumbNail = article.getThumbNail();

        viewHolder.thumbNail.setImageResource(0);
        //populate the thumbnail image if it has a thumbnail
        if(!TextUtils.isEmpty(thumbNail)){
            Picasso.with(getContext()).load(Uri.parse(article.getThumbNail())).into(viewHolder.thumbNail);
        }

        return convertView;
    }
}

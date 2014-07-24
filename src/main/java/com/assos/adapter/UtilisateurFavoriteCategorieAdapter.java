package com.assos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.assos.activity.R;
import com.assos.model.Categorie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 03/06/2014.
 */
public class UtilisateurFavoriteCategorieAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Categorie> categories;
    private ArrayList<Categorie> categoriesAlreadySelected;
    private Context mContext;

    public UtilisateurFavoriteCategorieAdapter(Context c, ArrayList<Categorie> categoriesSelected){
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
        categories = new ArrayList<Categorie>();
        categoriesAlreadySelected = categoriesSelected;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        if(categories.get(position) != null)
            return categories.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_favorite_categorie, null);
            viewHolder = new ViewHolder();
            viewHolder.check = (ImageView) convertView.findViewById(R.id.categorie_favorite_adapter_button);
            viewHolder.text = (TextView) convertView.findViewById(R.id.categorie_favorite_adapter_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Categorie current = categories.get(position);
        viewHolder.text.setText(current.getNom());
        if(categoriesAlreadySelected.contains(current)){
            viewHolder.check.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            viewHolder.check.setBackground(mContext.getResources().getDrawable(R.drawable.ic_action_important));
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView check;
        public TextView text;
    }

    public void removeAll(){
        categories.clear();
    }

    public void addAll(List<Categorie> list){
        categories.clear();
        categories.addAll(list);
    }
}

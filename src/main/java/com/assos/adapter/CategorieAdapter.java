package com.assos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.assos.activity.R;
import com.assos.com.assos.callback.ICategorieCallBack;
import com.assos.model.Categorie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 03/06/2014.
 */
public class CategorieAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Categorie> categories;
    private ICategorieCallBack listener;
    private ArrayList<Categorie> categoriesAlreadySelected;

    public CategorieAdapter(Context c, ArrayList<Categorie> categoriesSelected){
        Context mContext = c;
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
            convertView = mInflater.inflate(R.layout.adapter_checkbox, null);

            viewHolder = new ViewHolder();
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.adapter_checkBox_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Categorie current = categories.get(position);
        viewHolder.check.setChecked(categoriesAlreadySelected.contains(current));
        viewHolder.check.setText(current.getNom());
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!viewHolder.check.isChecked()){
                    listener.removeCategorie(current);
                    viewHolder.check.setChecked(false);
                } else {
                    listener.addCategorie(current);
                    viewHolder.check.setChecked(true);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public CheckBox check;
    }

    public void removeAll(){
        categories.clear();
    }

    public void addAll(List<Categorie> list){
        categories.clear();
        categories.addAll(list);
    }

    public void addCallback(ICategorieCallBack c){
        listener = c;
    }
}

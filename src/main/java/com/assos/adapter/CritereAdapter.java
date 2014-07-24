package com.assos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.assos.activity.R;
import com.assos.com.assos.callback.ICritereCallback;
import com.assos.model.CritereRecherche;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 03/06/2014.
 */
public class CritereAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CritereRecherche> critere;
    private ICritereCallback listener;
    private ArrayList<CritereRecherche> categoriesAlreadySelected;

    public CritereAdapter(Context c, ArrayList<CritereRecherche> critereSelected){
        Context mContext = c;
        mInflater = LayoutInflater.from(mContext);
        critere = new ArrayList<CritereRecherche>();
        categoriesAlreadySelected = critereSelected;
    }

    @Override
    public int getCount() {
        return critere.size();
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
        final CritereRecherche current = critere.get(position);
        viewHolder.check.setChecked(categoriesAlreadySelected.contains(current));
        viewHolder.check.setText(current.getNom());
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.check.isChecked()) {
                    listener.removeCritere(current);
                    viewHolder.check.setChecked(false);
                } else {
                    listener.addCritere(current);
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
        critere.clear();
    }

    public void addAll(List<CritereRecherche> list){
        critere.clear();
        critere.addAll(list);
    }

    public void addCallback(ICritereCallback c){
        listener = c;
    }
}

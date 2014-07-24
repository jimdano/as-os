package com.assos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.assos.activity.R;
import com.assos.adapter.CategorieAdapter;
import com.assos.com.assos.callback.ICategorieCallBack;
import com.assos.com.assos.callback.ICategorieManagerCallBack;
import com.assos.interfaces.IRechercheCritereTypeFragment;
import com.assos.manager.CategorieManager;
import com.assos.manager.CritereRechercheManager;
import com.assos.model.Categorie;
import com.assos.model.CritereRecherche;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 07/07/2014
 */
public class TypeFragmentCategorie extends Fragment implements IRechercheCritereTypeFragment {

    private CritereRecherche critereCourant;
    private ListView type_layout_categorie_fragment_liste;
    private TextView type_layout_categorie_fragment_text_view;
    private ArrayList<Categorie> categories;
    private CategorieAdapter ca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.type_layout_categorie_fragment,
                container, false);

        type_layout_categorie_fragment_liste = (ListView) rootView.findViewById(R.id.type_layout_categorie_fragment_liste);
        type_layout_categorie_fragment_text_view = (TextView) rootView.findViewById(R.id.type_layout_categorie_fragment_text_view);
        ca = new CategorieAdapter(getActivity(), categories);
        init();
        gestionCategories();
        return rootView;
    }

    private void gestionCategories() {
        if (CategorieManager.getInstance().categ.isEmpty()) {
            CategorieManager.getInstance().getCategories(getActivity(), new ICategorieManagerCallBack() {
                @Override
                public void onSuccess(List<Categorie> list) {
                    ca.addAll(list);
                    ca.notifyDataSetChanged();
                }

                @Override
                public void onError() {
                    Toast.makeText(getActivity(), "Erreur recuperation categories, excusez nous", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ca.addAll(CategorieManager.getInstance().categ);
            ca.notifyDataSetChanged();
        }
        gestionListeners();
    }

    private void gestionListeners() {
        ca.addCallback(new ICategorieCallBack() {
            @Override
            public void addCategorie(Categorie c) {
                if (!categories.contains(c)) {
                    categories.add(c);
                }
            }

            @Override
            public void removeCategorie(Categorie c) {
                if (categories.contains(c)) {
                    categories.remove(c);
                }
            }
        });
        type_layout_categorie_fragment_liste.setAdapter(ca);
    }

    private void init() {
        type_layout_categorie_fragment_text_view.setText("Veuillez renseigner : " + critereCourant.getNom());
        if(CritereRechercheManager.getInstance().getCriteresFaits().containsKey(critereCourant)){
            //TODO
        }
    }

    public static TypeFragmentCategorie newInstance() {
        return new TypeFragmentCategorie();
    }

    @Override
    public void init(CritereRecherche critere) {
        critereCourant = critere;
    }

    @Override
    public boolean next() {
        if(categories.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public String getValue() {
        String ret = "";
        for(int i = 0; i < categories.size(); i ++){
            if(i == categories.size() - 1){
                ret += categories.get(i).getId();
            } else {
                ret += categories.get(i).getId() + "/";
            }
        }
        return ret;
    }
}

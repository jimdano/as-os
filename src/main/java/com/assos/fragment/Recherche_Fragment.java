package com.assos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.assos.activity.CritereEtapeRechercheActivity;
import com.assos.activity.R;
import com.assos.adapter.CritereAdapter;
import com.assos.com.assos.callback.ICritereCallback;
import com.assos.interfaces.NamedFragment;
import com.assos.manager.CritereRechercheManager;
import com.assos.model.CritereRecherche;

import java.util.ArrayList;


/**
 * Created by jimmy on 11/06/2014.
 */
public class Recherche_Fragment extends Fragment implements NamedFragment {

    private Button association_recherche_suivant;
    private ListView association_recherche_listView;
    private ArrayList<CritereRecherche> criteresSelected;
    private CritereAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recherche_fragment,
                container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        association_recherche_listView = (ListView) rootView.findViewById(R.id.recherche_fragment_liste);
        association_recherche_suivant = (Button) rootView.findViewById(R.id.recherche_fragment_suivant);

        CritereRechercheManager.getInstance().init();
        criteresSelected = new ArrayList<CritereRecherche>();
        adapter = new CritereAdapter(getActivity(),criteresSelected);

        criteresInitialisation();
        gestionBoutonSuivant();

        return rootView;
    }

    private void gestionBoutonSuivant() {
        association_recherche_suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CritereRechercheManager.getInstance().setCriteresSelectionnes(criteresSelected);
                CritereEtapeRechercheActivity c = new CritereEtapeRechercheActivity();
                c.launchActivity(getActivity());
            }
        });
    }

    private void criteresInitialisation() {
        CritereRechercheManager.getInstance().init();
        adapter.addAll(CritereRechercheManager.getInstance().getCriteres());
        adapter.notifyDataSetChanged();

        adapter.addCallback(new ICritereCallback() {
            @Override
            public void addCritere(CritereRecherche c) {
                if(!criteresSelected.contains(c)){
                    criteresSelected.add(c);
                    association_recherche_suivant.setEnabled(true);
                }
            }

            @Override
            public void removeCritere(CritereRecherche c) {
                if(criteresSelected.contains(c)){
                    criteresSelected.remove(c);
                    if(criteresSelected.isEmpty()){
                        association_recherche_suivant.setEnabled(false);
                    }
                }
            }
        });

        association_recherche_listView.setAdapter(adapter);
    }

    @Override
    public String getName() {
        return "Rechercher";
    }

    public static Recherche_Fragment newInstance() {
        return new Recherche_Fragment();
    }

}

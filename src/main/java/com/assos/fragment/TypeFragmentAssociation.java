package com.assos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.assos.activity.R;
import com.assos.interfaces.IRechercheCritereTypeFragment;
import com.assos.manager.CritereRechercheManager;
import com.assos.model.CritereRecherche;

/**
 * Created by jimmy on 07/07/2014
 */
public class TypeFragmentAssociation extends Fragment implements IRechercheCritereTypeFragment {

    private CritereRecherche critereCourant;
    private ListView type_layout_association_fragment_liste;
    private TextView type_layout_association_fragment_text_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.type_layout_association_fragment,
                container, false);

        type_layout_association_fragment_liste = (ListView) rootView.findViewById(R.id.type_layout_association_fragment_liste);

        type_layout_association_fragment_text_view = (TextView) rootView.findViewById(R.id.type_layout_association_fragment_text_view);

        init();
        return rootView;
    }

    private void init() {
        type_layout_association_fragment_text_view.setText("Veuillez renseigner : " + critereCourant.getNom());
        if(CritereRechercheManager.getInstance().getCriteresFaits().containsKey(critereCourant)){
            //TODO
        }
    }

    public static TypeFragmentAssociation newInstance() {
        return new TypeFragmentAssociation();
    }

    @Override
    public void init(CritereRecherche critere) {
        critereCourant = critere;
    }

    @Override
    public boolean next() {
        return false;
    }

    @Override
    public String getValue() {
        return null;
    }
}

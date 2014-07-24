package com.assos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.assos.activity.R;
import com.assos.interfaces.IRechercheCritereTypeFragment;
import com.assos.manager.CritereRechercheManager;
import com.assos.model.CritereRecherche;

/**
 * Created by jimmy on 07/07/2014
 */
public class TypeFragmentString extends Fragment implements IRechercheCritereTypeFragment {

    private EditText type_layout_string_fragment_text;
    private TextView type_layout_string_fragment_text_view;
    private CritereRecherche critereCourant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.type_layout_string_fragment,
                container, false);

        type_layout_string_fragment_text = (EditText) rootView.findViewById(R.id.type_layout_string_fragment_text);
        type_layout_string_fragment_text_view = (TextView) rootView.findViewById(R.id.type_layout_string_fragment_text_view);

        init();
        return rootView;
    }

    private void init() {
        type_layout_string_fragment_text_view.setText("Veuillez renseigner : " + critereCourant.getNom());
        if(CritereRechercheManager.getInstance().getCriteresFaits().containsKey(critereCourant)){
            type_layout_string_fragment_text.setText(CritereRechercheManager.getInstance().getCriteresFaits().get(critereCourant));
        }
}

    public static TypeFragmentString newInstance() {
        return new TypeFragmentString();
    }

    @Override
    public void init(CritereRecherche critere) {
        critereCourant = critere;
    }

    @Override
    public boolean next() {
        if(type_layout_string_fragment_text.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public String getValue() {
        return type_layout_string_fragment_text.getText().toString();
    }
}

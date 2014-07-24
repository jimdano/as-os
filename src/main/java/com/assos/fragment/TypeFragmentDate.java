package com.assos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.assos.activity.R;
import com.assos.interfaces.IRechercheCritereTypeFragment;
import com.assos.manager.CritereRechercheManager;
import com.assos.model.CritereRecherche;
import com.dateslider.AlternativeDateSlider;
import com.dateslider.DateSlider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jimmy on 07/07/2014
 */
public class TypeFragmentDate extends Fragment implements IRechercheCritereTypeFragment {

    private CritereRecherche critereCourant;
    private TextView type_layout_date_fragment_text_view;
    private Button type_layout_date_fragment_date;
    private SimpleDateFormat formatDate;
    private Date rechercheDate;

    public static TypeFragmentDate newInstance() {
        return new TypeFragmentDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.type_layout_date_fragment,
                container, false);

        type_layout_date_fragment_text_view = (TextView) rootView.findViewById(R.id.type_layout_date_fragment_text_view);
        type_layout_date_fragment_date = (Button) rootView.findViewById(R.id.type_layout_date_fragment_date);

        formatDate = new SimpleDateFormat("dd-MM-yyyy");
        type_layout_date_fragment_date.setOnClickListener(new DatePickerListener());

        init();
        return rootView;
    }

    private void init() {
        type_layout_date_fragment_text_view.setText("Veuillez renseigner : " + critereCourant.getNom());
        if(CritereRechercheManager.getInstance().getCriteresFaits().containsKey(critereCourant)){
            type_layout_date_fragment_date.setText(formatDate.format(CritereRechercheManager.getInstance().getCriteresFaits().get(critereCourant)));
        }
    }

    @Override
    public void init(CritereRecherche critere) {
        critereCourant = critere;
    }

    @Override
    public boolean next() {
        if(rechercheDate != null){
            return true;
        }
        return false;
    }

    @Override
    public String getValue() {
        return formatDate.format(rechercheDate).toString();
    }

    private class DatePickerListener implements View.OnClickListener {
        final Calendar c = Calendar.getInstance();
        @Override
        public void onClick(View v) {
            new AlternativeDateSlider(getActivity(),mDateDuListener,c).show();
        }
    }

    private DateSlider.OnDateSetListener mDateDuListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    rechercheDate = selectedDate.getTime();
                    type_layout_date_fragment_date.setText(formatDate.format(selectedDate.getTime()));
                }
            };
}

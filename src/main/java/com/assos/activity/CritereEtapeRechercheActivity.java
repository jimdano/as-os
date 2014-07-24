package com.assos.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.assos.erreurs.ErreurManager;
import com.assos.fragment.TypeFragmentAssociation;
import com.assos.fragment.TypeFragmentCategorie;
import com.assos.fragment.TypeFragmentDate;
import com.assos.fragment.TypeFragmentLieu;
import com.assos.fragment.TypeFragmentString;
import com.assos.interfaces.IRechercheCritereTypeFragment;
import com.assos.manager.CritereRechercheManager;
import com.assos.model.CritereRecherche;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by jimmy on 06/07/2014
 */
public class CritereEtapeRechercheActivity extends FragmentActivity {

    private ArrayList<CritereRecherche> criteres;
    private FragmentTransaction ft;
    private FragmentManager fm;
    private CritereRecherche critereCourant;

    private Button activity_critere_etape_recherche_back;
    private Button activity_critere_etape_recherche_next;
    private Button activity_critere_etape_recherche_finish;

    private FrameLayout activity_critere_etape_recherche_container;

    private IRechercheCritereTypeFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critere_etape_recherche);
        activity_critere_etape_recherche_back = (Button) findViewById(R.id.activity_critere_etape_recherche_back);
        activity_critere_etape_recherche_next = (Button) findViewById(R.id.activity_critere_etape_recherche_next);
        activity_critere_etape_recherche_finish = (Button) findViewById(R.id.activity_critere_etape_recherche_finish);
        activity_critere_etape_recherche_container = (FrameLayout) findViewById(R.id.activity_critere_etape_recherche_container);

        criteres = CritereRechercheManager.getInstance().getCriteresSelectionnes();
        gererLesCriteres();
        afficherVueCritereCourant();
        gestionBoutons();
    }

    private void gestionBoutons() {
        activity_critere_etape_recherche_back.setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         if(criteres.indexOf(critereCourant) != 0) {
                                                                             if (CritereRechercheManager.getInstance().getCriteresFaits().containsKey(critereCourant.getModelName()))
                                                                                 CritereRechercheManager.getInstance().removeFromHashmap(critereCourant);
                                                                             critereCourant = CritereRechercheManager.getInstance().getCriterePrecedant();
                                                                             CritereRechercheManager.getInstance().setCritereSuivant(CritereRechercheManager.getInstance().recupererCritereSuivant(critereCourant));
                                                                             CritereRechercheManager.getInstance().setCriterePrecedant(CritereRechercheManager.getInstance().recupererCriterePrecedent(critereCourant));
                                                                             reload();
                                                                         } else {
                                                                             CritereEtapeRechercheActivity.this.finish();
                                                                         }
                                                                     }
                                                                 }
        );

        activity_critere_etape_recherche_next.setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View v) {
                                                                         if (currentFragment.next()) {
                                                                             CritereRechercheManager.getInstance().addIntoHashmap(critereCourant, currentFragment.getValue());
                                                                             critereCourant = CritereRechercheManager.getInstance().getCritereSuivant();
                                                                             CritereRechercheManager.getInstance().setCritereSuivant(CritereRechercheManager.getInstance().recupererCritereSuivant(critereCourant));
                                                                             CritereRechercheManager.getInstance().setCriterePrecedant(CritereRechercheManager.getInstance().recupererCriterePrecedent(critereCourant));
                                                                             reload();
                                                                         } else {
                                                                             Toast.makeText(CritereEtapeRechercheActivity.this, "vous devez entrer une valeur pour ce critère", Toast.LENGTH_SHORT).show();
                                                                         }
                                                                     }
                                                                 }
        );

        activity_critere_etape_recherche_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFragment.next()) {
                    lancerRequete();
                } else {
                    Toast.makeText(CritereEtapeRechercheActivity.this, "vous devez entrer une valeur pour ce critère", Toast.LENGTH_SHORT).show();
}
}
        });
        }

    private void lancerRequete() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Evenement");
        Set cle = CritereRechercheManager.getInstance().getCriteresFaits().keySet();
        Iterator it = cle.iterator();
        while (it.hasNext()){
            CritereRecherche model = (CritereRecherche) it.next(); // le critere
            String val = CritereRechercheManager.getInstance().getCriteresFaits().get(cle); //la valeur du critere
            gestionQuery(query, model.getModelName(), val, model.getType());
        }
        gestionQuery(query, critereCourant.getModelName(), currentFragment.getValue(), critereCourant.getType());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {

                } else {
                    ErreurManager.gestionErreurCode(CritereEtapeRechercheActivity.this, e.getCode());
                }
            }
        });
    }

    private ParseQuery<ParseObject> gestionQuery(ParseQuery<ParseObject> query, String modelName, String value, String type) {
        if(type == CritereRechercheManager.TYPE_STRING ||type == CritereRechercheManager.TYPE_CATEGORIE){
            query.whereContains(modelName,value);
        } else {
            query.whereEqualTo(modelName,value);
        }
        return query;
    }

    private void afficherVueCritereCourant() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (activity_critere_etape_recherche_container != null) {
            if (critereCourant.getType().equalsIgnoreCase(CritereRechercheManager.TYPE_STRING)) {
                currentFragment = TypeFragmentString.newInstance();
                currentFragment.init(critereCourant);
                ft.replace(R.id.activity_critere_etape_recherche_container, (TypeFragmentString)currentFragment);
            } else if (critereCourant.getType().equalsIgnoreCase(CritereRechercheManager.TYPE_ASSOCIATION)) {
                currentFragment = TypeFragmentAssociation.newInstance();
                currentFragment.init(critereCourant);
                ft.replace(R.id.activity_critere_etape_recherche_container, (TypeFragmentAssociation)currentFragment);
            } else if (critereCourant.getType().equalsIgnoreCase(CritereRechercheManager.TYPE_CATEGORIE)) {
                currentFragment = TypeFragmentCategorie.newInstance();
                currentFragment.init(critereCourant);
                ft.replace(R.id.activity_critere_etape_recherche_container, (TypeFragmentCategorie) currentFragment);
            } else if (critereCourant.getType().equalsIgnoreCase(CritereRechercheManager.TYPE_DATE)) {
                currentFragment = TypeFragmentDate.newInstance();
                currentFragment.init(critereCourant);
                ft.replace(R.id.activity_critere_etape_recherche_container, (TypeFragmentDate)currentFragment);
            } else if (critereCourant.getType().equalsIgnoreCase(CritereRechercheManager.TYPE_LIEU)) {
                currentFragment = TypeFragmentLieu.newInstance();
                currentFragment.init(critereCourant);
                ft.replace(R.id.activity_critere_etape_recherche_container, (TypeFragmentLieu)currentFragment);
            }
        }
        ft.commit();
        if (CritereRechercheManager.getInstance().getCritereSuivant() == null) {
            activity_critere_etape_recherche_next.setVisibility(View.GONE);
            activity_critere_etape_recherche_finish.setVisibility(View.VISIBLE);
        } else {
            activity_critere_etape_recherche_next.setVisibility(View.VISIBLE);
            activity_critere_etape_recherche_finish.setVisibility(View.GONE);
        }
    }

    private void gererLesCriteres() {
        critereCourant = criteres.get(0);
        CritereRechercheManager.getInstance().setCritereSuivant(CritereRechercheManager.getInstance().recupererCritereSuivant(critereCourant));
    }

    public void reload() {
        afficherVueCritereCourant();
    }

    public void launchActivity(Context context) {
        final Intent intent = new Intent(context, CritereEtapeRechercheActivity.class);
        context.startActivity(intent);
    }
}

package com.assos.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.assos.activity.R;
import com.assos.adapter.UtilisateurFavoriteCategorieAdapter;
import com.assos.com.assos.callback.ICategorieManagerCallBack;
import com.assos.erreurs.ErreurManager;
import com.assos.interfaces.NamedFragment;
import com.assos.manager.CategorieManager;
import com.assos.manager.ParseUserManager;
import com.assos.model.Categorie;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 10/06/2014
 */
public class FragmentUtilisateurGestionCompte extends Fragment implements NamedFragment {

    private EditText userMail;
    private EditText userPhone;
    private ArrayList<Categorie> categoriesSelected;
    private UtilisateurFavoriteCategorieAdapter adapter;
    private ParseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.utilisateur_gestion_compte_fragment,
                container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        userMail = (EditText) rootView.findViewById(R.id.utilisateur_gestion_compte_mail);
        userPhone = (EditText) rootView.findViewById(R.id.utilisateur_gestion_compte_phone);
        Button register = (Button) rootView.findViewById(R.id.utilisateur_gestion_compte_validate);
        Button utilisateur_gestion_compte_reset = (Button) rootView.findViewById(R.id.utilisateur_gestion_compte_reset);
        GridView utilisateur_gestion_compte_categorie_grid = (GridView) rootView.findViewById(R.id.utilisateur_gestion_compte_categorie_grid);
        categoriesSelected = new ArrayList<Categorie>();
        adapter = new UtilisateurFavoriteCategorieAdapter(getActivity(),categoriesSelected);

        majCategories();

        utilisateur_gestion_compte_categorie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Categorie clickedCateg = (Categorie) adapter.getItem(position);
                if (!categoriesSelected.contains(clickedCateg)) {
                    categoriesSelected.add(clickedCateg);
                } else {
                    categoriesSelected.remove(clickedCateg);
                }
                adapter.notifyDataSetChanged();
            }
        });
        utilisateur_gestion_compte_categorie_grid.setAdapter(adapter);
        register.setOnClickListener(new RegisterListener());
        utilisateur_gestion_compte_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Un Mail va vous être envoyé");
                builderSingle.setNegativeButton("Annuler",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
                builderSingle.setPositiveButton("Poursuivre",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseUser.requestPasswordResetInBackground(ParseUser.getCurrentUser().getEmail(),
                                        new RequestPasswordResetCallback() {
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(getActivity(), "Un EMail vient d'être envoyé à votre adresse", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    ErreurManager.gestionErreurCode(getActivity(), e.getCode());
                                                }
                                            }
                                        }
                                );
                                dialog.dismiss();
                            }
                        }
                );
                builderSingle.show();
            }
        });
        return rootView;
    }

    private void majFields() {
        currentUser = ParseUser.getCurrentUser();
        userMail.setText(currentUser.getEmail());
        if(currentUser.getString(ParseUserManager.PARSE_USER_PHONE) != null) {
            userPhone.setText(currentUser.getString(ParseUserManager.PARSE_USER_PHONE));
        }
        if(currentUser.getJSONArray(ParseUserManager.PARSE_CATEGORIE) != null) {
            try {
                categoriesSelected.addAll(CategorieManager.getInstance().parseJSONIntoCategories(currentUser.getJSONArray(ParseUserManager.PARSE_CATEGORIE)));
                adapter.notifyDataSetChanged();
            } catch (Exception e) {}
        }
   }

    private void majCategories() {
        if (CategorieManager.getInstance().categ.isEmpty()) {
            CategorieManager.getInstance().getCategories(getActivity(), new ICategorieManagerCallBack() {
                @Override
                public void onSuccess(List<Categorie> list) {
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                    majFields();
                }

                @Override
                public void onError() {
                    Toast.makeText(getActivity(), "Erreur recuperation categories, excusez nous", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            adapter.addAll(CategorieManager.getInstance().categ);
            adapter.notifyDataSetChanged();
            majFields();
        }
    }

    public void onCreate() {
        Parse.initialize(getActivity(), "raooEIp6ziCuzXjZ4T8Vt83WpG23G2fDZhZiNZWf", "tct119JCo8u1edN48znQhAJ1jCMtailcCzG17xgl");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class RegisterListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            updateUser();
        }
    }

    private void updateUser() {
        if (formIsValid()) {
            currentUser.setEmail(userMail.getText().toString());
            if (!userPhone.getText().toString().trim().isEmpty()) {
                currentUser.put(ParseUserManager.PARSE_USER_PHONE, userPhone.getText().toString());
            }
            saveUser(currentUser);
        }
    }

    private void saveUser(final ParseUser user) {
        if(!categoriesSelected.isEmpty()) {
            List<ParseObject> assoCategories = new ArrayList<ParseObject>();
            for (Categorie c : categoriesSelected) {
                ParseObject p = new ParseObject(ParseUserManager.PARSE_CATEGORIE);
                p.put(ParseUserManager.PARSE_USER_NOM_CATEG, c.getNom());
                p.setObjectId(c.getId());
                assoCategories.add(p);
            }
            user.put(ParseUserManager.PARSE_CATEGORIE, assoCategories);
        } else {
            user.put(ParseUserManager.PARSE_CATEGORIE, new ArrayList<Categorie>());
        }
        user.saveInBackground();
        Toast.makeText(getActivity(), "Utilisateur mis à jour", Toast.LENGTH_SHORT).show();
    }

    private boolean formIsValid() {
        if (userMail.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "L'adresse EMail doit être valide", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "Gestion Du Compte";
    }

    public static FragmentUtilisateurGestionCompte newInstance() {
        return new FragmentUtilisateurGestionCompte();
    }
}
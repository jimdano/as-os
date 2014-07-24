package com.assos.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.assos.activity.HomeActivity;
import com.assos.activity.R;
import com.assos.adapter.CategorieAdapter;
import com.assos.com.assos.callback.ICategorieCallBack;
import com.assos.com.assos.callback.ICategorieManagerCallBack;
import com.assos.erreurs.ErreurManager;
import com.assos.manager.CategorieManager;
import com.assos.manager.ParseUserManager;
import com.assos.model.Categorie;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 03/06/2014.
 */
public class SignInFragment extends Fragment {

    private EditText assoName;
    private EditText login;
    private EditText userMail;
    private EditText assoAddress;
    private EditText userPass;
    private EditText userPassConfirm;
    private EditText userPhone;
    private EditText assoCP;
    private EditText assoCity;
    private RadioButton assoRadio;
    private RadioButton userRadio;
    private ArrayList<Categorie> categoriesSelected;
    private ImageView sign_in_categ;
    private TextView sign_in_categorie_symbol;
    private TextView sign_in_categ_text;
    private CategorieAdapter ca;
    private boolean isAdditionnable = false;
    private TextView sign_in_number_categorie;
    private String password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_in_fragment,
                container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        assoName = (EditText) rootView.findViewById(R.id.sign_in_asso_name);
        assoCity = (EditText) rootView.findViewById(R.id.sign_in_city);
        assoCP = (EditText) rootView.findViewById(R.id.sign_in_code_postal);
        login = (EditText) rootView.findViewById(R.id.sign_in_login);
        userMail = (EditText) rootView.findViewById(R.id.sign_in_mail);
        userPass = (EditText) rootView.findViewById(R.id.sign_in_password);
        userPassConfirm = (EditText) rootView.findViewById(R.id.sign_in_password_confirm);
        userPhone = (EditText) rootView.findViewById(R.id.sign_in_phone);
        assoRadio = (RadioButton) rootView.findViewById(R.id.sign_in_asso_radio);
        userRadio = (RadioButton) rootView.findViewById(R.id.sign_in_user_radio);
        Button register = (Button) rootView.findViewById(R.id.sign_in_validate);
        sign_in_categ = (ImageView) rootView.findViewById(R.id.sign_in_select_categ);
        sign_in_categ_text = (TextView) rootView.findViewById(R.id.sign_in_select_categ_text);
        sign_in_categorie_symbol = (TextView) rootView.findViewById(R.id.sign_in_categorie_symbol);
        sign_in_number_categorie = (TextView) rootView.findViewById(R.id.sign_in_number_categorie);
        assoAddress = (EditText) rootView.findViewById(R.id.sign_in_address);

        categoriesSelected = new ArrayList<Categorie>();

        majCategories();
        assoRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContext(View.VISIBLE);
                userRadio.setChecked(false);
                assoRadio.setChecked(true);
            }
        });
        userRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContext(View.GONE);
                assoRadio.setChecked(false);
                userRadio.setChecked(true);
            }
        });
        register.setOnClickListener(new RegisterListener());
        setContext(View.GONE);
        sign_in_categ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestionPopUp(CategorieManager.getInstance().categ);
            }
        });

        sign_in_categ_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestionPopUp(CategorieManager.getInstance().categ);
            }
        });
        return rootView;
    }

    private void majCategories() {
        CategorieManager.getInstance().getCategories(getActivity(), new ICategorieManagerCallBack() {
            @Override
            public void onSuccess(List<Categorie> list) {
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "Erreur recuperation categories, excusez nous", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gestionPopUp(List<Categorie> list) {
        if (list.isEmpty()) {
            Toast.makeText(getActivity(), "Récupération catégorie... Veuillez patienter", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
            builderSingle.setIcon(R.drawable.ic_launcher);
            builderSingle.setTitle("Selectionnez la/les Categorie");

            builderSingle.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isAdditionnable = false;
                            updateCategoriesSelected();
                            dialog.dismiss();
                        }
                    }
            );
            gestionCategorieAdapter(list);
            isAdditionnable = true;
            builderSingle.setAdapter(ca,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isAdditionnable = false;
                        }
                    }
            );
            builderSingle.show();
        }
    }

    private void updateCategoriesSelected() {
        String ret = "";
        for (Categorie c : categoriesSelected) {
            ret += c.getNom() + '\n';
        }
        sign_in_categorie_symbol.setText(ret);
    }

    private void gestionCategorieAdapter(List<Categorie> list) {
        ca = new CategorieAdapter(getActivity(), categoriesSelected);
        ca.addAll(list);
        ca.addCallback(new ICategorieCallBack() {
            @Override
            public void addCategorie(Categorie c) {
                if (!categoriesSelected.contains(c) && isAdditionnable) {
                    categoriesSelected.add(c);
                    if (categoriesSelected.size() > 0) {
                        sign_in_number_categorie.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void removeCategorie(Categorie c) {
                if (categoriesSelected.contains(c) && isAdditionnable) {
                    categoriesSelected.remove(c);
                    if (categoriesSelected.size() <= 0) {
                        sign_in_number_categorie.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void setContext(int b) {
        assoName.setVisibility(b);
        sign_in_categ_text.setVisibility(b);
        sign_in_categ.setVisibility(b);
        sign_in_categorie_symbol.setVisibility(b);
        if(b == View.VISIBLE) {
            if(categoriesSelected.size() > 0)
                sign_in_number_categorie.setVisibility(b);
        } else {
            sign_in_number_categorie.setVisibility(b);
        }
        assoAddress.setVisibility(b);
        assoCity.setVisibility(b);
        assoCP.setVisibility(b);
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
            registerUser(assoRadio.isChecked());
        }
    }

    private void registerUser(boolean assoChecked) {
        final ParseUser user = new ParseUser();
        if (formIsValid(assoChecked)) {
            user.setUsername(login.getText().toString());
            user.setPassword(userPass.getText().toString());
            user.setEmail(userMail.getText().toString());
            if (!userPhone.getText().toString().trim().isEmpty())
                user.put(ParseUserManager.PARSE_USER_PHONE, userPhone.getText().toString());
            if (assoChecked) {
                user.put(ParseUserManager.PARSE_USER_TYPE, ParseUserManager.ASSO_TYPE);
            } else {
                user.put(ParseUserManager.PARSE_USER_TYPE, ParseUserManager.USER_TYPE);
            }
            if (assoChecked) {
                user.put(ParseUserManager.PARSE_ASSO_NAME, assoName.getText().toString());
                user.put(ParseUserManager.PARSE_ASSO_ADDRESS, assoAddress.getText().toString());
                user.put(ParseUserManager.PARSE_ASSO_CP, assoCP.getText().toString());
                user.put(ParseUserManager.PARSE_ASSO_CITY, assoCity.getText().toString());
                if (categoriesSelected.size() <= 0) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                    builderSingle.setIcon(R.drawable.ic_launcher);
                    builderSingle.setTitle("Vous n'avez pas sélectionné de catégories, poursuivre quand même ?");
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
                                    saveUser(user);
                                    dialog.dismiss();
                                }
                            }
                    );
                    builderSingle.show();
                } else {
                    saveUser(user);
                }
            } else {
                saveUser(user);
            }
        }
    }

    private void saveUser(final ParseUser user) {
        if (assoRadio.isChecked()) {
            user.put(ParseUserManager.PARSE_CATEGORIE, CategorieManager.getInstance().parseListIntoParseObject(categoriesSelected));
        }

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser.logInInBackground(user.getUsername(),password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (parseUser != null) {
                                HomeActivity.launchActivity(getActivity());
                                getActivity().finish();
                            } else {
                                ErreurManager.gestionErreurCode(getActivity(), e.getCode());
                            }
                        }
                    });
                } else {
                    ErreurManager.gestionErreurCode(getActivity(), e.getCode());
                }
            }
        });
    }

    private boolean formIsValid(boolean assoChecked) {
        if (assoChecked && assoName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Le nom de l'association est manquant", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (assoChecked && assoAddress.getText().toString().isEmpty() || (assoChecked && assoCP.getText().toString().isEmpty()) ||
                (assoChecked && assoCity.getText().toString().isEmpty())) {
            Toast.makeText(getActivity(), "L'adresse de l'association est incomplète", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (login.getText().toString().isEmpty() ||
                userPass.getText().toString().isEmpty() || userPassConfirm.getText().toString().isEmpty()
                || userMail.getText().toString().isEmpty()
                ) {
            Toast.makeText(getActivity(), "Un des champs est manquant ou vide", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!userPass.getText().toString().equals(userPassConfirm.getText().toString())) {
            Toast.makeText(getActivity(), "Les mots de passes ne correspondent pas", Toast.LENGTH_SHORT).show();
            return false;
        }
        password = userPass.getText().toString();
        return true;
    }
}

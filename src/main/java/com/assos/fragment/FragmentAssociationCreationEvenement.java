package com.assos.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.assos.activity.R;
import com.assos.adapter.CategorieAdapter;
import com.assos.com.assos.callback.ICategorieCallBack;
import com.assos.com.assos.callback.ICategorieManagerCallBack;
import com.assos.interfaces.NamedFragment;
import com.assos.manager.CategorieManager;
import com.assos.manager.ParseUserManager;
import com.assos.model.Categorie;
import com.assos.model.Evenement;
import com.assos.utils.DebugLog;
import com.dateslider.AlternativeDateSlider;
import com.dateslider.DateSlider;
import com.dateslider.TimeSlider;
import com.parse.ParseUser;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar; 
import java.util.Date;
import java.util.List;

/**
 * Created by jimmy on 11/06/2014.
 */
public class FragmentAssociationCreationEvenement extends Fragment implements NamedFragment {

    private EditText association_creation_evenement_titre;
    private EditText association_creation_evenement_lieu;
    private EditText association_creation_evenement_descriptif;

    private Button association_creation_evenement_lieu_bouton;
    private Button association_creation_evenement_categorie_selection;
    private Button association_creation_evenement_valider;
    private Button association_creation_evenement_spinner_du;
    private Button association_creation_evenement_spinner_au;
    private Button association_creation_evenement_spinner_au_hour;
    private Button association_creation_evenement_spinner_du_hour;

    private CheckBox association_creation_evenement_lieu_asso;
    private CheckBox association_creation_evenement_journee_entiere;
    private CheckBox association_creation_evenement_categorie_asso;

    private Spinner association_creation_evenement_spinner_frequence;
    private Evenement currentEvenement;
    private ArrayList<Evenement> currentEvenementFrequenciels;
    private String[] frequences;
    private String currentFrequence;
    private ParseUser currentUser;

    private ArrayList<Categorie> evenementCategories;
    private ArrayList<Categorie> userCategories;
    private CategorieAdapter ca;
    private Date evenement_du;
    private Date evenement_au;
    private Date heure_du;
    private Date heure_au;
    private boolean isAddable = false;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat formathour;
    private SimpleDateFormat formatFinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.association_creation_evenement,
                container, false);

        association_creation_evenement_titre = (EditText) rootView.findViewById(R.id.association_creation_evenement_titre);
        association_creation_evenement_lieu = (EditText) rootView.findViewById(R.id.association_creation_evenement_lieu);
        association_creation_evenement_descriptif = (EditText) rootView.findViewById(R.id.association_creation_evenement_descriptif);

        association_creation_evenement_lieu_bouton = (Button) rootView.findViewById(R.id.association_creation_evenement_lieu_bouton);
        association_creation_evenement_categorie_selection = (Button) rootView.findViewById(R.id.association_creation_evenement_categorie_selection);
        association_creation_evenement_valider = (Button) rootView.findViewById(R.id.activity_critere_etape_recherche);
        association_creation_evenement_spinner_du = (Button) rootView.findViewById(R.id.association_creation_evenement_spinner_du);
        association_creation_evenement_spinner_au = (Button) rootView.findViewById(R.id.association_creation_evenement_spinner_au);
        association_creation_evenement_spinner_au_hour = (Button) rootView.findViewById(R.id.association_creation_evenement_spinner_au_hour);
        association_creation_evenement_spinner_du_hour = (Button) rootView.findViewById(R.id.association_creation_evenement_spinner_du_hour);

        association_creation_evenement_lieu_asso = (CheckBox) rootView.findViewById(R.id.association_creation_evenement_lieu_asso);
        association_creation_evenement_journee_entiere = (CheckBox) rootView.findViewById(R.id.association_creation_evenement_journee_entiere);
        association_creation_evenement_categorie_asso = (CheckBox) rootView.findViewById(R.id.association_creation_evenement_categorie_asso);

        association_creation_evenement_spinner_frequence = (Spinner) rootView.findViewById(R.id.association_creation_evenement_spinner_frequence);

        formatDate = new SimpleDateFormat("dd-MM-yyyy");
        formathour = new SimpleDateFormat("HH:mm");
        formatFinal = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        evenementCategories = new ArrayList<Categorie>();
        ca = new CategorieAdapter(getActivity(), evenementCategories);
        addAllCateg();
        currentEvenement = new Evenement();
        currentEvenementFrequenciels = new ArrayList<Evenement>();

        frequences = getResources().getStringArray(R.array.frequence_array);
        currentUser = ParseUser.getCurrentUser();
        userCategories = new ArrayList<Categorie>();

        return rootView;
    }

    private void addAllCateg() {
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
    }

    private void saveEvenement() {
        //evenements gérés par les checkbox et l'alert dialog
        //adresse gérée si check
        if (currentEvenement.isEstJourneeEntiere()) {
            currentEvenement.setDebut(new Timestamp(evenement_du.getTime()));
            currentEvenement.setFin(new Timestamp(evenement_au.getTime()));
        } else {
            currentEvenement.setDebut(new Timestamp(evenement_du.getTime() + heure_du.getTime()));
            currentEvenement.setFin(new Timestamp(evenement_au.getTime() + heure_au.getTime()));
        }
        if (association_creation_evenement_lieu_asso.isChecked()) {
            currentEvenement.setLieu(association_creation_evenement_lieu.getText().toString());
        } else {
            currentEvenement.setLieu(currentUser.getString("lieu"));
            try {
                currentEvenement.setPositionX(currentUser.getDouble("positionX"));
                currentEvenement.setPositionY(currentUser.getDouble("positionY"));
            } catch (Exception e) {
            }
        }
        currentEvenement.setTitre(association_creation_evenement_titre.getText().toString());

        currentEvenement.setAssoId(currentUser.getObjectId());
        if(association_creation_evenement_lieu_asso.isChecked()) {
            currentEvenement.setLieu((String) currentUser.get("address"));
        } else {
            currentEvenement.setLieu(association_creation_evenement_lieu.getText().toString());
        }
        currentEvenement.setDescription(association_creation_evenement_descriptif.getText().toString());
        if (currentFrequence != null && !currentFrequence.equals(frequences[0])) {
            //créer plusieurs évenements
            //Hebdomadaire
            if (currentFrequence.equals(frequences[1])) {

            } //journalier
            else if (currentFrequence.equals(frequences[1])) {

            } // mensuel
            else if (currentFrequence.equals(frequences[1])) {

            } // annuel
            else {

            }
            saveinBDD(currentEvenementFrequenciels);
        }

        saveinBDD(currentEvenement);
    }

    private void saveinBDD(ArrayList<Evenement> list) {
        for (Evenement aList : list) {
            saveinBDD(aList);
        }
    }

    private void saveinBDD(Evenement e) {
        e.saveInBackground();
    }

    private boolean isValidForm() {
        if (association_creation_evenement_titre.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Le nom de l'évenement est manquant", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!association_creation_evenement_lieu_asso.isChecked() && association_creation_evenement_lieu_asso.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "L'adresse de l'association est incomplète", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!association_creation_evenement_categorie_asso.isChecked() && evenementCategories.size() <= 0) {
            Toast.makeText(getActivity(), "Aucune catégorie n'a été selectionnée", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!association_creation_evenement_journee_entiere.isChecked() && (evenement_au == null || evenement_du == null
                || heure_au == null || heure_du == null)) {
            Toast.makeText(getActivity(), "Sélectionnez une date ainsi que des horaires svp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (association_creation_evenement_journee_entiere.isChecked() && (evenement_au == null || evenement_du == null)) {
            Toast.makeText(getActivity(), "Sélectionnez une date svp", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (association_creation_evenement_descriptif.getText().toString() == null) {
            Toast.makeText(getActivity(), "Veuillez remplir le descriptif svp", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void gestionCategorieAdapter() {
        ca.addCallback(new ICategorieCallBack() {
            @Override
            public void addCategorie(Categorie c) {
                if (!evenementCategories.contains(c) && isAddable) {
                    evenementCategories.add(c);
                }
            }

            @Override
            public void removeCategorie(Categorie c) {
                if (evenementCategories.contains(c) && isAddable) {
                    evenementCategories.remove(c);
                }
            }
        });
    }

    private void majUserFields() {
        if (currentUser.getJSONArray(ParseUserManager.PARSE_CATEGORIE) != null) {
            try {
                userCategories.addAll(CategorieManager.getInstance().parseJSONIntoCategories(currentUser.getJSONArray(ParseUserManager.PARSE_CATEGORIE)));
            } catch (Exception e) {
                DebugLog.log("erreur parsing user categories / creation evenement");
            }
        }
        if (userCategories.size() <= 0) {
            association_creation_evenement_categorie_asso.setEnabled(false);
        }
    }

    private void gestionSpinners() {
        association_creation_evenement_spinner_frequence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFrequence = frequences[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentFrequence = null;
            }
        });
    }

    public void gestionCheckboxes() {
        association_creation_evenement_lieu_asso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    association_creation_evenement_lieu.setEnabled(false);
                    association_creation_evenement_lieu_bouton.setEnabled(false);
                } else {
                    association_creation_evenement_lieu.setEnabled(true);
                    association_creation_evenement_lieu_bouton.setEnabled(true);
                }
            }
        });
        association_creation_evenement_categorie_asso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    association_creation_evenement_categorie_selection.setEnabled(false);
                    currentEvenement.setCategories(userCategories);
                } else {
                    association_creation_evenement_categorie_selection.setEnabled(true);
                    currentEvenement.setCategories(evenementCategories);
                }
            }
        });
        association_creation_evenement_journee_entiere.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currentEvenement.setEstJourneeEntiere(true);
                    association_creation_evenement_spinner_au_hour.setEnabled(false);
                    association_creation_evenement_spinner_du_hour.setEnabled(false);
                } else {
                    currentEvenement.setEstJourneeEntiere(false);
                    association_creation_evenement_spinner_au_hour.setEnabled(true);
                    association_creation_evenement_spinner_du_hour.setEnabled(true);
                }
            }
        });
    }

    private void gestionButtons() {
        association_creation_evenement_spinner_du.setOnClickListener(new DatePickerDuListener());
        association_creation_evenement_spinner_au.setOnClickListener(new DatePickerAuListener());
        association_creation_evenement_spinner_au_hour.setOnClickListener(new DatePickerHourAuListener());
        association_creation_evenement_spinner_du_hour.setOnClickListener(new DatePickerHourDuListener());
        association_creation_evenement_categorie_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddable = true;
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Selectionnez la/les Categorie");
                builderSingle.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isAddable = false;
                            }
                        }
                );
                gestionCategorieAdapter();
                builderSingle.setAdapter(ca,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                isAddable = false;
                            }
                        }
                );
                builderSingle.show();
            }
        });
        association_creation_evenement_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm()) {
                    saveEvenement();
                }
            }
        });
    }

    public static FragmentAssociationCreationEvenement newInstance() {
        return new FragmentAssociationCreationEvenement();
    }

    @Override
    public String getName() {
        return "Créer Evenement";
    }

    public void reload() {
        majUserFields();
        gestionCheckboxes();
        gestionSpinners();
        gestionButtons();
    }

    public class DatePickerAuListener implements View.OnClickListener {
        final Calendar c = Calendar.getInstance();

        @Override
        public void onClick(View v) {
            new AlternativeDateSlider(getActivity(), mDateAuListener, c).show();
        }
    }

    public class DatePickerHourDuListener implements View.OnClickListener {
        final Calendar c = Calendar.getInstance();

        @Override
        public void onClick(View v) {
            new TimeSlider(getActivity(), mDateHourDuListener, c).show();
        }
    }

    public class DatePickerHourAuListener implements View.OnClickListener {
        final Calendar c = Calendar.getInstance();

        @Override
        public void onClick(View v) {
            new TimeSlider(getActivity(), mDateHourAuListener, c).show();
        }
    }

    private DateSlider.OnDateSetListener mDateHourDuListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    heure_du = selectedDate.getTime();
                    association_creation_evenement_spinner_du_hour.setText(formathour.format(selectedDate.getTime()));
                }
            };

    private DateSlider.OnDateSetListener mDateHourAuListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    heure_au = selectedDate.getTime();
                    association_creation_evenement_spinner_au_hour.setText(formathour.format(selectedDate.getTime()));
                }
            };

    public class DatePickerDuListener implements View.OnClickListener {
        final Calendar c = Calendar.getInstance();

        @Override
        public void onClick(View v) {
            new AlternativeDateSlider(getActivity(), mDateDuListener, c).show();
        }
    }

    private DateSlider.OnDateSetListener mDateAuListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    evenement_au = selectedDate.getTime();
                    association_creation_evenement_spinner_au.setText(formatDate.format(selectedDate.getTime()));
                }
            };

    private DateSlider.OnDateSetListener mDateDuListener =
            new DateSlider.OnDateSetListener() {
                public void onDateSet(DateSlider view, Calendar selectedDate) {
                    evenement_du = selectedDate.getTime();
                    association_creation_evenement_spinner_du.setText(formatDate.format(selectedDate.getTime()));
                }
            };
}
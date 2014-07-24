package com.assos.manager;

import com.assos.model.CritereRecherche;
import com.assos.model.Evenement;
import com.assos.model.ICategories;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jimmy on 03/07/2014
 */
public class CritereRechercheManager {

    public static String CRITERE_LIEU = "Le lieu";
    public static String CRITERE_DATE_DU = "La date de début";
    public static String CRITERE_DATE_AU = "La date de fin";
    public static String CRITERE_ASSOCIATION = "L'association organisatrice";
    public static String CRITERE_CATEGORIE = "Le/Les types d'activités";
    public static String CRITERE_MOTS = "Recherche par mots clés";

    public static String TYPE_LIEU = "Lieu";
    public static final String TYPE_STRING = "String";
    public static String TYPE_DATE = "Date";
    public static String TYPE_ASSOCIATION = "Association";
    public static String TYPE_CATEGORIE = "Catégorie";

    private ArrayList<CritereRecherche> criteres = new ArrayList<CritereRecherche>();
    private ArrayList<CritereRecherche> criteresSelectionnes = new ArrayList<CritereRecherche>();
    private HashMap<CritereRecherche , String> criteresFaits = new HashMap<CritereRecherche, String>();

    private CritereRecherche criterePrecedant;
    private CritereRecherche critereSuivant;

    /**
     * Constructeur privé
        */
        private CritereRechercheManager() {
    }

    public ArrayList<CritereRecherche> getCriteres() {
        return criteres;
    }

    public HashMap<CritereRecherche , String> getCriteresFaits() {
        return criteresFaits;
    }

    public ArrayList<CritereRecherche> getCriteresSelectionnes() {
        return criteresSelectionnes;
    }

    public void setCriteresSelectionnes(ArrayList<CritereRecherche> criteresSelectionnes) {
        this.criteresSelectionnes = criteresSelectionnes;
    }

    public CritereRecherche getCriterePrecedant() {
        return criterePrecedant;
    }

    public void setCriterePrecedant(CritereRecherche criterePrecedant) {
        this.criterePrecedant = criterePrecedant;
    }

    public CritereRecherche getCritereSuivant() {
        return critereSuivant;
    }

    public void setCritereSuivant(CritereRecherche critereSuivant) {
        this.critereSuivant = critereSuivant;
    }

    /**
     * Holder
     */
    private static class SingletonHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static CritereRechercheManager instance = new CritereRechercheManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static CritereRechercheManager getInstance() {
        return SingletonHolder.instance;
    }

    public void removeFromHashmap(CritereRecherche key){
        criteresFaits.remove(key);
    }

    public void addIntoHashmap(CritereRecherche key, String valeur){
        criteresFaits.put(key, valeur);
    }

    public void addIntoHashmap(CritereRecherche key, ArrayList<ICategories> valeur){
        String str = "";
        for(int i = 0; i < valeur.size(); i ++){
            str += valeur.get(i).getId();
        }
        criteresFaits.put(key, str);
    }



    public void init(){
        criteres = new ArrayList<CritereRecherche>();
        criteres.add(new CritereRecherche(CRITERE_LIEU, TYPE_LIEU, Evenement.EVENEMENT_LIEU));
        criteres.add(new CritereRecherche(CRITERE_ASSOCIATION, TYPE_ASSOCIATION,Evenement.EVENEMENT_ASSO));
        criteres.add(new CritereRecherche(CRITERE_CATEGORIE, TYPE_CATEGORIE,Evenement.EVENEMENT_CATEG));
        criteres.add(new CritereRecherche(CRITERE_DATE_DU, TYPE_DATE,Evenement.EVENEMENT_DATE_DU));
        criteres.add(new CritereRecherche(CRITERE_DATE_AU, TYPE_DATE,Evenement.EVENEMENT_DATE_AU));
        criteres.add(new CritereRecherche(CRITERE_MOTS, TYPE_STRING,Evenement.EVENEMENT_DESC));
    }

    public CritereRecherche recupererCritereSuivant(CritereRecherche crit) {
        int critereIndex = 0;
        for (int i = 0; i < criteresSelectionnes.size(); i++) {
            if (criteresSelectionnes.get(i).getNom() == crit.getNom()) {
                if (i == criteresSelectionnes.size() - 1) {
                    return null;
                } else {
                    critereIndex = i + 1;
                    break;
                }
            }
        }
        return criteresSelectionnes.get(critereIndex);
    }

    public CritereRecherche recupererCriterePrecedent(CritereRecherche crit) {
        int critereIndex = 0;
        for (int i = 0; i < criteresSelectionnes.size(); i++) {
            if (criteresSelectionnes.get(i).getNom() == crit.getNom()) {
                if (i > 0) {
                    critereIndex = i - 1;
                    break;
                } else {
                    return null;
                }
            }
        }
        return criteresSelectionnes.get(critereIndex);
    }
}

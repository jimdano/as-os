package com.assos.model;

import com.assos.manager.CategorieManager;
import com.parse.ParseObject;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jimmy on 11/06/2014.
 */
public class Evenement {

    public static String EVENEMENT_LIEU = "lieu";
    public static String EVENEMENT_DESC = "description";
    public static String EVENEMENT_TITRE = "titre";
    public static String EVENEMENT_ASSO = "assoId";
    public static String EVENEMENT_CATEG = "categories";
    public static String EVENEMENT_DATE_DU = "debut";
    public static String EVENEMENT_DATE_AU = "fin";

    private List<Categorie> categories;

    private String assoId;
    private String titre;
    private String description;
    private Timestamp debut;
    private Timestamp fin;
    private String lieu;

    private boolean estJourneeEntiere = false;

    private double positionX;
    private double positionY;

    public Evenement () {
    }

    public Evenement(List<Categorie> categories, String assoId,
                     String titre, String description, Timestamp debut,
                     Timestamp fin, boolean estJourneeEntiere,
                     double positionX, double positionY) {
        this.categories = categories;
        this.assoId = assoId;
        this.titre = titre;
        this.description = description;
        this.debut = debut;
        this.fin = fin;
        this.estJourneeEntiere = estJourneeEntiere;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public List<Categorie> getCategories() {
        return categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public String getAssoId() {
        return assoId;
    }

    public void setAssoId(String assoId) {
        this.assoId = assoId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDebut() {
        return debut;
    }

    public void setDebut(Timestamp debut) {
        this.debut = debut;
    }

    public Timestamp getFin() {
        return fin;
    }

    public void setFin(Timestamp fin) {
        this.fin = fin;
    }

    public boolean isEstJourneeEntiere() {
        return estJourneeEntiere;
    }

    public void setEstJourneeEntiere(boolean estJourneeEntiere) {
        this.estJourneeEntiere = estJourneeEntiere;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void saveInBackground() {
        ParseObject o = new ParseObject("Evenement");
        o.put("categories", CategorieManager.getInstance().parseListIntoParseObject(categories));
        o.put("assoId", assoId);
        o.put("titre", titre);
        o.put("description", description);
        o.put("debut", debut);
        o.put("fin", fin);
        o.put("lieu", lieu);
        o.put("estJourneeEntiere", estJourneeEntiere);
        o.put("positionX", positionX);
        o.put("positionY", positionY);
        o.saveInBackground();
    }
}

package com.assos.model;

/**
 * Created by jimmy on 03/06/2014.
 */
public class Categorie implements ICategories{

    private String nom;
    private String id;

    public Categorie(String a, String name) {
        id = a;
        nom = name;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

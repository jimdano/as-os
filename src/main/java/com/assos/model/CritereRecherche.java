package com.assos.model;

/**
 * Created by jimmy on 03/07/2014
 */
public class CritereRecherche {

    private String nom;
    private String type;
    private String modelName;

    public CritereRecherche(String n, String t, String m){
        nom = n;
        type = t;
        modelName = m;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}

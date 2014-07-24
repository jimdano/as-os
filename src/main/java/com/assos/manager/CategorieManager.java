package com.assos.manager;

import android.content.Context;
import android.util.Log;

import com.assos.com.assos.callback.ICategorieManagerCallBack;
import com.assos.model.Categorie;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 03/06/2014.
 */
public class CategorieManager {

    public ArrayList<Categorie> categ = new ArrayList<Categorie>();

    /**
     * Constructeur privé
     */
    private CategorieManager() {
    }

    /**
     * Holder
     */
    private static class SingletonHolder {
        /**
         * Instance unique non préinitialisée
         */
        private final static CategorieManager instance = new CategorieManager();
    }

    /**
     * Point d'accès pour l'instance unique du singleton
     */
    public static CategorieManager getInstance() {
        return SingletonHolder.instance;
    }

    public void getCategories(final Context activity, final ICategorieManagerCallBack categorieManagerCallBack){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Categorie");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categories, ParseException e) {
                if (categories != null) {
                    parseIntoCategories(categories);
                    categorieManagerCallBack.onSuccess(categ);
                }
                else {
                    categorieManagerCallBack.onError();
                }
            }
        });
    }

    private void parseIntoCategories(List<ParseObject> categories) {
        categ.clear();
        for (ParseObject current : categories) {
            Categorie c = new Categorie(current.getObjectId(),current.getString("nom"));
            categ.add(c);
        }
    }

    public List<Categorie> parseJSONIntoCategories(JSONArray categories) {
        List<Categorie> userCategories = new ArrayList<Categorie>();
        for (int i = 0; i < categories.length(); i ++){
            try {
                JSONObject o = categories.getJSONObject(i);
                Categorie c = findById(o.getString("objectId"));
                if(c != null) {
                    userCategories.add(c);
                }
            } catch (Exception e) {
                Log.d("AS_OS" , "  " + e);
            }
        }
        return userCategories;
    }

    public List<ParseObject> parseListIntoParseObject(List<Categorie> l){
        List<ParseObject> categorieList = new ArrayList<ParseObject>();
        for (Categorie c : l) {
            ParseObject p = new ParseObject(ParseUserManager.PARSE_CATEGORIE);
            p.put(ParseUserManager.PARSE_USER_NOM_CATEG, c.getNom());
            p.setObjectId(c.getId());
            categorieList.add(p);
        }
        return categorieList;
    }

    public Categorie findById(String objectId){
        if(categ != null){
            for(Categorie c : categ){
                if(objectId.equalsIgnoreCase(c.getId())){
                    return c;
                }
            }
        }
        return null;
    }
}

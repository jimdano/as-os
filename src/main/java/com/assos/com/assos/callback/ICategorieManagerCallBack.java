package com.assos.com.assos.callback;

import com.assos.model.Categorie;

import java.util.List;

/**
 * Created by jimmy on 03/06/2014.
 */
public interface ICategorieManagerCallBack {
    public void onSuccess(List<Categorie> list);
    public void onError();
}

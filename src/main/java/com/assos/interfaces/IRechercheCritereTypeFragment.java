package com.assos.interfaces;

import com.assos.model.CritereRecherche;

/**
 * Created by jimmy on 07/07/2014
 */
public interface IRechercheCritereTypeFragment {
    public void init(CritereRecherche critere);
    public boolean next();
    public String getValue();
}

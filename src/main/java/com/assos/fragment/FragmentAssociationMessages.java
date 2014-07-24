package com.assos.fragment;

import android.support.v4.app.Fragment;

import com.assos.interfaces.NamedFragment;

/**
 * Created by jimmy on 11/06/2014.
 */
public class FragmentAssociationMessages extends Fragment implements NamedFragment {

    public static FragmentAssociationMessages newInstance() {
        return new FragmentAssociationMessages();
    }
    @Override
    public String getName() {
        return "Messages";
    }
}

package com.assos.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.assos.fragment.FragmentAssociationCreationEvenement;
import com.assos.fragment.FragmentAssociationGestionCompte;
import com.assos.fragment.Recherche_Fragment;
import com.assos.fragment.FragmentUtilisateurGestionCompte;
import com.assos.interfaces.NamedFragment;
import com.assos.manager.ParseUserManager;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 06/06/2014.
 */
public class HomeActivity extends FragmentActivity implements ActionBar.TabListener {

    FragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static List<NamedFragment> fragments;
    private ParseUser user;
    private FragmentUtilisateurGestionCompte userGestionFragment;
    private FragmentAssociationGestionCompte assoGestionFragment;
    private Recherche_Fragment rechercheFrag;
    private FragmentAssociationCreationEvenement creerEvenementFragment;

    private static final int TAB_ASSO_GESTION_COMPTE = 0;
    private static final int TAB_ASSO_CREER_EVENEMENT = 1;
    private static final int TAB_ASSO_RECHERCHER = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        userGestionFragment = FragmentUtilisateurGestionCompte.newInstance();
        assoGestionFragment = FragmentAssociationGestionCompte.newInstance();
        rechercheFrag = Recherche_Fragment.newInstance();
        creerEvenementFragment = FragmentAssociationCreationEvenement.newInstance();

        mSectionsPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public int getCount(){ return fragments.size(); }
            @Override
            public Fragment getItem(int tabNumber){ return (Fragment) fragments.get(tabNumber); }
            @Override
            public CharSequence getPageTitle(int tabNumber){ return fragments.get(tabNumber).getName(); }
        };

        fragments = new ArrayList<NamedFragment>();
        gestionTabs();

        mViewPager = (ViewPager)findViewById(R.id.asos_pager);
        mViewPager.post(new Runnable() {
            public void run() {
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        mViewPager.setOffscreenPageLimit(fragments.size());

        for (int i = 0; i < fragments.size(); i++)
            actionBar.addTab(actionBar.newTab().setText(fragments.get(i).getName()).setTabListener(this));
    }

    private void gestionTabs() {
        user = ParseUser.getCurrentUser();
        String type = user.getString(ParseUserManager.PARSE_USER_TYPE);
        if(type.equalsIgnoreCase(ParseUserManager.USER_TYPE)){
            fragments.add(userGestionFragment);
        } else {
            fragments.add(assoGestionFragment);
            fragments.add(creerEvenementFragment);
        }
        fragments.add(rechercheFrag);
    }

    public static void launchActivity(Context context) {
        final Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        int tabPosition = tab.getPosition();
        mViewPager.setCurrentItem(tabPosition);
        if(user.getString("type").equalsIgnoreCase(ParseUserManager.USER_TYPE)){

        } else {
            switch(tabPosition) {
                case TAB_ASSO_GESTION_COMPTE : break;
                case TAB_ASSO_CREER_EVENEMENT :
                    creerEvenementFragment.reload();
                default: break;
            }
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}

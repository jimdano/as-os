package com.assos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.assos.activity.HomeActivity;
import com.assos.activity.R;
import com.assos.erreurs.ErreurManager;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ConnexionFragment extends Fragment {

    private EditText login;
    private EditText password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connexion_fragment,
                container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        login = (EditText) rootView.findViewById(R.id.login_username);
        password = (EditText) rootView.findViewById(R.id.login_password);
        Button connect = (Button) rootView.findViewById(R.id.login_connect);
        Button register = (Button) rootView.findViewById(R.id.login_sign_in);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "un des champs est manquant", Toast.LENGTH_SHORT).show();
                } else {
                    ParseUser.logInInBackground(login.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (parseUser != null) {
                                HomeActivity.launchActivity(getActivity());
                                getActivity().finish();
                            } else {
                                ErreurManager.gestionErreurCode(getActivity(), e.getCode());
                            }
                        }
                    });
                }
            }
        });

    register.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        FrameLayout frameHome = (FrameLayout) getActivity().findViewById(R.id.container);
                                        Fragment signIn = new SignInFragment();
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        if (frameHome != null) {
                                            ft.replace(R.id.container, signIn);
                                        }
                                        ft.commit();
                                    }
                                }
    );

    return rootView;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

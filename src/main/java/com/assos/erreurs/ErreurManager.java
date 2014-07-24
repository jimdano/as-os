package com.assos.erreurs;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jimmy on 06/06/2014.
 */
public class ErreurManager {

    public static void gestionErreurCode (Context c, int errorCode){
        switch (errorCode){
            case 125 :
                Toast.makeText(c, "Email invalide, veuillez réessayer", Toast.LENGTH_SHORT).show();
                break;
            case 100 :
                Toast.makeText(c, "Activez internet SVP", Toast.LENGTH_SHORT).show();
                break;
            case 101 :
                Toast.makeText(c, "Objet non trouvé, contactez le support svp", Toast.LENGTH_SHORT).show();
                break;
            case 202 :
                Toast.makeText(c, "Ce login est déja utilisée, changez svp", Toast.LENGTH_SHORT).show();
                break;
            case 203 :
                Toast.makeText(c, "Cette adresse mail est déja utilisée, changez svp", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(c, "Un problème est survenu", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

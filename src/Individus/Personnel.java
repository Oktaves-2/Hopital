package Individus;

import java.util.Date;

public class Personnel extends Personne {
    String idPers;

    public Personnel(String nom, String prenom, Date naissance, String idPers, String email, String mpasse,
            String adresse) {
        super(nom, prenom, naissance, adresse, email, mpasse);
        this.idPers = idPers;

    }

    public String getidPers() {
        return this.idPers;
    }

}

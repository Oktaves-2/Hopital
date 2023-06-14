package Individus;

import java.util.Date;

public class Personnel extends Personne {
    String idPers;
    Service service;

    public Personnel(String nom, String prenom, Date naissance, String idPers, String email, String mpasse, String adresse, Service service) {
        super(nom, prenom, naissance, adresse, email, mpasse);
        this.idPers = idPers;
        this.service=service;
    }

    public String getidPers() {
        return this.idPers;
    }

    public Service getService() {
        return this.service;

    }

    public void setService(Service service) {
        this.service = service;
    }
}

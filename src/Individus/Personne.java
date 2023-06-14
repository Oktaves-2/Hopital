package Individus;

import java.util.Date;

public class Personne {
    private String nom;
    private String prenom;
    private Date naissance;
    private String adresse;
    private String email;
    private String mpasse;

    public Personne(String nom,  String prenom, Date naissance, String adresse, String email, String mpasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.naissance = naissance;
        this.adresse = adresse;
        this.email=email;
        this.mpasse=mpasse;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }
    public Date getNaissance(){
        return this.naissance;
    }
    public String getAdresse(){
        return this.adresse;
    }
    public void setAdresse(String adresse){
        this.adresse=adresse;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setMpasse(String mpasse){
        this.mpasse=mpasse;
    }

}

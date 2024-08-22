package org.example.model;

import java.time.LocalDate;

/**
 * La classe Professeur représente un professeur dans l'application.
 * Elle hérite de la classe Personne et implémente les interfaces IEducation et ICRUDProfesseur.
 */
public class Professeur extends Personne  {
    /**
     * Indique si le professeur est vacant.
     */
    private boolean vacant;
    /**
     * La matière enseignée par le professeur.
     */
    private String matiereEnseigne;
    /**
     * Le prochain cours que le professeur doit préparer.
     */
    private String prochainCours;
    /**
     * Le sujet de la prochaine réunion que le professeur doit assister.
     */
    private String sujetProchaineReunion;


    /**
     * Constructeur de la classe Professeur.
     *
     * @param id            L'ID du professeur.
     * @param dateNaissance La date de naissance du professeur.
     * @param ville         La ville du professeur.
     * @param prenom        Le prénom du professeur.
     * @param nom           Le nom du professeur.
     * @param telephone     Le numéro de téléphone du professeur.
     */

    public Professeur(int id, LocalDate dateNaissance, String ville, String prenom, String nom, String telephone,
                      boolean vacant, String matiereEnseigne, String prochainCours, String sujetProchaineReunion) {
        super(id, dateNaissance, ville, prenom, nom, telephone);
        this.vacant = vacant;
        this.matiereEnseigne = matiereEnseigne;
        this.prochainCours = prochainCours;
        this.sujetProchaineReunion = sujetProchaineReunion;
    }


    public boolean isVacant() {
        return vacant;
    }

    public void setVacant(boolean vacant) {
        this.vacant = vacant;
    }

    public String getMatiereEnseigne() {
        return matiereEnseigne;
    }

    public void setMatiereEnseigne(String matiereEnseigne) {
        this.matiereEnseigne = matiereEnseigne;
    }

    public String getProchainCours() {
        return prochainCours;
    }

    public void setProchainCours(String prochainCours) {
        this.prochainCours = prochainCours;
    }

    public String getSujetProchaineReunion() {
        return sujetProchaineReunion;
    }

    public void setSujetProchaineReunion(String sujetProchaineReunion) {
        this.sujetProchaineReunion = sujetProchaineReunion;
    }
}

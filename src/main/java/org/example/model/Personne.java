package org.example.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * La classe {@code Personne} représente une personne avec des attributs personnels tels que l'identifiant,
 * la date de naissance, la ville, le prénom, le nom et le numéro de téléphone.
 * Elle fournit des méthodes pour accéder et modifier ces attributs, ainsi qu'une méthode pour obtenir l'âge
 * d'une personne à partir de sa date de naissance.
 */
@Data
public class Personne {
    protected int id;
    protected LocalDate dateNaissance;
    protected String ville;
    protected String prenom;
    protected String nom;
    protected String telephone;

    /**
     * Constructeur pour initialiser les attributs de la personne.
     *
     * @param id           L'identifiant de la personne.
     * @param dateNaissance La date de naissance de la personne sous forme de chaîne.
     * @param ville         La ville où réside la personne.
     * @param prenom        Le prénom de la personne.
     * @param nom           Le nom de la personne.
     * @param telephone     Le numéro de téléphone de la personne.
     */
    public Personne(int id, LocalDate  dateNaissance, String ville, String prenom, String nom, String telephone) {
        this.id = id;
        this.dateNaissance = dateNaissance;
        this.ville = ville;
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
    }



}

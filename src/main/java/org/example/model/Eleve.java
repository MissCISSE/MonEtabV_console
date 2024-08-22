package org.example.model;


import java.time.LocalDate;



/**
 * La classe Eleve represente un eleve dans l'application ETAB.
 * Elle hérite de la classe Personne et implemente l'interface ICRUDEleve.
 */
public class Eleve extends Personne {
    private String classe;
    private String matricule;



    /**
     * Constructeur de la classe Eleve.
     *
     * @param id           L'identifiant de l'élève.
     * @param dateNaissance La date de naissance de l'élève.
     * @param ville        La ville de l'élève.
     * @param prenom       Le prénom de l'élève.
     * @param nom          Le nom de l'élève.
     * @param telephone    Le numéro de téléphone de l'élève.
     * @param classe       La classe de l'élève.
     * @param matricule    Le matricule de l'élève.
     */
    public Eleve(int id, LocalDate dateNaissance, String ville, String prenom, String nom, String telephone, String classe, String matricule) {
        super(id, dateNaissance, ville, prenom, nom, telephone);
        this.classe = classe;
        this.matricule = matricule;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
}

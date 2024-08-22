package org.example.model;

/**
 * La classe {@code Utilisateur} gère l'authentification des utilisateurs et la gestion de leurs comptes.
 * Elle fournit des méthodes pour vérifier les identifiants, ajouter, modifier et supprimer des comptes,
 * ainsi qu'afficher le menu principal pour la gestion des utilisateurs.
 */
public class Utilisateur {

    private int id;
    private String identifiant;
    private String motDePass;

    /**
     * Constructeur par défaut de la classe {@code Utilisateur}.
     */
    public Utilisateur() {
    }

    /**
     * Constructeur avec paramètres de la classe {@code Utilisateur}.
     *
     * @param id          L'ID de l'utilisateur.
     * @param identifiant L'identifiant de l'utilisateur.
     * @param motDePass   Le mot de passe de l'utilisateur.
     */
    public Utilisateur(int id, String identifiant, String motDePass) {
        this.id = id;
        this.identifiant = identifiant;
        this.motDePass = motDePass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMotDePass() {
        return motDePass;
    }

    public void setMotDePass(String motDePass) {
        this.motDePass = motDePass;
    }
}

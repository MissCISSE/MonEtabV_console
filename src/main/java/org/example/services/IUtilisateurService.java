package org.example.services;


import org.example.model.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public interface IUtilisateurService {
    public boolean authentification(String identifiant, String motDePasse) throws SQLException;
    public boolean ajouterCompte(String identifiant, String motDePasse);
    public boolean modifierMotDepass(String identifiant, String motDePasse);
    public boolean supprimerCompte(String identifiant, String motDePasse);
    public List<Utilisateur> listeUtilisateur();
    boolean saveUser(Utilisateur utilisateur); // Ajout de la méthode pour enregistrer un utilisateur
}

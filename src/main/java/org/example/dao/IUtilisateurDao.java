package org.example.dao;

import org.example.model.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public interface IUtilisateurDao {

    boolean saveUser(Utilisateur utilisateur);  // Ajout de la méthode pour enregistrer un utilisateur
    public Utilisateur getUser(String identifiant, String motDePasse) throws SQLException;
    public Utilisateur updateUser(String identifiant, String motDePasse);
    public void deleteUser(String identifiant, String motDePasse);
    public List<Utilisateur> listeUtilisateur();
}

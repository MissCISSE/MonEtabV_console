package org.example;

import org.example.dao.impl.UtilisateurDaoImpl;

import java.sql.SQLException;

/**
 * La classe {@code Main} est le point d'entrée de l'application ETAB v1.2.
 * Elle gère l'initialisation de l'utilisateur et le flux de l'application.
 */
public class Main {

    /**
     * Le point d'entrée de l'application.
     * Cette méthode initialise l'utilisateur, gère l'authentification et affiche le menu principal.
     *
     * @param args Les arguments de la ligne de commande (non utilisés).
     * @throws SQLException Si une erreur SQL se produit lors de l'initialisation ou de l'authentification.
     */
    public static void main(String[] args) throws SQLException {
        UtilisateurDaoImpl utilisateurDao = new UtilisateurDaoImpl();
        utilisateurDao.initialiser();
        if (utilisateurDao.Authentification()) {
            MenuPrincipal menuPrincipal = new MenuPrincipal();
            menuPrincipal.afficherMenu();
        }
    }
}

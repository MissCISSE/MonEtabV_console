package org.example;

import org.example.dao.impl.EleveDaoImpl;
import org.example.dao.impl.ProfesseurDaoImpl;
import org.example.exceptions.MenuNotFoundException;
import org.example.services.impl.EleveServiceImpl;
import org.example.services.impl.ProfesseurServiceImpl;
import org.example.services.impl.UtilisateurServiceImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;

public class MenuPrincipal {

    Date date = new Date();
    Instant debutSession = Instant.now();
    Scanner scanner = new Scanner(System.in);

    // Ajouter une instance d'EleveDaoImpl et EleveServiceImpl
    EleveDaoImpl eleveDao = new EleveDaoImpl();
    EleveServiceImpl eleveService = new EleveServiceImpl(eleveDao);

    ProfesseurDaoImpl professeurDao = new ProfesseurDaoImpl();
    ProfesseurServiceImpl professeurService = new ProfesseurServiceImpl(professeurDao);

    UtilisateurServiceImpl utilisateurService = new UtilisateurServiceImpl();

    public void afficherMenu() {

        System.out.println("     * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" +
                "     " +
                "      BIENVENU DANS L’APPLICATION ETAB v1.2 \n" +
                "      * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" + "\n" +
                "  MENU: \n" + "    " +
                "1: Gestion des élèves \n" + "    " +
                "2: Gestion des professeurs \n" + "    " +
                "3: Gestion des utilisateurs \n" + "    " +
                "0: Quitter\n");

        System.out.println("Date système : " + date.getHours() + ":" + date.getMinutes() + "\n");

        // Utiliser MenuUtils pour gérer l'entrée utilisateur
        int choix = MenuNotFoundException.obtenirChoixUtilisateur(scanner, 3);

        switch (choix) {
            case 1:
                eleveService.afficherMenuGestionEleves();
                break;
            case 2:
                professeurService.afficherMenuGestionProfesseurs();
                break;
            case 3:
                utilisateurService.afficherMenu();
                break;
            case 0:
                finSession();
                System.exit(0);
                return;
            default:
                break;
        }
    }

    public void finSession() {
        Instant finSession = Instant.now();
        Duration duree = Duration.between(debutSession, finSession);

        long heures = duree.toHours();
        long minutes = duree.toMinutes() % 60;
        long secondes = duree.getSeconds() % 60;

        System.out.println("Merci d'avoir utilisé l'application ETAB. Au revoir !");
        System.out.println("Durée de la session : " + heures + " heures, " + minutes + " minutes, " + secondes + " secondes.");
    }
}

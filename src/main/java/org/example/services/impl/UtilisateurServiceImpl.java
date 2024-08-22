package org.example.services.impl;

import org.example.MenuPrincipal;
import org.example.dao.IUtilisateurDao;
import org.example.dao.impl.UtilisateurDaoImpl;
import org.example.exceptions.MenuNotFoundException;
import org.example.model.Utilisateur;
import org.example.services.IUtilisateurService;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;

public class UtilisateurServiceImpl implements IUtilisateurService {

    static Scanner scanner = new Scanner(System.in);
    private IUtilisateurDao utilisateurDao;


    public UtilisateurServiceImpl() {
        this.utilisateurDao = new UtilisateurDaoImpl();
    }

    @Override
    public boolean authentification(String identifiant, String motDePasse) throws SQLException {
        Utilisateur user = utilisateurDao.getUser(identifiant, motDePasse);
        return user != null;
    }

    @Override
    public boolean ajouterCompte(String identifiant, String motDePasse) {
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setIdentifiant(identifiant);
            utilisateur.setMotDePass(motDePasse);
            return utilisateurDao.saveUser(utilisateur);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifierMotDepass(String identifiant, String motDePasse) {
        try {
            Utilisateur utilisateur = utilisateurDao.updateUser(identifiant, motDePasse);
            return utilisateur != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean supprimerCompte(String identifiant, String motDePasse) {
        try {
            utilisateurDao.deleteUser(identifiant, motDePasse);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Utilisateur> listeUtilisateur() {
        return utilisateurDao.listeUtilisateur();
    }

    @Override
    public boolean saveUser(Utilisateur utilisateur) {
        return utilisateurDao.saveUser(utilisateur);
    }



    public static void afficherMenu() {
        Instant debutSession = Instant.now();  // Capturer l'instant du début de la session

        int choix;

        do {
            System.out.println("     * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" +
                    "     " +
                    "     GESTION DES UTILISATEURS \n" +
                    "      * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" + "\n\n" +

                    "  MENU: \n\n" + "    " +
                    "1: Ajouter un utilisateur \n" + "    " +
                    "2: Supprimer un utilisateur \n" + "    " +
                    "3: Modifier les informations d'utilisateur\n" + "    " +
                    "4: Lister les utilisateurs \n" + "    " +
                    "5: Retour \n" + "    " +
                    "0: Quitter\n");


            choix = MenuNotFoundException.obtenirChoixUtilisateur(scanner, 6);

            switch (choix) {
                case 1:
                    ajouterUtilisateur();
                    break;
                case 2:
                    supprimerUtilisateur();
                    break;
                case 3:
                    modifierUtilisateur();
                    break;
                case 4:
                    listerUtilisateurs();
                    break;
                case 5:
                    new MenuPrincipal().afficherMenu();
                    break;
                case 0:
                    System.out.println("Au revoir!");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        } while (choix != 0);
    }


    public static void ajouterUtilisateur() {
        System.out.print("ID : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Entrez l'identifiant de l'utilisateur : ");
        String identifiant = scanner.nextLine();
        System.out.print("Entrez le mot de passe de l'utilisateur : ");
        String motDePasse = scanner.nextLine();

        Utilisateur utilisateur = new Utilisateur(id,identifiant, motDePasse);
        IUtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
        boolean reussi = utilisateurDao.saveUser(utilisateur);

        if (reussi) {
            System.out.println("Utilisateur ajouté avec succès.");
        } else {
            System.out.println("Erreur lors de l'ajout de l'utilisateur.");
        }
    }

    public static void supprimerUtilisateur() {
        System.out.print("Entrez l'identifiant de l'utilisateur à supprimer : ");
        String identifiant = scanner.nextLine();
        System.out.print("Entrez le mot de passe correspondant : ");
        String motDePasse = scanner.nextLine();

        IUtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
        utilisateurDao.deleteUser(identifiant, motDePasse);

        System.out.println("Utilisateur supprimé.");
    }

    public static void modifierUtilisateur() {
        System.out.print("Entrez l'identifiant de l'utilisateur à modifier : ");
        String identifiant = scanner.nextLine();
        System.out.print("Entrez le nouveau mot de passe : ");
        String nouveauMotDePasse = scanner.nextLine();

        IUtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
        Utilisateur utilisateur = utilisateurDao.updateUser(identifiant, nouveauMotDePasse);

        if (utilisateur != null) {
            System.out.println("Utilisateur modifié avec succès.");
        } else {
            System.out.println("Erreur lors de la modification de l'utilisateur.");
        }
    }

    public static void listerUtilisateurs() {
        IUtilisateurDao utilisateurDao = new UtilisateurDaoImpl();
        List<Utilisateur> utilisateurs = utilisateurDao.listeUtilisateur();

        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            System.out.println("Liste des utilisateurs :");
            for (Utilisateur utilisateur : utilisateurs) {
                System.out.println("ID : " + utilisateur.getId() + ", Identifiant : " + utilisateur.getIdentifiant() + ", Mot de passe : " + utilisateur.getMotDePass());
            }
        }
    }
}
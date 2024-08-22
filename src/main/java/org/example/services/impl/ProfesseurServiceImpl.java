package org.example.services.impl;

import org.example.MenuPrincipal;
import org.example.dao.impl.ProfesseurDaoImpl;
import org.example.exceptions.MenuNotFoundException;
import org.example.model.Professeur;
import org.example.services.IProfesseurService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ProfesseurServiceImpl implements IProfesseurService {

    Instant debutSession = Instant.now();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ProfesseurDaoImpl professeurDaoImpl;

    public ProfesseurServiceImpl(ProfesseurDaoImpl professeurDaoImpl) {
        this.professeurDaoImpl = professeurDaoImpl;
    }

    @Override
    public Professeur save(Professeur professeur) {
        return professeurDaoImpl.ajouter(professeur);
    }

    @Override
    public Professeur update(Professeur professeur) {
        return professeurDaoImpl.modifier(professeur);
    }

    @Override
    public void delete(int id) {
        professeurDaoImpl.supprimer(id);
    }

    @Override
    public List<Professeur> getAll() {
        return professeurDaoImpl.obtenirProfesseurs();
    }

    @Override
    public Professeur getOne(int id) {
        return professeurDaoImpl.obtenir(id);
    }

    public void afficherMenuGestionProfesseurs() {
        int choix;

        do {
            System.out.println("     * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" +
                    "     " +
                    "      GESTION DES PROFESSEURS \n" +
                    "      * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" + "\n\n" +
                    "  MENU: \n\n" +
                    "    " +
                    "1: Ajouter un professeur \n" +
                    "    " +
                    "2: Supprimer un professeur \n" +
                    "    " +
                    "3: Modifier les informations du professeur\n" +
                    "    " +
                    "4: Lister les professeurs \n" +
                    "    " +
                    "5: Obtenir le dernier professeur ajouté \n" +
                    "    " +
                    "6: Retour \n" +
                    "    " +
                    "0: Quitter\n");

            choix = MenuNotFoundException.obtenirChoixUtilisateur(scanner, 6); // Assurez-vous que cette méthode existe

            switch (choix) {
                case 1:
                    ajouterProfesseur();
                    break;
                case 2:
                    supprimerProfesseur();
                    break;
                case 3:
                    modifierProfesseur();
                    break;
                case 4:
                    listerProfesseurs();
                    break;
                case 5:
                    obtenirDernierProfesseurAjoute();
                    break;
                case 6:
                    new MenuPrincipal().afficherMenu();
                    break;
                case 0:
                    finSession();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choix != 6);
    }

    private void ajouterProfesseur() {
        System.out.println("Veuillez entrer les informations du professeur");

        System.out.print("ID :");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consomme le retour à la ligne

        System.out.print("Nom : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();

        LocalDate dateNaissance = null;
        while (dateNaissance == null) {
            System.out.print("Date de naissance (ex: 23/12/1998) : ");
            String dateString = scanner.nextLine();
            try {
                dateNaissance = LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Format de date invalide. Veuillez entrer la date au format dd/MM/yyyy.");
            }
        }

        System.out.print("Ville : ");
        String ville = scanner.nextLine();

        System.out.print("Téléphone : ");
        String telephone = scanner.nextLine();

        System.out.print("Vacant : ");
        Boolean vacant = scanner.hasNext();
        scanner.nextLine();

        System.out.print("Matière enseignée : ");
        String matiereEnseigne = scanner.nextLine();

        System.out.print("Prochain cours : ");
        String prochainCours = scanner.nextLine();

        System.out.print("Sujet de la prochaine réunion : ");
        String sujetProchaineReunion = scanner.nextLine();

        Professeur nouveauProfesseur = new Professeur(id, dateNaissance, ville, prenom, nom, telephone,vacant,matiereEnseigne, prochainCours, sujetProchaineReunion);
        save(nouveauProfesseur);
    }

    private void supprimerProfesseur() {
        System.out.print("Entrez l'ID du professeur à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consomme le retour à la ligne

        delete(id);
    }

    private void modifierProfesseur() {
        System.out.print("Entrez l'ID du professeur à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consomme le retour à la ligne

        Professeur professeur = getOne(id);
        if (professeur != null) {
            System.out.println("Modifiez les informations du professeur (ID: " + id + ")");

            // Modification du nom
            System.out.print("Nouveau nom (laisser vide pour ne pas modifier) : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) {
                professeur.setNom(nom);
            }

            // Modification du prénom
            System.out.print("Nouveau prénom (laisser vide pour ne pas modifier) : ");
            String prenom = scanner.nextLine();
            if (!prenom.isEmpty()) {
                professeur.setPrenom(prenom);
            }

            // Modification de la date de naissance
            System.out.print("Nouvelle date de naissance (ex: 23/12/1998) (laisser vide pour ne pas modifier) : ");
            String dateString = scanner.nextLine();
            if (!dateString.isEmpty()) {
                try {
                    LocalDate dateNaissance = LocalDate.parse(dateString, formatter);
                    professeur.setDateNaissance(dateNaissance);
                } catch (DateTimeParseException e) {
                    System.out.println("Format de date invalide. Veuillez entrer la date au format dd/MM/yyyy.");
                }
            }

            // Modification de la ville
            System.out.print("Nouvelle ville (laisser vide pour ne pas modifier) : ");
            String ville = scanner.nextLine();
            if (!ville.isEmpty()) {
                professeur.setVille(ville);
            }

            // Modification du téléphone
            System.out.print("Nouveau téléphone (laisser vide pour ne pas modifier) : ");
            String telephone = scanner.nextLine();
            if (!telephone.isEmpty()) {
                professeur.setTelephone(telephone);
            }

            // Modification du statut vacant
            System.out.print("Statut vacant (true/false) (laisser vide pour ne pas modifier) : ");
            String statut = scanner.nextLine();
            if (!statut.isEmpty()) {
                try {
                    boolean vacant = Boolean.parseBoolean(statut);
                    professeur.setVacant(vacant);
                } catch (IllegalArgumentException e) {
                    System.out.println("Statut invalide. Veuillez entrer true ou false.");
                }
            }

            // Modification de la matière enseignée
            System.out.print("Nouvelle matière enseignée (laisser vide pour ne pas modifier) : ");
            String matiereEnseigne = scanner.nextLine();
            if (!matiereEnseigne.isEmpty()) {
                professeur.setMatiereEnseigne(matiereEnseigne);
            }

            // Modification du prochain cours
            System.out.print("Nouveau prochain cours (laisser vide pour ne pas modifier) : ");
            String prochainCours = scanner.nextLine();
            if (!prochainCours.isEmpty()) {
                professeur.setProchainCours(prochainCours);
            }

            // Modification du sujet de la prochaine réunion
            System.out.print("Nouveau sujet de la prochaine réunion (laisser vide pour ne pas modifier) : ");
            String sujetProchaineReunion = scanner.nextLine();
            if (!sujetProchaineReunion.isEmpty()) {
                professeur.setSujetProchaineReunion(sujetProchaineReunion);
            }

            // Mise à jour des informations du professeur
            update(professeur);
            System.out.println("Les informations du professeur ont été mises à jour avec succès.");
        } else {
            System.out.println("Aucun professeur trouvé avec l'ID spécifié.");
        }
    }



    private void listerProfesseurs() {
        List<Professeur> professeurs = getAll();
        if (professeurs.isEmpty()) {
            System.out.println("Aucun professeur n'est enregistré.");
        } else {
            for (Professeur professeur : professeurs) {
                System.out.println("ID: " + professeur.getId() + "\n" +
                        "Nom: " + professeur.getNom() + "\n" +
                        "Prénom: " + professeur.getPrenom() + "\n" +
                        "Matière enseignée: " + professeur.getMatiereEnseigne() + "\n" +
                        "Prochain cours: " + professeur.getProchainCours() + "\n" +
                        "Sujet de la prochaine réunion: " + professeur.getSujetProchaineReunion() + "\n");
            }
        }
    }

    private void obtenirDernierProfesseurAjoute() {
        List<Professeur> professeurs = getAll();
        if (professeurs.isEmpty()) {
            System.out.println("Aucun professeur n'est enregistré.");
        } else {
            Professeur dernierProfesseur = professeurs.get(professeurs.size() - 1);
            System.out.println("Dernier professeur ajouté : \n" +
                    "        ID: " + dernierProfesseur.getId() + "\n" +
                    "        Nom: " + dernierProfesseur.getNom() + "\n" +
                    "        Prénom: " + dernierProfesseur.getPrenom() + "\n" +
                    "        Matière enseignée: " + dernierProfesseur.getMatiereEnseigne() + "\n" +
                    "        Prochain cours: " + dernierProfesseur.getProchainCours() + "\n" +
                    "        Sujet de la prochaine réunion: " + dernierProfesseur.getSujetProchaineReunion() + "\n");
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

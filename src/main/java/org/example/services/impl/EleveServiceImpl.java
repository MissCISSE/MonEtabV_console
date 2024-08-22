package org.example.services.impl;

import org.example.MenuPrincipal;
import org.example.dao.impl.EleveDaoImpl;
import org.example.exceptions.MenuNotFoundException;
import org.example.model.Eleve;
import org.example.services.IEleveService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class EleveServiceImpl implements IEleveService {

    Instant debutSession = Instant.now();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final EleveDaoImpl eleveDaoImpl;

    public EleveServiceImpl(EleveDaoImpl eleveDaoImpl) {
        this.eleveDaoImpl = eleveDaoImpl;
    }

    @Override
    public Eleve save(Eleve eleve) {
        return eleveDaoImpl.ajouter(eleve);
    }

    @Override
    public Eleve update(Eleve eleve) {
        return eleveDaoImpl.modifier(eleve);
    }



    public void afficherMenuGestionEleves() {
        int choix;

        do {
            System.out.println("     * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" +
                    "     " +
                    "      GESTION DES ELEVES \n" +
                    "      * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" + "\n\n" +
                    "  MENU: \n\n" +
                    "    " +
                    "1: Ajouter un élève \n" +
                    "    " +
                    "2: Supprimer un élève \n" +
                    "    " +
                    "3: Modifier les informations de l'élève\n" +
                    "    " +
                    "4: Lister les élèves \n" +
                    "    " +
                    "5: Obtenir le dernier élève ajouté \n" +
                    "    " +
                    "6: Retour \n" +
                    "    " +
                    "0: Quitter\n");

            choix = MenuNotFoundException.obtenirChoixUtilisateur(scanner, 6); // Assurez-vous que cette méthode existe

            switch (choix) {
                case 1:
                    ajouterEleve();
                    break;
                case 2:
                    supprimerEleve();
                    break;
                case 3:
                    modifierEleve();
                    break;
                case 4:
                    listerEleves();
                    break;
                case 5:
                    obtenirDernierEleveAjoute();
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

    private void ajouterEleve() {
        System.out.println("Veuillez entrer les informations de l'élève");

        System.out.print("ID : ");
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

        System.out.print("Classe : ");
        String classe = scanner.nextLine();

        System.out.print("Téléphone : ");
        String telephone = scanner.nextLine();

        System.out.print("Matricule : ");
        String matricule = scanner.nextLine();

        Eleve nouvelEleve = new Eleve(id, dateNaissance, ville, prenom, nom, telephone, classe, matricule);
        save(nouvelEleve);
    }

    private void supprimerEleve() {
        System.out.print("Entrez l'ID de l'élève à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consomme le retour à la ligne

        delete(id);
    }

    private void modifierEleve() {
        System.out.print("Entrez l'ID de l'élève à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consomme le retour à la ligne

        Eleve eleve = getOne(id);
        if (eleve != null) {
            System.out.println("Modifiez les informations de l'élève (ID: " + id + ")");

            // Modification du nom
            System.out.print("Nouveau nom (laisser vide pour ne pas modifier) : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) {
                eleve.setNom(nom);
            }

            // Modification du prénom
            System.out.print("Nouveau prénom (laisser vide pour ne pas modifier) : ");
            String prenom = scanner.nextLine();
            if (!prenom.isEmpty()) {
                eleve.setPrenom(prenom);
            }

            // Modification de la date de naissance
            LocalDate dateNaissance = null;
            while (dateNaissance == null) {
                System.out.print("Nouvelle date de naissance (ex: 23/12/1998) (laisser vide pour ne pas modifier) : ");
                String dateString = scanner.nextLine();
                if (dateString.isEmpty()) {
                    break;
                }
                try {
                    dateNaissance = LocalDate.parse(dateString, formatter);
                    eleve.setDateNaissance(dateNaissance);
                } catch (DateTimeParseException e) {
                    System.out.println("Format de date invalide. Veuillez entrer la date au format dd/MM/yyyy.");
                }
            }

            // Modification de la ville
            System.out.print("Nouvelle ville (laisser vide pour ne pas modifier) : ");
            String ville = scanner.nextLine();
            if (!ville.isEmpty()) {
                eleve.setVille(ville);
            }

            // Modification du téléphone
            System.out.print("Nouveau téléphone (laisser vide pour ne pas modifier) : ");
            String telephone = scanner.nextLine();
            if (!telephone.isEmpty()) {
                eleve.setTelephone(telephone);
            }

            // Modification de la classe
            System.out.print("Nouvelle classe (laisser vide pour ne pas modifier) : ");
            String classe = scanner.nextLine();
            if (!classe.isEmpty()) {
                eleve.setClasse(classe);
            }

            // Mise à jour des informations de l'élève
            update(eleve);
            System.out.println("Les informations de l'élève ont été mises à jour avec succès.");
        } else {
            System.out.println("Aucun élève trouvé avec l'ID spécifié.");
        }
    }


    private void listerEleves() {
        List<Eleve> eleves = getAll();
        if (eleves.isEmpty()) {
            System.out.println("Aucun élève n'est enregistré.");
        } else {
            for (Eleve eleve : eleves) {
                System.out.println("ID: " + eleve.getId() + "\n" +
                        "Nom: " + eleve.getNom() + "\n" +
                        "Prénom: " + eleve.getPrenom() + "\n" +
                        "Classe: " + eleve.getClasse() + "\n" +
                        "Ville: " + eleve.getVille() + "\n" +
                        "Date de Naissance: " + eleve.getDateNaissance() + "\n");
            }
        }
    }

    private void obtenirDernierEleveAjoute() {
        List<Eleve> eleves = getAll();
        if (eleves.isEmpty()) {
            System.out.println("Aucun élève n'est enregistré.");
        } else {
            Eleve dernierEleve = eleves.get(eleves.size() - 1);
            System.out.println("Dernier élève ajouté : \n" +
                    "        ID: " + dernierEleve.getId() + "\n" +
                    "        Nom: " + dernierEleve.getNom() + "\n" +
                    "        Prénom: " + dernierEleve.getPrenom() + "\n" +
                    "        Classe: " + dernierEleve.getClasse() + "\n" +
                    "        Ville: " + dernierEleve.getVille() + "\n" +
                    "        Date de Naissance: " + dernierEleve.getDateNaissance() + "\n");
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



    @Override
    public void delete(int id) {
        eleveDaoImpl.supprimer(id);
    }

    @Override
    public List<Eleve> getAll() {
        return eleveDaoImpl.obtenirEleves();
    }

    @Override
    public Eleve getOne(int id) {
        return eleveDaoImpl.obtenir(id);
    }
}

package org.example.dao.impl;

import org.example.dao.IUtilisateurDao;
import org.example.dao.SingletonDataBase;
import org.example.model.Utilisateur;
import org.example.services.IUtilisateurService;
import org.example.services.impl.UtilisateurServiceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtilisateurDaoImpl implements IUtilisateurDao {
    static Scanner scanner = new Scanner(System.in);


    @Override
    public Utilisateur updateUser(String identifiant, String motDePasse) {
        Connection connection = null;
        Utilisateur utilisateur = null;
        String query = "UPDATE utilisateur SET motDePasse = ? WHERE pseudo = ?";
        try {
            connection = SingletonDataBase.getInstance();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La connexion à la base de données est fermée ou non disponible.");
            }
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, motDePasse);
                statement.setString(2, identifiant);
                int affectedRows = statement.executeUpdate();
                if (affectedRows > 0) {
                    utilisateur = new Utilisateur(); // Récupérez les détails mis à jour si nécessaire
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateur;
    }

    @Override
    public void deleteUser(String identifiant, String motDePasse) {
        Connection connection = null;
        String query = "DELETE FROM utilisateur WHERE pseudo = ? AND motDePasse = ?";
        try {
            connection = SingletonDataBase.getInstance();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La connexion à la base de données est fermée ou non disponible.");
            }
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, identifiant);
                statement.setString(2, motDePasse);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Utilisateur> listeUtilisateur() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";
        try (Connection connection = SingletonDataBase.getInstance();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String pseudo = resultSet.getString("pseudo");
                String motDePass = resultSet.getString("motDePasse");
                utilisateurs.add(new Utilisateur(id, pseudo, motDePass));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }




    private static final String QUERY_GET_USER_BY_PSEUDO_AND_PASSWORD = "SELECT * FROM utilisateur WHERE pseudo = ? AND motDePasse = ?";



    @Override
    public boolean saveUser(Utilisateur utilisateur) {
        Connection connection = null;
        String query = "INSERT INTO utilisateur (pseudo, motDePasse) VALUES (?, ?)";
        try {
            connection = SingletonDataBase.getInstance();
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La connexion à la base de données est fermée ou non disponible.");
            }
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, utilisateur.getIdentifiant());
                statement.setString(2, utilisateur.getMotDePass());
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Utilisateur getUser(String identifiant, String motDePasse) throws SQLException {
        Connection connection = SingletonDataBase.getInstance();

        if (connection == null || connection.isClosed()) {
            System.out.println("Tentative de réouverture de la connexion...");
            connection = SingletonDataBase.getInstance(); // Tentative de réouverture
            if (connection == null || connection.isClosed()) {
                throw new SQLException("La connexion à la base de données est fermée ou non disponible.");
            }
        }

        Utilisateur utilisateur = null;

        try (PreparedStatement statement = connection.prepareStatement(QUERY_GET_USER_BY_PSEUDO_AND_PASSWORD)) {
            statement.setString(1, identifiant);
            statement.setString(2, motDePasse);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String pseudo = resultSet.getString("pseudo");
                    String motDePass = resultSet.getString("motDePasse");
                    utilisateur = new Utilisateur(id, pseudo, motDePass);
                }
            }
        }

        return utilisateur;
    }


    /**
     * Permet à un utilisateur de se connecter en fournissant un identifiant et un mot de passe.
     * La méthode vérifie les identifiants et informe l'utilisateur si la connexion est réussie ou non.
     *
     * @return {@code true} si les identifiants sont corrects, {@code false} sinon.
     * @throws SQLException si une erreur SQL se produit lors de l'authentification.
     */
    public boolean Authentification() throws SQLException {
        boolean auth = false;
        System.out.println("     * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n" +
                "     " +
                "      BIENVENU DANS L’APPLICATION ETAB v1.2 \n" +
                "      * * * * * * * * * * * * * * * * * * * * * * * * * * * * \n\n" +
                "CONNEXION\n");

        while (!auth) {  // Boucle jusqu'à ce que l'authentification soit réussie
            System.out.print("Identifiant : ");
            String username = scanner.nextLine();
            System.out.print("Mot de passe : ");
            String password = scanner.nextLine();
            IUtilisateurService utilisateurService = new UtilisateurServiceImpl();
            auth = utilisateurService.authentification(username, password);

            if (auth) {
                System.out.println("Connexion réussie ! \n\n");
            } else {
                System.out.println("Identifiant ou Mot de passe incorrect. Veuillez réessayer.\n\n");
            }
        }
        return auth;
    }

    private static final String QUERY_VERIFIER_UTILISATEUR = "SELECT COUNT(*) FROM Utilisateur WHERE pseudo = ?";
    private static final String QUERY_AJOUTER_UTILISATEUR = "INSERT INTO Utilisateur (pseudo, motDePasse, dateCreation) VALUES (?, ?, NOW())";

    /**
     * Initialise la base de données en vérifiant l'existence de l'utilisateur 'admin'.
     * Si l'utilisateur 'admin' n'existe pas, il est ajouté à la base de données.
     */
    public static void initialiser() {
        try (Connection connexion = SingletonDataBase.getInstance()) {

            // Vérifier si l'utilisateur existe déjà
            try (PreparedStatement instructionVerifier = connexion.prepareStatement(QUERY_VERIFIER_UTILISATEUR)) {
                instructionVerifier.setString(1, "admin");

                try (ResultSet resultat = instructionVerifier.executeQuery()) {
                    if (resultat.next() && resultat.getInt(1) > 0) {
                        System.out.println("Initialisation de utilisation succes !!.");
                        return;
                    }
                }
            }

            // Ajouter l'utilisateur si non existant
            try (PreparedStatement instructionAjouter = connexion.prepareStatement(QUERY_AJOUTER_UTILISATEUR)) {
                instructionAjouter.setString(1, "admin");
                instructionAjouter.setString(2, "admin");
                instructionAjouter.executeUpdate();
                System.out.println("Utilisateur 'admin' ajouté avec succès.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

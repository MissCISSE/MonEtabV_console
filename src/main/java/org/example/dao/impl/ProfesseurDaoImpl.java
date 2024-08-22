package org.example.dao.impl;

import org.example.dao.IProfesseurDAO;
import org.example.dao.SingletonDataBase;
import org.example.model.Professeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesseurDaoImpl implements IProfesseurDAO {

    private final Connection connection;

    public ProfesseurDaoImpl() {
        this.connection = SingletonDataBase.getInstance();
    }

    @Override
    public Professeur ajouter(Professeur professeur) {
        String insertPersonneSQL = "INSERT INTO personne (nom, prenom, date_naissance, ville, telephone) VALUES (?, ?, ?, ?, ?)";
        String insertProfesseurSQL = "INSERT INTO professeur (id, vacant, matiere_enseigne, prochain_cours, sujet_prochaine_reunion) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement personneStatement = connection.prepareStatement(insertPersonneSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            personneStatement.setString(1, professeur.getNom());
            personneStatement.setString(2, professeur.getPrenom());
            personneStatement.setDate(3, Date.valueOf(professeur.getDateNaissance()));
            personneStatement.setString(4, professeur.getVille());
            personneStatement.setString(5, professeur.getTelephone());

            personneStatement.executeUpdate();

            ResultSet generatedKeys = personneStatement.getGeneratedKeys();
            int personneId = 0;
            if (generatedKeys.next()) {
                personneId = generatedKeys.getInt(1);
            }

            try (PreparedStatement professeurStatement = connection.prepareStatement(insertProfesseurSQL)) {
                professeurStatement.setInt(1, personneId);
                professeurStatement.setBoolean(2, professeur.isVacant());
                professeurStatement.setString(3, professeur.getMatiereEnseigne());
                professeurStatement.setString(4, professeur.getProchainCours());
                professeurStatement.setString(5, professeur.getSujetProchaineReunion());

                professeurStatement.executeUpdate();
                System.out.println("Professeur ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du professeur : " + e.getMessage());
        }
        return professeur;
    }

    @Override
    public Professeur modifier(Professeur professeur) {
        String query = "UPDATE personne SET nom=?, prenom=?, date_naissance=?, ville=?, telephone=? WHERE id=?";
        String queryProfesseur = "UPDATE professeur SET vacant=?, matiere_enseigne=?, prochain_cours=?, sujet_prochaine_reunion=? WHERE id=?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, professeur.getNom());
                statement.setString(2, professeur.getPrenom());
                statement.setDate(3, Date.valueOf(professeur.getDateNaissance()));
                statement.setString(4, professeur.getVille());
                statement.setString(5, professeur.getTelephone());
                statement.setInt(6, professeur.getId());
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    try (PreparedStatement statementProfesseur = connection.prepareStatement(queryProfesseur)) {
                        statementProfesseur.setBoolean(1, professeur.isVacant());
                        statementProfesseur.setString(2, professeur.getMatiereEnseigne());
                        statementProfesseur.setString(3, professeur.getProchainCours());
                        statementProfesseur.setString(4, professeur.getSujetProchaineReunion());
                        statementProfesseur.setInt(5, professeur.getId());

                        statementProfesseur.executeUpdate();
                        connection.commit();
                        System.out.println("Professeur modifié avec succès !");
                    }
                } else {
                    connection.rollback();
                    System.out.println("Aucun professeur trouvé avec cet ID.");
                }
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Erreur lors de l'annulation de la transaction : " + rollbackEx.getMessage());
            }
            System.out.println("Erreur lors de la modification du professeur : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Erreur lors du rétablissement de l'auto-commit : " + e.getMessage());
            }
        }
        return professeur;
    }

    @Override
    public void supprimer(int id) {
        String deleteProfesseurSQL = "DELETE FROM professeur WHERE id = ?";
        String deletePersonneSQL = "DELETE FROM personne WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteProfesseurStmt = connection.prepareStatement(deleteProfesseurSQL)) {
                deleteProfesseurStmt.setInt(1, id);
                int rowsDeletedProfesseur = deleteProfesseurStmt.executeUpdate();

                if (rowsDeletedProfesseur > 0) {
                    try (PreparedStatement deletePersonneStmt = connection.prepareStatement(deletePersonneSQL)) {
                        deletePersonneStmt.setInt(1, id);
                        int rowsDeletedPersonne = deletePersonneStmt.executeUpdate();

                        if (rowsDeletedPersonne > 0) {
                            connection.commit();
                            System.out.println("Professeur supprimé avec succès !");
                        } else {
                            connection.rollback();
                            System.out.println("Erreur lors de la suppression du professeur dans la table personne.");
                        }
                    }
                } else {
                    connection.rollback();
                    System.out.println("Aucun professeur trouvé avec cet ID.");
                }
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Erreur lors de la suppression du professeur : " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion de la transaction : " + e.getMessage());
        }
    }

    @Override
    public List<Professeur> obtenirProfesseurs() {
        List<Professeur> professeurs = new ArrayList<>();
        String query = "SELECT p.id, p.nom, p.prenom, p.date_naissance, p.ville, p.telephone, pr.vacant, pr.matiere_enseigne, pr.prochain_cours, pr.sujet_prochaine_reunion FROM personne p INNER JOIN professeur pr ON p.id = pr.id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Professeur professeur = new Professeur(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_naissance").toLocalDate(),
                        resultSet.getString("ville"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("telephone"),
                        resultSet.getBoolean("vacant"),
                        resultSet.getString("matiere_enseigne"),
                        resultSet.getString("prochain_cours"),
                        resultSet.getString("sujet_prochaine_reunion")
                );
                professeurs.add(professeur);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des professeurs : " + e.getMessage());
        }
        return professeurs;
    }

    @Override
    public Professeur obtenir(int id) {
        String query = "SELECT p.id, p.nom, p.prenom, p.date_naissance, p.ville, p.telephone, pr.vacant, pr.matiere_enseigne, pr.prochain_cours, pr.sujet_prochaine_reunion FROM personne p INNER JOIN professeur pr ON p.id = pr.id WHERE p.id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Professeur(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_naissance").toLocalDate(),
                        resultSet.getString("ville"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("telephone"),
                        resultSet.getBoolean("vacant"),
                        resultSet.getString("matiere_enseigne"),
                        resultSet.getString("prochain_cours"),
                        resultSet.getString("sujet_prochaine_reunion")
                );
            } else {
                System.out.println("Aucun professeur trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du professeur : " + e.getMessage());
        }
        return null;
    }
}

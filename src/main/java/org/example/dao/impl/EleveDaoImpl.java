package org.example.dao.impl;

import org.example.dao.IEleveDAO;
import org.example.dao.SingletonDataBase;
import org.example.model.Eleve;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EleveDaoImpl implements IEleveDAO {


    private final Connection connection;

    public EleveDaoImpl() {
        this.connection = SingletonDataBase.getInstance();
    }

    @Override
    public Eleve ajouter(Eleve eleve) {
        String insertPersonneSQL = "INSERT INTO personne (nom, prenom, date_naissance, ville, telephone) VALUES (?, ?, ?, ?, ?)";
        String insertEleveSQL = "INSERT INTO eleve (id, classe, matricule) VALUES (?, ?, ?)";

        try (PreparedStatement personneStatement = connection.prepareStatement(insertPersonneSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            personneStatement.setString(1, eleve.getNom());
            personneStatement.setString(2, eleve.getPrenom());
            personneStatement.setDate(3, Date.valueOf(eleve.getDateNaissance()));
            personneStatement.setString(4, eleve.getVille());
            personneStatement.setString(5, eleve.getTelephone());

            personneStatement.executeUpdate();

            // Récupération de l'ID généré pour l'insertion dans la table eleve
            ResultSet generatedKeys = personneStatement.getGeneratedKeys();
            int personneId = 0;
            if (generatedKeys.next()) {
                personneId = generatedKeys.getInt(1);
            }

            try (PreparedStatement eleveStatement = connection.prepareStatement(insertEleveSQL)) {
                eleveStatement.setInt(1, personneId);
                eleveStatement.setString(2, eleve.getClasse());
                eleveStatement.setString(3, eleve.getMatricule());

                eleveStatement.executeUpdate();
                System.out.println("Élève ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'élève : " + e.getMessage());
        }
        return eleve;
    }

    @Override
    public Eleve modifier(Eleve eleve) {
        String query = "UPDATE personne SET nom=?, prenom=?, date_naissance=?, ville=?, telephone=? WHERE id=?";
        String queryEleve = "UPDATE eleve SET classe=?, matricule=? WHERE id=?";

        try {
            connection.setAutoCommit(false); // Commencer la transaction

            // Mettre à jour la table personne
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, eleve.getNom());
                statement.setString(2, eleve.getPrenom());
                statement.setDate(3, Date.valueOf(eleve.getDateNaissance()));
                statement.setString(4, eleve.getVille());
                statement.setString(5, eleve.getTelephone());
                statement.setInt(6, eleve.getId());
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    // Mettre à jour la table eleve
                    try (PreparedStatement statementEleve = connection.prepareStatement(queryEleve)) {
                        statementEleve.setString(1, eleve.getClasse());
                        statementEleve.setString(2, eleve.getMatricule());
                        statementEleve.setInt(3, eleve.getId());
                        statementEleve.executeUpdate();

                        connection.commit(); // Valider la transaction
                        System.out.println("Élève modifié avec succès !");
                    }
                } else {
                    connection.rollback(); // Annuler la transaction si l'ID n'existe pas
                    System.out.println("Aucun élève trouvé avec cet ID.");
                }
            }

        } catch (SQLException e) {
            try {
                connection.rollback(); // Annuler la transaction en cas d'erreur
            } catch (SQLException rollbackEx) {
                System.out.println("Erreur lors de l'annulation de la transaction : " + rollbackEx.getMessage());
            }
            System.out.println("Erreur lors de la modification de l'élève : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true); // Remettre l'auto-commit à true
            } catch (SQLException e) {
                System.out.println("Erreur lors du rétablissement de l'auto-commit : " + e.getMessage());
            }
        }
        return eleve;
    }

    @Override
    public void supprimer(int id) {
        String deleteEleveSQL = "DELETE FROM eleve WHERE id = ?";
        String deletePersonneSQL = "DELETE FROM personne WHERE id = ?";

        try {
            connection.setAutoCommit(false); // Commencer la transaction

            try (PreparedStatement deleteEleveStmt = connection.prepareStatement(deleteEleveSQL)) {
                deleteEleveStmt.setInt(1, id);
                int rowsDeletedEleve = deleteEleveStmt.executeUpdate();

                if (rowsDeletedEleve > 0) {
                    try (PreparedStatement deletePersonneStmt = connection.prepareStatement(deletePersonneSQL)) {
                        deletePersonneStmt.setInt(1, id);
                        int rowsDeletedPersonne = deletePersonneStmt.executeUpdate();

                        if (rowsDeletedPersonne > 0) {
                            connection.commit(); // Valider la transaction
                            System.out.println("Élève supprimé avec succès !");
                        } else {
                            connection.rollback(); // Annuler la transaction si l'ID n'existe pas dans la table personne
                            System.out.println("Erreur lors de la suppression de l'élève dans la table personne.");
                        }
                    }
                } else {
                    connection.rollback(); // Annuler la transaction si l'ID n'existe pas dans la table eleve
                    System.out.println("Aucun élève trouvé avec cet ID dans la table eleve.");
                }
            } catch (SQLException e) {
                connection.rollback(); // Annuler la transaction en cas d'erreur
                System.out.println("Erreur lors de la suppression de l'élève : " + e.getMessage());
            } finally {
                connection.setAutoCommit(true); // Remettre l'auto-commit à true
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la gestion de la transaction : " + e.getMessage());
        }
    }

    @Override
    public List<Eleve> obtenirEleves() {
        List<Eleve> eleves = new ArrayList<>();
        String query = "SELECT p.id, p.nom, p.prenom, p.date_naissance, p.ville, p.telephone, e.classe, e.matricule FROM personne p INNER JOIN eleve e ON p.id = e.id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Eleve eleve = new Eleve(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_naissance").toLocalDate(),
                        resultSet.getString("ville"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("telephone"),
                        resultSet.getString("classe"),
                        resultSet.getString("matricule")
                );
                eleves.add(eleve);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des élèves : " + e.getMessage());
        }
        return eleves;
    }

    @Override
    public Eleve obtenir(int id) {
        String query = "SELECT p.id, p.nom, p.prenom, p.date_naissance, p.ville, p.telephone, e.classe, e.matricule FROM personne p INNER JOIN eleve e ON p.id = e.id WHERE p.id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Eleve(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_naissance").toLocalDate(),
                        resultSet.getString("ville"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("telephone"),
                        resultSet.getString("classe"),
                        resultSet.getString("matricule")
                );
            } else {
                System.out.println("Aucun élève trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'élève : " + e.getMessage());
        }
        return null;
    }

}
package org.example.dao;

import org.example.model.Professeur;

import java.util.List;

public interface IProfesseurDAO {
    Professeur ajouter(Professeur professeur);
    Professeur modifier(Professeur professeur);
    void supprimer(int id);
    List<Professeur> obtenirProfesseurs();
    Professeur obtenir(int id);
}

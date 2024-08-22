package org.example.dao;

import org.example.model.Eleve;

import java.util.List;

public interface IEleveDAO {
    Eleve ajouter(Eleve eleve);
    Eleve modifier(Eleve eleve);
    void supprimer(int id);
    List<Eleve> obtenirEleves();
    Eleve obtenir(int id);
}

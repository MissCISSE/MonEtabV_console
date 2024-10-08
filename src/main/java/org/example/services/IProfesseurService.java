package org.example.services;

import org.example.model.Professeur;

import java.util.List;

public interface IProfesseurService {

    Professeur save(Professeur professeur);
    Professeur update(Professeur professeur);
    void delete(int id);
    List<Professeur> getAll();
    Professeur getOne(int id);
}


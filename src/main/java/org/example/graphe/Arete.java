package org.example.graphe;

import java.util.Objects;

public class Arete {
    private int id;
    private static int compteur = 1;
    private Station sommet1;
    private Station sommet2;
    private String nomMetro;
    private Integer tempsEnSecondes;
    private String ligne;

    /**
     * Créer une arete à partir des informations fournies et lie celle-ci à son Station de départ et à son Station de sommet2
     * @param sommet1 : son Station de départ
     * @param sommet2 : son Station de sommet2
     * @param tempsEnSecondes : tempsEnSecondes de l'arete
     */
    public Arete(Station sommet1, Station sommet2, Integer tempsEnSecondes, String ligne) {
        this.id = compteur;
        compteur++;
        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
        this.tempsEnSecondes = tempsEnSecondes;
        this.sommet2.ajouterArete(this);
        this.sommet1.ajouterArete(this);
        this.ligne = ligne;
    }

    /**
     * Créer une arete à partir des informations fournies et lie celle-ci à son Station de départ et à son Station de sommet2
     * @param sommet1 : son Station de départ
     * @param sommet2 : son Station de sommet2
     */
    public Arete(Station sommet1, Station sommet2, String nomMetro) {
        this.id = compteur;
        compteur++;
        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
        this.nomMetro = nomMetro;
        this.tempsEnSecondes = 0;
        this.sommet2.ajouterArete(this);
        this.sommet1.ajouterArete(this);
    }

    public Arete(Station sommet1, Station sommet2) {
        this.id = compteur;
        compteur++;
        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
        this.sommet2.ajouterArete(this);
        this.sommet1.ajouterArete(this);
    }

    public String getLigne() {
        return ligne;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomMetro() {
        return nomMetro;
    }

    public Station getSommet1() {
        return sommet1;
    }

    public void setSommet1(Station sommet1) {
        this.sommet1 = sommet1;
    }

    public Station getSommet2() {
        return sommet2;
    }

    public void setSommet2(Station sommet2) {
        this.sommet2 = sommet2;
    }


    public void setNomMetro(String nomMetro) {
        this.nomMetro = nomMetro;
    }

    public Integer getTempsEnSecondes() {
        return tempsEnSecondes;
    }


    public void setTempsEnSecondes(Integer tempsEnSecondes) {
        this.tempsEnSecondes = tempsEnSecondes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Arete other = (Arete) obj;
        return id == other.id;
    }

    public String toString() {
        return "(" + this.getSommet1().getNom() + ")" + "-" + "(" + this.getSommet2().getNom()   + ")[métro " + this.getLigne()+ "]";


    }
}
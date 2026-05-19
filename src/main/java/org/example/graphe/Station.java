package org.example.graphe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class Station {
        
    private String nom;
        private ArrayList<Arete> aretes;
        private ArrayList<Integer> numero;
        private String couleur;
        private boolean marquer;
        private String lignes; // lignes est un string à cause de la ligne 7bis
        private boolean si_terminus;
        private int branchement;

        public Station(String nom,int numero, boolean si_terminus,  int branchement, String lignes) {
            this.nom = nom;
            this.numero = new ArrayList<Integer>();
            this.numero.add(numero) ;
            this.lignes = lignes;
            this.si_terminus = si_terminus;
            this.branchement = branchement;
            this.marquer = false;
            this.aretes = new ArrayList<Arete>();
        }

        public Station(String nom) {
            this.nom = nom;
        }

    public boolean isSi_terminus() {
        return si_terminus;
    }

    public ArrayList<Integer> getNumero() {
        return numero;
    }

    public String getLignes() {
        return lignes;
    }

    public int getBranchement() {
        return branchement;
    }

    public void setNumero(ArrayList<Integer> numero) {
        this.numero = numero;
    }

    public void setLignes(String lignes) {
        this.lignes = lignes;
    }

    public void addLignes(String lignes) {
        this.lignes = this.lignes + ", " + lignes;
    }

    public void setSi_terminus(boolean si_terminus) {
        this.si_terminus = si_terminus;
    }

    public void setBranchement(int branchement) {
        this.branchement = branchement;
    }

    /**
         * Renvoie la liste des voisin d'un Station par ordre lexicographique
         * @return la liste des voisins
         */
        public ArrayList<Station> getVoisins(){
            ArrayList<Station> voisins = new ArrayList<Station>();
            for (Arete arete : this.aretes) {
                if(!arete.getSommet1().getNom().equals(this.getNom())) {
                    voisins.add(arete.getSommet1());
                }
                else if(!arete.getSommet2().getNom().equals(this.getNom())) {
                    voisins.add(arete.getSommet2());
                }
            }
            voisins.sort(Comparator.comparing(Station -> Station.getNom()));
            return voisins;
        }

    public ArrayList<Arete> getAretes(){
        ArrayList<Arete> aretes = new ArrayList<Arete>();
        for (Arete arete : this.aretes) {
            if(!arete.getSommet1().getNom().equals(this.getNom())) {
                aretes.add(arete);
            }
            else if(!arete.getSommet2().getNom().equals(this.getNom())) {
                aretes.add(arete);
            }
        }
        return aretes;
    }

        public String getLigneVoisin(String nom, String nom2) {
            return (this.aretes.stream()
                    .filter(arete -> arete.getSommet1().getNom().equals(nom) || arete.getSommet2().getNom().equals(nom))
                    .findFirst().get().getLigne());
        }


        public boolean isMarquer() {
            return marquer;
        }

        public void setMarquer(boolean marquer) {
            this.marquer = marquer;
        }

        public String getNom() {
            return nom;
        }



        public void setNom(String nom) {
            this.nom = nom;
        }


        /**
         * Renvoie la liste des arêtes d'un Station par ordre de création
         * @return la liste des arêtes
         */

        /**
         * Renvoie la liste des arêtes d'un Station 
         * @return la liste des arêtes
         */
        public ArrayList<Arete> getAretesSansDoublons(){
            return (ArrayList<Arete>) this.getAretes().stream().distinct().collect(Collectors.toList());
        }



        public void setAretes(ArrayList<Arete> aretes) {
            this.aretes = aretes;
        }



        public String getCouleur() {
            return couleur;
        }



        public void setCouleur(String couleur) {
            this.couleur = couleur;
        }




        public void ajouterArete( Arete arete){
            this.aretes.add(arete);
        }

        public void supprimerArete( Arete arete){
            this.aretes.remove(arete);
        }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(nom, station.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nom);
    }

    @Override
    public String toString() {
        return this.getNom();

    }






    
}

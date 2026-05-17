package org.example.graphe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class Station {
        
    private String nom;
        private ArrayList<Arete> aretes;
        private int numero;
        private String couleur;
        private boolean marquer;
        private Integer[] lignes;
        private boolean si_terminus;
        private int branchement;

        public Station(String nom,int numero, boolean si_terminus,  int branchement, Integer... lignes) {
            this.nom = nom;
            this.numero = numero;
            this.lignes = lignes;
            this.si_terminus = si_terminus;
            this.branchement = branchement;
            this.marquer = false;
            this.aretes = new ArrayList<Arete>();
        }


        /**
         * Renvoie la liste des voisin d'un Station par ordre lexicographique
         * @return la liste des voisins
         */
        public ArrayList<Station> getVoisins(){
            ArrayList<Station> voisins = new ArrayList<Station>();
            for (Arete arete : this.aretes) {
                if(arete.getSommet1() != this) {
                    voisins.add(arete.getSommet1());
                }
                else {
                    voisins.add(arete.getSommet2());
                }
            }
            voisins.sort(Comparator.comparing(Station -> Station.getNom()));
            return voisins;
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
        public ArrayList<Arete> getAretes() {
            return (ArrayList<Arete>) this.aretes.stream().sorted(Comparator.comparingInt(arete -> arete.getId())).collect(Collectors.toList());
        }

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
        return numero == station.numero && marquer == station.marquer && si_terminus == station.si_terminus && branchement == station.branchement && Objects.equals(nom, station.nom) && Objects.equals(aretes, station.aretes) && Objects.equals(couleur, station.couleur) && Objects.deepEquals(lignes, station.lignes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, aretes, numero, couleur, marquer, Arrays.hashCode(lignes), si_terminus, branchement);
    }

    @Override
        public String toString() {
            return "Station [nom=" + nom ;
        }






    
}

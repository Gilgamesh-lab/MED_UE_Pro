package org.example.graphe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

public class Graphe {
    private ArrayList<Station> stations;
    private ArrayList<String> lignes;

    public Graphe() {
        this.stations = new ArrayList<>();
        this.lignes = new ArrayList<>();
        this.loadCsv("/sommets.csv");
        this.loadCsv("/aretes.csv");
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    private void loadAretes(String[] parts) {
        final int num_sommet1 = Integer.parseInt(parts[0]);
        final int num_sommet2 = Integer.parseInt(parts[1]);
        final int temps_en_secondes = Integer.parseInt(parts[2]);
        new Arete(findStation(num_sommet1), findStation(num_sommet2), temps_en_secondes, this.lignes.get(num_sommet1));
    }

    private void loadStations(String[] parts) {
        final int num_sommet = Integer.parseInt(parts[0].strip());
        final String nom_sommet = parts[1].strip();
        final String numero_ligne = parts[2].strip();
        final boolean si_terminus = Boolean.parseBoolean(parts[3]);
        final int branchement = Integer.parseInt(parts[4].strip());
        Station station;
        this.lignes.add(numero_ligne);

        if (stationDejaExistante(nom_sommet)) {
            station = findStation(nom_sommet);
            station.addLignes(numero_ligne);
            station.getNumero().add(num_sommet);
        } else {
            station = new Station(nom_sommet, num_sommet, si_terminus, branchement, numero_ligne);
            this.stations.add(station);
        }


    }

    public void reset() {
        this.getStations().forEach(sommet -> sommet.setMarquer(false));
    }

    public Resultat getBFS(String nomPointDepart) {
        this.reset();
        Station s = this.getStations().stream().filter(sommet -> sommet.getNom().equals(nomPointDepart)).findFirst().get();
        String chemin = s.getNom();
        Graphe grapheBFS = new Graphe();
        Resultat resultat = new Resultat();

        ArrayList<Station> sommetAVisiter = new ArrayList<Station>();
        sommetAVisiter.add(s);
        s.setMarquer(true);
        Station aVisiter;
        grapheBFS.addStation(s.getNom());

        while (!sommetAVisiter.isEmpty()) {
            aVisiter = sommetAVisiter.get(0);
            sommetAVisiter.remove(0);
            for (Station voisin : aVisiter.getVoisins()) {
                if(!voisin.isMarquer()) {
                    grapheBFS.addStation(voisin.getNom());
                    new Arete(grapheBFS.getStationParNom(aVisiter.getNom()), grapheBFS.getStationParNom(voisin.getNom()));
                    sommetAVisiter.add(voisin);
                    voisin.setMarquer(true);
                    chemin += "->" + voisin.getNom();
                }
            }
        }

        resultat.setChemin(chemin);
        resultat.setGraphe(grapheBFS);

        return resultat;
    }

    public ArrayList<Arete> getAretes(){
        ArrayList<Arete> aretes = new ArrayList<Arete>();
        this.getStationTrier().stream().forEach(sommet -> sommet.getAretes().stream().forEach(arete -> aretes.add(arete)));
        return aretes;
    }

    public ArrayList<Station> getStationTrier() {
        return (ArrayList<Station>) this.stations.stream().sorted(Comparator.comparing(Station::getNom)).collect(Collectors.toList());
    }

    public ArrayList<Arete> getAretesSansDoublons(){
        ArrayList<Arete> aretes = new ArrayList<Arete>();
        this.getStationTrier().stream().forEach(sommet -> sommet.getAretes().stream().forEach(arete -> aretes.add(arete)));
        return (ArrayList<Arete>) this.getAretes().stream().distinct().collect(Collectors.toList());
    }

    private ArrayList<String> getEnsemble (ArrayList<Arete> aretes){
        ArrayList<String> aretesChoisi = new ArrayList<String>();
        aretesChoisi.add(aretes.get(0).getSommet1().getNom());

        boolean nouveauSommetDetecter = true;
        while(nouveauSommetDetecter) {
            nouveauSommetDetecter = false;
            for (Arete arete : aretes) {
                if(!aretesChoisi.contains(arete.getSommet1().getNom()) &&  aretesChoisi.contains(arete.getSommet2().getNom())) {
                    aretesChoisi.add(arete.getSommet1().getNom());
                    nouveauSommetDetecter = true;
                }
                else if(aretesChoisi.contains(arete.getSommet1().getNom()) &&  !aretesChoisi.contains(arete.getSommet2().getNom())) {
                    aretesChoisi.add(arete.getSommet2().getNom());
                    nouveauSommetDetecter = true;
                }
            }
        }

        return aretesChoisi;
    }

    public ArrayList<Arete> getKruskal() {
        ArrayList<Arete> aretesTrierParPoids = this.getAretesSansDoublons();
        aretesTrierParPoids.sort(Comparator.comparingInt(arete -> arete.getTempsEnSecondes()));

        ArrayList<Station> sommetVisiter = new ArrayList<Station>();
        ArrayList<Arete> aretes = new ArrayList<Arete>();

        while (aretes.size() != (this.getStationTrier().size() - 1)) {
            Arete arete = null;
            try {
                arete = aretesTrierParPoids.stream().filter(arete2 -> !sommetVisiter.contains(arete2.getSommet1()) || !sommetVisiter.contains(arete2.getSommet2()) ).findFirst().get();
            }
            catch (NoSuchElementException e) { // cas où deux arbres non connectés se sont crées
                ArrayList<String> sommets = this.getEnsemble(aretes);
                for (Arete arete2 : aretesTrierParPoids) {
                    if(sommets.contains(arete2.getSommet2().getNom()) != sommets.contains(arete2.getSommet1().getNom())){
                        arete = arete2;
                        break;
                    }
                }
            }

            if(!sommetVisiter.contains(arete.getSommet1())) {
                sommetVisiter.add(arete.getSommet1());
            }

            if(!sommetVisiter.contains(arete.getSommet2())) {
                sommetVisiter.add(arete.getSommet2());
            }
            aretes.add(arete);
        }

        return aretes;


    }

    public boolean stationDejaExistante(String nom_sommet) {
        return stations.stream().anyMatch(station -> station.getNom().equals(nom_sommet));
    }

    public Station findStation(String nom_sommet) {
        return this.stations.stream().filter(station -> station.getNom().equals(nom_sommet)).findAny().get();
    }

    public Station findStation(int num_sommet) {
        return this.stations.stream().filter(station -> station.getNumero().contains(num_sommet)).findAny().get();
    }

    public void loadCsv(String fileName) {
        try (BufferedReader reader = createCsvReader(fileName);) {
            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                final String[] parts = line.split(";");
                if (fileName.equals("/aretes.csv")) {
                    loadAretes(parts);
                } else if (fileName.equals("/sommets.csv")) {
                    loadStations(parts);
                }
                line = reader.readLine();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private BufferedReader createCsvReader(String filename) {
        final InputStream inputStream = Objects.requireNonNull(
                this.getClass().getResourceAsStream(filename),
                "CSV resource not found: " + filename
        );
        final InputStreamReader isReader = new InputStreamReader(inputStream);
        return new BufferedReader(isReader);
    }


    public void addStation(Station station) {
        this.stations.add(station);
    }

    public void addStation(String station) {
        this.stations.add(new Station(station));
    }

    public Station getStationParNom(String nom) {
        return this.getStations().stream().filter(station -> station.getNom().equals(nom)).findFirst().get();
    }
}


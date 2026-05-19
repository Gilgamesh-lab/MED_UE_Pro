package org.example.graphe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

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


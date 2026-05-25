package org.example;

import org.example.graphe.Arete;
import org.example.graphe.Graphe;
import org.example.graphe.Resultat;
import org.example.graphe.Station;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import com.sun.net.httpserver.*;

public class Main {
    static void main() throws FileNotFoundException, IOException {
        /*
         * 
         * // 159 ; Lamarck Caulaincourt ;12 ;False; 0
         * // 0000; Abbesses ;12 ;False; 0
         * // 0;159;46
         * Station Abbesses = new Station("Abbesses", 0, false, 0, "12");
         * Station LamarckCaulaincourt = new Station("Lamarck Caulaincourt", 159, false,
         * 0, "12");
         * 
         * System.out.println(Abbesses.getVoisins());
         * System.out.println(LamarckCaulaincourt.getVoisins());
         * 
         * Arete arete = new Arete(Abbesses, LamarckCaulaincourt, 46, "12");
         * 
         * System.out.println(Abbesses.getVoisins());
         * System.out.println(LamarckCaulaincourt.getVoisins());
         * 
         * Graphe graphe = new Graphe();
         * System.out.println(graphe.findStation(16).getNom());
         * System.out.println(graphe.findStation("Bastille").getLignes());
         * System.out.println(graphe.findStation("Bastille").getAretes());
         * Resultat resultat = graphe.getBFS("Bastille");
         * System.out.println(resultat.getChemin());
         * 
         * System.out.println("");
         * if (graphe.getStations().stream().allMatch(station -> station.isMarquer())) {
         * System.out.println("Le graphe est connexe");
         * } else {
         * System.out.println("Le graphe n'est pas connexe");
         * }
         * 
         * if (graphe.getStations().stream().allMatch(station -> station.getX() != 0 &&
         * station.getY() != 0)) {
         * System.out.println("Toutes les stations ont des coordonnées");
         * } else {
         * System.out.println("Des coordonnées pour certaines stations sont manquante");
         * graphe.getStations().stream()
         * .filter(station -> station.getX() == 0 || station.getY() == 0)
         * .forEach(station -> System.out.println(station.getNom()));
         * }
         * System.out.println("");
         * System.out.println("");
         * ArrayList<Arete> aretes = graphe.getKruskal();
         * aretes.stream().forEach(
         * arete2 -> System.out.println(arete2.getSommet1().getNom() + " -> " +
         * arete2.getSommet2().getNom()
         * + " (métro " + arete2.getLigne() + ") " + arete2.getTempsEnSecondes() +
         * " secondes"));
         * System.out.println();
         * System.out.println("Poids total du plus court chemin trouvé = "
         * + (aretes.stream().mapToInt(arete2 -> arete2.getTempsEnSecondes()).sum()) /
         * 360 + " heures");
         * 
         * System.out.println();
         * System.out.
         * println("Algorithme de Dijkstra entre Maison Blanche et Villejuif Louis Aragon"
         * );
         * System.out.println();
         * 
         * System.out.println();
         * resultat = graphe.getDijkstra("Maison Blanche", "Villejuif, Louis Aragon");
         * 
         * System.out.println(
         * "Le chemin le plus court trouvé entre Maison Blanche et Villejuif, Louis Aragon avec l’algorithme de Dijkstra est : "
         * + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids());
         */

        System.out.println("------------------------");

        // creation serveur http
        HttpServer serveur = HttpServer.create(new InetSocketAddress(8080), 0);

        serveur.createContext("/chemin", exchange -> {
            // evite de bloquer les requêtes du front end
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            // récupère de la requête en front end
            String query = exchange.getRequestURI().getQuery();
            String depart = "";
            String arrivee = "";

            // récupère les valeurs des stations de départ et d'arrivée
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair[0].equals("departure"))
                    depart = pair[1];
                if (pair[0].equals("arrival"))
                    arrivee = pair[1];
            }

            // calcule le plus court chemin
            Graphe newGraphe = new Graphe();
            String chemin = newGraphe.getDijkstra(depart, arrivee).getChemin();

            // renvoie le résultat
            byte[] response = chemin.getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        });

        serveur.start();
        System.out.println("the server is running on port 8080 :D");

    }
}

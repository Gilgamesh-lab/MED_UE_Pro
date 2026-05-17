package org.example;

import org.example.graphe.Arete;
import org.example.graphe.Station;


public class Main {
    static void main() {
        // 159 ; Lamarck Caulaincourt ;12 ;False; 0
        // 0000; Abbesses ;12 ;False; 0
        //0;159;46
        Station Abbesses = new Station("Abbesses",0 , false,  0, 12);
        Station LamarckCaulaincourt = new Station("Lamarck Caulaincourt",159 , false,  0, 12);

        System.out.println(Abbesses.getVoisins());
        System.out.println(LamarckCaulaincourt.getVoisins());

        Arete arete = new Arete(Abbesses, LamarckCaulaincourt, 46);

        System.out.println(Abbesses.getVoisins());
        System.out.println(LamarckCaulaincourt.getVoisins());


    }
}

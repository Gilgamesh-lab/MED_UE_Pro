import React, { useState, useEffect } from 'react';

function MetroMap() {
    const [aretes, setAretes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedEdge, setSelectedEdge] = useState(null);

    useEffect(() => {
        fetchACPM();
    }, []);

    const fetchACPM = async () => {
        try {
            setLoading(true);
            // Appel à votre endpoint Java
            const response = await fetch('http://localhost:8080/acpm', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            setAretes(data);
            setError(null);
        } catch (err) {
            setError(err.message);
            console.error('Erreur lors du chargement de l\'ACPM:', err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center h-full bg-slate-900">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-cyan-400 mx-auto mb-4"></div>
                    <p className="text-cyan-400 font-light tracking-widest">Calcul de l'ACPM en cours...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex justify-center items-center h-full bg-slate-900">
                <div className="text-center">
                    <p className="text-red-400 font-semibold mb-4">⚠️ Erreur</p>
                    <p className="text-red-300">{error}</p>
                    <button
                        onClick={fetchACPM}
                        className="mt-4 px-4 py-2 bg-cyan-500 hover:bg-cyan-600 text-white rounded transition-colors"
                    >
                        Réessayer
                    </button>
                </div>
            </div>
        );
    }

    // Calculer les positions des stations pour la visualisation
    const calculatePositions = () => {
        const positions = new Map();

        // Extraire toutes les stations uniques
        const stations = new Set();
        aretes.forEach(arete => {
            stations.add(arete.sommet1.nom);
            stations.add(arete.sommet2.nom);
        });

        // Créer une disposition circulaire
        const stationArray = Array.from(stations);
        const centerX = 400;
        const centerY = 300;
        const radius = 200;

        stationArray.forEach((station, index) => {
            const angle = (index / stationArray.length) * 2 * Math.PI;
            const x = centerX + radius * Math.cos(angle);
            const y = centerY + radius * Math.sin(angle);
            positions.set(station, { x, y });
        });

        return positions;
    };

    const positions = calculatePositions();

    // Récupérer les stations uniques
    const allStations = new Map();
    aretes.forEach(arete => {
        if (!allStations.has(arete.sommet1.nom)) {
            allStations.set(arete.sommet1.nom, arete.sommet1);
        }
        if (!allStations.has(arete.sommet2.nom)) {
            allStations.set(arete.sommet2.nom, arete.sommet2);
        }
    });

    const totalTime = aretes.reduce((sum, arete) => sum + arete.tempsEnSecondes, 0);

    return (
        <div className="w-full h-full bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 p-6 overflow-auto">
            <div className="max-w-7xl mx-auto">
                {/* Titre et statistiques */}
                <div className="mb-8">
                    <h1 className="text-4xl font-bold text-transparent bg-clip-text bg-gradient-to-r from-cyan-400 to-blue-500 mb-2">
                        Arbre Couvrant de Poids Minimal
                    </h1>
                    <p className="text-slate-400">Algorithme de Kruskal - Réseau de Métro RATP</p>
                </div>

                {/* Conteneur principal avec graphe et liste */}
                <div className="grid grid-cols-3 gap-6">
                    {/* Visualisation du graphe */}
                    <div className="col-span-2">
                        <div className="bg-slate-800 rounded-lg border border-slate-700 p-4 shadow-2xl">
                            <svg
                                width="100%"
                                height="600"
                                viewBox="0 0 800 600"
                                className="bg-slate-900 rounded-lg"
                            >
                                {/* Dessiner les arêtes */}
                                {aretes.map((arete, index) => {
                                    const pos1 = positions.get(arete.sommet1.nom);
                                    const pos2 = positions.get(arete.sommet2.nom);

                                    if (!pos1 || !pos2) return null;

                                    const isSelected = selectedEdge === index;

                                    return (
                                        <g key={`edge-${index}`}>
                                            {/* Ligne */}
                                            <line
                                                x1={pos1.x}
                                                y1={pos1.y}
                                                x2={pos2.x}
                                                y2={pos2.y}
                                                stroke={isSelected ? '#06b6d4' : '#0ea5e9'}
                                                strokeWidth={isSelected ? 4 : 2.5}
                                                opacity={isSelected ? 1 : 0.7}
                                                className="transition-all cursor-pointer hover:stroke-cyan-300"
                                                onClick={() => setSelectedEdge(isSelected ? null : index)}
                                                style={{
                                                    filter: 'drop-shadow(0 0 8px rgba(6, 182, 212, 0.3))'
                                                }}
                                            />
                                            {/* Temps au milieu de la ligne */}
                                            {isSelected && (
                                                <text
                                                    x={(pos1.x + pos2.x) / 2}
                                                    y={(pos1.y + pos2.y) / 2 - 10}
                                                    textAnchor="middle"
                                                    fill="#06b6d4"
                                                    fontSize="12"
                                                    fontWeight="bold"
                                                    className="pointer-events-none"
                                                >
                                                    {arete.tempsEnSecondes}s
                                                </text>
                                            )}
                                        </g>
                                    );
                                })}

                                {/* Dessiner les stations */}
                                {Array.from(allStations.entries()).map(([nomStation, station]) => {
                                    const pos = positions.get(nomStation);
                                    if (!pos) return null;

                                    return (
                                        <g key={`station-${nomStation}`}>
                                            {/* Cercle de la station */}
                                            <circle
                                                cx={pos.x}
                                                cy={pos.y}
                                                r="18"
                                                fill="#1e293b"
                                                stroke="#06b6d4"
                                                strokeWidth="2"
                                                className="hover:r-24 transition-all"
                                                style={{
                                                    filter: 'drop-shadow(0 0 12px rgba(6, 182, 212, 0.4))'
                                                }}
                                            />
                                            {/* Label de la station */}
                                            <text
                                                x={pos.x}
                                                y={pos.y + 40}
                                                textAnchor="middle"
                                                fill="#e2e8f0"
                                                fontSize="13"
                                                fontWeight="500"
                                                className="pointer-events-none"
                                            >
                                                {nomStation}
                                            </text>
                                        </g>
                                    );
                                })}
                            </svg>
                        </div>
                    </div>

                    {/* Panneau latéral avec statistiques et liste */}
                    <div className="space-y-6">
                        {/* Statistiques */}
                        <div className="bg-slate-800 rounded-lg border border-slate-700 p-5 shadow-lg">
                            <h2 className="text-lg font-semibold text-cyan-400 mb-4">Statistiques</h2>
                            <div className="space-y-3">
                                <div className="flex justify-between items-center">
                                    <span className="text-slate-400">Stations:</span>
                                    <span className="text-cyan-300 font-semibold">{allStations.size}</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-slate-400">Connexions:</span>
                                    <span className="text-cyan-300 font-semibold">{aretes.length}</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-slate-400">Temps total:</span>
                                    <span className="text-cyan-300 font-semibold">{totalTime}s</span>
                                </div>
                                <div className="flex justify-between items-center">
                                    <span className="text-slate-400">Temps moyen:</span>
                                    <span className="text-cyan-300 font-semibold">
                                        {(totalTime / aretes.length).toFixed(1)}s
                                    </span>
                                </div>
                                <div className="flex justify-between items-center pt-3 border-t border-slate-600">
                                    <span className="text-slate-400">Temps total:</span>
                                    <span className="text-cyan-300 font-semibold">
                                        {(totalTime / 3600).toFixed(2)}h
                                    </span>
                                </div>
                            </div>
                        </div>

                        {/* Liste des arêtes */}
                        <div className="bg-slate-800 rounded-lg border border-slate-700 p-5 shadow-lg">
                            <h2 className="text-lg font-semibold text-cyan-400 mb-4">Connexions ACPM</h2>
                            <div className="space-y-2 max-h-96 overflow-y-auto">
                                {aretes.map((arete, index) => (
                                    <div
                                        key={index}
                                        onClick={() => setSelectedEdge(selectedEdge === index ? null : index)}
                                        className={`p-3 rounded-lg cursor-pointer transition-all text-sm ${
                                            selectedEdge === index
                                                ? 'bg-cyan-500 bg-opacity-20 border border-cyan-400'
                                                : 'bg-slate-700 hover:bg-slate-600 border border-slate-600'
                                        }`}
                                    >
                                        <div className="font-medium text-cyan-300">
                                            {arete.sommet1.nom} ↔ {arete.sommet2.nom}
                                        </div>
                                        <div className="text-slate-400 text-xs mt-1">
                                            Ligne {arete.ligne} • {arete.tempsEnSecondes}s
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        {/* Bouton de rafraîchissement */}
                        <button
                            onClick={fetchACPM}
                            className="w-full bg-gradient-to-r from-cyan-500 to-blue-500 hover:from-cyan-600 hover:to-blue-600 text-white font-semibold py-3 rounded-lg transition-all shadow-lg hover:shadow-xl"
                        >
                            Recalculer l'ACPM
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}


export default MetroMap;
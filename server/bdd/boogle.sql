-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : mer. 25 jan. 2023 à 20:55
-- Version du serveur : 10.9.4-MariaDB-log
-- Version de PHP : 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `boogle`
--

-- --------------------------------------------------------

--
-- Structure de la table `chatprive`
--

CREATE TABLE `chatprive` (
  `idChat` varchar(50) NOT NULL,
  `messageChat` varchar(50) DEFAULT NULL,
  `idMessageJoueur` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `jouer`
--

CREATE TABLE `jouer` (
  `idUser` int(11) NOT NULL,
  `idPartie` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;




-- --------------------------------------------------------

--
-- Structure de la table `partie`
--

CREATE TABLE `partie` (
  `idPartie` varchar(50) NOT NULL,
  `NbMotsTrouves` varchar(50) DEFAULT NULL,
  `idVainqueur` varchar(50) DEFAULT NULL,
  `dimensionsGrille` varchar(10) NOT NULL,
  `Grille` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`Grille`)),
  `NomDicoUtilise` varchar(50) DEFAULT NULL,
  `DateDebutPartie` datetime DEFAULT NULL,
  `DateFinPartie` datetime DEFAULT NULL,
  `idChat` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Déchargement des données de la table `partie`
--


--
-- Structure de la table `proposemots`
--

CREATE TABLE `proposemots` (
  `idUser` varchar(50) NOT NULL,
  `idPartie` varchar(50) NOT NULL,
  `MotsPropose` varchar(50) NOT NULL,
  `ScoreMots` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `idUser` int(11) NOT NULL,
  `photoProfil` varchar(50) DEFAULT NULL,
  `pseudoUser` varchar(50) DEFAULT NULL,
  `login` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

-- --------------------------------------------------------

--
-- Structure de la table `verifmail`
--

CREATE TABLE `verifmail` (
  `idVerif` varchar(50) NOT NULL,
  `idLien` varchar(50) DEFAULT NULL,
  `codeVerif` varchar(6) DEFAULT NULL,
  `idUser` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `chatprive`
--
ALTER TABLE `chatprive`
  ADD PRIMARY KEY (`idChat`);

--
-- Index pour la table `jouer`
--
ALTER TABLE `jouer`
  ADD PRIMARY KEY (`idUser`,`idPartie`);

--
-- Index pour la table `partie`
--
ALTER TABLE `partie`
  ADD PRIMARY KEY (`idPartie`);

--
-- Index pour la table `proposemots`
--
ALTER TABLE `proposemots`
  ADD PRIMARY KEY (`idUser`,`idPartie`,`MotsPropose`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`idUser`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `jouer`
--
ALTER TABLE `jouer`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

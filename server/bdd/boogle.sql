-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : mar. 31 jan. 2023 à 12:25
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
-- Structure de la table `amis`
--

CREATE TABLE `amis` (
  `idUser1` int(11) NOT NULL,
  `idUser2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `amis`
--

INSERT INTO `amis` (`idUser1`, `idUser2`) VALUES
(3, 4);

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

--
-- Déchargement des données de la table `jouer`
--

INSERT INTO `jouer` (`idUser`, `idPartie`) VALUES
(3, '158673bef73beeae14cfdc10713a13e8'),
(3, '1f19a5bae1c1d2b06041090e5cae3aa6'),
(3, '34489944aea10854268db0b4036459aa'),
(3, '424cf35f417a6478842e1fb49ce497eb'),
(3, '43012d611131ed486fd75f4033670400'),
(3, '430c1ab2244efad841dced15c68086be'),
(3, '6fa24f022c6d2f565b041241e9696d02'),
(3, '7354dcaf2696938a902f3c6811819ebe'),
(3, '82187c0c8e3022ab539a204aaf84701a'),
(3, 'c4b418e7675a8e0ef9ab64d46e73ec6f'),
(3, 'd81e3ebc8c3b6405ed1001ffe845c8de'),
(3, 'da80673b641a663a4e46ce91f9a85ca7'),
(3, 'e9bc5073427fe762c45205a8b534fa45'),
(3, 'f3f7f0c02fc3338ff43b69297ee61a6a'),
(4, '108c53fbd48f991ccfb0b6ea57343be2'),
(4, '1ba1d1d3ad1f04dd9e015d215a705f9c'),
(4, '2f06feaab45657ac5ff9b8c1945f628a'),
(4, '54f10b753116b9ed594d44822308e037'),
(4, 'b6650d0c96dd625647a4fa96ef78694b'),
(4, 'd064929bde6a156248333d825d32aa96'),
(4, 'dcd94ccbc8d6fbf4187835670ea62362'),
(4, 'df3752fd67b519596080b7ce8d052507'),
(4, 'e01009993b60ce12a794d4d05e068c16'),
(5, '0055e6527ddef75bf5fc35185b64550a'),
(5, 'd75a6c88192ae98cfab3fd4a9a5ef8aa');

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

INSERT INTO `partie` (`idPartie`, `NbMotsTrouves`, `idVainqueur`, `dimensionsGrille`, `Grille`, `NomDicoUtilise`, `DateDebutPartie`, `DateFinPartie`, `idChat`) VALUES
('0055e6527ddef75bf5fc35185b64550a', '1', '5', '4x4', '[[\"M\",\"T\",\"F\",\"E\"],[\"S\",\"S\",\"D\",\"N\"],[\"E\",\"D\",\"D\",\"R\"],[\"O\",\"QU\",\"D\",\"N\"]]', NULL, '2023-01-31 13:24:41', '2023-01-31 13:24:55', NULL),
('108c53fbd48f991ccfb0b6ea57343be2', '1', '4', '4x4', '[[\"L\",\"E\",\"I\",\"U\"],[\"E\",\"R\",\"D\",\"E\"],[\"I\",\"D\",\"D\",\"E\"],[\"E\",\"N\",\"P\",\"S\"]]', NULL, '2023-01-26 00:19:16', '2023-01-26 00:28:37', NULL),
('158673bef73beeae14cfdc10713a13e8', '1', '3', '4x4', '[[\"E\",\"D\",\"V\",\"E\"],[\"I\",\"I\",\"M\",\"S\"],[\"E\",\"P\",\"T\",\"L\"],[\"D\",\"S\",\"P\",\"U\"]]', NULL, '2023-01-26 19:50:28', '2023-01-26 19:56:52', NULL),
('1ba1d1d3ad1f04dd9e015d215a705f9c', '1', '4', '4x4', '[[\"L\",\"E\",\"I\",\"U\"],[\"E\",\"R\",\"D\",\"E\"],[\"I\",\"D\",\"D\",\"E\"],[\"E\",\"N\",\"P\",\"S\"]]', NULL, '2023-01-26 00:19:16', '2023-01-26 00:29:02', NULL),
('1f19a5bae1c1d2b06041090e5cae3aa6', '1', '3', '4x4', '[[\"E\",\"D\",\"E\",\"N\"],[\"E\",\"M\",\"E\",\"M\"],[\"I\",\"E\",\"N\",\"V\"],[\"R\",\"N\",\"E\",\"F\"]]', NULL, '2023-01-26 00:29:13', '2023-01-26 00:33:14', NULL),
('2f06feaab45657ac5ff9b8c1945f628a', '1', '4', '4x4', '[[\"P\",\"O\",\"I\",\"O\"],[\"I\",\"T\",\"G\",\"E\"],[\"E\",\"A\",\"E\",\"U\"],[\"E\",\"U\",\"U\",\"E\"]]', NULL, '2023-01-25 21:43:03', '2023-01-25 21:46:31', NULL),
('34489944aea10854268db0b4036459aa', '1', '3', '4x4', '[[\"T\",\"E\",\"T\",\"R\"],[\"D\",\"A\",\"F\",\"F\"],[\"I\",\"E\",\"I\",\"N\"],[\"S\",\"V\",\"S\",\"S\"]]', NULL, '2023-01-31 13:10:10', '2023-01-31 13:10:34', NULL),
('424cf35f417a6478842e1fb49ce497eb', '1', '3', '4x4', '[[\"N\",\"N\",\"P\",\"O\"],[\"U\",\"D\",\"A\",\"N\"],[\"A\",\"P\",\"M\",\"O\"],[\"S\",\"F\",\"F\",\"N\"]]', NULL, '2023-01-25 20:18:43', '2023-01-25 20:18:58', NULL),
('43012d611131ed486fd75f4033670400', '1', '3', '4x4', '[[\"E\",\"D\",\"V\",\"E\"],[\"I\",\"I\",\"M\",\"S\"],[\"E\",\"P\",\"T\",\"L\"],[\"D\",\"S\",\"P\",\"U\"]]', NULL, '2023-01-26 19:50:28', '2023-01-26 19:56:48', NULL),
('430c1ab2244efad841dced15c68086be', '1', '3', '4x4', '[[\"E\",\"M\",\"N\",\"N\"],[\"T\",\"I\",\"QU\",\"D\"],[\"I\",\"L\",\"L\",\"L\"],[\"C\",\"P\",\"E\",\"E\"]]', NULL, '2023-01-27 11:31:51', '2023-01-27 11:57:47', NULL),
('54f10b753116b9ed594d44822308e037', '1', '4', '4x4', '[[\"S\",\"R\",\"T\",\"A\"],[\"S\",\"P\",\"QU\",\"G\"],[\"D\",\"U\",\"A\",\"H\"],[\"R\",\"N\",\"R\",\"R\"]]', NULL, '2023-01-25 21:31:49', '2023-01-25 21:34:46', NULL),
('6fa24f022c6d2f565b041241e9696d02', '1', '3', '4x4', '[[\"E\",\"D\",\"V\",\"E\"],[\"I\",\"I\",\"M\",\"S\"],[\"E\",\"P\",\"T\",\"L\"],[\"D\",\"S\",\"P\",\"U\"]]', NULL, '2023-01-26 19:50:28', '2023-01-26 19:56:47', NULL),
('7354dcaf2696938a902f3c6811819ebe', '1', '3', '4x4', '[[\"E\",\"D\",\"V\",\"E\"],[\"I\",\"I\",\"M\",\"S\"],[\"E\",\"P\",\"T\",\"L\"],[\"D\",\"S\",\"P\",\"U\"]]', NULL, '2023-01-26 19:50:28', '2023-01-26 19:56:47', NULL),
('82187c0c8e3022ab539a204aaf84701a', '1', '3', '4x6', '[[\"S\",\"R\",\"P\",\"N\",\"E\",\"G\"],[\"J\",\"F\",\"E\",\"U\",\"R\",\"I\"],[\"W\",\"E\",\"L\",\"M\",\"U\",\"O\"],[\"N\",\"I\",\"E\",\"R\",\"V\",\"A\"]]', NULL, '2023-01-25 20:54:33', '2023-01-25 20:56:44', NULL),
('b6650d0c96dd625647a4fa96ef78694b', '1', '4', '4x4', '[[\"L\",\"E\",\"I\",\"U\"],[\"E\",\"R\",\"D\",\"E\"],[\"I\",\"D\",\"D\",\"E\"],[\"E\",\"N\",\"P\",\"S\"]]', NULL, '2023-01-26 00:19:16', '2023-01-26 00:29:02', NULL),
('c4b418e7675a8e0ef9ab64d46e73ec6f', '1', '3', '4x4', '[[\"E\",\"D\",\"V\",\"E\"],[\"I\",\"I\",\"M\",\"S\"],[\"E\",\"P\",\"T\",\"L\"],[\"D\",\"S\",\"P\",\"U\"]]', NULL, '2023-01-26 19:50:28', '2023-01-26 19:56:54', NULL),
('d064929bde6a156248333d825d32aa96', '1', '4', '4x4', '[[\"U\",\"I\",\"R\",\"U\"],[\"L\",\"I\",\"G\",\"E\"],[\"I\",\"U\",\"E\",\"A\"],[\"N\",\"N\",\"B\",\"U\"]]', NULL, '2023-01-26 00:17:06', '2023-01-26 00:19:16', NULL),
('d75a6c88192ae98cfab3fd4a9a5ef8aa', '1', '5', '4x4', '[[\"E\",\"E\",\"R\",\"M\"],[\"A\",\"N\",\"L\",\"O\"],[\"A\",\"R\",\"U\",\"L\"],[\"E\",\"D\",\"R\",\"C\"]]', NULL, '2023-01-31 13:23:27', '2023-01-31 13:24:22', NULL),
('d81e3ebc8c3b6405ed1001ffe845c8de', '1', '3', '4x4', '[[\"E\",\"D\",\"V\",\"E\"],[\"I\",\"I\",\"M\",\"S\"],[\"E\",\"P\",\"T\",\"L\"],[\"D\",\"S\",\"P\",\"U\"]]', NULL, '2023-01-26 19:50:28', '2023-01-26 19:56:45', NULL),
('da80673b641a663a4e46ce91f9a85ca7', '1', '3', '4x4', '[[\"E\",\"D\",\"V\",\"E\"],[\"I\",\"I\",\"M\",\"S\"],[\"E\",\"P\",\"T\",\"L\"],[\"D\",\"S\",\"P\",\"U\"]]', NULL, '2023-01-26 19:50:28', '2023-01-26 19:56:48', NULL),
('dcd94ccbc8d6fbf4187835670ea62362', '1', '4', '4x4', '[[\"L\",\"E\",\"I\",\"U\"],[\"E\",\"R\",\"D\",\"E\"],[\"I\",\"D\",\"D\",\"E\"],[\"E\",\"N\",\"P\",\"S\"]]', NULL, '2023-01-26 00:19:16', '2023-01-26 00:28:46', NULL),
('df3752fd67b519596080b7ce8d052507', '1', '4', '4x4', '[[\"L\",\"E\",\"I\",\"U\"],[\"E\",\"R\",\"D\",\"E\"],[\"I\",\"D\",\"D\",\"E\"],[\"E\",\"N\",\"P\",\"S\"]]', NULL, '2023-01-26 00:19:16', '2023-01-26 00:28:45', NULL),
('e01009993b60ce12a794d4d05e068c16', '1', '4', '4x4', '[[\"L\",\"E\",\"I\",\"U\"],[\"E\",\"R\",\"D\",\"E\"],[\"I\",\"D\",\"D\",\"E\"],[\"E\",\"N\",\"P\",\"S\"]]', NULL, '2023-01-26 00:19:16', '2023-01-26 00:28:43', NULL),
('e9bc5073427fe762c45205a8b534fa45', '1', '3', '4x4', '[[\"I\",\"I\",\"I\",\"E\"],[\"A\",\"O\",\"T\",\"N\"],[\"N\",\"S\",\"R\",\"E\"],[\"P\",\"A\",\"D\",\"O\"]]', NULL, '2023-01-27 11:57:50', '2023-01-27 11:59:05', NULL),
('f3f7f0c02fc3338ff43b69297ee61a6a', '1', '3', '4x4', '[[\"O\",\"N\",\"M\",\"O\"],[\"H\",\"M\",\"O\",\"U\"],[\"T\",\"S\",\"C\",\"G\"],[\"QU\",\"R\",\"F\",\"U\"]]', NULL, '2023-01-25 21:01:56', '2023-01-25 22:13:22', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `proposemots`
--

CREATE TABLE `proposemots` (
  `idUser` varchar(50) NOT NULL,
  `idPartie` varchar(50) NOT NULL,
  `MotsPropose` varchar(50) NOT NULL,
  `ScoreMots` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Déchargement des données de la table `proposemots`
--

INSERT INTO `proposemots` (`idUser`, `idPartie`, `MotsPropose`, `ScoreMots`) VALUES
('3', '158673bef73beeae14cfdc10713a13e8', '', '0'),
('3', '158673bef73beeae14cfdc10713a13e8', 'DEP', '0'),
('3', '1f19a5bae1c1d2b06041090e5cae3aa6', 'FEN', '0'),
('3', '1f19a5bae1c1d2b06041090e5cae3aa6', 'RIEN', '0'),
('3', '1f19a5bae1c1d2b06041090e5cae3aa6', 'VEM', '0'),
('3', '34489944aea10854268db0b4036459aa', '', '0'),
('3', '34489944aea10854268db0b4036459aa', 'TET', '0'),
('3', '424cf35f417a6478842e1fb49ce497eb', 'PAN', '0'),
('3', '43012d611131ed486fd75f4033670400', '', '0'),
('3', '43012d611131ed486fd75f4033670400', 'DEP', '0'),
('3', '430c1ab2244efad841dced15c68086be', 'LE', '0'),
('3', '6fa24f022c6d2f565b041241e9696d02', '', '0'),
('3', '6fa24f022c6d2f565b041241e9696d02', 'DEP', '0'),
('3', '7354dcaf2696938a902f3c6811819ebe', '', '0'),
('3', '7354dcaf2696938a902f3c6811819ebe', 'DEP', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'FEU', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'MU', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'MUR', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'MURE', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'NI', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'NIE', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'NIER', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'RE', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'RI', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'VA', '0'),
('3', '82187c0c8e3022ab539a204aaf84701a', 'VU', '0'),
('3', 'c4b418e7675a8e0ef9ab64d46e73ec6f', '', '0'),
('3', 'c4b418e7675a8e0ef9ab64d46e73ec6f', 'DEP', '0'),
('3', 'd81e3ebc8c3b6405ed1001ffe845c8de', '', '0'),
('3', 'd81e3ebc8c3b6405ed1001ffe845c8de', 'DEP', '0'),
('3', 'da80673b641a663a4e46ce91f9a85ca7', '', '0'),
('3', 'da80673b641a663a4e46ce91f9a85ca7', 'DEP', '0'),
('3', 'e9bc5073427fe762c45205a8b534fa45', 'RE', '0'),
('3', 'e9bc5073427fe762c45205a8b534fa45', 'ROTE', '0'),
('3', 'f3f7f0c02fc3338ff43b69297ee61a6a', 'HOMO', '0'),
('3', 'f3f7f0c02fc3338ff43b69297ee61a6a', 'HOMOS', '0'),
('4', '108c53fbd48f991ccfb0b6ea57343be2', 'BONJOUR', '0'),
('4', '108c53fbd48f991ccfb0b6ea57343be2', 'ERDE', '0'),
('4', '1ba1d1d3ad1f04dd9e015d215a705f9c', 'BONJOUR', '0'),
('4', '1ba1d1d3ad1f04dd9e015d215a705f9c', 'ERDE', '0'),
('4', '2f06feaab45657ac5ff9b8c1945f628a', 'EAU', '0'),
('4', '2f06feaab45657ac5ff9b8c1945f628a', 'EU', '0'),
('4', '2f06feaab45657ac5ff9b8c1945f628a', 'POTAGE', '0'),
('4', '54f10b753116b9ed594d44822308e037', 'DU', '0'),
('4', '54f10b753116b9ed594d44822308e037', 'DUP', '0'),
('4', '54f10b753116b9ed594d44822308e037', 'PUA', '0'),
('4', '54f10b753116b9ed594d44822308e037', 'QUA', '0'),
('4', '54f10b753116b9ed594d44822308e037', 'RUA', '0'),
('4', '54f10b753116b9ed594d44822308e037', 'RUS', '0'),
('4', '54f10b753116b9ed594d44822308e037', 'TA', '0'),
('4', 'b6650d0c96dd625647a4fa96ef78694b', 'BONJOUR', '0'),
('4', 'b6650d0c96dd625647a4fa96ef78694b', 'ERDE', '0'),
('4', 'd064929bde6a156248333d825d32aa96', 'BUA', '0'),
('4', 'd064929bde6a156248333d825d32aa96', 'LIGEA', '0'),
('4', 'd064929bde6a156248333d825d32aa96', 'LIRE', '0'),
('4', 'd064929bde6a156248333d825d32aa96', 'RIULI', '0'),
('4', 'dcd94ccbc8d6fbf4187835670ea62362', 'BONJOUR', '0'),
('4', 'dcd94ccbc8d6fbf4187835670ea62362', 'ERDE', '0'),
('4', 'df3752fd67b519596080b7ce8d052507', 'BONJOUR', '0'),
('4', 'df3752fd67b519596080b7ce8d052507', 'ERDE', '0'),
('4', 'e01009993b60ce12a794d4d05e068c16', 'BONJOUR', '0'),
('4', 'e01009993b60ce12a794d4d05e068c16', 'ERDE', '0'),
('5', '0055e6527ddef75bf5fc35185b64550a', 'DE', '0'),
('5', '0055e6527ddef75bf5fc35185b64550a', 'QUO', '0'),
('5', 'd75a6c88192ae98cfab3fd4a9a5ef8aa', 'DRU', '0'),
('5', 'd75a6c88192ae98cfab3fd4a9a5ef8aa', 'MOLE', '0'),
('5', 'd75a6c88192ae98cfab3fd4a9a5ef8aa', 'RADE', '0'),
('5', 'd75a6c88192ae98cfab3fd4a9a5ef8aa', 'RE', '0'),
('5', 'd75a6c88192ae98cfab3fd4a9a5ef8aa', 'RU', '0');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `idUser` int(11) NOT NULL,
  `photoProfil` varchar(50) DEFAULT NULL,
  `pseudoUser` varchar(50) DEFAULT NULL,
  `login` varchar(50) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`idUser`, `photoProfil`, `pseudoUser`, `login`, `email`, `password`) VALUES
(3, NULL, 'admin', 'admin', 'admin@boogle.edmeeleon.fr', '$2y$10$s.HHKAJMA868P74ez.xzKey3PPPESS4zgsyhQQBWyvb4YBB35tzfG'),
(4, NULL, 'soso', 'soso', 'soso@reims.fr', '$2y$10$61JS21Vok3wGm/2KT5mAYOdqnBb/JDCoLJ8D/4.TCZxH17Eu4FQC6'),
(5, NULL, 'test', 'test', 'test@email.fr', '$2y$10$7rE0FTy4GMtWKZrGIL7Ka.Xedbpy5.qUNo3anNRC8zW6WSLozsS5e');

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
-- Index pour la table `amis`
--
ALTER TABLE `amis`
  ADD PRIMARY KEY (`idUser1`,`idUser2`),
  ADD KEY `idUser2` (`idUser2`),
  ADD KEY `idUser1` (`idUser1`);

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
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `amis`
--
ALTER TABLE `amis`
  ADD CONSTRAINT `amis_ibfk_1` FOREIGN KEY (`idUser1`) REFERENCES `utilisateur` (`idUser`),
  ADD CONSTRAINT `amis_ibfk_2` FOREIGN KEY (`idUser2`) REFERENCES `utilisateur` (`idUser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

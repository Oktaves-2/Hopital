-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : ven. 16 juin 2023 à 01:18
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `base_hopital`
--

-- --------------------------------------------------------

--
-- Structure de la table `appareil`
--

CREATE TABLE `appareil` (
  `type_appareil` varchar(100) NOT NULL,
  `idappareil` int(11) NOT NULL,
  `octroye` tinyint(1) NOT NULL DEFAULT 0,
  `idPatient` varchar(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `appareil`
--

INSERT INTO `appareil` (`type_appareil`, `idappareil`, `octroye`, `idPatient`) VALUES
('lit', 1, 1, 'PL300'),
('lit', 2, 0, NULL),
('lit', 3, 0, NULL),
('lit', 14, 1, 'PU789'),
('lit', 15, 0, NULL),
('lit', 16, 0, NULL),
('lit', 17, 0, NULL),
('lit', 18, 0, NULL),
('lit', 19, 0, NULL),
('lit', 20, 0, NULL),
('canne', 39, 0, NULL),
('canne', 40, 1, 'PL300'),
('canne', 45, 0, NULL),
('prothese', 46, 0, NULL),
('prothese', 47, 0, NULL),
('prothese', 49, 0, NULL),
('canne', 50, 0, NULL),
('canne', 51, 0, NULL),
('canne', 52, 0, NULL),
('canne', 53, 0, NULL),
('canne', 54, 0, NULL),
('fauteuil roulant', 55, 0, NULL),
('fauteuil roulant', 60, 1, 'PL300'),
('fauteuil roulant', 61, 0, NULL),
('fauteuil roulant', 62, 0, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `consultation`
--

CREATE TABLE `consultation` (
  `idcons` varchar(6) NOT NULL,
  `creation` date NOT NULL DEFAULT current_timestamp(),
  `modification` date DEFAULT NULL,
  `idPatient` varchar(6) NOT NULL,
  `idMedecin` varchar(6) NOT NULL,
  `prescription` text NOT NULL,
  `cout` double NOT NULL DEFAULT 35,
  `assignation_appareil` varchar(100) DEFAULT NULL,
  `statut_appareil` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `consultation`
--

INSERT INTO `consultation` (`idcons`, `creation`, `modification`, `idPatient`, `idMedecin`, `prescription`, `cout`, `assignation_appareil`, `statut_appareil`) VALUES
('G18564', '2018-06-14', NULL, 'PK228', 'MF987', 'Anemie causant une grande fatigue, nécéssite un régime a base de plus de fer.', 35, 'canne', 'a_assigner'),
('G19456', '2019-06-13', NULL, 'PU789', 'MF987', 'Patient atteint d\'asthme léger, pas de traitement nécéssaire pour l\'instant.', 35, NULL, NULL),
('G19457', '2019-04-17', NULL, 'PL300', 'MF987', 'Diabète, nécessite régulation alimentaire et traitement à base de metformine.', 35, NULL, NULL),
('G19660', '2019-06-29', NULL, 'PS123', 'MG564', 'Forte grippe, 500 mg de paracétamol', 35, NULL, NULL),
('G20001', '2020-02-13', NULL, 'PU789', 'MG564', 'Forte Grippe, besoin de repos et prescriptions de 2 boites de Relenza.', 35, 'lit', 'a_assigner'),
('G20005', '2023-06-10', NULL, 'PU789', 'MG564', 'Fièvre et Anémie, prescription de  Timoferol', 35, 'lit', 'assigne'),
('G20201', '2020-11-19', NULL, 'PT451', 'MF987', 'Legere grippe et faiblesse physique, 500mg de paracétamol et conseil de prise de spiruline.', 35, NULL, NULL),
('G20265', '2020-06-11', NULL, 'PS123', 'MF987', 'Diabète, nécessite régulation alimentaire et injections de sucre.', 35, NULL, NULL),
('G20500', '2020-11-13', NULL, 'PS123', 'MF987', 'légère fièvre, 500mg de paracétamol', 35, NULL, NULL),
('G21001', '2021-10-21', NULL, 'PL300', 'MF987', 'légère fièvre, 500mg de paracétamol à prendre matin et soir.', 35, 'canne', 'assigne'),
('G21056', '2021-06-17', NULL, 'PK228', 'MF987', 'Aucun problème remarqué, légère fatigue', 35, NULL, NULL),
('G21058', '2021-10-08', NULL, 'PS132', 'MG564', 'Forte Grippe, besoin de repos et prescriptions de 2 boites de Relenza.', 35, NULL, NULL),
('G21086', '2021-06-17', NULL, 'PK228', 'MF987', 'Hépatite B observée, enregistré pour injection d interféron pégylé.', 35, 'lit', 'a_assigner'),
('G21560', '2021-10-06', NULL, 'PS123', 'MG564', 'légère fièvre, 500mg de paracétamol à prendre matin et soir.', 35, NULL, NULL),
('G22001', '2022-09-01', NULL, 'PL300', 'MF987', 'Patient atteint de Diabète, nécessite étude approfondie.', 35, 'lit', 'assigne'),
('G22050', '2022-03-23', NULL, 'PK228', 'MG564', 'Legere toux, 1/4 de litre de sirop pour la toux à prendre le matin.', 35, NULL, NULL),
('G22153', '2022-01-13', NULL, 'PK228', 'MG564', 'Forte Grippe, besoin de repos et prescriptions de 2 boites de Relenza.', 35, NULL, NULL),
('G22444', '2022-11-17', NULL, 'PA564', 'MF987', 'Diabète, nécessite régulation alimentaire et traitement à base de metformine.', 35, NULL, NULL),
('G22897', '2022-09-15', NULL, 'PA564', 'MF987', 'Patient atteint d\'arthrite aux doigts et à la pointe des pieds, nécéssite étude supplémentaire.', 35, NULL, NULL),
('G23001', '2023-06-08', NULL, 'PA564', 'MG564', 'Patient atteint de Grippe,\r\nPrescrit 2 boites de paracétamol', 35, NULL, NULL),
('G23006', '2023-01-12', NULL, 'PU789', 'MF987', 'Diabète, nécessite régulation alimentaire et traitement à base de metformine.', 35, NULL, NULL),
('G23244', '2023-06-15', NULL, 'PL300', 'MG564', 'Incapacité de se mouvoir convenablement suite à une forte fièvre, assignation de chaise roulante temporaire.', 35, 'fauteuil roulant', 'assigne'),
('G23560', '2023-06-13', NULL, 'PS132', 'MG564', 'Patient atteint d\'asthme assez severe, prescription de corticoides sous forme inhalés pour l\'instant.', 35, NULL, 'a_assigner');

-- --------------------------------------------------------

--
-- Structure de la table `malade`
--

CREATE TABLE `malade` (
  `idPatient` varchar(6) NOT NULL,
  `idPathologie` varchar(15) NOT NULL,
  `idCons` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `malade`
--

INSERT INTO `malade` (`idPatient`, `idPathologie`, `idCons`) VALUES
('PK228', 'HPTB', 'G21086'),
('PA564', 'GRP', 'G23001'),
('PL300', 'DBT', 'G22001'),
('PU789', 'FVR', 'G20005'),
('PU789', 'ANM', 'G20005'),
('PS132', 'ASTH', 'G23560'),
('PS123', 'GRP', 'G19660'),
('PK228', 'ANM', 'G18564'),
('PU789', 'ASTH', 'G19456'),
('PA564', 'ARTH', 'G22897'),
('PS123', 'DBT', 'G20265'),
('PL300', 'DBT', 'G19457'),
('PA564', 'DBT', 'G22444'),
('PU789', 'DBT', 'G23006'),
('PU789', 'GRP', 'G20001'),
('PK228', 'GRP', 'G22153'),
('PS132', 'GRP', 'G21058'),
('PS123', 'FVR', 'G20500'),
('PS123', 'FVR', 'G21560'),
('PL300', 'FVR', 'G21001'),
('PT451', 'GRP', 'G20201');

-- --------------------------------------------------------

--
-- Structure de la table `pathologie`
--

CREATE TABLE `pathologie` (
  `nom` varchar(100) NOT NULL,
  `idPathologie` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `pathologie`
--

INSERT INTO `pathologie` (`nom`, `idPathologie`) VALUES
('Anemie', 'ANM'),
('Arthrite', 'ARTH'),
('Asthme', 'ASTH'),
('Diabète', 'DBT'),
('Fievre', 'FVR'),
('Grippe', 'GRP'),
('Hépatite B', 'HPTB'),
('Pneumonie', 'PNM'),
('Rhume', 'RHM'),
('Varicelle', 'VRCL');

-- --------------------------------------------------------

--
-- Structure de la table `patient`
--

CREATE TABLE `patient` (
  `idPatient` varchar(6) NOT NULL,
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `naissance` date NOT NULL,
  `adresse` varchar(120) NOT NULL,
  `email` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `patient`
--

INSERT INTO `patient` (`idPatient`, `nom`, `prenom`, `naissance`, `adresse`, `email`) VALUES
('PA564', 'Akko', 'Yun', '2000-06-06', '150 Boulevard Daumesnil', 'AG@homtail.fr'),
('PH356', 'Herman', 'Solange', '2005-05-05', '54 Boulevard Central', 'SolangeH@gmail.com'),
('PK228', 'Khol', 'François', '1939-10-27', '13 rue de Lourdes', 'kholf@gmail.com'),
('PL300', 'Lancel', 'Lucien', '1965-10-11', '13 rue Ravel', 'll@gmail.com'),
('PS123', 'Simane', 'Jen', '1987-06-12', '152 Boulevard Daumesnil', 'JenSim@hotmail.fr'),
('PS132', 'Lars', 'Karl', '1982-09-08', '54 Boulevard Daumesnil', 'llk@gmail.com'),
('PT451', 'Tarr', 'Issam', '1938-06-17', '45 Rue de l\'or', 'IssamTar@hotmail.fr'),
('PU789', 'Uni', 'Fabien', '1973-06-14', '43 Rue du vide', 'FabienUni@hotmail.fr');

-- --------------------------------------------------------

--
-- Structure de la table `personnel`
--

CREATE TABLE `personnel` (
  `id` varchar(6) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `naissance` date NOT NULL,
  `adresse` varchar(120) NOT NULL,
  `email` varchar(150) NOT NULL,
  `mpasse` varchar(25) NOT NULL,
  `profession` varchar(20) NOT NULL,
  `role` varchar(15) NOT NULL DEFAULT 'Utilisateur'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `personnel`
--

INSERT INTO `personnel` (`id`, `nom`, `prenom`, `naissance`, `adresse`, `email`, `mpasse`, `profession`, `role`) VALUES
('AB258', 'Bass', 'Frederick', '1999-09-09', '14 Rue du silence', 'Bass@gmail.com', 'Hpsmui8', 'Agent', 'Admin'),
('AO888', 'Ono', 'Yuji', '1999-09-09', '45 Boulevard Central', 'Yujio@hotmail.fr', 'Lupin52', 'Agent', 'SuperAdmin'),
('ID124', 'Darce', 'Clothilde', '1999-09-09', '45 Rue du monde', 'Clo99@free.fr', 'C09', 'Agent', 'Admin'),
('IE556', 'Esthello', 'Jean', '1999-09-09', '1 Rue du temple', 'Jje@hotmail.fr', 'Jj56', 'Medecin', 'Utilisateur'),
('MF987', 'Food', 'Kelvin', '1999-09-09', '15 Rue des medecins', 'FoodK@gmail.com', 'Ffk', 'Medecin', 'Utilisateur'),
('MG564', 'Ghor', 'Ian', '1999-09-09', '35 Rue vide', 'Gian@gmail.com', 'Gimp', 'Medecin', 'Utilisateur'),
('TJ111', 'Jass', 'Jean', '1999-09-09', '45 Avenue Montparnasse', 'JJ@gmail.com', 'J5678', 'Technicien', 'Utilisateur'),
('TT156', 'Thiollier', 'Fiona', '1999-09-09', '12 Rue du vide', 'FionaT@gmail.com', '0506F', 'Technicien', 'Utilisateur');

-- --------------------------------------------------------

--
-- Structure de la table `type_appareil`
--

CREATE TABLE `type_appareil` (
  `nom_type_appareil` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `type_appareil`
--

INSERT INTO `type_appareil` (`nom_type_appareil`) VALUES
('canne'),
('chaise'),
('deambulateur'),
('fauteuil roulant'),
('lit'),
('prothese');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `appareil`
--
ALTER TABLE `appareil`
  ADD PRIMARY KEY (`idappareil`),
  ADD KEY `fkidpatap` (`idPatient`);

--
-- Index pour la table `consultation`
--
ALTER TABLE `consultation`
  ADD PRIMARY KEY (`idcons`),
  ADD KEY `fkmedcp` (`idMedecin`),
  ADD KEY `idPatient` (`idPatient`,`idMedecin`) USING BTREE;

--
-- Index pour la table `malade`
--
ALTER TABLE `malade`
  ADD KEY `fkidpatimp` (`idPatient`),
  ADD KEY `fkidconsmc` (`idCons`);

--
-- Index pour la table `pathologie`
--
ALTER TABLE `pathologie`
  ADD PRIMARY KEY (`nom`,`idPathologie`);

--
-- Index pour la table `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`idPatient`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `personnel`
--
ALTER TABLE `personnel`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `type_appareil`
--
ALTER TABLE `type_appareil`
  ADD PRIMARY KEY (`nom_type_appareil`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `appareil`
--
ALTER TABLE `appareil`
  MODIFY `idappareil` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `appareil`
--
ALTER TABLE `appareil`
  ADD CONSTRAINT `fkidpatap` FOREIGN KEY (`idPatient`) REFERENCES `patient` (`idPatient`) ON DELETE CASCADE;

--
-- Contraintes pour la table `consultation`
--
ALTER TABLE `consultation`
  ADD CONSTRAINT `fkmedcp` FOREIGN KEY (`idMedecin`) REFERENCES `personnel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fkpatcp` FOREIGN KEY (`idPatient`) REFERENCES `patient` (`idPatient`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `malade`
--
ALTER TABLE `malade`
  ADD CONSTRAINT `fkidconsmc` FOREIGN KEY (`idcons`) REFERENCES `consultation` (`idcons`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fkidpatimp` FOREIGN KEY (`idpatient`) REFERENCES `patient` (`idPatient`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

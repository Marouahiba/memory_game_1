-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 02 mai 2026 à 19:16
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `memory_game`
--

-- --------------------------------------------------------

--
-- Structure de la table `joueur`
--

CREATE TABLE `joueur` (
  `id` bigint(20) NOT NULL,
  `pseudo` varchar(255) NOT NULL,
  `mot_de_passe` varchar(255) NOT NULL,
  `date_inscription` datetime NOT NULL,
  `dateInscription` datetime(6) DEFAULT NULL,
  `motDePasse` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `joueur`
--

INSERT INTO `joueur` (`id`, `pseudo`, `mot_de_passe`, `date_inscription`, `dateInscription`, `motDePasse`) VALUES
(1, 'chanez', '$2a$10$ZxoZsWY9BeUSAcDZJh7Bb.zWsnBGx0khGjt2AS.V4KDo588HXFJGy', '2026-04-23 15:12:06', NULL, ''),
(3, 'imene', '$2a$10$LcYcoUXoomWNaLpeJPg0HeNFCOVj2g0.ftZxfsjOJgdrDpofacL/W', '2026-04-29 15:21:03', NULL, '');

-- --------------------------------------------------------

--
-- Structure de la table `partie`
--

CREATE TABLE `partie` (
  `id` bigint(20) NOT NULL,
  `cartes` longtext DEFAULT NULL,
  `coups` int(11) NOT NULL,
  `date_sauvegarde` datetime(6) DEFAULT NULL,
  `niveau` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  `id_joueur` bigint(20) NOT NULL,
  `dateSauvegarde` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `partie`
--

INSERT INTO `partie` (`id`, `cartes`, `coups`, `date_sauvegarde`, `niveau`, `score`, `id_joueur`, `dateSauvegarde`) VALUES
(1, '[{\"valeur\":\"/images/img4.jpg\",\"retournee\":true},{\"valeur\":\"/images/img4.jpg\",\"retournee\":true},{\"valeur\":\"/images/img6.jpg\",\"retournee\":true},{\"valeur\":\"/images/img3.jpg\",\"retournee\":false},{\"valeur\":\"/images/img5.jpg\",\"retournee\":true},{\"valeur\":\"/images/img2.jpg\",\"retournee\":false},{\"valeur\":\"/images/img1.jpg\",\"retournee\":true},{\"valeur\":\"/images/img8.jpg\",\"retournee\":false},{\"valeur\":\"/images/img6.jpg\",\"retournee\":true},{\"valeur\":\"/images/img3.jpg\",\"retournee\":false},{\"valeur\":\"/images/img7.jpg\",\"retournee\":true},{\"valeur\":\"/images/img1.jpg\",\"retournee\":true},{\"valeur\":\"/images/img5.jpg\",\"retournee\":true},{\"valeur\":\"/images/img7.jpg\",\"retournee\":true},{\"valeur\":\"/images/img8.jpg\",\"retournee\":false},{\"valeur\":\"/images/img2.jpg\",\"retournee\":false}]', 13, '2026-04-23 15:17:05.000000', 2, 50, 1, NULL),
(3, '[{\"valeur\":\"/images/img2.jpg\",\"retournee\":false},{\"valeur\":\"/images/img12.jpg\",\"retournee\":false},{\"valeur\":\"/images/img3.jpg\",\"retournee\":false},{\"valeur\":\"/images/img1.jpg\",\"retournee\":false},{\"valeur\":\"/images/img8.jpg\",\"retournee\":false},{\"valeur\":\"/images/img11.jpg\",\"retournee\":false},{\"valeur\":\"/images/img7.jpg\",\"retournee\":false},{\"valeur\":\"/images/img5.jpg\",\"retournee\":false},{\"valeur\":\"/images/img11.jpg\",\"retournee\":false},{\"valeur\":\"/images/img6.jpg\",\"retournee\":false},{\"valeur\":\"/images/img5.jpg\",\"retournee\":false},{\"valeur\":\"/images/img2.jpg\",\"retournee\":false},{\"valeur\":\"/images/img4.jpg\",\"retournee\":false},{\"valeur\":\"/images/img12.jpg\",\"retournee\":false},{\"valeur\":\"/images/img3.jpg\",\"retournee\":false},{\"valeur\":\"/images/img8.jpg\",\"retournee\":false},{\"valeur\":\"/images/img10.jpg\",\"retournee\":true},{\"valeur\":\"/images/img1.jpg\",\"retournee\":false},{\"valeur\":\"/images/img7.jpg\",\"retournee\":false},{\"valeur\":\"/images/img10.jpg\",\"retournee\":true},{\"valeur\":\"/images/img9.jpg\",\"retournee\":false},{\"valeur\":\"/images/img9.jpg\",\"retournee\":false},{\"valeur\":\"/images/img4.jpg\",\"retournee\":false},{\"valeur\":\"/images/img6.jpg\",\"retournee\":false}]', 4, '2026-04-29 15:24:28.000000', 3, 10, 3, NULL),
(4, '[{\"valeur\":\"/images/img2.jpg\",\"retournee\":false},{\"valeur\":\"/images/img12.jpg\",\"retournee\":false},{\"valeur\":\"/images/img3.jpg\",\"retournee\":false},{\"valeur\":\"/images/img1.jpg\",\"retournee\":false},{\"valeur\":\"/images/img8.jpg\",\"retournee\":false},{\"valeur\":\"/images/img11.jpg\",\"retournee\":false},{\"valeur\":\"/images/img7.jpg\",\"retournee\":false},{\"valeur\":\"/images/img5.jpg\",\"retournee\":false},{\"valeur\":\"/images/img11.jpg\",\"retournee\":false},{\"valeur\":\"/images/img6.jpg\",\"retournee\":false},{\"valeur\":\"/images/img5.jpg\",\"retournee\":false},{\"valeur\":\"/images/img2.jpg\",\"retournee\":false},{\"valeur\":\"/images/img4.jpg\",\"retournee\":false},{\"valeur\":\"/images/img12.jpg\",\"retournee\":false},{\"valeur\":\"/images/img3.jpg\",\"retournee\":false},{\"valeur\":\"/images/img8.jpg\",\"retournee\":false},{\"valeur\":\"/images/img10.jpg\",\"retournee\":true},{\"valeur\":\"/images/img1.jpg\",\"retournee\":false},{\"valeur\":\"/images/img7.jpg\",\"retournee\":false},{\"valeur\":\"/images/img10.jpg\",\"retournee\":true},{\"valeur\":\"/images/img9.jpg\",\"retournee\":false},{\"valeur\":\"/images/img9.jpg\",\"retournee\":false},{\"valeur\":\"/images/img4.jpg\",\"retournee\":false},{\"valeur\":\"/images/img6.jpg\",\"retournee\":false}]', 4, '2026-04-29 15:24:28.000000', 3, 10, 3, NULL);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `joueur`
--
ALTER TABLE `joueur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pseudo` (`pseudo`);

--
-- Index pour la table `partie`
--
ALTER TABLE `partie`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_partie_joueur` (`id_joueur`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `joueur`
--
ALTER TABLE `joueur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `partie`
--
ALTER TABLE `partie`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `partie`
--
ALTER TABLE `partie`
  ADD CONSTRAINT `fk_partie_joueur` FOREIGN KEY (`id_joueur`) REFERENCES `joueur` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

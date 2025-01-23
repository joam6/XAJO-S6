-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Temps de generació: 18-11-2024 a les 12:07:50
-- Versió del servidor: 10.3.31-MariaDB-0+deb10u1
-- Versió de PHP: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de dades: `sailingdb`
--

-- --------------------------------------------------------

--
-- Estructura de la taula `actions`
--

CREATE TABLE `actions` (
  `type` varchar(31) COLLATE utf8mb4_spanish_ci NOT NULL,
  `id` bigint(20) NOT NULL,
  `comments` varchar(255) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp(),
  `old_date` date DEFAULT NULL,
  `old_departure` time(6) DEFAULT NULL,
  `performer_username` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `trip_id` bigint(20) NOT NULL,
  `reason` varchar(255) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `new_date` date DEFAULT NULL,
  `new_departure` time(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Bolcament de dades per a la taula `actions`
--

INSERT INTO `actions` (`type`, `id`, `comments`, `date`, `old_date`, `old_departure`, `performer_username`, `trip_id`, `reason`, `new_date`, `new_departure`) VALUES
('BOOKING', 1, NULL, '2024-10-19 14:46:04', NULL, NULL, 'lola', 1, NULL, NULL, NULL),
('BOOKING', 2, NULL, '2024-10-19 14:46:04', NULL, NULL, 'robert', 2, NULL, NULL, NULL),
('BOOKING', 3, NULL, '2024-10-19 14:46:22', NULL, NULL, 'maria', 3, NULL, NULL, NULL),
('CANCELLATION', 4, NULL, '2024-10-19 14:56:04', NULL, NULL, 'lola', 1, NULL, NULL, NULL),
('DONE', 5, 'Finalitzat tot OK', '2024-10-20 14:56:04', NULL, NULL, 'alex', 3, NULL, NULL, NULL),
('BOOKING', 6, NULL, '2024-11-04 18:38:03', NULL, NULL, 'lola', 4, NULL, NULL, NULL),
('BOOKING', 7, NULL, '2024-11-07 11:04:11', NULL, NULL, 'lola', 6, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de la taula `departures`
--

CREATE TABLE `departures` (
  `id` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `departure` time(6) NOT NULL,
  `trip_type_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Bolcament de dades per a la taula `departures`
--

INSERT INTO `departures` (`id`, `date`, `departure`, `trip_type_id`) VALUES
(1, '2024-10-30', '09:30:00.000000', 1),
(2, '2024-10-29', '15:30:00.000000', 5),
(8, '2024-11-13', '07:00:00.000000', 7),
(11, '2024-11-18', '11:00:00.000000', 7);

-- --------------------------------------------------------

--
-- Estructura de la taula `trips`
--

CREATE TABLE `trips` (
  `id` bigint(20) NOT NULL,
  `places` int(11) NOT NULL,
  `client_username` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `departure_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Bolcament de dades per a la taula `trips`
--

INSERT INTO `trips` (`id`, `places`, `client_username`, `departure_id`) VALUES
(1, 3, 'lola', 1),
(2, 2, 'robert', 1),
(3, 5, 'maria', 2),
(4, 2, 'lola', 8),
(6, 3, 'lola', 11);

-- --------------------------------------------------------

--
-- Estructura de la taula `trip_types`
--

CREATE TABLE `trip_types` (
  `id` bigint(20) NOT NULL,
  `category` enum('GROUP','PRIVATE') COLLATE utf8mb4_spanish_ci NOT NULL,
  `departures` varchar(255) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `duration` int(11) NOT NULL,
  `max_places` int(11) DEFAULT NULL,
  `price` double NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Bolcament de dades per a la taula `trip_types`
--

INSERT INTO `trip_types` (`id`, `category`, `departures`, `description`, `duration`, `max_places`, `price`, `title`) VALUES
(1, 'GROUP', '9:30;13:30;17:30', '1h Sailing Experience', 1, 9, 30, '1 Hour Sailing Tour'),
(2, 'GROUP', '11:30;15:30', 'Relaxing 2h Sailing Tour', 2, 9, 45, '2 Hours Sailing Tour'),
(3, 'GROUP', '19:30', 'Sensational Sunset Sail', 2, 9, 50, 'Sunset Sail'),
(4, 'GROUP', '12:30', 'Watch America’s Cup Barcelona 2024 Live (shared)', 6, 11, 350, 'Watch Live: America’s Cup'),
(5, 'PRIVATE', NULL, 'Private Sailing Tour (max. 9)', 1, 9, 200, 'Private Sailing Tour (max. 9)'),
(6, 'PRIVATE', NULL, 'Private Sailing Tour (max.11)', 6, 11, 300, 'Private Sailing Tour (max.11)'),
(7, 'PRIVATE', NULL, 'Private Luxury Sailing Tour (max.12)', 2, 12, 350, 'Private Luxury Sailing Tour (max.12)');

-- --------------------------------------------------------

--
-- Estructura de la taula `users`
--

CREATE TABLE `users` (
  `role` varchar(31) COLLATE utf8mb4_spanish_ci NOT NULL,
  `username` varchar(25) COLLATE utf8mb4_spanish_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_spanish_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `phone` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Bolcament de dades per a la taula `users`
--

INSERT INTO `users` (`role`, `username`, `password`, `full_name`, `phone`) VALUES
('ADMIN', 'alex', '$2a$10$w2USjLND2dheaXiXFSRgo.pIOiYUBecdmFlFnxHN8HRxwFYuYp3g6', NULL, NULL),
('CLIENT', 'genis', '$2a$10$fN.nOfWlRY/LLotIWiseoeh/foQ1vFCY9bnpXmK3k8.VwW7F1xoPi', 'Genís Esteve i Prats', 633151523),
('ADMIN', 'laia', '$2a$10$EwsBI6trHD56ncjlsxAmwuic5R/qAzx6AyekBpCafndN.CiFuwJjK', NULL, NULL),
('CLIENT', 'lola', '$2a$10$/oyUYwpxe1m40Qe070kO5u44g50XrQe0htB08nwOXcU7/PMO1TBKS', 'Lola Valls i Vilalta', 633251101),
('CLIENT', 'maria', '$2a$10$EogCF6kJDxTPsfQFciZjROaSBd/8Ok3orVe49KdEebVdyVTYrCKs2', 'Maria Lopez i Castells', 633352133),
('ADMIN', 'marta', '$2a$10$0TIRK3JqDKs.4HXqN//yweu9EFdOCn64p3X1zGIkeGhKtD8CQ73ou', NULL, NULL),
('CLIENT', 'raul', '$2a$10$Zt92wjlBEPx2zXwdTfA4ZeM2cFAAX4MXY4y9y1BKMEZmYbNh.8dz6', 'Raul Casanova i Ferrer', 633154041),
('CLIENT', 'robert', '$2a$10$H1S18hqeIbIoPgVU7ECURu6nsitQ0/sGSEJ9Z0Dw6rBV/bloAmCTS', 'Robert Planes i Pujol', 633356101),
('CLIENT', 'toni', '$2a$10$T1lgKgp5XiQAuvTiq4alQeWkgCVHpsHVgmgk/X7wIkJwHypR6TMP2', 'Antoni Bosc i Cases', 633250108);

--
-- Índexs per a les taules bolcades
--

--
-- Índexs per a la taula `actions`
--
ALTER TABLE `actions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `trip_x_date` (`trip_id`,`date`),
  ADD KEY `type` (`type`),
  ADD KEY `FKfg2ab9wjrg68jliat7wp1te1o` (`performer_username`);

--
-- Índexs per a la taula `departures`
--
ALTER TABLE `departures`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `triptype_date_departure` (`trip_type_id`,`date`,`departure`);

--
-- Índexs per a la taula `trips`
--
ALTER TABLE `trips`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKafyhcr89q11frd05a9d8b9j4a` (`client_username`),
  ADD KEY `FKssv2rh4g5jh32yc3fw7ornkb9` (`departure_id`);

--
-- Índexs per a la taula `trip_types`
--
ALTER TABLE `trip_types`
  ADD PRIMARY KEY (`id`);

--
-- Índexs per a la taula `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`),
  ADD KEY `role` (`role`),
  ADD KEY `full_name` (`full_name`),
  ADD KEY `role_x_full_name` (`role`,`full_name`);

--
-- AUTO_INCREMENT per les taules bolcades
--

--
-- AUTO_INCREMENT per la taula `actions`
--
ALTER TABLE `actions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT per la taula `departures`
--
ALTER TABLE `departures`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT per la taula `trips`
--
ALTER TABLE `trips`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT per la taula `trip_types`
--
ALTER TABLE `trip_types`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restriccions per a les taules bolcades
--

--
-- Restriccions per a la taula `actions`
--
ALTER TABLE `actions`
  ADD CONSTRAINT `FK36uefrlow4b2rbuv4flbennyv` FOREIGN KEY (`trip_id`) REFERENCES `trips` (`id`),
  ADD CONSTRAINT `FKfg2ab9wjrg68jliat7wp1te1o` FOREIGN KEY (`performer_username`) REFERENCES `users` (`username`);

--
-- Restriccions per a la taula `departures`
--
ALTER TABLE `departures`
  ADD CONSTRAINT `FKdurn176cdw0q76r320a0hc7a4` FOREIGN KEY (`trip_type_id`) REFERENCES `trip_types` (`id`);

--
-- Restriccions per a la taula `trips`
--
ALTER TABLE `trips`
  ADD CONSTRAINT `FKafyhcr89q11frd05a9d8b9j4a` FOREIGN KEY (`client_username`) REFERENCES `users` (`username`),
  ADD CONSTRAINT `FKssv2rh4g5jh32yc3fw7ornkb9` FOREIGN KEY (`departure_id`) REFERENCES `departures` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

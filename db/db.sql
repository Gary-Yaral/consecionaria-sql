/*
SQLyog Ultimate v11.11 (64 bit)
MySQL - 5.5.5-10.4.24-MariaDB : Database - concesionaria
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`concesionaria` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `concesionaria`;

/*Table structure for table `accidentes` */

DROP TABLE IF EXISTS `accidentes`;

CREATE TABLE `accidentes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` date DEFAULT NULL,
  `lugar` varchar(200) DEFAULT NULL,
  `descripción` varchar(10000) DEFAULT NULL,
  `patente` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `accidentes_ibfk_1` (`patente`),
  CONSTRAINT `accidentes_ibfk_1` FOREIGN KEY (`patente`) REFERENCES `vehiculos` (`patente`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

/*Data for the table `accidentes` */

insert  into `accidentes`(`id`,`fecha`,`lugar`,`descripción`,`patente`) values (1,'2022-07-13','Babahoyo - Puertas Negras','Choque frontal con bus','ERF34'),(2,'2022-07-09','Via a Quevedo - Zapotal','Pérdida de frenos y encunetamineto','ERF34');

/*Table structure for table `admins` */

DROP TABLE IF EXISTS `admins`;

CREATE TABLE `admins` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) DEFAULT NULL,
  `usuario` varchar(50) DEFAULT NULL,
  `clave` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `admins` */

insert  into `admins`(`id`,`nombre`,`usuario`,`clave`) values (1,'super','ABC123','pass2022');

/*Table structure for table `clientes` */

DROP TABLE IF EXISTS `clientes`;

CREATE TABLE `clientes` (
  `documento` varchar(200) NOT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `domicilio` varchar(10000) DEFAULT NULL,
  `telefono` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`documento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `clientes` */

insert  into `clientes`(`documento`,`nombre`,`domicilio`,`telefono`) values ('1234344564','Pedro Angel Murillo Estrada','Vergeles 6','0975564877');

/*Table structure for table `polizas` */

DROP TABLE IF EXISTS `polizas`;

CREATE TABLE `polizas` (
  `nro_poliza` int(11) NOT NULL AUTO_INCREMENT,
  `patente` varchar(200) DEFAULT NULL,
  `vendedor` varchar(200) DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `duracion` float DEFAULT NULL,
  `tipo` int(11) DEFAULT NULL,
  PRIMARY KEY (`nro_poliza`),
  KEY `tipo_fk` (`tipo`),
  KEY `polizas_ibfk_1` (`patente`),
  KEY `polizas_ibfk_2` (`vendedor`),
  CONSTRAINT `polizas_ibfk_1` FOREIGN KEY (`patente`) REFERENCES `vehiculos` (`patente`) ON UPDATE CASCADE,
  CONSTRAINT `polizas_ibfk_2` FOREIGN KEY (`vendedor`) REFERENCES `vendedor` (`documento`) ON UPDATE CASCADE,
  CONSTRAINT `tipo_fk` FOREIGN KEY (`tipo`) REFERENCES `tipo_polizas` (`id`) ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

/*Data for the table `polizas` */

insert  into `polizas`(`nro_poliza`,`patente`,`vendedor`,`fecha_inicio`,`duracion`,`tipo`) values (1,'ERF34','09574723477','2022-07-13',3,1),(2,'EFRT456','09574723477','2022-07-09',8,1);

/*Table structure for table `sucursal` */

DROP TABLE IF EXISTS `sucursal`;

CREATE TABLE `sucursal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nro` int(11) DEFAULT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `domicilio` varchar(200) DEFAULT NULL,
  `telefono` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

/*Data for the table `sucursal` */

insert  into `sucursal`(`id`,`nro`,`nombre`,`domicilio`,`telefono`) values (1,1,'Seguros Fernandez S.A','Pedro Carbo y 5 de Junio','0522444522'),(2,2,'Seguros Fernandez 2','Guayaquil, La alborada','0533243529');

/*Table structure for table `tipo_polizas` */

DROP TABLE IF EXISTS `tipo_polizas`;

CREATE TABLE `tipo_polizas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

/*Data for the table `tipo_polizas` */

insert  into `tipo_polizas`(`id`,`nombre`) values (1,'Invalidez total'),(4,'Accidente y fallecimiento');

/*Table structure for table `usuarios` */

DROP TABLE IF EXISTS `usuarios`;

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(200) DEFAULT NULL,
  `clave` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

/*Data for the table `usuarios` */

insert  into `usuarios`(`id`,`usuario`,`clave`) values (1,'usuario_1','1234');

/*Table structure for table `vehiculos` */

DROP TABLE IF EXISTS `vehiculos`;

CREATE TABLE `vehiculos` (
  `patente` varchar(200) NOT NULL,
  `nro_motor` varchar(200) DEFAULT NULL,
  `marca` varchar(200) DEFAULT NULL,
  `modelo` varchar(200) DEFAULT NULL,
  `documento_dueno` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`patente`),
  KEY `cliente_fk` (`documento_dueno`),
  CONSTRAINT `cliente_fk` FOREIGN KEY (`documento_dueno`) REFERENCES `clientes` (`documento`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `vehiculos` */

insert  into `vehiculos`(`patente`,`nro_motor`,`marca`,`modelo`,`documento_dueno`) values ('EFRT456','43546','Mazda Fiesta','Mazda Fiesta','1234344564'),('ERF34','3247','Ford','Ford','1234344564');

/*Table structure for table `vendedor` */

DROP TABLE IF EXISTS `vendedor`;

CREATE TABLE `vendedor` (
  `documento` varchar(200) NOT NULL,
  `nombre` varchar(200) DEFAULT NULL,
  `domicilio` varchar(1000) DEFAULT NULL,
  `telefono` varchar(200) DEFAULT NULL,
  `nro_sucursal` int(11) DEFAULT NULL,
  PRIMARY KEY (`documento`),
  KEY `nro_sucursal` (`nro_sucursal`),
  CONSTRAINT `vendedor_ibfk_1` FOREIGN KEY (`nro_sucursal`) REFERENCES `sucursal` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `vendedor` */

insert  into `vendedor`(`documento`,`nombre`,`domicilio`,`telefono`,`nro_sucursal`) values ('09574723477','Gabriel Santiago Flores Mendez','Guayaquil, 10 de agosto y olmedo','0973562332',2),('1203584582','Luis Andrés Carriel Montero','Sto. Domingo','0983735325',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

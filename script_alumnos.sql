-- Base de datos y tabla usadas por el programa (SQL Server)
IF DB_ID('AvanzadaII') IS NULL
    CREATE DATABASE AvanzadaII;
GO

USE AvanzadaII;
GO

IF OBJECT_ID('Alumnos', 'U') IS NULL
CREATE TABLE Alumnos (
    id     INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL
);
GO

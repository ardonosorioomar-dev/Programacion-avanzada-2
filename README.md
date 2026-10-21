# CRUD de Alumnos con Java y JDBC (Primer parcial)

Aplicación de consola en Java que se conecta a SQL Server mediante JDBC y permite
**insertar, listar, actualizar y eliminar** alumnos de la tabla `Alumnos`.

## Archivos
- `Hola_Mundo.java`: menú principal y operaciones del CRUD (usa `PreparedStatement`).
- `ConnectionDB.java`: conexión a la base de datos.
- `script_alumnos.sql`: crea la base de datos `AvanzadaII` y la tabla `Alumnos`.

## Requisitos
- Java JDK 17 o superior (o el que use tu proyecto).
- SQL Server en `localhost:1433`.
- Driver `mssql-jdbc` agregado al proyecto.

## Cómo ejecutarlo
1. Ejecuta `script_alumnos.sql` en SQL Server.
2. Define la contraseña de tu usuario de SQL Server (no está en el código):
   - Variable de entorno: `DB_PASS=tu_contraseña` (opcionalmente `DB_USER`), o
   - Opción de la JVM: `-DDB_PASS=tu_contraseña`.
3. Ejecuta la clase `org.example.Hola_Mundo`.

## Menú
1. Ingresar nuevo alumno
2. Listar todos los alumnos
3. Actualizar un registro
4. Eliminar un alumno
5. Salir

## Autor
[Tu nombre] - [Clase] - [Docente]

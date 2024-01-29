# API de Gestión de Biblioteca

Esta es una API simple de gestión de biblioteca que proporciona endpoints para realizar operaciones CRUD (Crear, Leer, Actualizar, Borrar) en libros almacenados en un archivo JSON.

## Endpoints Disponibles

### Obtener todos los libros

**Endpoint:** `GET /books`

Este endpoint devuelve todos los libros almacenados en el sistema.

### Obtener libro por coincidencia

**Endpoint:** `GET /bookByLike`

Este endpoint permite obtener un libro por coincidencia de libro, autor o fecha.

### Registrar un nuevo libro

**Endpoint:** `POST /registerBook`

Este endpoint permite registrar un nuevo libro en el sistema. Se espera un objeto JSON en el cuerpo de la solicitud con los detalles del libro.

### Actualizar un libro existente

**Endpoint:** `PUT /updateBook`

Este endpoint permite actualizar un libro existente en el sistema. Se espera un objeto JSON en el cuerpo de la solicitud con los detalles del libro a actualizar.

### Borrar un libro

**Endpoint:** `DELETE /deleteBook`

Este endpoint permite borrar un libro del sistema. Se espera un objeto JSON en el cuerpo de la solicitud con los detalles del libro a borrar.

## Testeo de la API

Se comparte una coleccion de Postman con los endpoints disponibles para sus pruebas.

# Ejecucion en entorno local

Una vez descargado el codigo fuente, se puede compilar con el siguiente comando.
#### `mvn clean compile package`

Al finalizar la compilacion del proyecto, se puede ejecutar la aplicacion, para ello se necesitan dos archivos:
#### `ms-library-management-1.0-SNAPSHOT-fat.jar`
#### `ms-library-management.json`

Posicionandose a nivel raiz del proyecto en la linea de comando (ejemplo: C:\workspace\projects\app-library-v1).
Se puede ejecutar el siguiente comando:

#### ejemplo:
#### `C:\workspace\projects\app-library-v1> java -jar .\ms-library-management\target\ms-library-management-1.0-SNAPSHOT-fat.jar -conf .\ms-library-management\ms-library-management.json`

#### comando:
#### `java -jar .\ms-library-management\target\ms-library-management-1.0-SNAPSHOT-fat.jar -conf .\ms-library-management\ms-library-management.json`
package com.nav.services.ms_library_management.repository;

import com.nav.services.ms_library_management.controller.LibraryController;
import com.nav.services.ms_library_management.model.Book;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Clase para uso de acceso a datos
 */
public class BookRepository implements CRUDRepository<Book> {

  private final Vertx vertx;

  private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

  public BookRepository(Vertx vertx) {
    this.vertx = vertx;
  }

  /**
   * Metodo para obtener la configuracion para cargar el contenido del archivo JSON
   * @param filePath
   * @return ConfigRetriever
   */
  private ConfigRetriever createFileConfigRetriever(String filePath) {
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
        .setType("file")
        .setConfig(new JsonObject().put("path", filePath));

    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
    return ConfigRetriever.create(vertx, options);
  }

  /**
   * Registro de libro en la BD
   * @param book entity
   * @return Future<Book>
   */
  @Override
  public Future<Book> create(Book book) {
    Promise<Book> promise = Promise.promise();

    // Obtener la lista actual de libros
    readBooksFromFile().onComplete(readAr -> {
      if (readAr.succeeded()) {
        List<Book> books = readAr.result();

        // Agregar el nuevo libro a la lista
        books.add(book);

        // Escribir la lista actualizada en el archivo
        writeBooksToFile(books).onComplete(writeAr -> {
          if (writeAr.succeeded()) {
            // Completar la promesa con el libro creado
            promise.complete(book);
          } else {
            // Manejar error al escribir en el archivo
            promise.fail(writeAr.cause());
          }
        });
      } else {
        // Manejar error al obtener la lista de libros
        promise.fail(readAr.cause());
      }
    });

    return promise.future();
  }

  /**
   * Se lee la lista de libros actuales del archivo
   * @return Future<List<Book>>
   */
  private Future<List<Book>> readBooksFromFile() {
    Promise<List<Book>> readPromise = Promise.promise();

    // Ruta del archivo JSON
    Path filePath = Paths.get("db/books.json");

    // Leer el contenido del archivo
    vertx.fileSystem().readFile(filePath.toString(), readAr -> {
      if (readAr.succeeded()) {
        try {
          String fileContent = readAr.result().toString();
          JsonArray jsonArray = new JsonArray(fileContent);

          // Convertir el JsonArray a una lista de objetos Book
          List<Book> books = jsonArray.stream()
              .map(json -> ((JsonObject) json).mapTo(Book.class))
              .collect(Collectors.toList());
          readPromise.complete(books);
        } catch (Exception e) {
          readPromise.fail(e);
        }
      } else {
        readPromise.fail(readAr.cause());
      }
    });

    return readPromise.future();
  }

  /**
   * Metodo de utilidad que realiza la escritura en el archivo
   * @param books entity
   * @return Future<Void>
   */
  private Future<Void> writeBooksToFile(List<Book> books) {
    Promise<Void> writePromise = Promise.promise();

    // Ruta del archivo JSON
    Path filePath = Paths.get("db/books.json");

    // Convertir la lista de libros a un JsonArray
    JsonArray jsonArray = new JsonArray(books.stream().map(JsonObject::mapFrom).collect(Collectors.toList()));

    // Convertir el JsonArray a una cadena y luego a un buffer
    String jsonString = jsonArray.encode();
    Buffer buffer = Buffer.buffer(jsonString);

    // Escribir el buffer en el archivo
    vertx.fileSystem().writeFile(filePath.toString(), buffer, writeAr -> {
      if (writeAr.succeeded()) {
        writePromise.complete();
      } else {
        writePromise.fail(writeAr.cause());
      }
    });

    return writePromise.future();
  }

  /**
   * Metodo generico que permite buscar un libro por medio de libro, autor o fecha
   * @param entity entidad a buscar por medio de los atributos del objeto
   * @return Future<Book>
   */
  @Override
  public Future<Book> findbyLikeObject(String entity) {
    Promise<Book> promise = Promise.promise();

    // Obtener la lista de libros
    findAll().onComplete(ar -> {
      if (ar.succeeded()) {
        List<Book> books = ar.result();

        // Buscar en la lista de libros utilizando Java Stream API
        Optional<Book> matchingBook = books.stream()
            .filter(book -> matches(book, entity))
            .findFirst();

        // Completar la promesa con el resultado de la búsqueda
        promise.complete(matchingBook.orElse(null));
      } else {
        // Fallar la promesa si hay un error al obtener la lista de libros
        promise.fail(ar.cause());
      }
    });

    return promise.future();
  }

  // Metodo para verificar si el libro coincide con el valor proporcionado
  private boolean matches(Book book, String entity) {
    return book.getBookName().equalsIgnoreCase(entity) ||
        book.getAuthor().equalsIgnoreCase(entity) ||
        book.getReleaseDate().equalsIgnoreCase(entity);
  }

  /**
   * Se encuentran todos los libros en el archivo json y se convierten a un objeto para ser manejados
   * @return Future<List<Book>>
   */
  @Override
  public Future<List<Book>> findAll() {
    Promise<List<Book>> promise = Promise.promise();

    // Ruta del archivo JSON
    Path filePath = Paths.get("db/books.json");

    // Leer el contenido del archivo
    vertx.fileSystem().readFile(filePath.toString(), readAr -> {
      if (readAr.succeeded()) {
        try {
          String fileContent = readAr.result().toString();
          JsonArray jsonArray = new JsonArray(fileContent);

          // Convertir el JsonArray a una lista de objetos Book
          List<Book> books = jsonArray.stream()
              .map(json -> ((JsonObject) json).mapTo(Book.class))
              .collect(Collectors.toList());
          promise.complete(books);

        } catch (Exception e) {
          promise.fail(e);
        }
      } else {
        promise.fail(readAr.cause());
      }
    });

    return promise.future();
  }

  /**
   * Actualizacion de un registro con base a su coincidencia
   * @param updatedBook
   * @return Future<Book>
   */
  @Override
  public Future<Book> update(Book updatedBook) {
    Promise<Book> promise = Promise.promise();

    // Obtener la lista actual de libros
    readBooksFromFile().onComplete(readAr -> {
      if (readAr.succeeded()) {
        List<Book> books = readAr.result();

        // Buscar el libro que coincide con el bookName o author del libro a actualizar
        for (Book existingBook : books) {
          if (existingBook.getBookName().equalsIgnoreCase(updatedBook.getBookName())
              || existingBook.getAuthor().equalsIgnoreCase(updatedBook.getAuthor())) {

            // Actualizar el libro existente con los nuevos valores
            existingBook.setBookName(updatedBook.getBookName());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setReleaseDate(updatedBook.getReleaseDate());

            // Escribir la lista actualizada en el archivo
            writeBooksToFile(books).onComplete(writeAr -> {
              if (writeAr.succeeded()) {
                // Completa la promesa con el libro actualizado después de la escritura
                promise.complete(existingBook);
              } else {
                promise.fail(writeAr.cause());
              }
            });

            // Termina la búsqueda después de la primera coincidencia
            return;
          }
        }

        // Si no se encuentra el libro, completar la promesa con un fallo
        promise.fail("Libro no encontrado para actualizar");
      } else {
        // Manejar error al obtener la lista de libros
        promise.fail(readAr.cause());
      }
    });

    return promise.future();
  }

  /**
   * Borrado de un registro con base a su coincidencia
   * @param bookToDelete entidad a borrar
   * @return Future<Book>
   */
  @Override
  public Future<Book> delete(Book bookToDelete) {
    Promise<Book> promise = Promise.promise();

    // Obtener la lista actual de libros
    readBooksFromFile().onComplete(readAr -> {
      if (readAr.succeeded()) {
        List<Book> books = readAr.result();

        // Iterar sobre la lista de libros
        Iterator<Book> iterator = books.iterator();
        while (iterator.hasNext()) {
          Book existingBook = iterator.next();

          // Verificar si el libro coincide con el libro proporcionado
          if (existingBook.equals(bookToDelete)) {
            // Eliminar el libro de la lista
            iterator.remove();

            // Escribir la lista actualizada en el archivo
            writeBooksToFile(books).onComplete(writeAr -> {
              if (writeAr.succeeded()) {
                // Completar la promesa con el libro eliminado
                promise.complete(existingBook);
              } else {
                promise.fail(writeAr.cause());
              }
            });

            // Terminar la búsqueda después de la primera coincidencia
            return;
          }
        }

        // Si no se encuentra el libro, completar la promesa con un fallo
        promise.fail("Libro no encontrado para eliminar");
      } else {
        // Manejar error al obtener la lista de libros
        promise.fail(readAr.cause());
      }
    });

    return promise.future();
  }

}

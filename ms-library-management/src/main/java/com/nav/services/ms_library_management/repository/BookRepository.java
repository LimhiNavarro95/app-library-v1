package com.nav.services.ms_library_management.repository;

import com.nav.services.ms_library_management.controller.LibraryController;
import com.nav.services.ms_library_management.model.Book;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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


  @Override
  public Future<Book> create(Book book) {
    return null;
  }

  @Override
  public Future<Book> findbyLikeObject(String entity) {
    return null;
  }

  /**
   * Se encuentran todos los libros en el archivo json y se convierten a un objeto para ser manejados
   * @return Future<List<Book>>
   */
  @Override
  public Future<List<Book>> findAll() {

    Promise<List<Book>> promise = Promise.promise();

    // Configuración para cargar el contenido del archivo JSON
    ConfigStoreOptions fileStore = new ConfigStoreOptions()
        .setType("file")
        .setConfig(new JsonObject().put("path", "books.json"));

    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);
    ConfigRetriever configRetriever = ConfigRetriever.create(vertx, options);

    // Lectura del contenido del archivo JSON
    try {
      configRetriever.getConfig(ar -> {
        if (ar.succeeded()) {
          List<Book> books = parseJsonArray(ar.result().getJsonArray("books"));
          promise.complete(books);
        } else {
          promise.fail(ar.cause());
        }
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
    }

    return promise.future();

  }

  // Metodo para convertir un JsonArray a una lista de objetos Book
  private List<Book> parseJsonArray(JsonArray jsonArray) {
    return jsonArray.stream()
        .map(json -> ((JsonObject) json).mapTo(Book.class))
        .collect(Collectors.toList());
  }

  @Override
  public Future<?> update(Book book) {
    return null;
  }

  @Override
  public Future<?> delete(String entity) {
    return null;
  }

}

package com.nav.services.ms_library_management.controller;

import com.nav.services.ms_library_management.service.LibraryServiceImpl;
import com.nav.services.ms_library_management.model.Book;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Capa Controller donde se encuentran los endpoints de la aplicacion
 */
public class LibraryController {

  private final LibraryServiceImpl libraryService;

  private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

  public LibraryController(LibraryServiceImpl libraryBusiness) {
    this.libraryService = libraryBusiness;
  }

  /**
   * Endpoint de GET para tener todos los libros almacenados en el JSON
   * @param routingContext objeto con datos de la solicitud entrante
   */
  public void getBooks(RoutingContext routingContext){
    Promise<List<Book>> promise = Promise.promise();

    libraryService.obtainBooksList().onComplete(ar -> {
      if (ar.succeeded()){
        List<Book> books = ar.result();
        promise.complete(books);

        routingContext.response()
            .putHeader("content-type", "application/json")
            .setStatusCode(200)
            .end(Json.encodePrettily(books));
        logger.info("proceso finalizado correctamente de obtencion de libros");

      } else {
        routingContext.response()
            .putHeader("content-type", "application/json")
            .setStatusCode(400)
            .end();
        logger.info("error en el proceso");
      }
    });
  }

  /**
   * Endpoint de GET obtener un libro almacenados en el JSON por medio de libro, autor o fecha
   * @param routingContext objeto con datos de la solicitud entrante
   */
  public void getBookByLikeObject(RoutingContext routingContext){

    String entity = routingContext.request().getParam("entity");

    Promise<Book> promise = Promise.promise();

    libraryService.findBookByLikeObject(entity).onComplete(ar -> {
      if (ar.succeeded()) {
        Book bookFinded = ar.result();
        promise.complete(bookFinded);

        //dependiendo si fue encontrado el libro buscado, se establece el tipo de respuesta
        if (!Objects.isNull(bookFinded)){
          routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(Json.encodePrettily(bookFinded));
        } else {
          routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(204)
              .end(Json.encodePrettily(bookFinded));
        }

        logger.info("proceso finalizado correctamente de obtencion de libro por Objeto");
      } else {
        routingContext.response()
            .putHeader("content-type", "application/json")
            .setStatusCode(400)
            .end();
        logger.info("error en el proceso");
      }
    });
  }

  /**
   * Endpoint de POST para registrar un libro
   * @param routingContext objeto con datos de la solicitud entrante, en request body se encuentra el entity
   */
  public void postRegisterBook(RoutingContext routingContext) {
    try {
      // Se obtiene el cuerpo de la solicitud como JSON y se convierte a un objeto Book
      Book book = Json.decodeValue(routingContext.getBodyAsString(), Book.class);

      // Llama al método correspondiente de tu servicio
      libraryService.registerBook(book).onComplete(ar -> {
        if (ar.succeeded()) {
          Book bookRegistered = ar.result();
          routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(Json.encodePrettily(bookRegistered));
          logger.info("proceso finalizado correctamente de registro el libro ->\n" + bookRegistered.toString());
        } else {
          routingContext.response()
              .setStatusCode(400)
              .end();
        }
      });

    } catch (Exception e) {
      // Manejar cualquier excepción que pueda ocurrir al decodificar el JSON
      routingContext.response()
          .setStatusCode(400)
          .end("Error al procesar el cuerpo de la solicitud");
    }
  }

  /**
   * Endpoint de PUT para actualizar un libro
   * @param routingContext objeto con datos de la solicitud entrante, en request body se encuentra el entity
   */
  public void putModifyBook(RoutingContext routingContext) {
    try {
      // Se obtiene el cuerpo de la solicitud como JSON y se convierte a un objeto Book
      Book book = Json.decodeValue(routingContext.getBodyAsString(), Book.class);

      // Llama al método correspondiente de tu servicio
      libraryService.updateBook(book).onComplete(ar -> {
        if (ar.succeeded()) {
          Book bookUpdated = ar.result();
          routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(Json.encodePrettily(bookUpdated));
          logger.info("proceso finalizado correctamente de actualizar el libro ->\n" + bookUpdated.toString());
        } else {
          routingContext.response()
              .setStatusCode(400)
              .end();
        }
      });

    } catch (Exception e) {
      // Manejar cualquier excepción que pueda ocurrir al decodificar el JSON
      routingContext.response()
          .setStatusCode(400)
          .end("Error al procesar el cuerpo de la solicitud");
    }
  }

  /**
   * Endpoint de DELETE para borrar un libro
   * @param routingContext objeto con datos de la solicitud entrante, en request body se encuentra el entity
   */
  public void deleteBook(RoutingContext routingContext) {
    try {
      // Se obtiene el cuerpo de la solicitud como JSON y se convierte a un objeto Book
      Book book = Json.decodeValue(routingContext.getBodyAsString(), Book.class);

      // Llama al método correspondiente de tu servicio
      libraryService.deleteBook(book).onComplete(ar -> {
        if (ar.succeeded()) {
          Book bookUpdated = ar.result();
          routingContext.response()
              .putHeader("content-type", "application/json")
              .setStatusCode(200)
              .end(Json.encodePrettily(bookUpdated));
          logger.info("proceso finalizado correctamente de borrar el libro ->\n" + bookUpdated.toString());
        } else {
          routingContext.response()
              .setStatusCode(400)
              .end();
        }
      });

    } catch (Exception e) {
      // Manejar cualquier excepción que pueda ocurrir al decodificar el JSON
      routingContext.response()
          .setStatusCode(400)
          .end("Error al procesar el cuerpo de la solicitud");
    }
  }

}

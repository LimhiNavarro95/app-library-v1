package com.nav.services.ms_library_management.controller;

import com.nav.services.ms_library_management.service.LibraryServiceImpl;
import com.nav.services.ms_library_management.model.Book;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

        routingContext.response()
            .putHeader("content-type", "application/json")
            .setStatusCode(200)
            .end(Json.encodePrettily(bookFinded));
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

}

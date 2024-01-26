package com.nav.services.ms_library_management.controller;

import com.nav.services.ms_library_management.business.LibraryBusiness;
import com.nav.services.ms_library_management.model.Book;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryController {

  private final LibraryBusiness libraryBusiness;

  private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

  public LibraryController(LibraryBusiness libraryBusiness) {
    this.libraryBusiness = libraryBusiness;
  }

  public void getBooks(RoutingContext routingContext){
    logger.info("saludando");
    Book book = libraryBusiness.readBooksList();
    routingContext.response().setStatusCode(200).end(Json.encode("hola :D"));
  }

}

package com.nav.services.ms_library_management.business;

import com.nav.services.ms_library_management.model.Book;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryBusiness {

  private final Vertx vertx;

  private static final Logger logger = LoggerFactory.getLogger(LibraryBusiness.class);

  public LibraryBusiness(Vertx vertx) {
    this.vertx = vertx;
  }

  public Book readBooksList(){
    Book book = new Book("El hombre en busca de sentido","Viktor Frankl","25-01-2024");
    logger.info("Libro creado");
    logger.info(book.toString());
    return book;
  }

}

package com.nav.services.ms_library_management.service;

import com.nav.services.ms_library_management.model.Book;
import com.nav.services.ms_library_management.repository.BookRepository;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Clase para manejo de operaciones en caso de necesitarse, desacoplando el acceso a datos
 */
public class LibraryServiceImpl implements ILibraryService {

  private final BookRepository bookRepository;

  private static final Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);

  public LibraryServiceImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  /**
   * Se obtiene la lista de libros a traves de la capa de datos
   * @return Future<List<Book>>
   */
  @Override
  public Future<List<Book>> obtainBooksList() {
    Promise<List<Book>> promise = Promise.promise();

    // Invocar el método findAll del repositorio
    bookRepository.findAll().onComplete(ar -> {
      if (ar.succeeded()) {
        List<Book> books = ar.result();
        promise.complete(books);
      } else {
        logger.error("Error obteniendo la lista de libros", ar.cause());
        promise.fail(ar.cause());
      }
    });

    return promise.future();
  }

  /**
   * Se obtiene un libro a traves de la capa de datos
   * @return Future<Book>
   */
  @Override
  public Future<Book> findBookByLikeObject(String entity) {

    Promise<Book> promise = Promise.promise();

    bookRepository.findbyLikeObject(entity).onComplete(ar -> {
      if (ar.succeeded()) {
        Book bookFinded = ar.result();
        promise.complete(bookFinded);
      } else {
        logger.error("Error obteniendo libro por " + entity, ar.cause());
        promise.fail(ar.cause());
      }
    });

    return promise.future();
  }

  public Future<Book> registerBook(Book book) {
    // Llama al método correspondiente de tu repositorio
    return bookRepository.create(book);
  }

}

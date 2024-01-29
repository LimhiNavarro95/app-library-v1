package com.nav.services.ms_library_management.service;

import com.nav.services.ms_library_management.model.Book;
import io.vertx.core.Future;

import java.util.List;

public interface ILibraryService {
  Future<List<Book>> obtainBooksList();
  Future<Book> findBookByLikeObject(String entity);
  Future<Book> registerBook(Book book);
}

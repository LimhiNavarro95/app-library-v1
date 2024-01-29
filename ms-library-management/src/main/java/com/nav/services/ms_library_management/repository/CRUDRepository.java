package com.nav.services.ms_library_management.repository;

import java.util.List;
import io.vertx.core.Future;

/**
 * Interfaz generica para manejo de contrato CRUD con cualquier tipo de objeto
 * @param <T>
 */
public interface CRUDRepository<T> {

  Future<T> create(T t);

  Future<T> findbyLikeObject(String entity);
  Future<List<T>> findAll();

  Future<T> update(T t);

  Future<T> delete(T t);

}

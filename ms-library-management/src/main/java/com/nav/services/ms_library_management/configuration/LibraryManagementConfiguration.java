package com.nav.services.ms_library_management.configuration;

import com.nav.services.ms_library_management.controller.LibraryController;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class LibraryManagementConfiguration {

  private final Vertx vertx;
  private final LibraryController libraryController;
  private final JsonObject service;

  public static final String APPLICATION_JSON = "application/json";

  public LibraryManagementConfiguration(Vertx vertx, LibraryController libraryController, JsonObject config) {
    this.vertx = vertx;
    this.libraryController = libraryController;
    this.service = config.getJsonObject("service");
  }

  /**
   * Configuracion del router del servicio
   * @return Router
   */
  public Router configurationRouter() {
    Router router = Router.router(vertx);
    router.route().consumes(APPLICATION_JSON);
    router.route().produces(APPLICATION_JSON);
    router.route().handler(BodyHandler.create());

    router.route().handler(CorsHandler.create("*")
        .allowedMethod(HttpMethod.GET)
        .allowedMethod(HttpMethod.POST)
        .allowedMethod(HttpMethod.PUT)
        .allowedMethod(HttpMethod.DELETE)
        .allowedHeader("Access-Control-Request-Method")
        .allowedHeader("Access-Control-Allow-Credentials")
        .allowedHeader("Access-Control-Allow-Origin")
        .allowedHeader("Access-Control-Allow-Headers")
        .allowedHeader("Content-Type"));

    String serviceURI = service.getString("SERVICE_URI");

    router.get(serviceURI + service.getString("GET_BOOKS")).handler(libraryController::getBooks);
    router.get(serviceURI + service.getString("GET_BOOK_BY_LIKE_OBJ")).handler(libraryController::getBookByLikeObject);

    return router;
  }

  public Integer getPort(){
    return service.getInteger("port");
  }

  public String getApiMessage() {
    return service.getString("api_message");
  }

}

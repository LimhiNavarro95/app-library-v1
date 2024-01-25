package com.nav.services.ms_library_management.controller;

import io.vertx.ext.web.RoutingContext;

public class LibraryController {

  //business object...

  public void getBooks(RoutingContext routingContext){
    routingContext.response().setStatusCode(200).end("hi :D");
  }

}

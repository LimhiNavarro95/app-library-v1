package com.nav.services.ms_library_management;

import com.nav.services.ms_library_management.configuration.LibraryManagementConfiguration;
import com.nav.services.ms_library_management.controller.LibraryController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;

public class LibraryManagementService extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {

    JsonObject config = config();

    LibraryController libraryController = new LibraryController();

    LibraryManagementConfiguration configuration = new LibraryManagementConfiguration(vertx, libraryController, config);

    run(configuration.configurationRouter(), configuration.getPort());
  }

  //Inicializar servicio
  public void run(Router router, Integer port) {
    System.out.println("Run!!");
    //SelfSignedCertificate certificate = SelfSignedCertificate.create();

    vertx.createHttpServer(
        new HttpServerOptions()
            .setSsl(true)
            .setKeyCertOptions(new JksOptions().setPassword("libraryCertificate").setPath("keystore.jks")))
            //.setTrustOptions(certificate.trustOptions()))
        .requestHandler(router)
        .listen(port);
  }

}
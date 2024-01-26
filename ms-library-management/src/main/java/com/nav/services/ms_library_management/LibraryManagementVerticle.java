package com.nav.services.ms_library_management;

import com.nav.services.ms_library_management.repository.BookRepository;
import com.nav.services.ms_library_management.service.LibraryServiceImpl;
import com.nav.services.ms_library_management.configuration.LibraryManagementConfiguration;
import com.nav.services.ms_library_management.controller.LibraryController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibraryManagementVerticle extends AbstractVerticle {

  private static final Logger logger = LoggerFactory.getLogger(LibraryManagementVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {

    JsonObject config = config();

    //repositories
    BookRepository bookRepository = new BookRepository(vertx);

    //services
    LibraryServiceImpl libraryService = new LibraryServiceImpl(bookRepository);

    //controllers
    LibraryController libraryController = new LibraryController(libraryService);

    //app configuration
    LibraryManagementConfiguration configuration = new LibraryManagementConfiguration(vertx, libraryController, config);

    //service initialization
    run(configuration.configurationRouter(), configuration.getPort(), configuration.getApiMessage());
  }

  //Inicializar servicio
  public void run(Router router, Integer port, String apiMessage) {
    logger.info(
        "\n==================================\n" +
        apiMessage + port +
        "\n==================================\n"
        );

    vertx.createHttpServer(new HttpServerOptions()
            .setSsl(true)
            .setKeyCertOptions(new JksOptions()
                .setPassword("libraryCertificate")
                .setPath("keystore.jks")))
        .requestHandler(router)
        .listen(port);
  }

}

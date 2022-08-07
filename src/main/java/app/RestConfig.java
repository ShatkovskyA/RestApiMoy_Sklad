package app;


import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * Данный класс позволяет не определять в web.xml сервлет, обрабатывающий запросы на rest api(org.glassfish.jersey.servlet.ServletContainer),
 * а также пакет, в котором находятся классы, обрабатывающие rest запросы. (может использоваться вместе с дескриптором развёртования web.xml)
 * аннотация {@link ApplicationPath} указывает url, который обрабатывает сервлет, обрабатывающий запросы на rest api(org.glassfish.jersey.servlet.ServletContainer)
 */
@ApplicationPath("/app")
public class RestConfig extends ResourceConfig{

    public RestConfig(){
        /**
         * указываем пакет, в котором находятся классы, обрабатывающие rest запросы, а также пакет,
         * необходимый для работы swagger
         */
        packages("io.swagger.jaxrs.listing", "app.api.rest");

        /**
         * настройки swagger
         */
        BeanConfig config = new BeanConfig();
        config.setHost("localhost:8080");
        config.setBasePath("/app");
        /**
         * пакет, в котором находятся классы, описывающие rest-сервисы
         */
        config.setResourcePackage("app.api.rest");
        config.setPrettyPrint(true);
        config.setScan(true);


    }

}

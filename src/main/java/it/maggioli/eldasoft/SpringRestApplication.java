package it.maggioli.eldasoft;
import it.maggioli.eldasoft.wstabelledicontesto.utils.CorsConfiguration;
import it.maggioli.eldasoft.wstabelledicontesto.utils.JacksonConfigurator;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.annotation.ComponentScan;

/**
 * Si configura l'applicazione REST mediante Jersey e la parte JSON mediante Jackson.
 *
 * @author Michele Di Napoli
 */
@ComponentScan("it.maggioli.eldasoft")
public class SpringRestApplication extends ResourceConfig {

  /**
   * Register JAX-RS application components.
   */
  public SpringRestApplication() {
    register(RequestContextFilter.class);
    register(MultiPartFeature.class);
    register(JacksonConfigurator.class);
    register(CorsConfiguration.class);
  }
}

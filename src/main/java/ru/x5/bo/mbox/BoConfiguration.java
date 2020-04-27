package ru.x5.bo.mbox;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jms.annotation.JmsListenerAnnotationBeanPostProcessor;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import ru.x5.bo.boapi.ServletContainerInitializingListener;
import ru.x5.bo.boapi.ServletContainerInitializingListener.XRGFilters;
import ru.x5.bo.requestprocessing.impl.SpringGkRequestProcessorImpl;

import javax.jms.ConnectionFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;

import static org.springframework.web.context.WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;

@Configuration
@Import(TxSpringConfiguration.class)
public class BoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BoConfiguration.class);

    @Bean
    public DataSource dataSource() {
        try {
            EmbeddedPostgres pg = EmbeddedPostgres.builder()
                    .setCleanDataDirectory(true)
                    .start();
            return pg.getPostgresDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(jdbcTemplate());
    }

    @Bean
    public ServletWebServerFactory servletWebServerFactory(Environment env){
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
        factory.setPort(Integer.parseInt(env.getProperty("server.port")));
        return factory;
    }

    @Bean
    public SpringGkRequestProcessorImpl processor() {
        return new SpringGkRequestProcessorImpl();
    }

    @Bean
    public ServletContextInitializer dispatcherServletInitializer() {
        return servletContext -> {
            final String authorizationFilter = "authorizationFilter";
            final String mobileThinServicesEnabler = "mobileThinServicesEnabler";
            FilterRegistration.Dynamic fr1 = servletContext
                .addFilter(authorizationFilter, SimpleFilterAuthorizationFilter.class);
            FilterRegistration.Dynamic fr2 = servletContext
                    .addFilter(mobileThinServicesEnabler, SimpleFilterMobileThinServicesEnabler.class);
            XRGFilters filters = XRGFilters.builder()
                    .authorizationFilter(authorizationFilter)
                    .mobileThinServicesEnabler(mobileThinServicesEnabler)
                    .build();

            ApplicationContext context = (ApplicationContext) servletContext.getAttribute(ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            Map<String, ServletContainerInitializingListener> beans = context.getBeansOfType(ServletContainerInitializingListener.class);
            if (!beans.isEmpty()) {
                XRGFilters xrgFilters = XRGFilters.builder()
                        .authorizationFilter(authorizationFilter)
                        .mobileThinServicesEnabler(mobileThinServicesEnabler)
                        .build();
                beans.values().forEach(bean -> bean.servletContextInitialized(servletContext, xrgFilters));
            }
        };
    }

    @Bean("amqJmsConnectionFactory")
    public ConnectionFactory jmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("vm://localhost?broker.persistent=false");
        connectionFactory.setUserName("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }

    @Bean("jmsTemplate")
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(jmsConnectionFactory());
        return template;
    }

    @Bean("jmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(jmsConnectionFactory());
        return jmsListenerContainerFactory;
    }

    @Bean
    public JmsListenerAnnotationBeanPostProcessor jmsListenerAnnotationBeanPostProcessor() {
        JmsListenerAnnotationBeanPostProcessor processor = new JmsListenerAnnotationBeanPostProcessor();
        processor.setContainerFactoryBeanName("jmsListenerContainerFactory");
        return processor;
    }

    @Bean ("org.springframework.jms.config.internalJmsListenerEndpointRegistry")
    public JmsListenerEndpointRegistry jmsListenerEndpointRegistry() {
        return new JmsListenerEndpointRegistry();
    }

    @Bean
    public JmsTransactionManager jmsTransactionManager() {
        return new JmsTransactionManager(jmsConnectionFactory());
    }

    public static class SimpleFilterAuthorizationFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) {
        }
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            log.info("request authorizationFilter {}", request);
            chain.doFilter(request, response);
        }
        @Override
        public void destroy() {
        }
    }
    public static class SimpleFilterMobileThinServicesEnabler implements Filter {
        @Override
        public void init(FilterConfig filterConfig) {
        }
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            log.info("request mobileThinServicesEnabler {}", request);
            chain.doFilter(request, response);
        }
        @Override
        public void destroy() {
        }
    }
}


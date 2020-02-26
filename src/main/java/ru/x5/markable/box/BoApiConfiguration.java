package ru.x5.markable.box;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.x5.bo.boapi.ServletContainerInitializingListener;
import ru.x5.bo.boapi.ServletContainerInitializingListener.XRGFilters;
import ru.x5.bo.requestprocessing.impl.SpringGkRequestProcessorImpl;

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
@EnableTransactionManagement
public class BoApiConfiguration {

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
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
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
    public ServletWebServerFactory servletWebServerFactory(){
        return new JettyServletWebServerFactory();
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
            if (beans != null && !beans.isEmpty()) {
                XRGFilters xrgFilters = XRGFilters.builder()
                        .authorizationFilter(authorizationFilter)
                        .mobileThinServicesEnabler(mobileThinServicesEnabler)
                        .build();
                beans.values().stream().forEach(bean -> bean.servletContextInitialized(servletContext, xrgFilters));
            }
        };
    }

    public static class SimpleFilterAuthorizationFilter implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            System.out.println("request authorizationFilter " + request);
            chain.doFilter(request, response);
        }
        @Override
        public void destroy() {
        }
    }
    public static class SimpleFilterMobileThinServicesEnabler implements Filter {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            System.out.println("request mobileThinServicesEnabler " + request);
            chain.doFilter(request, response);
        }
        @Override
        public void destroy() {
        }
    }
}


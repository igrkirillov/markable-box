package ru.x5.bo.mbox;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;

/**
 * Декларативное управление транзакциями в BO реализовано с помощью Spring AOP Advisor,
 * а точнее {@link TransactionAttributeSourceAdvisor}<br>
 * Поэтому, здесь мы повторяем конфигурацию транзакций из BO.
 */
@Configuration
public class TxSpringConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public TransactionAttributeSourceAdvisor transactionAttributeSourceAdvisor(TransactionInterceptor transactionInterceptor) {
        return new TransactionAttributeSourceAdvisor(transactionInterceptor);
    }

    @Bean
    public AnnotationTransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource();
    }

    @Bean
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager,
                                                         AnnotationTransactionAttributeSource transactionAttributeSource) {
        return new TransactionInterceptor(transactionManager, transactionAttributeSource);
    }
}

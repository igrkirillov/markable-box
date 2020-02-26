package ru.x5.markable.box;

import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MockInterfacesBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private String scanPackage;

    public MockInterfacesBeanFactoryPostProcessor(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            for (Class iClass : findInterfaces()) {
                beanFactory.registerSingleton(iClass.getName(), Mockito.mock(iClass));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new BeanDefinitionValidationException("Error of create bean", e);
        }
    }

    private Set<Class> findInterfaces() throws IOException, ClassNotFoundException {
        Set<Class> result = new HashSet<>();

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        final String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(scanPackage)) +
                "/**/*.class";

        org.springframework.core.io.Resource[] elements = resourcePatternResolver.getResources(packageSearchPath);

        for (org.springframework.core.io.Resource e: elements) {
            if (e.isReadable()) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(e);
                if (metadataReader.getClassMetadata().isInterface()) {
                    Class<?> aClass = Class.forName(metadataReader.getClassMetadata().getClassName());
                    result.add(aClass);
                }
            }
        }

        return result;
    }
}

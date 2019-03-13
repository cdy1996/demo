package com.cdy.demo.framework.spring.scan;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

public class TestScannerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RemoteScan.class.getName()));
        ClassPathTestScanner scanner = new ClassPathTestScanner(registry, annotationAttributes.getString("defaultConfiguration"));

        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }


        scanner.registerFilters();
        scanner.doScan(annotationAttributes.getStringArray("basePackages"));

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}

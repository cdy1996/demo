package com.cdy.demo.framework.spring.scan;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Set;

public class ClassPathTestScanner extends ClassPathBeanDefinitionScanner {

    private String defaultConfiguratuion;

    public ClassPathTestScanner(BeanDefinitionRegistry registry, String defaultConfiguratuion) {
        super(registry);
        this.defaultConfiguratuion = defaultConfiguratuion;
    }

    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        processBeanDefinition(definitionHolder, registry);
        super.registerBeanDefinition(definitionHolder, registry);
    }

    private void processBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        ScannedGenericBeanDefinition beanDefinition = (ScannedGenericBeanDefinition) definitionHolder.getBeanDefinition();

        MultiValueMap<String, Object> allAnnotationAttributes = beanDefinition.getMetadata().getAllAnnotationAttributes(Remoting.class.getName());
        ResourceLoader resourceLoader = getResourceLoader();
        String name = null,conextPath = null;
        if(resourceLoader instanceof ConfigurableApplicationContext)   {
            ConfigurableApplicationContext resourceLoader1 = (ConfigurableApplicationContext) resourceLoader;
            name = (String) allAnnotationAttributes.getFirst("name");
            if (StringUtils.hasText(name)) {
                resourceLoader1.getEnvironment().resolvePlaceholders(name);
            }
            conextPath = (String) allAnnotationAttributes.getFirst("conextPath");
            if (StringUtils.hasText(conextPath)) {
                resourceLoader1.getEnvironment().resolvePlaceholders(conextPath);
            }
        }
        String configuration1 = (String) allAnnotationAttributes.getFirst("configuration");
        if (!StringUtils.hasText(configuration1)) {
            configuration1 = defaultConfiguratuion;
        }
        RemoteDefine remoteDefine = new RemoteDefine(name, conextPath, configuration1);

        ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, beanDefinition.getBeanClassName());
        constructorArgumentValues.addIndexedArgumentValue(1, remoteDefine);
        beanDefinition.setBeanClass(RemoteFactoryBean.class);
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

    }


    public void registerFilters() {
        super.addIncludeFilter(new AnnotationTypeFilter(Remoting.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}

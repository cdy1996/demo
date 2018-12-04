package com.hexin.demo.scan;

public class TestDefine {

    //serivceid
    private String name;
    //context-path
    private String contextPath;
    //configuration beanname
    private String configuration;

    public TestDefine(String name, String contextPath, String configuration) {
        this.name = name;
        this.contextPath = contextPath;
        this.configuration = configuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}

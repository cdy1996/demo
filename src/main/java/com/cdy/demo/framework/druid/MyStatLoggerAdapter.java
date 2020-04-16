package com.cdy.demo.framework.druid;

import com.alibaba.druid.pool.DruidDataSourceStatLoggerAdapter;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import com.alibaba.druid.stat.JdbcSqlStat;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.List;
import java.util.Properties;

public class MyStatLoggerAdapter extends DruidDataSourceStatLoggerAdapter {

    @Override
    public void log(DruidDataSourceStatValue statValue) {
        long connectCount = statValue.getConnectCount();
        List<JdbcSqlStatValue> sqlList = statValue.getSqlList();
        sqlList.forEach(e -> {
            String sql = e.getSql();
            long id = e.getId();
            String file = e.getFile();
        });

        super.log(statValue);
    }


}

class DruidMybatisInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatements = (MappedStatement) args[0];
        JdbcSqlStat.setContextSqlName(mappedStatements.getId());
        JdbcSqlStat.setContextSqlFile(mappedStatements.getResource());
        try {
            return invocation.proceed();
        } finally {
            JdbcSqlStat.setContextSqlName(null);
            JdbcSqlStat.setContextSqlFile(null);
        }
    }

    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

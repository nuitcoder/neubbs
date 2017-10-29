package org.neusoft.neubbs.controller.data;

import org.neusoft.neubbs.controller.handler.SwitchDataSourceHandler;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据库源
 *
 * @author Suvan
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        //返回当前线程所指定数据源(使用该数据源进行数据库操作，若配置文件无此数据源，默认使用默认的)
        return SwitchDataSourceHandler.getDataSourceType();
    }
}

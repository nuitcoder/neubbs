package org.neusoft.neubbs.controller.data;

import org.neusoft.neubbs.constant.api.SetConst;
import org.neusoft.neubbs.utils.StringUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态切换数据源
 *      - 每个请求，启动新的线程，每次调用一次
 *
 * @author Suvan
 */
public class DynamicSwitchDataSource extends AbstractRoutingDataSource {

    private static ThreadLocal<String> currentThreadDataSource = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        //Get the current thread specified data(default cloud data source)
        String currentDataSource = currentThreadDataSource.get();

        //each request finished, to clear thread data
        currentThreadDataSource.remove();

        return StringUtil.isEmpty(currentDataSource) ? SetConst.CLOUD_DATA_SOURCE_MYSQL : currentDataSource;
    }

    /**
     * 设置数据源
     *      - 定义当前线程数据源
     *      - 在 spring-mybatis-context.xml 内定义多数据源
     *
     * @param dataSourceName 配置文件数据源id（应输入为全局静态变量数据源）
     */
    public static void setDataSource(String dataSourceName) {
        currentThreadDataSource.set(dataSourceName);
    }
}

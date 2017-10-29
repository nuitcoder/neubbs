package org.neusoft.neubbs.controller.handler;

import org.neusoft.neubbs.utils.StringUtil;

/**
 * 客户端切换数据源处理器
 *
 * 切换方法（代码中调用）：
 *      SwitchDataSourceHandler.setDataSourceType(SwitchDataSourceHandler.CLOUD_DATA_SOURCE);
 *
 * @author Suvan
 */
public final class SwitchDataSourceHandler {
    /**
     * 定义数据源类型
     */
    public static final String CLOUD_DATA_SOURCE_MYSQL = "cloudDataSourceMysql";
    public static final String LOCALHOST_DATA_SOURCE_MYSQL = "localDataSourceMysql";

    private static  ThreadLocal<String> currentThreadDataSource = new ThreadLocal<>();

    private SwitchDataSourceHandler() { }

    /**
     * 设置当前线程数据源类型
     *      注：ThreadLocal 创建爱你的变量只能被当前线程访问，其余线程无法访问和修改
     *
     * @param dataSourceType 数据源类型（上述定义的常量）
     */
    public static void setDataSourceType(String dataSourceType) {
        currentThreadDataSource.set(dataSourceType);
    }

    /**
     * 获取当前线程所使用的数据源类型（若未获取，默认是云数据源）
     *
     * @return String 数据源类型（上述已定义的常量）
     */
    public static String getDataSourceType() {
        String dataSource = currentThreadDataSource.get();
        if (StringUtil.isEmpty(dataSource)) {
            return CLOUD_DATA_SOURCE_MYSQL;
        } else {
            return dataSource;
        }
    }

    /**
     * 清空当前线程所保存的数据源类型
     */
    public static void clearDataSourceType() {
        currentThreadDataSource.remove();
    }
}

package org.neusoft.neubbs.constant.db;

/**
 * 数据库请求状态
 */
public interface DBRequestStatus {

    String INSERT_SUCCESS = "插入成功";
    String DETELE_SUCCESS  = "删除成功";
    String SELECT_SUCCESS  = "查询成功";
    String UPDATE_SUCCESS  = "更新成功";
    String TRUNCATE_SUCCESS = "删减表成功";

    String INSERT_FAIL  = "插入失败";
    String DETELE_FAIL  = "删除失败";
    String SELECT_FAIL  = "查询失败";
    String UPDATE_FAIL  = "更新失败";
    String TRUNCATE_FAIL  = "删减表失败";
}
package cn.worth.core.interceptor;

//import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
//import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.xiaoleilu.hutool.collection.CollectionUtil;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.reflection.MetaObject;
//import org.apache.ibatis.reflection.SystemMetaObject;
//
//import java.sql.Connection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;

/**
 * 数据权限插件，from guns 项目
 * @author chenxiaoqing
 * @date 2018/7/25
 */
//@Slf4j
//@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
//public class DataScopeInterceptor extends SqlParserHelper implements Interceptor {
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
//        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
//        getSqlParserInfo(metaObject);
//        // 先判断是不是SELECT操作
//        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
//        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
//            return invocation.proceed();
//        }
//
//        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
//        String originalSql = boundSql.getSql();
//        Object parameterObject = boundSql.getParameterObject();
//
//        //查找参数中包含DataScope类型的参数
//        DataScope dataScope = findDataScopeObject(parameterObject);
//
//        if (dataScope == null) {
//            return invocation.proceed();
//        } else {
//            String scopeName = dataScope.getScopeName();
//            List<String> deptIds = dataScope.getDeptIds();
//            if(StringUtils.isNotBlank(scopeName) && CollectionUtil.isNotEmpty(deptIds)){
//                String join = CollectionUtil.join(deptIds, ",");
//                originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
//                metaObject.setValue("delegate.boundSql.sql", originalSql);
//            }
//            return invocation.proceed();
//        }
//    }
//
//    /**
//     * 生成拦截对象的代理
//     *
//     * @param target 目标对象
//     * @return 代理对象
//     */
//    @Override
//    public Object plugin(Object target) {
//        if (target instanceof StatementHandler) {
//            return Plugin.wrap(target, this);
//        }
//        return target;
//    }
//
//    /**
//     * mybatis配置的属性
//     *
//     * @param properties mybatis配置的属性
//     */
//    @Override
//    public void setProperties(Properties properties) {
//
//    }
//
//    /**
//     * 查找参数是否包括DataScope对象
//     *
//     * @param parameterObj 参数列表
//     * @return DataScope
//     */
//    private DataScope findDataScopeObject(Object parameterObj) {
//        if (parameterObj instanceof DataScope) {
//            return (DataScope) parameterObj;
//        } else if (parameterObj instanceof Map) {
//            for (Object val : ((Map<?, ?>) parameterObj).values()) {
//                if (val instanceof DataScope) {
//                    return (DataScope) val;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 数据权限、参考guns实现
//     * 增强查询参数
//     * @author chenxiaoqing
//     * @date 2018/7/25
//     */
//    @Data
//    public class DataScope extends HashMap {
//        /**
//         * 限制范围的字段名称
//         */
//        private String scopeName = "dept_id";
//
//        /**
//         * 具体的数据范围
//         */
//        private List<String> deptIds;
//
//        /**
//         * 是否只查询本部门
//         */
//        private Boolean isOnly = false;
//    }
//
//}
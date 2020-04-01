package cn.worth.core.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HttpServletRequest添加自定义请求头包装器
 * @Author: MyIreland on.
 * @Description: https://gitee.com/renrenio/renren-fast/blob/master/src/main/java/io/renren/common/xss/SQLFilter.java
 * @Date: 2019/3/9
 */
public final class MutableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    // holds custom header and value mapping
    private final Map<String, String> customHeaders;

    private Map<String , String[]> customParams;
 
    public MutableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new ConcurrentHashMap<>();
        this.customParams = new ConcurrentHashMap<>();
        this.customParams.putAll(request.getParameterMap());
    }

    //重载一个构造方法
    public MutableHttpServletRequestWrapper(HttpServletRequest request , Map<String , Object> extendParams) {
        this(request);
        addAllParameters(extendParams);//这里将扩展参数写入参数表
    }


    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }
 
    @Override
    public String getHeader(String name) {
        // check the custom headers first
        String headerValue = customHeaders.get(name);
        
        if (headerValue != null){
            return headerValue;
        }
        // else return from into the original wrapped object
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }
 
    @Override
    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> set = new HashSet<>(customHeaders.keySet());
        
        // now add the headers from the wrapped request object
        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }
 
        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }

    @Override
    public String getParameter(String name) {//重写getParameter，代表参数从当前类中的map获取
        String[] values = customParams.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        //Return an unmodifiable collection because we need to uphold the interface contract.
        return Collections.unmodifiableMap(customParams);
    }

    @Override
    public Enumeration<String> getParameterNames()
    {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(String name) {//同上
        return customParams.get(name);
    }

    public void addAllParameters(Map<String , Object> otherParams) {//增加多个参数
        for (Map.Entry<String , Object> entry : otherParams.entrySet()) {
            putParameter(entry.getKey() , entry.getValue());
        }
    }

    public void putParameter(String name , Object value) {//增加参数
        if (value != null) {
            if (value instanceof String[]) {
                customParams.put(name , (String[])value);
            } else if(value instanceof String) {
                customParams.put(name , new String[] {(String)value});
            } else {
                customParams.put(name , new String[] {String.valueOf(value)});
            }
        }
    }
}
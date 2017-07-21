package com.brandonscottbrown.multitenant.interceptor;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MultitenancyInterceptor extends HandlerInterceptorAdapter {
    
    private static final String TENANT_ID = "tenantId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = request.getHeader(TENANT_ID);
         if (!StringUtils.isEmpty(tenantId)) {
             request.setAttribute(TENANT_ID, tenantId);
         }
         return true;
    }

}

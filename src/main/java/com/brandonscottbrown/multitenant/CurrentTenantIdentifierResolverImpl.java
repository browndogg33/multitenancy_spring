package com.brandonscottbrown.multitenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String TENANT_ID = "tenantId";
    private static final String DEFAULT_TENANT_ID = "got";

     @Override
     public String resolveCurrentTenantIdentifier() {
         RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
         if (requestAttributes != null) {
             String tenantId = (String) requestAttributes.getAttribute(TENANT_ID, RequestAttributes.SCOPE_REQUEST);
             if (tenantId != null) {
                 return tenantId;
             }
         }
         return DEFAULT_TENANT_ID;
     }
    
     @Override
     public boolean validateExistingCurrentSessions() {
         return true;
     }
}

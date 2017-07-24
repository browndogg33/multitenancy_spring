package com.brandonscottbrown.multitenant.primarydb.repository;

import com.brandonscottbrown.multitenant.primarydb.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long>{
}

package com.brandonscottbrown.multitenant.repository;

import com.brandonscottbrown.multitenant.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CharacterRepository extends JpaRepository<Character, Long> {

    List<Character> findByTenantId(String tenantId);
}

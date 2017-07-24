package com.brandonscottbrown.multitenant.controller;

import com.brandonscottbrown.multitenant.tenantdb.domain.Character;
import com.brandonscottbrown.multitenant.tenantdb.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterRepository characterRepository;

    @GetMapping
    public ResponseEntity<List<Character>> getCharacters(@RequestHeader("tenantId") String tenantId){
        return new ResponseEntity<>(characterRepository.findByTenantId(tenantId), HttpStatus.OK);
    }
}

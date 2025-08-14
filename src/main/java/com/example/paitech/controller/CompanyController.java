package com.example.paitech.controller;

import com.example.paitech.model.Company;
import com.example.paitech.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) { this.companyService = companyService; }

    @PostMapping
    public ResponseEntity<Company> create(@Valid @RequestBody Company company) {
        return ResponseEntity.ok(companyService.create(company));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> get(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<Company>> list() {
        return ResponseEntity.ok(companyService.list());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> update(@PathVariable Long id, @Valid @RequestBody Company company) {
        return ResponseEntity.ok(companyService.update(id, company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

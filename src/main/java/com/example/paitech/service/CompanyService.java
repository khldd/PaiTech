package com.example.paitech.service;

import com.example.paitech.exception.ResourceNotFoundException;
import com.example.paitech.model.Company;
import com.example.paitech.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) { this.companyRepository = companyRepository; }

    public Company create(Company c) {
        return companyRepository.save(c);
    }

    public Company get(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    public List<Company> list() {
        return companyRepository.findAll();
    }

    public Company update(Long id, Company payload) {
        Company c = get(id);
        c.setName(payload.getName());
        c.setAddress(payload.getAddress());
        c.setTaxId(payload.getTaxId());
        return companyRepository.save(c);
    }

    public void delete(Long id) {
        Company c = get(id);
        companyRepository.delete(c);
    }
}


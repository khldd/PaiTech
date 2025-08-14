package com.example.paitech.repository;

import com.example.paitech.model.Company;
import com.example.paitech.model.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRunRepository extends JpaRepository<PayrollRun, Long> {
    List<PayrollRun> findByCompany(Company company);
}

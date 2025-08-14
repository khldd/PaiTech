package com.example.paitech.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "payroll_items")
public class PayrollItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "run_id")
    private PayrollRun payrollRun;

    @ManyToOne(optional = false) @JoinColumn(name = "employee_id")
    private Employee employee;

    @NotNull
    @Column(name = "gross", precision = 18, scale = 2, nullable = false)
    private BigDecimal gross = BigDecimal.ZERO;

    @NotNull
    @Column(name = "taxes", precision = 18, scale = 2, nullable = false)
    private BigDecimal taxes = BigDecimal.ZERO;

    @NotNull
    @Column(name = "deductions", precision = 18, scale = 2, nullable = false)
    private BigDecimal deductions = BigDecimal.ZERO;

    @NotNull
    @Column(name = "net", precision = 18, scale = 2, nullable = false)
    private BigDecimal net = BigDecimal.ZERO;

    public Long getId() { return id; }
    public PayrollRun getPayrollRun() { return payrollRun; }
    public Employee getEmployee() { return employee; }
    public BigDecimal getGross() { return gross; }
    public BigDecimal getTaxes() { return taxes; }
    public BigDecimal getDeductions() { return deductions; }
    public BigDecimal getNet() { return net; }

    public void setId(Long id) { this.id = id; }
    public void setPayrollRun(PayrollRun payrollRun) { this.payrollRun = payrollRun; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public void setGross(BigDecimal gross) { this.gross = gross; }
    public void setTaxes(BigDecimal taxes) { this.taxes = taxes; }
    public void setDeductions(BigDecimal deductions) { this.deductions = deductions; }
    public void setNet(BigDecimal net) { this.net = net; }
}

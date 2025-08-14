package com.example.paitech.model;

import com.example.paitech.model.enums.PeriodStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payroll_runs")
public class PayrollRun {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PeriodStatus status = PeriodStatus.OPEN;

    @Column(name = "total_gross", precision = 18, scale = 2)
    private BigDecimal totalGross = BigDecimal.ZERO;

    @Column(name = "total_net", precision = 18, scale = 2)
    private BigDecimal totalNet = BigDecimal.ZERO;

    @OneToMany(mappedBy = "payrollRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayrollItem> items = new ArrayList<>();

    public Long getId() { return id; }
    public Company getCompany() { return company; }
    public LocalDate getPeriodStart() { return periodStart; }
    public LocalDate getPeriodEnd() { return periodEnd; }
    public PeriodStatus getStatus() { return status; }
    public BigDecimal getTotalGross() { return totalGross; }
    public BigDecimal getTotalNet() { return totalNet; }
    public List<PayrollItem> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setCompany(Company company) { this.company = company; }
    public void setPeriodStart(LocalDate periodStart) { this.periodStart = periodStart; }
    public void setPeriodEnd(LocalDate periodEnd) { this.periodEnd = periodEnd; }
    public void setStatus(PeriodStatus status) { this.status = status; }
    public void setTotalGross(BigDecimal totalGross) { this.totalGross = totalGross; }
    public void setTotalNet(BigDecimal totalNet) { this.totalNet = totalNet; }
    public void setItems(List<PayrollItem> items) { this.items = items; }
}

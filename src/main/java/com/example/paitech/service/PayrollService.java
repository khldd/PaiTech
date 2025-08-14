package com.example.paitech.service;

import com.example.paitech.dto.response.PayrollSummaryResponse;
import com.example.paitech.exception.ResourceNotFoundException;
import com.example.paitech.model.Company;
import com.example.paitech.model.PayrollItem;
import com.example.paitech.model.PayrollRun;
import com.example.paitech.model.enums.PeriodStatus;
import com.example.paitech.repository.CompanyRepository;
import com.example.paitech.repository.EmployeeRepository;
import com.example.paitech.repository.PayrollRunRepository;
import com.example.paitech.util.PayrollCalculationUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PayrollService {

    private final PayrollRunRepository runRepo;
    private final CompanyRepository companyRepo;
    private final EmployeeRepository employeeRepo;

    public PayrollService(PayrollRunRepository runRepo,
                          CompanyRepository companyRepo,
                          EmployeeRepository employeeRepo) {
        this.runRepo = runRepo;
        this.companyRepo = companyRepo;
        this.employeeRepo = employeeRepo;
    }

    public PayrollRun createRun(Long companyId, LocalDate start, LocalDate end) {
        Company c = companyRepo.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        PayrollRun run = new PayrollRun();
        run.setCompany(c);
        run.setPeriodStart(start);
        run.setPeriodEnd(end);
        run.setStatus(PeriodStatus.OPEN);
        run.setTotalGross(BigDecimal.ZERO);
        run.setTotalNet(BigDecimal.ZERO);
        return runRepo.save(run);
    }

    public PayrollRun getRun(Long id) {
        return runRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found"));
    }

    public List<PayrollRun> listRuns(Long companyId) {
        if (companyId == null) return runRepo.findAll();
        Company c = companyRepo.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        return runRepo.findByCompany(c);
    }

    public PayrollRun closeRun(Long id) {
        PayrollRun run = getRun(id);
        run.setStatus(PeriodStatus.CLOSED);
        // recompute totals for safety
        var totals = PayrollCalculationUtil.sumItems(run.getItems());
        run.setTotalGross(totals[0]);
        run.setTotalNet(totals[1]);
        return runRepo.save(run);
    }

    public void deleteRun(Long id) {
        PayrollRun run = getRun(id);
        runRepo.delete(run);
    }

    public PayrollItem addItem(Long runId, PayrollItem item) {
        PayrollRun run = getRun(runId);
        item.setPayrollRun(run);
        // compute net if not provided or inconsistent
        item.setNet(PayrollCalculationUtil.net(item.getGross(), item.getTaxes(), item.getDeductions()));
        run.getItems().add(item);
        var totals = PayrollCalculationUtil.sumItems(run.getItems());
        run.setTotalGross(totals[0]);
        run.setTotalNet(totals[1]);
        runRepo.save(run); // cascade persists item
        // find saved item (last in list)
        return run.getItems().get(run.getItems().size() - 1);
    }

    public List<PayrollItem> listItems(Long runId) {
        return getRun(runId).getItems();
    }

    public PayrollItem updateItem(Long itemId, PayrollItem payload) {
        // find run containing this item
        PayrollRun holder = runRepo.findAll().stream()
                .filter(r -> r.getItems().stream().anyMatch(i -> i.getId().equals(itemId)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payroll item not found"));

        PayrollItem item = holder.getItems().stream()
                .filter(i -> i.getId().equals(itemId)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payroll item not found"));

        item.setEmployee(payload.getEmployee() != null ? payload.getEmployee() : item.getEmployee());
        item.setGross(payload.getGross());
        item.setTaxes(payload.getTaxes());
        item.setDeductions(payload.getDeductions());
        item.setNet(PayrollCalculationUtil.net(item.getGross(), item.getTaxes(), item.getDeductions()));

        var totals = PayrollCalculationUtil.sumItems(holder.getItems());
        holder.setTotalGross(totals[0]);
        holder.setTotalNet(totals[1]);

        runRepo.save(holder);
        return item;
    }

    public void deleteItem(Long itemId) {
        PayrollRun holder = runRepo.findAll().stream()
                .filter(r -> r.getItems().stream().anyMatch(i -> i.getId().equals(itemId)))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payroll item not found"));
        holder.getItems().removeIf(i -> i.getId().equals(itemId));
        var totals = PayrollCalculationUtil.sumItems(holder.getItems());
        holder.setTotalGross(totals[0]);
        holder.setTotalNet(totals[1]);
        runRepo.save(holder);
    }

    public com.example.paitech.dto.response.PayrollSummaryResponse summary(Long runId) {
        PayrollRun run = getRun(runId);
        var resp = new com.example.paitech.dto.response.PayrollSummaryResponse();
        resp.setRunId(run.getId());
        resp.setCompanyId(run.getCompany().getId());
        resp.setPeriodStart(run.getPeriodStart());
        resp.setPeriodEnd(run.getPeriodEnd());
        resp.setStatus(run.getStatus());
        var totals = PayrollCalculationUtil.sumItems(run.getItems());
        resp.setTotalGross(totals[0]);
        resp.setTotalNet(totals[1]);
        resp.setItemsCount(run.getItems().size());
        return resp;
    }
}

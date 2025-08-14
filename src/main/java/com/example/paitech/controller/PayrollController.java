package com.example.paitech.controller;

import com.example.paitech.dto.response.PayrollSummaryResponse;
import com.example.paitech.model.PayrollItem;
import com.example.paitech.model.PayrollRun;
import com.example.paitech.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private final PayrollService payrollService;
    public PayrollController(PayrollService payrollService) { this.payrollService = payrollService; }

    // Payroll Runs
    @PostMapping("/runs")
    public ResponseEntity<PayrollRun> createRun(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {
        return ResponseEntity.ok(payrollService.createRun(companyId, periodStart, periodEnd));
    }

    @GetMapping("/runs/{id}")
    public ResponseEntity<PayrollRun> getRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.getRun(id));
    }

    @GetMapping("/runs")
    public ResponseEntity<List<PayrollRun>> listRuns(@RequestParam(required = false) Long companyId) {
        return ResponseEntity.ok(payrollService.listRuns(companyId));
    }

    @PutMapping("/runs/{id}/close")
    public ResponseEntity<PayrollRun> closeRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.closeRun(id));
    }

    @DeleteMapping("/runs/{id}")
    public ResponseEntity<Void> deleteRun(@PathVariable Long id) {
        payrollService.deleteRun(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/runs/{id}/summary")
    public ResponseEntity<PayrollSummaryResponse> summary(@PathVariable Long id) {
        return ResponseEntity.ok(payrollService.summary(id));
    }

    // Payroll Items
    @PostMapping("/runs/{runId}/items")
    public ResponseEntity<PayrollItem> addItem(@PathVariable Long runId, @Valid @RequestBody PayrollItem item) {
        return ResponseEntity.ok(payrollService.addItem(runId, item));
    }

    @GetMapping("/runs/{runId}/items")
    public ResponseEntity<List<PayrollItem>> listItems(@PathVariable Long runId) {
        return ResponseEntity.ok(payrollService.listItems(runId));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<PayrollItem> updateItem(@PathVariable Long itemId, @Valid @RequestBody PayrollItem item) {
        return ResponseEntity.ok(payrollService.updateItem(itemId, item));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        payrollService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}

package com.invoices.invoices.service;

import com.invoices.invoices.models.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    Invoice createInvoice(double amount, LocalDate dueDate);

    List<Invoice> getAllInvoices();

    Optional<Invoice> getInvoice(Long id);

    Invoice payInvoice(Long id, double amount);

    void processOverDueInvoices(double lateFee, int overDueDays);

}

package com.invoices.invoices.service.impl;

import com.invoices.invoices.models.Invoice;
import com.invoices.invoices.models.InvoiceStatus;
import com.invoices.invoices.repository.InvoiceRepository;
import com.invoices.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;


    @Override
    public Invoice createInvoice(double amount, LocalDate dueDate) {
        var invoice = Invoice.builder().
                amount(amount).
                paidAmount(0).
                dueDate(dueDate).
                status(InvoiceStatus.PENDING).build();
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public Optional<Invoice> getInvoice(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Invoice payInvoice(Long id, double amount) {
        var invoiceById = this.getInvoice(id);
        if (invoiceById.isPresent()) {
            var invoice = invoiceById.get();
            invoice.setPaidAmount(invoice.getPaidAmount() + amount);
            if (invoice.getPaidAmount() >= invoice.getAmount()) {
                invoice.setStatus(InvoiceStatus.PAID);
            }
            return invoiceRepository.save(invoice);
        }
        return null;
    }

    @Override
    public void processOverDueInvoices(double lateFee, int overDueDays) {
        var overDueInvoices = invoiceRepository.findByStatus(InvoiceStatus.PENDING);
        var today = LocalDate.now();

        for (var invoice : overDueInvoices) {
            if (invoice.getDueDate().plusDays(overDueDays).isBefore(today)) {
                if (invoice.getPaidAmount() > 0) {
                    var newInvoice = Invoice.builder().
                            amount(invoice.getAmount() - invoice.getPaidAmount() + lateFee).
                            paidAmount(0).
                            dueDate(today.plusDays(overDueDays)).
                            status(InvoiceStatus.PENDING).build();
                    invoiceRepository.save(newInvoice);
                    invoice.setStatus(InvoiceStatus.PAID);
                } else {
                    var newInvoice = Invoice.builder().
                            amount(invoice.getAmount()+ lateFee).
                            paidAmount(0).
                            dueDate(today.plusDays(overDueDays)).
                            status(InvoiceStatus.PENDING).build();
                    invoiceRepository.save(newInvoice);
                    invoice.setStatus(InvoiceStatus.VOID);
                }
                invoiceRepository.save(invoice);
            }
        }
    }
}

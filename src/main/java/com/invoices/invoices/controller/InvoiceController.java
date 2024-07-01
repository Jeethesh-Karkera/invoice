package com.invoices.invoices.controller;

import com.invoices.invoices.dto.PaymentDto;
import com.invoices.invoices.models.Invoice;
import com.invoices.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<Long> createInvoice(@RequestBody Invoice invoiceRequest){
        var invoice = invoiceService.createInvoice(invoiceRequest.getAmount(), invoiceRequest.getDueDate());
        return new ResponseEntity<>(invoice.getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices(){
        return new ResponseEntity<>(invoiceService.getAllInvoices(),HttpStatus.OK);
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<?> payInvoice(@PathVariable(value = "id") Long id, PaymentDto.payment paymentDto)
    {
        var invoice = invoiceService.payInvoice(id,paymentDto.amount());
        if(!Objects.isNull(invoice))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/process-overdue")
    public ResponseEntity<?> processOverDueInvoices(@RequestParam(value = "lateFee") double lateFee,
                                                    @RequestParam(value = "overDueDays") int overDueDays){
        invoiceService.processOverDueInvoices(lateFee,overDueDays);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

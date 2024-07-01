package com.invoices.invoices.service;

import com.invoices.invoices.models.Invoice;
import com.invoices.invoices.models.InvoiceStatus;
import com.invoices.invoices.repository.InvoiceRepository;
import com.invoices.invoices.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createInvoiceTest(){
        var invoice = setInvoiceParams();
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        var result = invoiceService.createInvoice(190.00,LocalDate.of(2024, 7, 1));
        assertEquals(190.00,result.getAmount());
        assertEquals(0,result.getPaidAmount());
        assertEquals(LocalDate.of(2024, 7, 1),result.getDueDate());
        assertEquals(InvoiceStatus.PENDING,result.getStatus());
    }

    @Test
    void getAllInvoiceTest(){
        var invoice1 = setInvoiceParams();
        var invoice2 = Invoice.builder().
                id(2L).
                amount(290.00).
                paidAmount(0).
                dueDate(LocalDate.of(2024, 7, 2)).
                status(InvoiceStatus.PENDING).build();

        var invoices= Arrays.asList(invoice1,invoice2);
        when(invoiceRepository.findAll()).thenReturn(invoices);

        var result = invoiceService.getAllInvoices();
        assertEquals(invoice1.getAmount(),result.get(0).getAmount());
        assertEquals(invoice2.getAmount(),result.get(1).getAmount());
    }

    @Test
    void getInvoiceByIdTest(){
        var invoice = setInvoiceParams();
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(invoice)).thenReturn(Optional.empty());
        var result = invoiceService.getInvoice(1L);
        var result1 = invoiceService.getInvoice(1L);
        assertTrue(result.isPresent());
        assertEquals(invoice.getAmount(),result.get().getAmount());
        assertFalse(result1.isPresent());
    }

    @Test
    void payInvoiceTest(){
        var invoice = setInvoiceParams();
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(invoice)).thenReturn(Optional.empty());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        var result = invoiceService.payInvoice(1L,190.00);
        assertEquals(190.00,result.getPaidAmount());
        assertEquals(InvoiceStatus.PAID,result.getStatus());

        var result1 = invoiceService.payInvoice(1L,190.00);
        assertNull(result1);
    }

    @Test
    void processOverDueInvoicesTest(){
        var invoice = setInvoiceParams();
        invoice.setPaidAmount(100.00);
        invoice.setDueDate(LocalDate.now().minusDays(15));
        when(invoiceRepository.findByStatus(InvoiceStatus.PENDING)).thenReturn(List.of(invoice));
        invoiceService.processOverDueInvoices(10.5,10);
        verify(invoiceRepository,times(2)).save(any(Invoice.class));
    }

    private Invoice setInvoiceParams(){
        return Invoice.builder().
                id(1L).
                amount(190.00).
                paidAmount(0).
                dueDate(LocalDate.of(2024, 7, 1)).
                status(InvoiceStatus.PENDING).build();
    }
}

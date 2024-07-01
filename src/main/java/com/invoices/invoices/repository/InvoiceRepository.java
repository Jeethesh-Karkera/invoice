package com.invoices.invoices.repository;

import com.invoices.invoices.models.Invoice;
import com.invoices.invoices.models.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    List<Invoice> findByStatus(InvoiceStatus status);
}

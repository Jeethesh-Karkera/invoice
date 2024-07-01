package com.invoices.invoices.dto;

import java.io.Serializable;

public interface PaymentDto {
    record payment(double amount) implements Serializable{}
}

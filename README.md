# Invoice Management System

This is a simple Invoice Management System built with Spring Boot. It allows creating invoices, paying invoices, and processing overdue invoices.

## Features
- Create invoices
- Pay invoices
- Process overdue invoices with late fees

## Prerequisites
- Docker
- Docker Compose

## Running the Application
docker-compose up --build

## DB Details
Used H2 in-memory DB to store the details
temporary. Data will be stored in in-memory db
until application is running

## Access the Application
The Spring Boot application will be accessible at http://localhost:8080.

The H2 database console will be accessible at http://localhost:8080/h2-console.

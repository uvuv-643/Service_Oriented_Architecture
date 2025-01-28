package ru.uvuv643.web;

import jakarta.jws.WebMethod;

import jakarta.jws.WebService;

@WebService
public interface CalculatorService {

    @WebMethod
    String add(String a, String b);

}

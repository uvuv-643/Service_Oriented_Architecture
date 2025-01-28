package ru.uvuv643;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface CalculatorService {

    @WebMethod
    String add(String a, String b);

}

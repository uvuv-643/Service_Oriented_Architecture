package ru.uvuv643;

import javax.jws.WebService;

@WebService(endpointInterface = "ru.uvuv643.web.CalculatorService")
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public String add(String a, String b) {
        return a + b;
    }

}

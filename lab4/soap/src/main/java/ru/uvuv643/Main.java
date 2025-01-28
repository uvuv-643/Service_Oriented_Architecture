package ru.uvuv643;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        String url = "http://localhost:9919/soapdemo";
        Endpoint.publish(url, new CalculatorServiceImpl());
        System.out.println("Hello world!");
    }
}
package ru.uvuv643;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.security.NoSuchAlgorithmException;

@WebService
public interface HumanWebService {

    @WebMethod
    public void makeSad(String heroId);

}

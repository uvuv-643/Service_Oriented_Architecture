package ru.uvuv643;

import javax.jws.WebService;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

@WebService(endpointInterface = "ru.uvuv643.HumanWebService")
public class HumanWebServiceImpl implements HumanWebService {

    @Override
    public void makeSad(String heroId)  {
        System.out.println(heroId);
        try {
            String xmlPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<ModifyHumanBeingRequest>"
                    + "<mood>SORROW</mood>"
                    + "</ModifyHumanBeingRequest>";

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            String url = "https://rwfsh39.ru/s1/api/v1/human-being/" + heroId;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("PUT");

            con.setRequestProperty("Content-Type", "application/xml");
            con.setRequestProperty("Accept", "application/xml");

            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(xmlPayload);
                wr.flush();
            }

            // Send request
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.writeBytes(xmlPayload);
                wr.flush();
            }

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

package ru.uvuv643.ribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Configuration
class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}

@SpringBootApplication
@EnableDiscoveryClient
public class RibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() throws Exception {
        // Disable SSL validation
        disableSslValidation();

        // Create a simple RestTemplate
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }


    private void disableSslValidation() throws Exception {
        // Trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Disable hostname verification
        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}

@RestController
class LoadBalancedController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> proxy(HttpServletRequest request, HttpServletResponse response, HttpEntity<String> entity) throws IOException {
        String prefix = "/";
        String requestUri = request.getRequestURI();
        String path = requestUri.substring(requestUri.indexOf(prefix) + prefix.length());

        String targetServiceUrl = "https://s2/" + path;

        try {
            // Forward the request method and body
            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            ResponseEntity<String> serviceResponse = restTemplate.exchange(targetServiceUrl, method, entity, String.class);

            // Return service response as-is with proper status, headers, and body
            return new ResponseEntity<>(
                    serviceResponse.getBody(),
                    serviceResponse.getHeaders(),
                    serviceResponse.getStatusCode()
            );

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            // In case of client or server-side errors, return the exact response from the server
            return new ResponseEntity<>(
                    ex.getResponseBodyAsString(),
                    ex.getResponseHeaders(),
                    ex.getStatusCode()
            );
        } catch (Exception ex) {
            // For any other server-side issues, return a generic error without defaulting to 500
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("An error occurred while processing the request. " + ex.getMessage());
        }
    }
}
package ru.uvuv643;

import org.apache.http.ssl.SSLContextBuilder;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.uvuv643.dto.human.ListHumanBeingDto;
import ru.uvuv643.dto.human.PairTeamHumanDto;

import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@RestController
@RequestMapping("/v1/team")
public class TeamWebServiceImpl {

    private final DSLContext dsl;

    @Autowired
    public TeamWebServiceImpl(DSLContext dslContext) {
        this.dsl = dslContext;
    }

    @GetMapping(value = "/heroes", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<ListHumanBeingDto> getHeroes() {
        try {
            List<PairTeamHumanDto> heroIds = dsl.select(
                            field("hero_id", Integer.class),
                            field("team_id", Integer.class))
                    .from(table("team_heroes"))
                    .fetch()
                    .map(record -> new PairTeamHumanDto(
                            record.get("hero_id", Integer.class),
                            record.get("team_id", Integer.class))
                    );
            return ResponseEntity.ok(new ListHumanBeingDto(heroIds));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping(value = "/{teamId}/add/{heroId}", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> addHeroToTheTeam(@PathVariable int heroId, @PathVariable int teamId) {
        try {
            int count = dsl.selectCount()
                    .from(table("team_heroes"))
                    .where(field("hero_id").eq(heroId))
                    .and(field("team_id").eq(teamId))
                    .fetchOne(0, int.class);

            if (count > 0) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>Hero is already in the team</response>");
            } else {
                dsl.insertInto(table("team_heroes"))
                        .columns(field("hero_id"), field("team_id"))
                        .values(heroId, teamId)
                        .execute();
                return ResponseEntity.ok("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>Hero added successfully to the team</response>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_XML)
                    .body("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>Error: " + ex.getMessage() + "</response>");
        }
    }

    @PutMapping(value = "/{teamId}/make-depressive", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> makeTeamDepressive(@PathVariable int teamId) {
        try {
            List<Integer> heroIds = dsl.select(field("hero_id", Integer.class))
                    .from(table("team_heroes"))
                    .where(field("team_id").eq(teamId))
                    .fetch()
                    .map(record -> record.get("hero_id", Integer.class));

            if (heroIds.isEmpty()) {
                return ResponseEntity.status(404)
                        .contentType(MediaType.APPLICATION_XML)
                        .body("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>No heroes found in the team</response>");
            }

            for (Integer heroId : heroIds) {
                String response = sendDepressiveMoodRequest(heroId);
                if (response != null) {
                    System.out.println("Response for Hero ID " + heroId + ": " + response);
                }
            }

            return ResponseEntity.ok("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>Team made depressive</response>");

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_XML)
                    .body("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>Error: " + ex.getMessage() + "</response>");
        }
    }

    public String sendDepressiveMoodRequest(Integer heroId) {
        RestTemplate restTemplate = createRestTemplate();

        String xmlPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<ModifyHumanBeingRequest>"
                + "<mood>SORROW</mood>"
                + "</ModifyHumanBeingRequest>";

        String url = "http://host.docker.internal:8099/v1/api/" + heroId;

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Content-Type", "application/xml");
        headers.set("Accept", "application/xml");
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(xmlPayload, headers);

        return restTemplate.exchange(url, org.springframework.http.HttpMethod.POST, entity, String.class).getBody();
    }

    private RestTemplate createRestTemplate() {
        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(null, acceptingTrustStrategy);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(socketFactory)
                    .build();
            return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RestTemplate with SSL support", e);
        }
    }

    @GetMapping(value = "/properties", produces = MediaType.APPLICATION_XML_VALUE)
    public String getPropertiesAsXml() throws Exception {
        Properties properties = new Properties();
        properties.load(new ClassPathResource("application.properties").getInputStream());
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(PropertiesXml.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(new PropertiesXml(properties), writer);
        return writer.toString();
    }

    @XmlRootElement(name = "Properties")
    public static class PropertiesXml {
        private Properties properties;
        public PropertiesXml() {}
        public PropertiesXml(Properties properties) { this.properties = properties; }
        @XmlElement(name = "Property")
        public Properties getProperties() { return properties; }
        public void setProperties(Properties properties) { this.properties = properties; }
    }

}
package ru.uvuv643.web.config;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.util.Collections;

@Startup
@Singleton
public class ConsulConfig {

    public static final String service = "payara";

    String serviceId = service.equals("wildfly") ? "s1-1" : "s1-2";
    Consul client;
    AgentClient agentClient;

    @PostConstruct
    void init() {
        client = Consul.builder()
                .withHostAndPort(HostAndPort.fromParts("consul", 8500)).build();
        agentClient = client.agentClient();
        Registration service = ImmutableRegistration.builder()
                .id(serviceId)
                .name("s1")
                .port(8080)
                .address(ConsulConfig.service)
                .tags(Collections.singletonList("s1"))
                .meta(Collections.singletonMap("version", "1.0"))
                .build();

        agentClient.register(service);
    }

    @PreDestroy
    private void deregisterService(){
        agentClient.deregister(serviceId);
    }

}
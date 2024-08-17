package Volt.example.Volt.ContentManagement.Application.Services;

import Volt.example.Volt.ContentManagement.Application.Interfaces.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
public class StreamServiceImpl implements StreamService {
    @Value("${zegocloud.appId}")
    private String appId;

    @Value("${zegocloud.appSecret}")
    private String appSecret;

    @Value("${zegocloud.baseUrl}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String createStream() {
        String url = baseUrl + "/live/streams";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("appId", appId);
        headers.set("appSecret", appSecret);

        String requestBody = String.format("{\"streamName\":\"%s\"}", "streamName");

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace
            throw new RuntimeException("Failed to create stream", e);
        }
    }
}

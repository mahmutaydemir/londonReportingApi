package com.maya.londonReportingApi.services;

import com.maya.londonReportingApi.models.ClientInfoRequest;
import com.maya.londonReportingApi.models.ClientInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.maya.londonReportingApi.entities.HttpContext.getHttpEntity;

@Service
public class ClientInformationService {

    private RestTemplate _restTemplate;

    @Value("${sandbox.client.url}")
    private String reportingServerUrl;

    public ClientInformationService(RestTemplate restTemplate) {
        this._restTemplate = restTemplate;
    }


    public Optional<ClientInfoResponse> getClientInfo(ClientInfoRequest request, String token) {
        HttpEntity<?> httpEntity = getHttpEntity(request, token);
        ClientInfoResponse response = _restTemplate.postForObject(reportingServerUrl+"/v3/transaction",httpEntity, ClientInfoResponse.class);
        return Optional.ofNullable(response);
    }
}
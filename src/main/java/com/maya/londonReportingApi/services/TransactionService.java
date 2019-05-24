package com.maya.londonReportingApi.services;

import com.maya.londonReportingApi.models.TransactionReportRequest;
import com.maya.londonReportingApi.models.TransactionReportResponse;
import com.maya.londonReportingApi.models.TransactionRequest;
import com.maya.londonReportingApi.models.TransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.maya.londonReportingApi.entities.HttpContext.getHttpEntity;

@Service
public class TransactionService {
    @Value("${sandbox.client.url}")
    private String reportingServerUrl;

    RestTemplate _restTemplate;

    public TransactionService(RestTemplate restTemplate) {
        _restTemplate = restTemplate;
    }

    public Optional<TransactionReportResponse> reportTransaction(TransactionReportRequest request, String token) {
        HttpEntity<?> httpEntity = getHttpEntity(request, token);
        ResponseEntity<TransactionReportResponse> response = _restTemplate.postForEntity(reportingServerUrl+"/v3/transactions/report", httpEntity, TransactionReportResponse.class);
        return Optional.ofNullable(response.getBody());
    }

    public Optional<TransactionResponse> getTransaction(TransactionRequest request, String token) {
        HttpEntity<?> httpEntity = getHttpEntity(request, token);
        ResponseEntity<TransactionResponse> response = _restTemplate.postForEntity(reportingServerUrl+"/v3/transaction", httpEntity, TransactionResponse.class);
        return Optional.ofNullable(response.getBody());
    }
}
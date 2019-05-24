package com.maya.londonReportingApi.controllers;

import com.maya.londonReportingApi.entities.IResponseEntity;
import com.maya.londonReportingApi.exceptions.RestApiException;
import com.maya.londonReportingApi.models.ClientInfoRequest;
import com.maya.londonReportingApi.models.ClientInfoResponse;
import com.maya.londonReportingApi.services.ClientInformationService;
import com.maya.londonReportingApi.services.TokenHandlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClientInformationController {

    public static final String DECLINED = "DECLINED";

    private ClientInformationService _clientInformationService;
    private TokenHandlerService _tokenHandlerService;

    public ClientInformationController(ClientInformationService clientInformationService, TokenHandlerService tokenHandlerService) {
        _clientInformationService = clientInformationService;
        _tokenHandlerService = tokenHandlerService;
    }

    @GetMapping("/customer-infos/{transactionId}")
    public ResponseEntity<ClientInfoResponse> getClientInformation(@PathVariable String transactionId) {
        String serviceToken = _tokenHandlerService.getServiceToken();
        Optional<ClientInfoResponse> clientInfo = _clientInformationService.getClientInfo(new ClientInfoRequest(transactionId), serviceToken);
        clientInfo.ifPresent(resp -> {
            if(DECLINED.equals(resp.getStatus()))
                throw new RestApiException(clientInfo.get().getMessage());
        });
        return IResponseEntity.getResponseEntity(clientInfo);
    }


}

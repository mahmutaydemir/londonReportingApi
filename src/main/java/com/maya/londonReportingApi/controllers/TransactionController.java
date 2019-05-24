package com.maya.londonReportingApi.controllers;

import com.maya.londonReportingApi.entities.IResponseEntity;
import com.maya.londonReportingApi.exceptions.RestApiException;
import com.maya.londonReportingApi.models.TransactionReportRequest;
import com.maya.londonReportingApi.models.TransactionReportResponse;
import com.maya.londonReportingApi.models.TransactionRequest;
import com.maya.londonReportingApi.models.TransactionResponse;
import com.maya.londonReportingApi.services.TokenHandlerService;
import com.maya.londonReportingApi.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {

    public static final String DECLINED = "DECLINED";

    private TransactionService _service;

    private TokenHandlerService _tokenHandlerService;

    public TransactionController(TransactionService service, TokenHandlerService tokenHandlerService) {
        _service = service;
        _tokenHandlerService = tokenHandlerService;
    }

    @GetMapping("/transaction-reports")
    public ResponseEntity<TransactionReportResponse> getTransactionReport(@RequestParam("start") String start, @RequestParam("end")  String end) {
        Optional<TransactionReportResponse> response = _service.reportTransaction(new TransactionReportRequest(start,end),_tokenHandlerService.getServiceToken());
        response.ifPresent(resp -> {
            if(DECLINED.equals(resp.getStatus()))
                throw new RestApiException(response.get().getMessage());
        });
        return IResponseEntity.getResponseEntity(response );
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String transactionId) {
        Optional<TransactionResponse> response = _service.getTransaction(new TransactionRequest(transactionId),_tokenHandlerService.getServiceToken());
        response.ifPresent(resp -> {
            if(DECLINED.equals(resp.getStatus()))
                throw new RestApiException(response.get().getMessage());
        });
        return IResponseEntity.getResponseEntity(response );
    }

}

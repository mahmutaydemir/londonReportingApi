package com.maya.londonReportingApi.controllers;

import com.maya.londonReportingApi.LondonReportingApiApplication;
import com.maya.londonReportingApi.entities.JsonConverter;
import com.maya.londonReportingApi.exceptions.RestApiExceptionHandler;
import com.maya.londonReportingApi.models.MerchantTransaction;
import com.maya.londonReportingApi.models.TransactionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LondonReportingApiApplication.class)
public class TransactionIntegrationTest {

    private MockMvc _mockMvc;

    @Autowired
    TransactionController controller;

    @Autowired
    RestApiExceptionHandler restExceptionHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        _mockMvc = standaloneSetup(this.controller)
                .setControllerAdvice(restExceptionHandler).build();
    }

    @Test
    public void givenStartAndEndDateWhenTransactionThenApproved() throws Exception {
        _mockMvc.perform(get("/api/transaction-reports?start={startDate}&end={endDate}","2015-07-01","2019-10-01" ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    public void givenTransactionIdWhenTransactionThenJsonFile() throws Exception {
        TransactionResponse transactionResponse = JsonConverter.convertToObject("transactionResponse.json", TransactionResponse.class);
        MerchantTransaction merchant = transactionResponse.getTransaction().getMerchant();
        _mockMvc.perform(get("/api/transactions/{transactionId}", "1010992-1539329625-1293" ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.transaction.merchant.id").value(merchant.getId().intValue()))
                .andExpect(jsonPath("$.transaction.merchant.referenceNo").value(merchant.getReferenceNo()))
                .andExpect(jsonPath("$.transaction.merchant.status").value(merchant.getStatus()))
                .andExpect(jsonPath("$.transaction.merchant.operation").value(merchant.getOperation()))
                .andExpect(jsonPath("$.transaction.merchant.type").value(merchant.getType()))
                .andExpect(jsonPath("$.transaction.merchant.transactionId").value(merchant.getTransactionId()));
    }
}
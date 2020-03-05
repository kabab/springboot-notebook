package com.oracle.challenge.controllers;

import com.oracle.challenge.domains.ExecutionRequest;
import com.oracle.challenge.domains.ExecutionResponse;
import com.oracle.challenge.services.NotebookService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class NotebookControllerTest {

    @InjectMocks
    private NotebookController notebookController;

    @Mock
    private NotebookService notebookService;

    @Test
    public void executeShouldReturnResponseEntity() {
        ExecutionRequest exReq = new ExecutionRequest();
        ExecutionResponse exResp = new ExecutionResponse("Hello world", "session-id");

        given(notebookService.executeCode(anyString(), anyString())).willReturn(exResp);

        ResponseEntity actual = notebookController.execute(exReq, null);

        ResponseEntity<ExecutionResponse> expected = ResponseEntity.ok(
                new ExecutionResponse("Hello world", "session-id")
        );

        Assert.assertEquals(expected, actual);
    }

}

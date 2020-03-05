package com.oracle.challenge.services;

import com.oracle.challenge.domains.ExecutionResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class NotebookControllerServiceTest {

    @Mock
    private PythonService pythonService;

    @InjectMocks
    private NotebookService notebookService;

    @Spy
    private List<Interpreter> interpreters = new ArrayList<>();

    @Before
    public void setUp() {
        interpreters.add(pythonService);
    }

    @Test
    public void executeCodeShouldReturnResponse() {
        ExecutionResponse executionResponse = new ExecutionResponse();
        executionResponse.setSessionId("session-id");
        executionResponse.setResult("1\n");
        given(pythonService.executeCode(anyString(), anyString())).willReturn(executionResponse);
        given(pythonService.getName()).willReturn("python");

        ExecutionResponse exResp = notebookService.executeCode(null, "%python print 1");

        Assert.assertEquals("1\n", exResp.getResult());
    }

    @Test
    public void executeCodeShouldReturnSyntaxError() {
        ExecutionResponse executionResponse = new ExecutionResponse();
        executionResponse.setSessionId("session-id");
        executionResponse.setResult("1\n");
        given(pythonService.executeCode(anyString(), anyString())).willReturn(executionResponse);
        given(pythonService.getName()).willReturn("python");

        ExecutionResponse exResp = notebookService.executeCode("session-id", "python print 1");

        Assert.assertEquals("Notebook Error: Syntax error : python print 1", exResp.getResult());
    }

    @Test
    public void executeCodeShouldReturnUnknownInterpreter() {
        ExecutionResponse executionResponse = new ExecutionResponse();
        executionResponse.setSessionId("session-id");
        executionResponse.setResult("1\n");
        given(pythonService.executeCode(anyString(), anyString())).willReturn(executionResponse);
        given(pythonService.sessionExist(anyString())).willReturn(true);
        given(pythonService.getName()).willReturn("python");

        ExecutionResponse exResp = notebookService.executeCode("session-id", "%r print 1");

        Assert.assertEquals("Notebook Error: Unknown interpreter r", exResp.getResult());
    }

    @Test
    public void executeCodeShouldReturnResponseCase2() {
        ExecutionResponse executionResponse = new ExecutionResponse();
        executionResponse.setSessionId("session-id");
        executionResponse.setResult("1\n");
        given(pythonService.executeCode(anyString(), anyString())).willReturn(executionResponse);
        given(pythonService.sessionExist(anyString())).willReturn(true);
        given(pythonService.getName()).willReturn("python");

        ExecutionResponse exResp = notebookService.executeCode("session-id", "%python print 1");

        Assert.assertEquals("1\n", exResp.getResult());
    }
}

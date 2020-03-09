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

import java.io.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class NotebookControllerTest {

    @InjectMocks
    private NotebookController notebookController;

    @Mock
    private NotebookService notebookService;


    @Test
    public void anotherTest() throws IOException {
        Process process = new ProcessBuilder("python3", "-u", "-i").redirectErrorStream(true).start();

        OutputStream outputStream = process.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);

        InputStreamReader in = new InputStreamReader(process.getInputStream());
        BufferedReader reader = new BufferedReader(in);
        String s = null;

        while (!reader.ready());

        while ( reader.ready()) {
            reader.read();
        }

        printWriter.println("a = 10");
        printWriter.flush();

        StringBuffer buff = new StringBuffer();

        while (!reader.ready());
        while ( reader.ready()) {
            buff.append((char)reader.read());
        }

        System.out.println(buff.substring(0, buff.length() - 4));
        System.out.println("----");

        printWriter.println("print(a)");
        printWriter.flush();

        buff.delete(0, buff.length());
        while (!reader.ready());
        while (reader.ready()) {
            buff.append((char)reader.read());
        }

        System.out.println(buff.substring(0, buff.length() - 4));
        System.out.println("----");
    }

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

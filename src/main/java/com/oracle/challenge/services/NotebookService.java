package com.oracle.challenge.services;

import com.oracle.challenge.conf.NotebookConf;
import com.oracle.challenge.domains.ExecutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotebookService {

    @Autowired
    private List<Interpreter> interpreters;

    @Autowired
    private NotebookConf notebookConf;

    public ExecutionResponse executeCode(String sessionId, String code) {
        String[] s = code.split(" ");
        List<String> supportedInterpreters =
                interpreters.stream().map(interpreter -> interpreter.getName()).collect(Collectors.toList());
        ExecutionResponse exResp = new ExecutionResponse();
        if (s.length < 2 || s[0].toCharArray()[0] != '%' ) {
            exResp.setResult("Notebook Error: Syntax error : " + code);
            return exResp;
        }

        String interpreterName = s[0].substring(1);

        int i = supportedInterpreters.indexOf(interpreterName);
        if (i < 0) {
            exResp.setResult("Notebook Error: Unknown interpreter " + interpreterName);
            return exResp;
        }

        Interpreter interpreter = interpreters.get(i);

        code = code.substring(s[0].length() + 1);

        if (!interpreter.sessionExist(sessionId)) {
            sessionId = interpreter.createInstance();
        }

        final ExecutionResponse[] executionResponse = new ExecutionResponse[1];

        String finalSessionId = sessionId;
        String finalCode = code;
        Thread thread = new Thread(() -> {
            executionResponse[0] = interpreter.executeCode(finalSessionId, finalCode);
        });

        exResp.setSessionId(sessionId);
        thread.start();

        try {
            thread.join(notebookConf.getTimeout());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread.State state = thread.getState();
        switch (state) {
            case TERMINATED:
                exResp.setResult(executionResponse[0].getResult());
                break;
            case TIMED_WAITING:
                exResp.setResult("Notebook Error: Timeout");
                thread.interrupt();
                break;
            default:
                exResp.setResult("Notebook Error: Unknown");
        }

        return exResp;
    }
}

package com.oracle.challenge.services;

import com.oracle.challenge.domains.ExecutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotebookService {

    @Autowired
    private List<Interpreter> interpreters;

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

        return interpreter.executeCode(sessionId, code);
    }
}

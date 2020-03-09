package com.oracle.challenge.services;


import com.oracle.challenge.conf.NotebookConf;
import com.oracle.challenge.domains.ExecutionResponse;
import org.python.core.PyException;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PythonService implements Interpreter {

    Map<String, PythonInterpreter> instances = new HashMap<>();

    public ExecutionResponse executeCode(String instanceId, String code) {
        PythonInterpreter pythonInterpreter = instances.get(instanceId);
        StringWriter stringWriter = new StringWriter();

        pythonInterpreter.setErr(stringWriter);
        pythonInterpreter.setOut(stringWriter);

        try {
            pythonInterpreter.exec(code);
        } catch (PyException e) {
            stringWriter.write(e.toString());
        }

        ExecutionResponse exResp = new ExecutionResponse();
        exResp.setSessionId(instanceId);
        exResp.setResult(stringWriter.toString());
        return exResp;
    }

    public String createInstance() {
        PythonInterpreter instance = new PythonInterpreter();
        UUID uuid = UUID.randomUUID();
        instances.put(uuid.toString(), instance);
        return uuid.toString();
    }

    @Override
    public String getName() {
        return "python";
    }

    @Override
    public boolean sessionExist(String sessionId) {
        return instances.containsKey(sessionId);
    }
}

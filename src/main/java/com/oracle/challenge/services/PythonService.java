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

    @Autowired
    private NotebookConf notebookConf;

    public ExecutionResponse executeCode(String instanceId, String code) {
        PythonInterpreter pythonInterpreter = instances.get(instanceId);
        StringWriter stringWriter = new StringWriter();

        pythonInterpreter.setErr(stringWriter);
        pythonInterpreter.setOut(stringWriter);

        Thread thread = new Thread(() -> {
            try {
                pythonInterpreter.exec(code);
            } catch (PyException e) {
                stringWriter.write(e.toString());
            }
        });

        thread.start();

        try {
            thread.join(notebookConf.getTimeout());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread.State state = thread.getState();
        ExecutionResponse exResp = new ExecutionResponse();
        exResp.setSessionId(instanceId);
        switch (state) {
            case TERMINATED:
                exResp.setResult(stringWriter.toString());
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

package com.oracle.challenge.services;

import com.oracle.challenge.domains.ExecutionResponse;
import lombok.SneakyThrows;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class Python3Interpreter implements Interpreter {

    Map<String, Process> instances = new HashMap<>();

    @Override
    public ExecutionResponse executeCode(String instanceId, String code) {
        StringBuffer buff = new StringBuffer();

        Process process = instances.get(instanceId);
        OutputStream outputStream = process.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);

        InputStreamReader in = new InputStreamReader(process.getInputStream());
        BufferedReader reader = new BufferedReader(in);

        printWriter.println(code);
        printWriter.flush();
        ExecutionResponse exResp = new ExecutionResponse();
        exResp.setSessionId(instanceId);
        try {
            while (!reader.ready());
            while ( reader.ready()) {
                buff.append((char)reader.read());
            }
            exResp.setResult(buff.substring(0, buff.length() - 4));
            return exResp;
        } catch (IOException e) {
            exResp.setResult("Error");
            return exResp;
        }
    }

    @SneakyThrows
    @Override
    public String createInstance() {
        Process process = new ProcessBuilder("python3", "-i", "-u").redirectErrorStream(true).start();

        InputStreamReader in = new InputStreamReader(process.getInputStream());
        BufferedReader reader = new BufferedReader(in);

        while (!reader.ready());
        while (reader.ready()) {
            reader.read();
        }

        UUID uuid = UUID.randomUUID();
        instances.put(uuid.toString(), process);
        return uuid.toString();
    }

    @Override
    public String getName() {
        return "python3";
    }

    @Override
    public boolean sessionExist(String sessionId) {
        return instances.containsKey(sessionId);
    }
}

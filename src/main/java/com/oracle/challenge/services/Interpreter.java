package com.oracle.challenge.services;

import com.oracle.challenge.domains.ExecutionResponse;

public interface Interpreter {
    ExecutionResponse executeCode(String instanceId, String code);
    String createInstance();
    String getName();
    boolean sessionExist(String sessionId);
}

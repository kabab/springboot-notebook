package com.oracle.challenge.controllers;

import com.oracle.challenge.domains.ExecutionRequest;
import com.oracle.challenge.domains.ExecutionResponse;
import com.oracle.challenge.services.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Notebook {

    @Autowired
    private NotebookService notebookService;

    @PostMapping("/execute")
    public ResponseEntity execute(@RequestBody ExecutionRequest request,
                                  @RequestParam(required = false, name = "sessionId") String sessionId) {
        /*ExecutionResponse exResp;
        if (sessionId != null) {
            exResp = notebookService.executeCode(sessionId, request.getCode());
        } else {
            sessionId = notebookService.createInstance();
            exResp = notebookService.executeCode(sessionId, request.getCode());
        }*/

        ExecutionResponse exResp = notebookService.executeCode(sessionId, request.getCode());

        return ResponseEntity.ok(exResp);
    }
}

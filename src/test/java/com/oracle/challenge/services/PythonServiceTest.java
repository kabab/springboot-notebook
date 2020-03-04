package com.oracle.challenge.services;

import com.oracle.challenge.conf.NotebookConf;
import com.oracle.challenge.domains.ExecutionResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PythonService.class)
public class PythonServiceTest {

    @Mock
    private NotebookConf notebookConf;

    @InjectMocks
    private PythonService pythonService;

    final private String ID = "493410b3-dd0b-4b78-97bf-289f50f6e74f";

    @Before
    public void setUp() {
        UUID uuid = UUID.fromString(ID);
        mockStatic(UUID.class);
        when(UUID.randomUUID()).thenReturn(uuid);
        when(notebookConf.getTimeout()).thenReturn(5000l);
    }

    @Test
    public void createInstanceShouldReturnId() {

        String sessionId = pythonService.createInstance();

        Assert.assertEquals(sessionId, ID);
    }

    @Test
    public void executeCodeShouldReturnSuccessfulResult() {
        String instance = pythonService.createInstance();
        ExecutionResponse executionResponse = pythonService.executeCode(instance, "print 100");

        Assert.assertEquals("100\n", executionResponse.getResult());
    }

    @Test
    public void executeCodeShouldReturnTimeout() {
        String instance = pythonService.createInstance();
        ExecutionResponse executionResponse = pythonService.executeCode(instance, "import time\ntime.sleep(10)");
        Assert.assertEquals("Notebook Error: Timeout", executionResponse.getResult());
    }

    @Test
    public void getNameShouldShouldReturnString() {
        Assert.assertEquals("python", pythonService.getName());
    }

    @Test
    public void sessionExistShouldReturnTrue() {
        pythonService.createInstance();
        Assert.assertEquals(true, pythonService.sessionExist(ID));
    }

    @Test
    public void sessionExistShouldReturnFalse() {
        Assert.assertEquals(false, pythonService.sessionExist(ID));
    }

}

package com.oracle.challenge;


import com.oracle.challenge.controllers.NotebookController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChallengeApplicationTests {

	@Autowired
	private NotebookController notebookController;

	@Test
	public void contextLoads() {
		assertThat(notebookController).isNotNull();
	}

}

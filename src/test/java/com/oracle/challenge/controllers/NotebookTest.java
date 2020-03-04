package com.oracle.challenge.controllers;

import org.junit.Test;

import java.io.*;
import java.util.UUID;

public class NotebookTest {

    @Test
    public void testRuntime() throws IOException, InterruptedException {
        Process process;
        BufferedReader result;
        PrintWriter input;

        process = new ProcessBuilder("python", "-u", "-i").redirectErrorStream(true).start();

        input = new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true);


        // input.close();
        result = new BufferedReader(new InputStreamReader(process.getInputStream()));


        Thread thread = new Thread(() -> {
            String line;
            try {
                while ((line = result.readLine()) != null && result.ready()) {

                    /* Some processing for the read line */

                    System.out.println("output:\t" + line);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        });

        input.println("print('hello world')");
        thread.start();
        thread.join();
        // input.close();
        System.out.println(process.isAlive());

    }

    @Test
    public void testUUID() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }

}

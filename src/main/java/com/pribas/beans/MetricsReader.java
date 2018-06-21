package com.pribas.beans;

import com.google.common.io.CharStreams;
import com.jcraft.jsch.*;
import com.pribas.controller.ServerMetricsInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MetricsReader implements ServerMetricsInterface {


    public Session sessionSSH(String username, String pass, String host, int port) {

        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(username, host, port);

        } catch (JSchException e) {
            System.out.println("It looks like somethink went wrong :( ");
            e.printStackTrace();
        }
        session.setPassword(pass);
        session.setConfig("StrictHostKeyChecking", "no");

        System.out.println("Establishing Connection...");
        try {
            if(!session.isConnected())
            {
                session.connect();
            }

        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

    public String execCommand(String command, Session session) {
        if (session == null || !session.isConnected()) {
            System.out.println("Null session variable or session is not connected.");
            return null;
        }
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            InputStream output = channel.getInputStream();

            channel.connect();
            String result = CharStreams.toString(new InputStreamReader(output));
            channel.disconnect();

            System.out.println("Result of command \n"+result+"on "+session.getHost()+":"+result);


            return result;
        } catch (JSchException | IOException e) {
            System.out.println("Failed "+command+"on "+session.getHost()+":"+e.toString());
            return null;
        }
    }
}

package com.pribas.controller;

import com.jcraft.jsch.Session;

public interface ServerMetricsInterface {
    Session sessionSSH(String username, String pass, String host, int port);

    String execCommand(String command, Session session);

}

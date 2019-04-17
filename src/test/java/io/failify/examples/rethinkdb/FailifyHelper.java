package io.failify.examples.rethinkdb;

import io.failify.dsl.entities.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FailifyHelper {
    public static final Logger logger = LoggerFactory.getLogger(FailifyHelper.class);

    public static Deployment getDeployment(int numOfNodes) {
        Deployment.Builder builder = Deployment.builder("example-rethinkdb")
                // Service Definitions
                .withService("rethinkdb")
                    .startCommand("rethinkdb --bind all --log-file /rethinkdb/log_file")
                    .dockerImageName("failify/example-rethinkdb")
                    .dockerFileAddress("docker/Dockerfile", false)
                    .logFile("/rethinkdb/log_file").and()
                .withNode("n1", "rethinkdb").tcpPort(28015).and();

        for (int i = 2; i <= numOfNodes; i++) {
            builder.withNode("n" + i, "rethinkdb")
                .startCommand("rethinkdb --join n1:29015 --bind all --log-file /rethinkdb/log_file").and();
        }

        return builder.build();
    }
}

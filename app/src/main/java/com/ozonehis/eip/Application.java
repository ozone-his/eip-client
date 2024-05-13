/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip;

import jakarta.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.lockservice.LockServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = {"org.openmrs.eip, com.ozonehis.eip"})
public class Application {
    @Value("${spring.mngt-datasource.jdbcUrl:#{null}}")
    private String url;

    @Value("${spring.mngt-datasource.username:#{null}}")
    private String username;

    @Value("${spring.mngt-datasource.password:#{null}}")
    private String password;

    public static void main(final String[] args) {
        log.info("Starting EIP Client Application . . .");
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void releaseLiquibaseLock() {
        if (url == null || username == null || password == null) {
            log.info("Skipping liquibase step: forceReleaseLock");
            return;
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            LockServiceFactory.getInstance().getLockService(database).forceReleaseLock();
        } catch (Exception e) {
            log.error("Error occurred while releasing lock from Liquibase: {}", e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Error occurred while closing connection: {}", e.getMessage(), e);
                }
            }
        }
    }
}

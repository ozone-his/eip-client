/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.lockservice.LockServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@SpringBootApplication(scanBasePackages = {"org.openmrs.eip, com.ozonehis.eip"})
public class Application {

    @Value("mngtDataSource")
    private static DataSource dataSource;

    public static void main(final String[] args) {
        log.info("Starting EIP Client Application . . .");
        releaseLiquibaseLock();
        SpringApplication.run(Application.class, args);
    }

    private static void releaseLiquibaseLock() {
        if (dataSource == null) {
            log.info("No datasource found, skipping liquibase force release lock step");
            return;
        }
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            LockServiceFactory.getInstance().getLockService(database).forceReleaseLock();
        } catch (Exception e) {
            log.error("Error occurred while releasing lock from Liquibase: {}", e.getMessage(), e);
        }
    }
}

/*
 * Copyright © 2021, Ozone HIS <info@ozone-his.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.ozonehis.eip;

import jakarta.annotation.PostConstruct;
import java.sql.SQLException;
import javax.sql.DataSource;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LockException;
import liquibase.lockservice.LockServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication(scanBasePackages = {"org.openmrs.eip, com.ozonehis.eip"})
public class Application {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(final String[] args) {
        log.info("Starting EIP Client Application . . .");
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void releaseLiquibaseLock() {
        if (applicationContext.containsBean("mngtDataSource")) {
            DataSource mgtDatasource = applicationContext.getBean("mngtDataSource", DataSource.class);
            try {
                Database database = DatabaseFactory.getInstance()
                        .findCorrectDatabaseImplementation(new JdbcConnection(mgtDatasource.getConnection()));
                LockServiceFactory.getInstance().getLockService(database).forceReleaseLock();
            } catch (DatabaseException | SQLException | LockException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

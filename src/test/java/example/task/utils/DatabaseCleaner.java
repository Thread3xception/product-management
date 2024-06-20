package example.task.utils;

import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static liquibase.database.DatabaseFactory.getInstance;

@Service
@Value
@ActiveProfiles("test")
public class DatabaseCleaner {
    Connection connection;
    Database database;
    Liquibase liquibase;

    public DatabaseCleaner() throws SQLException, DatabaseException {
        this.connection = DriverManager.getConnection("jdbc:h2:mem:product-test-db", "sa", "password");
        this.database = getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        this.liquibase = new Liquibase("liquibase-test-changelog.xml", new ClassLoaderResourceAccessor(), database);
    }

    public void cleanUp() throws LiquibaseException {
        liquibase.dropAll();

        CommandScope updateCommand = new CommandScope("update");
        updateCommand.addArgumentValue("liquibase", liquibase);
        updateCommand.addArgumentValue("url", database.getConnection().getURL());
        updateCommand.addArgumentValue("username", database.getConnection().getConnectionUserName());
        updateCommand.addArgumentValue("password", "password");
        updateCommand.addArgumentValue("changelogFile", "liquibase-test-changelog.xml");
        updateCommand.execute();
    }
}

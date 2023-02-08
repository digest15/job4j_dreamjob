package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Optional;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        sql2oUserRepository.findAll()
                .forEach(u -> sql2oUserRepository.deleteById(u.getId()));
    }

    @Test
    public void whenSaveThenGetSame() {
        var name = "Ivan";
        var email = "ivan@mail.ru";
        var password = "password";

        Optional<User> user = sql2oUserRepository.save(new User(name, email, password));
        assertThat(user.isPresent()).isTrue();

        Optional<User> savedUser = sql2oUserRepository.findByEmailAndPassword(email, password);
        assertThat(savedUser.isPresent()).isTrue();

        assertThat(user.get()).isEqualTo(savedUser.get());
    }

    @Test
    public void whenSaveTwiceThenGetEmptyOptional() {
        var name = "Ivan";
        var email = "ivan@mail.ru";
        var password = "password";

        Optional<User> user = sql2oUserRepository.save(new User(name, email, password));
        assertThat(user.isPresent()).isTrue();

        Optional<User> savedUser = sql2oUserRepository.save(new User(name, email, password));
        assertThat(savedUser.isPresent()).isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oUserRepository.findByEmailAndPassword("email", "paswd")).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var name = "Ivan";
        var email = "ivan@mail.ru";
        var password = "password";

        User user = sql2oUserRepository.save(new User(name, email, password)).get();
        var isDeleted = sql2oUserRepository.deleteById(user.getId());
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.getName(), user.getPassword());

        assertThat(isDeleted).isTrue();
        assertThat(savedUser).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oUserRepository.deleteById(0)).isFalse();
    }
}
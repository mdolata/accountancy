package DAO;

import Model.User;

import java.util.Optional;

/**
 * Created by mateu on 31.05.2017 , 21:50.
 * Interface for data access object
 */
public interface UserDAO {
    Optional<User> getUserById(Integer id);
    Optional<User> getUserByLogin(String login);

    Optional<User> addNewUser(String login, String password);

}

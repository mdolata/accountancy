package DAO;

import Model.User;

import java.security.MessageDigest;
import java.util.*;

/**
 * Created by mateu on 31.05.2017 , 21:54.
 *
 * Implementation of UserDao based on ArrayList instead of database.
 * Database will be in the future
 */
public class UserDAOArrayList implements UserDAO {

    private List<User> usersList = new ArrayList<>();
    private Integer maxId = 0;


    @Override
    public Optional<User> getUserById(Integer id) {
        User resultUser = null;
        for (User user : usersList){
            if (user.getId().equals(id) && user.getActive()){
                resultUser = user;
                break;
            }
        }
        return Optional.ofNullable(resultUser);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        User resultUser = null;
        for (User user : usersList){
            if (user.getLogin().equals(login) && user.getActive()){
                resultUser = user;
                break;
            }
        }
        return Optional.ofNullable(resultUser);
    }

    @Override
    public Optional<User> addNewUser(String login, String password) {
        User newUser = null; // new User( ++ maxId , login, password);
        if(isUserExistsByLogin(login)){
            newUser = new User( ++ maxId , login, password);
            usersList.add(newUser);
        }
        return Optional.ofNullable(newUser);
    }

    @Override
    public Set<User> getAllUsers() {
        Set<User> usersSet = new HashSet<>();
        for (User user : usersList){
            usersSet.add(user);
        }
        return usersSet;
    }

    private Boolean isUserExistsByLogin(String login){
        Optional<User> userOptional = getUserByLogin(login);

        return !userOptional.isPresent();
    }

}

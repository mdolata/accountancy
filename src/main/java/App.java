import DAO.UserDAO;
import DAO.UserDAOArrayList;
import Model.User;
import Utils.Filters;
import Utils.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static spark.Spark.*;


public class App {

    private static UserDAO userDAO = new UserDAOArrayList();
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        staticFileLocation("/public");
        int usersAmount = 5;
        addTemporaryUsersToUserDao(usersAmount);
      //  before("*", Filters.addTrailigSlashes);

        get("/", (request, response) -> {
            Map<String,String> model = new HashMap<>();
            model.put("template", Path.hello);
            return new ModelAndView(model, Path.layout);
        }, new VelocityTemplateEngine());


        get("/login", (request, response) -> {
            HashMap<String,String> model = new HashMap<>();
            model.put("template", Path.login);
            model.put("error", "");
            return new ModelAndView(model, Path.layout);
        }, new VelocityTemplateEngine());

        get( "/register", (request, response) -> {
            HashMap<String,String> model = new HashMap<>();
            model.put("template", Path.registration);
            return new ModelAndView(model, Path.layout);
        }, new VelocityTemplateEngine());

        get("/checkRegistration", (request, response) -> {
            String login = request.queryParams("login");
            String password = request.queryParams("password");

            Optional<User> userOptional = userDAO.addNewUser(login, password);
            if (userOptional.isPresent()){
                response.redirect("/login");
            }else{
                response.redirect("/register");
            }
            return new ModelAndView(null,null);
        });


        get("/main", (request, response) -> {
            HashMap<String, String> model = new HashMap<>();
            String login = request.queryParams("login");
            String password = request.queryParams("password");

            if (!checkCredentials(login, password)) {
                response.redirect("/login");
            }
            model.put("login", login);
            model.put("template", Path.main);
            return new ModelAndView(model, Path.layout);

        }, new VelocityTemplateEngine());

        get("/allUsers", (request, response) -> {
            HashMap<String,String> model = new HashMap<>();
            Set<User> allUsers = userDAO.getAllUsers();
            String usersModel = "";
            logger.info("ilosc userow -> " + allUsers.size());
            for (User user : allUsers){
                usersModel += user.getId() + " " + user.getLogin() + "</br>";
            }
            model.put("users", usersModel);
            model.put("template", Path.allUsers);
            return new ModelAndView(model, Path.layout);
        }, new VelocityTemplateEngine());


    }

    private static void addTemporaryUsersToUserDao(int usersAmount) {
        for (int i = 0; i < usersAmount; i++) {
            userDAO.addNewUser("test"+i, "test");
        }
        logger.info("Added " + usersAmount + " temporary users");
    }


    private static boolean checkCredentials(String login, String password) {
        Optional<User> userByLogin = userDAO.getUserByLogin(login);
        Boolean result;
        result = userByLogin.isPresent() && userByLogin.get().getPassword().equals(password);
        return result;
    }
}
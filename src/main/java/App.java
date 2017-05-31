import DAO.UserDAO;
import DAO.UserDAOArrayList;
import Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static spark.Spark.*;


public class App {

    private static UserDAO userDAO = new UserDAOArrayList();

    public static void main(String[] args) {
        staticFileLocation("/public");
        String layout = "templates/layout.vtl";
        Logger logger = LoggerFactory.getLogger(App.class);

        get("/", (request, response) -> {
            Map<String,String> model = new HashMap<>();
            model.put("template", "templates/hello.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());


        get("/login", (request, response) -> {
            logger.info("login page " + request.cookies());
            HashMap<String,String> model = new HashMap<>();
            model.put("template", "templates/login.vtl");
            model.put("error", "");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get( "/register", (request, response) -> {
            logger.info("rejestracja");
            HashMap<String,String> model = new HashMap<>();
            model.put("template", "templates/registration.vtl");
            return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

        get("/checkRegistration", (request, response) -> {
            logger.info("sprawdzam rejestracje");
            String login = request.queryParams("login");
            String password = request.queryParams("password");

            Optional<User> userOptional = userDAO.addNewUser(login, password);
            if (userOptional.isPresent()){
                logger.info("dodano nowego urzytkownika");
                response.redirect("/login");
            }else{
                logger.info("blad w dodawaniu nowego uzykownika");
                response.redirect("/register");
            }
            return null;
        });


        get("/main", (request, response) -> {
            HashMap<String,String> model = new HashMap<>();
            String login = request.queryParams("login");
            String password = request.queryParams("password");

            if (!checkCredentials(login, password)){
                logger.info("blad logowania");
                response.redirect("/login");
            }else {
                logger.info("zalogowano ");
                model.put("login", login);
                model.put("template", "templates/main.vtl");
                return new ModelAndView(model, "templates/layout.vtl");
            }
            return null;
        }, new VelocityTemplateEngine());
    }

    private static boolean checkCredentials(String login, String password) {
        Optional<User> userByLogin = userDAO.getUserByLogin(login);
        Boolean result;
        result = userByLogin.isPresent() && userByLogin.get().getPassword().equals(password);
        return result;
    }
}
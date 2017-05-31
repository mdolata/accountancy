package Model;

import com.sun.istack.internal.NotNull;
import lombok.Data;

/**
 * Created by mateu on 31.05.2017 , 21:51.
 * Simple model of user
 *
 *
 * *** not final
 */
@Data
public class User {
    final Integer id;
    @NotNull
    final String login;
    @NotNull
    final String password;

    Boolean active = true;

}

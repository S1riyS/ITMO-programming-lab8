package s1riys.lab8.common.network.responses;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;

public class LoginResponse extends Response {
    public final User user;

    public LoginResponse(User user, String error) {
        super(Commands.REGISTER, error);
        this.user = user;
    }
}

package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;

public class RegisterRequest extends Request {
    public RegisterRequest(User user) {
        super(Commands.REGISTER, user);
    }
}

package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;
import s1riys.lab8.common.network.requests.LoginRequest;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.LoginResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.AuthManager;

public class Login extends Command {
    private final AuthManager authManager;

    public Login(AuthManager authManager) {
        super(Commands.LOGIN);
        this.authManager = authManager;
    }

    @Override
    public Response execute(Request data) {
        LoginRequest request = (LoginRequest) data;
        User user = request.getUser();

        try {
            Long userId = authManager.login(user);

            if (userId < 0) return new LoginResponse(null, "Неверно указан логин или пароль");
            else return new LoginResponse(user.copy(userId), null);
        } catch (Exception e) {
            return new LoginResponse(null, e.toString());
        }
    }
}

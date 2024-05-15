package s1riys.lab8.server.commands;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;
import s1riys.lab8.common.network.requests.RegisterRequest;
import s1riys.lab8.common.network.requests.Request;
import s1riys.lab8.common.network.responses.RegisterResponse;
import s1riys.lab8.common.network.responses.Response;
import s1riys.lab8.server.managers.AuthManager;

public class Register extends Command {
    private final AuthManager authManager;

    public Register(AuthManager authManager) {
        super(Commands.REGISTER);
        this.authManager = authManager;
    }

    @Override
    public Response execute(Request data) {
        RegisterRequest request = (RegisterRequest) data;
        User user = request.getUser();

        try {
            Long newUserId = authManager.register(user);

            if (newUserId < 0) return new RegisterResponse(null, "Не удалось создать пользователя");
            else return new RegisterResponse(user.copy(newUserId), null);

        } catch (Exception e) {
            return new RegisterResponse(null, e.toString());
        }
    }
}

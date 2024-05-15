package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;

public class ClearRequest extends Request {
    public ClearRequest(User user) {
        super(Commands.CLEAR, user);
    }
}

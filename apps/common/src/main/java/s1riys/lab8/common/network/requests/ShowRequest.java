package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;

public class ShowRequest extends Request {
    public ShowRequest(User user) {
        super(Commands.SHOW, user);
    }
}

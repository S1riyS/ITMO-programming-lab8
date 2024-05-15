package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;

public class MaxByCreationDateRequest extends Request {
    public MaxByCreationDateRequest(User user) {
        super(Commands.MAX_BY_CREATION_DATE, user);
    }
}

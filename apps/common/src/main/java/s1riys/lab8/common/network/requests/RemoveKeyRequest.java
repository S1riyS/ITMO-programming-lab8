package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.User;

public class RemoveKeyRequest extends Request {
    public final Long id;
    public RemoveKeyRequest(Long id, User user) {
        super(Commands.REMOVE_KEY, user);
        this.id = id;
    }
}

package s1riys.lab8.common.network.responses;

import s1riys.lab8.common.constants.Commands;

public class AddResponse extends Response {
    public final Long newId;

    public AddResponse(Long newId, String error) {
        super(Commands.INSERT, error);
        this.newId = newId;
    }
}

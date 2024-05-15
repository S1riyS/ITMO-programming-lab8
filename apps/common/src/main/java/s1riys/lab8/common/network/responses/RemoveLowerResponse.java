package s1riys.lab8.common.network.responses;

import s1riys.lab8.common.constants.Commands;

public class RemoveLowerResponse extends Response {
    public final int count;

    public RemoveLowerResponse(int count, String error) {
        super(Commands.REMOVE_LOWER, error);
        this.count = count;
    }
}

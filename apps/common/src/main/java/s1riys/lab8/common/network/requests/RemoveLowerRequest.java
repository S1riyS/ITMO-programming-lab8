package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.models.User;

public class RemoveLowerRequest extends Request {
    public final Product product;
    public RemoveLowerRequest(Product product, User user) {
        super(Commands.REMOVE_LOWER, user);
        this.product = product;
    }
}

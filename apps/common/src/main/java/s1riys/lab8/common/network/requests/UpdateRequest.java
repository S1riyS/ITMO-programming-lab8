package s1riys.lab8.common.network.requests;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.Product;
import s1riys.lab8.common.models.User;

public class UpdateRequest extends Request {
    public final Long id;
    public final Product product;
    public UpdateRequest(Long id, Product product, User user) {
        super(Commands.UPDATE, user);
        this.id = id;
        this.product = product;
    }
}

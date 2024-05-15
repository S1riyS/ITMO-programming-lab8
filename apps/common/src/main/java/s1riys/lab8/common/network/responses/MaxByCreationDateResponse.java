package s1riys.lab8.common.network.responses;

import s1riys.lab8.common.constants.Commands;
import s1riys.lab8.common.models.Product;

public class MaxByCreationDateResponse extends Response {
    public final Product product;

    public MaxByCreationDateResponse(Product product, String error) {
        super(Commands.MAX_BY_CREATION_DATE, error);
        this.product = product;
    }
}

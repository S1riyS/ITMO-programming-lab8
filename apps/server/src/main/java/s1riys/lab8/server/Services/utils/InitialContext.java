package s1riys.lab8.server.Services.utils;

import s1riys.lab8.server.Services.OrganizationService;
import s1riys.lab8.server.Services.ProductService;
import s1riys.lab8.server.Services.UserService;

class InitialContext {
    public Object lookup(String name)
    {
        if (name.equalsIgnoreCase("ProductService")) {
            return new ProductService();
        }
        else if (name.equalsIgnoreCase("OrganizationService")) {
            return new OrganizationService();
        }
        else if (name.equalsIgnoreCase("UserService")) {
            return new UserService();
        }
        return null;
    }
}

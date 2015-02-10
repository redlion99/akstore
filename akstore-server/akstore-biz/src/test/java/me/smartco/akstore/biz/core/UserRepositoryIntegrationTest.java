package me.smartco.akstore.biz.core;

import me.smartco.akstore.store.service.MallService;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.biz.AbstractIntegrationTest;
import me.smartco.akstore.user.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by libin on 14-11-10.
 */
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {


    @Autowired
    private MallService mallManager;

    @Autowired
    private UserService userService;

    @Autowired
    public void findUserByName() {
        try {
            userService.register("a","b","Customer",mallManager.getValidateCode("a"));
        } catch (CodeValidateFailedException e) {
            e.printStackTrace();
        }

    }



    @Test
    public void listAll() {

    }
}

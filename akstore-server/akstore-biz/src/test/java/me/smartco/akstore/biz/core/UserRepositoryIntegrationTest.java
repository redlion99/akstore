package me.smartco.akstore.biz.core;

import me.smartco.akstore.biz.service.MallService;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.biz.AbstractIntegrationTest;
import me.smartco.akstore.store.mongodb.core.user.User;
import me.smartco.akstore.store.mongodb.core.user.UserRepository;
import me.smartco.akstore.common.util.MD5Util;
import junit.framework.Assert;
import me.smartco.akstore.user.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by libin on 14-11-10.
 */
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private UserRepository userRepository;


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
        User u=userRepository.findByUsername("a");
        Assert.assertNotNull(u);
        u=userRepository.findByUsernameAndPasswordAndActive("a", MD5Util.MD5("b"), true);
        Assert.assertNotNull(u);
        userRepository.delete(u);
    }



    @Test
    public void listAll() {
        List<User> list=userRepository.findAll();
        Assert.assertTrue(list.size()>=0);
    }
}

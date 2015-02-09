package me.smartco.akstore.integration;

import me.smartco.akstore.biz.service.MallService;
import me.smartco.akstore.biz.service.PartnerService;
import me.smartco.akstore.biz.service.TransactionService;
import me.smartco.akstore.common.model.EmailAddress;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.store.mongodb.mall.Customer;
import me.smartco.akstore.user.model.User;
import me.smartco.akstore.user.service.UserService;

/**
 * Created by libin on 15-2-9.
 */

public class CompositeService {
    private UserService userService;
    private MallService mallService;
    private PartnerService partnerService;
    private TransactionService transactionService;

    public CompositeService(UserService userService, MallService mallService, PartnerService partnerService, TransactionService transactionService) {
        this.userService = userService;
        this.mallService = mallService;
        this.partnerService = partnerService;
        this.transactionService = transactionService;
    }

    public Customer register(String username,String password,String email,String mobile,String validateCode) throws CodeValidateFailedException {
        validateCode=userService.getValidateCode(username);
        User user=userService.register(username, password, "Customer", validateCode);
        if(null!=user){
            Customer customer =mallService.getCustomerByUserID(user.getId());
            if(null==customer) {
                customer = new Customer(user.getId(),user.getUsername());
                customer.contact().setPhone(mobile);
                if (null != email) {
                    customer.contact().setEmail(new EmailAddress(email));
                }
                mallService.getCustomerRepository().save(customer);
            }
            userService.getAvailableToken(user);
            return customer;
        }else {
            return null;
        }
    }


    public User registerStaff(String username, String password, String validateCode) throws CodeValidateFailedException {
        return userService.register(username, password, "Staff", validateCode);
    }
}

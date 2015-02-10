package me.smartco.akstore.biz.core;

import me.smartco.akstore.store.service.MallService;
import me.smartco.akstore.store.service.PartnerService;
import me.smartco.akstore.exception.CodeValidateFailedException;
import me.smartco.akstore.biz.AbstractIntegrationTest;
import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.PartnerStaff;
import me.smartco.akstore.user.model.User;
import me.smartco.akstore.user.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 * Created by libin on 14-11-13.
 */
public class PartnerManagerIntegratonTest extends AbstractIntegrationTest {

    @Autowired
    private PartnerService partnerManager;
    @Autowired
    private MallService mallManager;

    @Autowired
    private UserService userService;

    @Test
    public void empty(){

    }

    @Test
    public void testCreatePartnerFormUser(){
        User user = null;
        try {
            user = userService.register("a1","partner","PartnerStaff",mallManager.getValidateCode("a1"));
        } catch (CodeValidateFailedException e) {
            e.printStackTrace();
        }
        PartnerStaff staff=partnerManager.createPartnerFromUser(user.getId(),user.getUsername(), "a1", "13598769876");
        System.out.println(staff.getPartner().getId());
        partnerManager.createShop4Partner(staff.getPartner(),"a1");

        try {
            user =userService.register("a2","partner","PartnerStaff",mallManager.getValidateCode("a2"));
        } catch (CodeValidateFailedException e) {
            e.printStackTrace();
        }
        staff=partnerManager.createPartnerFromUser(user.getId(),user.getUsername(), "a2", "13598769876");
        System.out.println(staff.getPartner().getId());
        partnerManager.createShop4Partner(staff.getPartner(),"a2");
    }

    @Test
    public void testSearchProduct(){
        Partner p=partnerManager.getPartnerRepository().findOne("54671dcb3004194fb9472058");
        partnerManager.getProductRepository().findByPartnerAndNameLike(p,"/a/",new PageRequest(0,50));

    }
}

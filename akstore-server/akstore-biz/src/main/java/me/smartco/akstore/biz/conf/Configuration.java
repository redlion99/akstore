package me.smartco.akstore.biz.conf;

import me.smartco.akstore.biz.service.PartnerService;
import me.smartco.akstore.common.model.Account;
import me.smartco.akstore.store.mongodb.core.AccountRepository;
import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.PartnerStaff;
import me.smartco.akstore.common.model.Staff;
import me.smartco.akstore.store.mongodb.partner.Shop;
import me.smartco.akstore.user.model.User;
import me.smartco.akstore.user.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

/**
 * Created by libin on 14-11-15.
 */
@Component
public class Configuration implements  InitializingBean {

    Logger logger= LoggerFactory.getLogger(Configuration.class);

    private Account primaryAccount;

    private Account promotionAccount;

    private Account selfRunAccount;

    private Account shippingFeeAccount;

    private Shop primaryShop;

    private Staff systemStaff;

    @Autowired
    private PartnerService partnerManager;

    @Autowired
    private UserService userService;


    public Account getPrimaryAccount() {
        return primaryAccount;
    }

    public Account getPromotionAccount() {
        return promotionAccount;
    }

    public Partner getPrimaryPartner() {
        return primaryShop.partner();
    }

    public Shop getPrimaryShop() {
        return primaryShop;
    }

    public Staff getSystemStaff() {
        return systemStaff;
    }


    public Account getShippingFeeAccount() {
        return shippingFeeAccount;
    }

    public Account getSelfRunAccount() {
        return selfRunAccount;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        AccountRepository accountRepository=partnerManager.getAccountRepository();
        primaryAccount=accountRepository.findOneByType(Account.AccountType.primary);
        if(null==primaryAccount){
            primaryAccount=new Account("主账户", Account.AccountType.primary);
            accountRepository.save(primaryAccount);
        }
        logger.info("初始化:========="+primaryAccount.getName()+"==========");
        promotionAccount=accountRepository.findOneByType(Account.AccountType.promotion);
        if(null==promotionAccount){
            promotionAccount=new Account("促销账户", Account.AccountType.promotion);
            accountRepository.save(promotionAccount);
        }
        logger.info("初始化:========="+promotionAccount.getName()+"==========");
        selfRunAccount=accountRepository.findOneByType(Account.AccountType.selfRun);
        if(null==selfRunAccount){
            selfRunAccount=new Account("自营账户", Account.AccountType.selfRun);
            accountRepository.save(selfRunAccount);
        }
        logger.info("初始化:========="+selfRunAccount.getName()+"==========");

        shippingFeeAccount=accountRepository.findOneByType(Account.AccountType.shippingFee);
        if(null==shippingFeeAccount){
            shippingFeeAccount=new Account("运费账户", Account.AccountType.shippingFee);
            accountRepository.save(shippingFeeAccount);
        }
        logger.info("初始化:========="+shippingFeeAccount.getName()+"==========");

        systemStaff=partnerManager.getStaffRepository().findOneByAccount(primaryAccount);
        if(null==systemStaff){
            User user =userService.register("system","CFT^YJM","Staff",partnerManager.getValidateCode("system"));
            systemStaff=new Staff(user.getUsername(),user.getId(),primaryAccount);
            partnerManager.getStaffRepository().save(systemStaff);
        }
        logger.info("初始化:========="+systemStaff.getLoginname()+"==========");
        primaryShop=partnerManager.getShopRepository().findOneByAccount(selfRunAccount);
        if(null==primaryShop){
            PartnerStaff staff=partnerManager.createPartnerFromAccount(selfRunAccount,systemStaff.getId(),systemStaff.getLoginname(),"自营商户","13598769876");
            primaryShop=partnerManager.createShop4Partner(staff.getPartner(),"自营商户");
            primaryShop.setMaxServeDistance(999999999);
            partnerManager.getShopRepository().save(primaryShop);
        }
        logger.info("初始化:========="+primaryShop.getName()+"==========");
    }
}

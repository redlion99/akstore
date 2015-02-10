package me.smartco.akstore.store.service;

import me.smartco.akstore.common.model.Account;
import me.smartco.akstore.store.mongodb.core.AccountRepository;
import me.smartco.akstore.store.mongodb.mall.*;
import me.smartco.akstore.store.mongodb.market.DispatchOrderRepository;
import me.smartco.akstore.store.mongodb.market.DispatchProductRepository;
import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.PartnerStaff;
import me.smartco.akstore.store.mongodb.partner.Shop;
import me.smartco.akstore.store.mongodb.partner.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by libin on 14-11-12.
 */
@Component
public class PartnerService extends AbstractService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;





    @Autowired
    private DispatchOrderRepository dispatchOrderRepository;

    @Autowired
    private DispatchProductRepository dispatchProductRepository;


    @Autowired
    private AdvertisementRepository advertisementRepository;

    public PartnerStaff createPartnerFromUser(String userId,String username, String name, String mobile){
        Account account = new Account(name, Account.AccountType.partner);
        accountRepository.save(account);
        return createPartnerFromAccount(account,userId,username,name,mobile);
    }

    public PartnerStaff createPartnerFromAccount(Account account,String userId,String username,String name,String mobile){
        Partner partner = new Partner(account,name);
        partnerRepository.save(partner);
        PartnerStaff partnerStaff = new PartnerStaff(username,userId,partner);
        partnerStaff.setMobile(mobile);
        return partnerStaffRepository.save(partnerStaff);
    }

    public Shop createShop4Partner(Partner partner,String name){
        Shop shop=new Shop(name,partner);
        return shopRepository.save(shop);
    }




    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public ShopRepository getShopRepository() {
        return shopRepository;
    }

    public void setShopRepository(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }





    public DispatchOrderRepository getDispatchOrderRepository() {
        return dispatchOrderRepository;
    }

    public DispatchProductRepository getDispatchProductRepository() {
        return dispatchProductRepository;
    }

    public AdvertisementRepository getAdvertisementRepository() {
        return advertisementRepository;
    }

}



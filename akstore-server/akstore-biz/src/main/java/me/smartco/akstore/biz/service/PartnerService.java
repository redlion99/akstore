package me.smartco.akstore.biz.service;

import me.smartco.akstore.common.event.order.OrderEvent;
import me.smartco.akstore.common.model.Account;
import me.smartco.akstore.store.mongodb.core.AccountRepository;
import me.smartco.akstore.store.mongodb.market.DispatchProduct;
import me.smartco.akstore.transaction.model.*;
import me.smartco.akstore.store.mongodb.mall.*;
import me.smartco.akstore.store.mongodb.market.DispatchOrderRepository;
import me.smartco.akstore.store.mongodb.market.DispatchProductRepository;
import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.PartnerStaff;
import me.smartco.akstore.store.mongodb.partner.Shop;
import me.smartco.akstore.store.mongodb.partner.ShopRepository;

import me.smartco.akstore.transaction.model.LineItem;
import me.smartco.akstore.transaction.model.OrderHistory;
import me.smartco.akstore.transaction.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private BillDAO billRepository;


    @Autowired
    private BillFlowDAO billFlowRepository;



    @Autowired
    private DispatchOrderRepository dispatchOrderRepository;

    @Autowired
    private DispatchProductRepository dispatchProductRepository;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderHistoryDAO orderHistoryDAO;

    @Autowired
    private LineItemDAO lineItemDAO;

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

    public Page<OrderEntity> searchOrder(Shop shop,Date dateStart,Date dateEnd,Pageable page){
        if(null!=shop&&null!=dateStart&&null!=dateEnd){
            return orderDAO.findByShopIdAndCreateTimeBetween(shop.getId(), dateStart, dateEnd, page);
        } else if(null!=shop&&null!=dateStart){
            return orderDAO.findByShopIdAndCreateTimeGreaterThan(shop.getId(), dateStart, page);
        }else if(null!=shop&&null!=dateEnd){
            return orderDAO.findByShopIdAndCreateTimeLessThan(shop.getId(), dateEnd, page);
        }else if(null!=shop){
            return orderDAO.findByShopId(shop.getId(), page);
        }
        return new PageImpl<OrderEntity>(new ArrayList<OrderEntity>());
    }

    public OrderHistory createOrderHistory(OrderEntity orderEntity){
        OrderHistory orderHistory=new OrderHistory(orderEntity);
        sendTaskMessage(OrderEvent.create(orderEntity.getStatus(),orderEntity.getId(),null));
        return orderHistoryDAO.save(orderHistory);
    }

    public BillDAO getBillRepository() {
        return billRepository;
    }



    public BillFlowDAO getBillFlowRepository() {
        return billFlowRepository;
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

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public OrderHistoryDAO getOrderHistoryDAO() {
        return orderHistoryDAO;
    }

    public LineItemDAO getLineItemDAO() {
        return lineItemDAO;
    }

    public Map<DispatchProduct,Integer> elementOfOrder(OrderEntity orderEntity) {
        Map<DispatchProduct,Integer> elementSet=new HashMap<DispatchProduct, Integer>();
        for(LineItem lineItem:orderEntity.lineItems()){
            int amount=lineItem.getAmount();
            Product product=productRepository.findOne(lineItem.getProductId());
            for(ProductElement productElement:product.elementSet()){
                DispatchProduct dispatchProduct=productElement.getProduct();
                if(elementSet.containsKey(dispatchProduct)){
                    elementSet.put(dispatchProduct,elementSet.get(dispatchProduct)+productElement.getQuantity()*amount);
                }else {
                    elementSet.put(dispatchProduct,productElement.getQuantity()*amount);
                }
                //elementSet.add(new ProductElement(productElement.product(),productElement.getQuantity()*amount));
            }
        }
        return elementSet;
    }
}



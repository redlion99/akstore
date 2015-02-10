package me.smartco.akstore.store.service;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import me.smartco.akstore.common.actor.ActorSystemFactory;
import me.smartco.akstore.store.mongodb.core.AttachmentsRepository;
import me.smartco.akstore.common.model.Staff;
import me.smartco.akstore.store.mongodb.mall.Customer;
import me.smartco.akstore.store.mongodb.mall.CustomerRepository;
import me.smartco.akstore.store.mongodb.partner.PartnerRepository;
import me.smartco.akstore.store.mongodb.partner.PartnerStaff;
import me.smartco.akstore.store.mongodb.partner.PartnerStaffRepository;
import me.smartco.akstore.common.util.MD5Util;
import me.smartco.akstore.store.mongodb.core.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 * Created by libin on 14-11-12.
 */
public abstract class AbstractService {


    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected StaffRepository staffRepository;

    @Autowired
    protected PartnerRepository partnerRepository;

    @Autowired
    protected PartnerStaffRepository partnerStaffRepository;

    @Autowired
    private AttachmentsRepository attachmentsRepository;


    @Autowired
    private MongoTemplate mongoTemplate;


    public Customer getCustomerByUserID(String userId){
        return customerRepository.findOne(userId);
    }

    public Staff getStaffByUserId(String userId){
        return staffRepository.findOne(userId);
    }

    public PartnerStaff getPartnerStaffByUserId(String userId){
        return partnerStaffRepository.findOne(userId);
    }



    public String getValidateCode(String username){
        return MD5Util.getValidateCode(username+new Date().getMonth()+"bigwin");
    }

    public void sendTaskMessage(Object message){
        ActorSelection taskActor=ActorSystemFactory.getActorSystem().actorSelection("/user/task");
        taskActor.tell(message, ActorRef.noSender());
    }



    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }

    public StaffRepository getStaffRepository() {
        return staffRepository;
    }

    public PartnerRepository getPartnerRepository() {
        return partnerRepository;
    }

    public PartnerStaffRepository getPartnerStaffRepository() {
        return partnerStaffRepository;
    }

    public AttachmentsRepository getAttachmentsRepository() {
        return attachmentsRepository;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}

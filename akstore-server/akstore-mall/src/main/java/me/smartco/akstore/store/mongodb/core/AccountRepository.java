package me.smartco.akstore.store.mongodb.core;

import me.smartco.akstore.common.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 14-11-13.
 */
public interface AccountRepository extends MongoRepository<Account,String> {

    Account findOneByType(Account.AccountType type);
}

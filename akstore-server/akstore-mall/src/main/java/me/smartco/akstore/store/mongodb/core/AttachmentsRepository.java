package me.smartco.akstore.store.mongodb.core;

import me.smartco.akstore.common.model.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 14-11-12.
 */
public interface AttachmentsRepository extends MongoRepository<Attachment,String> {
}

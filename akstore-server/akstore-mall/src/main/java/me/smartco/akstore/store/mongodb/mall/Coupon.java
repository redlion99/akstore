package me.smartco.akstore.store.mongodb.mall;

import me.smartco.akstore.common.model.AbstractDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Created by libin on 14-11-27.
 */
@Document
public class Coupon extends AbstractDocument {
    private BigDecimal worth;

}

package me.smartco.akstore.biz.product;

import me.smartco.akstore.biz.AbstractIntegrationTest;

import java.util.List;

import me.smartco.akstore.store.mongodb.mall.Product;
import me.smartco.akstore.store.mongodb.mall.ProductRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

/**
 * Created by libin on 14-11-7.
 */
public class ProductRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    ProductRepository repository;

    @Test
    public void createProduct() {
        //Product product = new Product("Camera bag", new BigDecimal(49.99));
        //product = repository.save(product);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void lookupProductsByDescription() {

        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "name");
        Page<Product> page = repository.findByDescriptionContaining("Apple", pageable);

//        assertThat(page.getContent(), hasSize(1));
//        assertThat(page, Matchers.<Product> hasItems(named("iPad")));
//        assertThat(page.isFirstPage(), is(true));
//        assertThat(page.isLastPage(), is(false));
//        assertThat(page.hasNextPage(), is(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findsProductsByAttributes() {

        List<Product> products = repository.findByAttributes("attributes.connector", "plug");

//        assertThat(products, Matchers.<Product> hasItems(named("Dock")));
    }

}

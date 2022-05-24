package lt.vu.persistence;

import lt.vu.entities.Product;
import lt.vu.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class ProductsDAO {

    @Inject
    private EntityManager em;

    public List<Product> loadAll() {
        return em.createNamedQuery("Product.findAll", Product.class).getResultList();
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void persist(Product product){
        this.em.persist(product);
    }

    public Product findOne(Integer id) {
        return em.find(Product.class, id);
    }
}

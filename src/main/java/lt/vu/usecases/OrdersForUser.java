package lt.vu.usecases;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.User;
import lt.vu.entities.Product;
import lt.vu.entities.UserOrder;
import lt.vu.persistence.OrdersDAO;
import lt.vu.persistence.ProductsDAO;
import lt.vu.persistence.UsersDAO;

@Model
public class OrdersForUser implements Serializable {

    @Inject
    private UsersDAO usersDAO;

    @Inject
    private ProductsDAO productsDAO;

    @Inject
    private OrdersDAO ordersDAO;

    @Getter @Setter
    private User user;

    @Getter @Setter
    private UserOrder orderToCreate = new UserOrder();

    @Getter @Setter
    private List<Integer> selectedProducts = new ArrayList<>();

    @PostConstruct
    public void init() {
        Map<String, String> requestParameters =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Integer userId = Integer.parseInt(requestParameters.get("userId"));
        this.user = usersDAO.findOne(userId);
    }
    @Transactional
    public void createOrder() {
        List<Product> products = selectedProducts.stream().map(i -> productsDAO.findOne(i)).collect(Collectors.toList());
        Double price = pricesAmount(products);
        orderToCreate.setPrice(price);
        orderToCreate.setProducts(products);
        orderToCreate.setUser(user);
        orderToCreate.setOrderedAt(new Date());
        ordersDAO.persist(orderToCreate);
    }

    private Double pricesAmount(List<Product> products) {
        List<Double> pricesList = products.stream().map(i -> i.getPrice()).collect(Collectors.toList());
        Double sum = 0.0;

        for (Double i : pricesList)
            sum = sum + i;

        return sum;
    }
}

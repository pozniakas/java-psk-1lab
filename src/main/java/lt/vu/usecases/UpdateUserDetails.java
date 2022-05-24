package lt.vu.usecases;


import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.User;
import lt.vu.persistence.UsersDAO;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Map;

@ViewScoped
@Named
@Getter @Setter
public class UpdateUserDetails implements Serializable {

    private User user;

    @Inject
    private UsersDAO usersDAO;

    @PostConstruct
    private void init() {
        Map<String, String> requestParameters =
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Integer userId = Integer.parseInt(requestParameters.get("userId"));
        this.user = usersDAO.findOne(userId);
    }

    @Transactional
    public String updateUserEmail() {
        try {
            usersDAO.update(this.user);
        } catch (OptimisticLockException e) {
                return "/orders.xhtml?faces-redirect=true&userId=" + this.user.getId();
        }
        return "orders.xhtml?userId=" + this.user.getId() + "&faces-redirect=true";
    }
}
package lt.vu.usecases;


import lombok.Getter;
import lombok.Setter;
import lt.vu.entities.User;
import lt.vu.persistence.UsersDAO;
import lt.vu.services.interfaces.IGenerateName;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SessionScoped
@Named
@Getter @Setter
public class UpdateUserDetails implements Serializable {
    @Inject
    IGenerateName nameGenerator;

    private CompletableFuture<String> taskToGenerateName = null;

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
                return "/orders.xhtml?faces-redirect=true&userId=" + this.user.getId()+ "&error=optimistic-lock-exception";
        }
        return "orders.xhtml?userId=" + this.user.getId() + "&faces-redirect=true";
    }

    @Transactional
    public String generateUserName() {
        taskToGenerateName = CompletableFuture.supplyAsync(() -> nameGenerator.generateName());
        return "orders.xhtml?userId=" + this.user.getId() + "&faces-redirect=true";
    }

    public String getGeneratedName() throws ExecutionException, InterruptedException {
        if (taskToGenerateName == null)
        {
            return "Not generating";
        }
        if (taskToGenerateName.isDone()) {
            return taskToGenerateName.get();
        }

        return "Generating...";
    }
}
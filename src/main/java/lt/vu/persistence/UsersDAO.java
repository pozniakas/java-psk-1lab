package lt.vu.persistence;

import lt.vu.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@ApplicationScoped
public class UsersDAO {

    @Inject
    private EntityManager em;

    public List<User> loadAll() {
        return em.createNamedQuery("User.findAll", User.class).getResultList();
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void persist(User user){
        this.em.persist(user);
    }

    public User update(User user){
        return em.merge(user);
    }

    public User findOne(Integer id) {
        return em.find(User.class, id);
    }

    public User findOneByName(String name) {
        try {
            User user = (User) em.createQuery("SELECT u FROM User u where u.name = :value1")
                    .setParameter("value1", name).getSingleResult();
            return user;
        }
        catch (NoResultException nre) {
            return null;
        }
    }
}

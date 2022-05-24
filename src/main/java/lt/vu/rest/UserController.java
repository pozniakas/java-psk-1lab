package lt.vu.rest;

import lombok.*;
import lt.vu.entities.User;
import lt.vu.persistence.UsersDAO;
import lt.vu.rest.contracts.UpdateUserDto;
import lt.vu.rest.contracts.UserDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/user")
public class UserController {

    @Inject
    @Setter @Getter
    private UsersDAO usersDAO;

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final Integer id) {
        User user = usersDAO.findOne(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return Response.ok(userDto).build();
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(
            UserDto userData) {
        User existingUser = usersDAO.findOneByName(userData.getName());
        if (existingUser != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        User user = new User();
        user.setName(userData.getName());
        user.setEmail(userData.getEmail());
        usersDAO.persist(user);

        return Response.ok().build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(
            @PathParam("id") final Integer userId,
            UpdateUserDto updateUserData) {
        try {
            User user = usersDAO.findOne(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            user.setEmail(updateUserData.getEmail());
            usersDAO.update(user);
            return Response.ok().build();
        } catch (OptimisticLockException ole) {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}

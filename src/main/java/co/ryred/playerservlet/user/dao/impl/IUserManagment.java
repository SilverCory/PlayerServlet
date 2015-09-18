package co.ryred.playerservlet.user.dao.impl;

import co.ryred.playerservlet.user.User;

import java.util.List;
import java.util.UUID;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 18/09/2015.
 */
public interface IUserManagment
{

	void insertUser( User user );

	User getUser( UUID uuid );

	User getUser( String username );

	List<User> getUsers();

}

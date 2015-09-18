package co.ryred.playerservlet.user.dao;

import co.ryred.playerservlet.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 18/09/2015.
 */
public class UserBean
{

	@Autowired
	private UserManagment userManager;

	@Transactional
	public void insertUser( User user )
	{
		userManager.insertUser( user );
	}

	@Transactional
	public User getUser( UUID uuid )
	{
		return userManager.getUser( uuid );
	}

	@Transactional
	public User getUser( String username )
	{
		return userManager.getUser( username );
	}

	@Transactional
	public List<User> getUsers()
	{
		return userManager.getUsers();
	}

}

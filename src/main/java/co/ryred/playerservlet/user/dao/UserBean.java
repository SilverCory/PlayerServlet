package co.ryred.playerservlet.user.dao;

import co.ryred.playerservlet.user.User;
import co.ryred.playerservlet.user.dao.impl.IUserBean;
import co.ryred.playerservlet.user.dao.impl.IUserManagment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 18/09/2015.
 */
@Transactional
@Component
public class UserBean implements IUserBean
{

	@Autowired
	private IUserManagment userManager;

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

	@Override
	public int getTotalUsers()
	{
		return userManager.getTotalUsers();
	}

	@Override
	public boolean exists( User user )
	{
		return userManager.exists( user );
	}

}

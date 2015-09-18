package co.ryred.playerservlet;

import co.ryred.playerservlet.user.User;
import co.ryred.playerservlet.user.dao.impl.IUserBean;
import com.google.gson.Gson;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 15/09/2015.
 */
@SuppressWarnings("unchecked")
public class PlayerServlet extends HttpServlet
{

	private static final Gson full_gson = new Gson();
	private final BeanFactory context;

	public PlayerServlet() throws Exception
	{

		PlayerServletConfig.init();

		/*Configuration cfg = new Configuration();
		cfg.addAnnotatedClass( User.class );
		cfg.configure( PlayerServletConfig.configFile );
		this.sessionFactory = cfg.buildSessionFactory();*/

		this.context = new FileSystemXmlApplicationContext( PlayerServletConfig.configFile.getPath() );

	}

	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{

		response.setContentType( "application/json" );


		int processed = 0;
		int committed = 0;

		try {

			IUserBean ub = (IUserBean) this.context.getBean( "userBean" );
			Map<String, String> parameters = request.getParameterMap();

			for ( Map.Entry<String, String> entry : parameters.entrySet() ) {

				if ( processed < 100 && entry.getValue() == null && entry.getKey() != null && entry.getKey().matches( "[a-zA-Z0-9_]{1,16}" ) ) {
					processed++;

					try {
						ub.insertUser( new User( entry.getKey() ) );
						committed++;
					} catch ( Exception e ) {
					}

					try {
						if ( !entry.getKey().toLowerCase().equals( entry.getKey() ) ) {
							ub.insertUser( new User( entry.getKey().toLowerCase() ) );
							committed++;
						}
					} catch ( Exception e ) {
					}

					try {
						if ( !entry.getKey().toUpperCase().equals( entry.getKey() ) ) {
							ub.insertUser( new User( entry.getKey().toUpperCase() ) );
							committed++;
						}
					} catch ( Exception e ) {
					}

				}

				if ( processed >= 100 ) break;

			}
		} catch ( Exception e ) {

			response.setStatus( 400 );
			response.getOutputStream().print( "{\"error\": \"" + e.getMessage() + "\" }" );
			return;

		}

		response.getOutputStream().print( "{\"success\": \"Done.\", \"totalCommitted\": " + committed + ", \"totalProcessed\": " + processed + " }" );

	}

	@Override
	protected void doTrace( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
	{
		resp.sendRedirect( "https://ryred.co/" );
	}

	@Override
	protected void doDelete( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
	{
		resp.sendRedirect( "https://ryred.co/" );
	}

	@Override
	protected void doOptions( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException
	{
		resp.sendRedirect( "https://ryred.co/" );
	}

	protected void doGet( HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException
	{

		response.setContentType( "application/json" );

		String[] requestArr = request.getRequestURI().substring( 1 ).replace( "/", "\u5584" ).split( "\u5584" );
		String userName = null;

		if ( requestArr.length >= 1 ) { userName = requestArr[ 0 ]; }

		if ( userName == null || userName.equals( "" ) ) {

			int rows = -1;

			try {
				IUserBean ub = (IUserBean) this.context.getBean( "userBean" );
				rows = ub.getTotalUsers();
			} catch ( Exception e ) {}

			response.getOutputStream().print( "{\"totalRows\": " + rows + " }" );
			return;
		}

		if ( !userName.matches( "[a-zA-Z0-9_]{1,16}" ) ) {
			response.setStatus( 400 );
			response.getOutputStream().print( "{\"error\": \"Not a valid Minecraft username.\" }" );
			return;
		}

		int committed = 0;
		try {

			IUserBean ub = (IUserBean) this.context.getBean( "userBean" );

			try {
				ub.insertUser( new User( userName ) );
				committed++;
			} catch ( Exception e ) {
				e.printStackTrace();
			}

			try {
				if ( !userName.toLowerCase().equals( userName ) ) {
					ub.insertUser( new User( userName.toLowerCase() ) );
					committed++;
				}
			} catch ( Exception e ) {
			}

			try {
				if ( !userName.toUpperCase().equals( userName ) ) {
					ub.insertUser( new User( userName.toUpperCase() ) );
					committed++;
				}
			} catch ( Exception e ) {
			}

		} catch ( Exception e ) {

			response.setStatus( 400 );
			response.getOutputStream().print( "{\"error\": \"" + e.getMessage() + "\" }" );
			return;

		}

		response.getOutputStream().print( "{\"success\": \"Done.\", \"totalCommitted\": " + committed + " }" );


	}

}

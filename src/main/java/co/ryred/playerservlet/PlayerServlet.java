package co.ryred.playerservlet;

import co.ryred.playerservlet.user.User;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

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
	private final SessionFactory sessionFactory;

	public PlayerServlet() throws Exception
	{

		PlayerServletConfig.init();

		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass( User.class );
		cfg.configure( PlayerServletConfig.configFile );
		this.sessionFactory = cfg.buildSessionFactory();

	}

	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{

		response.setContentType( "application/json" );


		int processed = 0;
		int committed = 0;
		Transaction tx = null;
		try {
			Session session = sessionFactory.openSession();
			tx = session.beginTransaction();

			Map<String, String> parameters = request.getParameterMap();

			for ( Map.Entry<String, String> entry : parameters.entrySet() ) {

				if ( processed < 100 && entry.getValue() == null && entry.getKey() != null && entry.getKey().matches( "[a-zA-Z0-9_]{1,16}" ) ) {
					processed++;

					try {
						session.save( new User( entry.getKey() ) );
						tx.commit();
						committed++;
					} catch ( Exception e ) {
						try {
							if ( tx.getStatus().canRollback() ) tx.rollback();
						} catch ( Exception ex ) {
							e.printStackTrace();
							ex.printStackTrace();
						}
					}

					try {
						if ( !entry.getKey().toLowerCase().equals( entry.getKey() ) ) {
							session.save( new User( entry.getKey().toLowerCase() ) );
							tx.commit();
							committed++;
						}
					} catch ( Exception e ) {
						try {
							if ( tx.getStatus().canRollback() ) tx.rollback();
						} catch ( Exception ex ) {
							e.printStackTrace();
							ex.printStackTrace();
						}
					}

					try {
						if ( !entry.getKey().toUpperCase().equals( entry.getKey() ) ) {
							session.save( new User( entry.getKey().toUpperCase() ) );
							tx.commit();
							committed++;
						}
					} catch ( Exception e ) {
						try {
							if ( tx.getStatus().canRollback() ) tx.rollback();
						} catch ( Exception ex ) {
							e.printStackTrace();
							ex.printStackTrace();
						}
					}

				}

				session.close();

				if ( processed >= 100 ) break;

			}
		} catch ( Exception e ) {

			try {

				if ( tx != null && tx.getStatus().canRollback() ) {
					tx.rollback();
				}

			} catch ( Exception ex ) {
				e.printStackTrace();
				ex.printStackTrace();
			}

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

		if ( userName == null ) {
			response.setStatus( 400 );
			response.getOutputStream().print( "{\"error\": \"No username provided.\" }" );
			return;
		}

		if ( !userName.matches( "[a-zA-Z0-9_]{1,16}" ) ) {
			response.setStatus( 400 );
			response.getOutputStream().print( "{\"error\": \"Not a valid Minecraft username.\" }" );
			return;
		}

		int i = 0;
		Transaction tx = null;
		try {

			Session session = sessionFactory.openSession();
			tx = session.beginTransaction();

			try {
				session.save( new User( userName ) );
				tx.commit();
				i++;
			} catch ( Exception e ) {
				try {
					if ( tx.getStatus().canRollback() ) tx.rollback();
				} catch ( Exception ex ) {
					e.printStackTrace();
					ex.printStackTrace();
				}
			}

			try {
				if ( !userName.toLowerCase().equals( userName ) ) {
					session.save( new User( userName.toLowerCase() ) );
					tx.commit();
					i++;
				}
			} catch ( Exception e ) {
				try {
					if ( tx.getStatus().canRollback() ) tx.rollback();
				} catch ( Exception ex ) {
					e.printStackTrace();
					ex.printStackTrace();
				}
			}

			try {
				if ( !userName.toUpperCase().equals( userName ) ) {
					session.save( new User( userName.toUpperCase() ) );
					tx.commit();
					i++;
				}
			} catch ( Exception e ) {
				try {
					if ( tx.getStatus().canRollback() ) tx.rollback();
				} catch ( Exception ex ) {
					e.printStackTrace();
					ex.printStackTrace();
				}
			}

			session.close();

		} catch ( Exception e ) {

			try {

				if ( tx != null && tx.getStatus().canRollback() ) {
					tx.rollback();
				}

			} catch ( Exception ex ) {
				e.printStackTrace();
				ex.printStackTrace();
			}

			response.setStatus( 400 );
			response.getOutputStream().print( "{\"error\": \"" + e.getMessage() + "\" }" );
			return;

		}

		response.getOutputStream().print( "{\"success\": \"Done.\", \"totalCommitted\": " + i + " }" );


	}

}

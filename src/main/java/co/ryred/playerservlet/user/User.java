package co.ryred.playerservlet.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.io.Charsets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Cory Redmond
 *         Created by acech_000 on 15/09/2015.
 */
@Entity
@Table(name = "players")
@AllArgsConstructor
public class User
{

	@Column(unique = true, name = "username", nullable = false, updatable = false)
	@Getter
	private final String name;

	@Column(unique = true, name = "uuid", nullable = false, updatable = false)
	@Getter
	@Id
	private final String uuid;

	public User( String name )
	{
		this.name = name;
		this.uuid = java.util.UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + name ).getBytes( Charsets.UTF_8 ) ).toString();
	}

}

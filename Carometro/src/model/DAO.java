package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
	// Variaveis para setar o banco de dados
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://10.26.49.104:3306/carometro";
	private String user = "dba";
	private String password = "123@senac";

	// Objeto (JDBC) usado para conectar o banco

	private Connection con;

	/**
	 * cONEXÃO
	 * @return con
	 */
	public Connection conectar() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;
		} catch (Exception e) {
			System.out.println(e);
			return null;	
		}
	}

}

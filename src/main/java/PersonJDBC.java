import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PersonJDBC implements PersonDAO{
	
	private Connection connection;
	
	public PersonJDBC(String url, String user, String password) throws ClassNotFoundException, SQLException {
		//load driver communication of postgresql.
		Class.forName("org.postgresql.Driver");
		//open the connection
		this.connection = DriverManager.getConnection(url, user, password);
        Statement stmt = this.connection.createStatement();
        String sql = 
"CREATE TABLE IF NOT EXISTS person (id_person INTEGER AUTO_INCREMENT, name VARCHAR NOT NULL, identity VARCHAR, birthday VARCHAR, PRIMARY KEY id_person )";
        stmt.execute(sql);
	}

	public void addPerson(Person person) throws SQLException {
		//query of postgresql
		String sql = "insert into person(name, identity, birthday)"
				+ "values (?,?,?)";
		
//         String sql = 
// "insert into person(name, identity, birthday) values ('Chloe','ZAA21','10_10')";

		PreparedStatement ps = this.connection.prepareStatement(sql);
		
		
		// 1 = first '?' 
		ps.setString(1, person.getName());
		// 2 - second '?'
		ps.setString(2, person.getIdentity());
		// 3 = third '?'
		ps.setString(3, person.getBirthday());
		
        System.out.println(ps.toString());
		//use execute update when the database return nothing
		ps.execute();
//insert into person(name, identity, birthday)values ('Chloe','ZAA21','10/10/1980')

		
		ResultSet generatedKeys =  ps.getGeneratedKeys();
		if(generatedKeys.next()) {
			person.setId(generatedKeys.getInt(1));
		}
		
		
	}

	public void removePerson(Person person) throws SQLException {
		String sql = "delete from person where id_person = ?";
		PreparedStatement ps = this.connection.prepareStatement(sql);
		ps.setInt(1, person.getId());
		ps.execute();
		
		
	}

	public Person getPerson(String name) throws SQLException {
		//get all persons
		ArrayList<Person> array = getAllPersons();
		for (Person person : array) {
			if(person.getName().equals(name)) {
				return person;
			}
		}
		return null;
	}

	public ArrayList<Person> getAllPersons() throws SQLException {
		ArrayList<Person> array = new ArrayList<Person>();
		
		//get all persons
		//query of postgresql
		ResultSet result = this.connection.prepareStatement("select id_person as id_person, name as name, identity, birthday from person").executeQuery();
		while(result.next()) {
			//new Person
			Person person = new Person();
			//get column of name
            System.out.println("column name: " + result.getMetaData().getColumnName(1));
            System.out.println("column name: " + result.getMetaData().getColumnName(2));
            System.out.println("column name: " + result.getMetaData().getColumnName(3));
            System.out.println("column name: " + result.getMetaData().getColumnName(4));
			person.setName(result.getString("(dbinstance.person.name)"));
			person.setId(result.getInt("(dbinstance.person.id_person)"));
			person.setIdentity(result.getString("(dbinstance.person.identity)"));
			person.setBirthday(result.getString("(dbinstance.person.birthday)"));
			array.add(person);
		}
		result.close();
		return array;
		
	}
}

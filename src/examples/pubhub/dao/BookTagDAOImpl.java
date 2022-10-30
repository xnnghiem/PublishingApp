package examples.pubhub.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import examples.pubhub.model.Book;
import examples.pubhub.model.BookTag;

import examples.pubhub.utilities.DAOUtilities;

public class BookTagDAOImpl implements BookTagDAO {
	
	Connection connection = null;	// Our connection to the database
	PreparedStatement stmt = null;	// We use prepared statements to help protect against SQL injection
	
	/*------------------------------------------------------------------------------------------------*/

	@Override
	public List<BookTag> getAllBookTags() {
		
		List<BookTag> book_tags = new ArrayList<>();

		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			String sql = "SELECT * FROM book_tags";			// Our SQL query
			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			ResultSet rs = stmt.executeQuery();			// Queries the database

			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				// We need to populate a Book object with info for each row from our query result
				BookTag book_tag = new BookTag();

				// Each variable in our Book object maps to a column in a row from our results.
				book_tag.setIsbn13(rs.getString("isbn_13"));
				book_tag.setTag(rs.getString("tag_name"));
				
				// Finally we add it to the list of Book objects returned by this query.
				book_tags.add(book_tag);
				
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		
		// return the list of Book objects populated by the DB.
		return book_tags;
	}

	@Override
	public List<BookTag> getBookTagsByISBN(String isbn13) {
		List<BookTag> book_tags = new ArrayList<>();
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "SELECT * FROM book_tags WHERE isbn_13 = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, isbn13);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				BookTag book_tag = new BookTag();
				
				book_tag.setIsbn13(rs.getString("isbn_13"));
				book_tag.setTag(rs.getString("tags"));
				
				book_tags.add(book_tag);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return book_tags;
	}
	
	public List<BookTag> getISBNByTag(String tag) {
		
		List<BookTag> book_tags = new ArrayList<>();
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "SELECT * FROM book_tags WHERE tag_name = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, tag);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				BookTag book_tag = new BookTag();
				
				book_tag.setIsbn13(rs.getString("isbn_13"));
				
				book_tags.add(book_tag);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return book_tags;
	}

	@Override
	public List<Book> getBooksByTag(String tag) {
		List<Book> books = new ArrayList<>();
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "SELECT * FROM books WHERE isbn_13 IN (SELECT isbn_13 FROM book_tags WHERE tag_name = ?)";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, tag);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Book book = new Book();
				
				book.setIsbn13(rs.getString("isbn_13"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setPublishDate(rs.getDate("publish_date").toLocalDate());
				book.setPrice(rs.getDouble("price"));

				books.add(book);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return books;
	}

	
	
	@Override
	public BookTag getBookTag(String isbn13, String tag) {
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "SELECT * FROM book_tags WHERE isbn_13 = ? AND tag_name = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, isbn13);
			stmt.setString(2, tag);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				BookTag book_tag = new BookTag();
				
				book_tag.setIsbn13(rs.getString("isbn_13"));
				book_tag.setTag(rs.getString("tag_name"));
				
				return book_tag;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		return null;
	}
	
	

	@Override
	public boolean addBookTag(BookTag Booktag) {
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "INSERT INTO book_tags VALUES (?, ?)";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, Booktag.getIsbn13());
			stmt.setString(2, Booktag.getTag());
			
			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}
	
	@Override
	public boolean updateBookTag(BookTag booktag) {
		try {
			connection = DAOUtilities.getConnection();
			String sql = "UPDATE book_tags SET tag_name=? WHERE isbn_13=?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, booktag.getTag());
			
			System.out.println(stmt);
			
			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
		
	}

	
	/*------------------------------------------------------------------------------------------------*/


	@Override
	public boolean deleteBookTag(BookTag Booktag) {
		try {
			connection = DAOUtilities.getConnection();
			String sql = "DELETE FROM book_tags WHERE isbn_13=? AND tag_name=?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, Booktag.getIsbn13());
			stmt.setString(2, Booktag.getTag());
			
			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
		
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}
	
	
	/*------------------------------------------------------------------------------------------------*/

	// Closing all resources is important, to prevent memory leaks. 
	// Ideally, you really want to close them in the reverse-order you open them
	private void closeResources() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			System.out.println("Could not close statement!");
			e.printStackTrace();
		}
		
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close connection!");
			e.printStackTrace();
		}
	}

}

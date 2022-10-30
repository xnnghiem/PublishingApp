package examples.pubhub.dao;

import java.util.List;

import examples.pubhub.model.BookTag;
import examples.pubhub.model.Book;

public interface BookTagDAO {

	public List<BookTag> getAllBookTags();
	public List<BookTag> getBookTagsByISBN(String isbn13);
	public List<BookTag> getISBNByTag(String tag);
	public List<Book> getBooksByTag(String tag);
	
	public BookTag getBookTag(String isbn13, String tag);

	public boolean addBookTag(BookTag booktag);
	public boolean updateBookTag(BookTag booktag);
	public boolean deleteBookTag(BookTag booktag);
	
}

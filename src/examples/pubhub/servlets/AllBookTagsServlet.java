package examples.pubhub.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import examples.pubhub.dao.BookDAO;
import examples.pubhub.model.Book;

import examples.pubhub.dao.BookTagDAO;
import examples.pubhub.model.BookTag;

import examples.pubhub.utilities.DAOUtilities;

/*
 * This servlet will take you to the homepage for the Book Publishing module (level 100)
 */
@WebServlet("/AllBookTags")

public class AllBookTagsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Grab the list of Books from the Database
		BookDAO dao = DAOUtilities.getBookDAO();
		List<Book> bookList = dao.getAllBooks();
		
		BookTagDAO tag_dao = DAOUtilities.getBookTagDAO();
		List<BookTag> bookTagList = tag_dao.getAllBookTags();

		// Populate the list into a variable that will be stored in the session
		request.getSession().setAttribute("books", bookList);
		request.getSession().setAttribute("bookTags", bookTagList);
		
		request.getRequestDispatcher("getAllBookTags.jsp").forward(request, response);
	}
}
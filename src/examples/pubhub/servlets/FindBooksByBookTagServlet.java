package examples.pubhub.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import examples.pubhub.dao.BookDAO;
import examples.pubhub.model.Book;
import examples.pubhub.dao.BookTagDAO;
import examples.pubhub.model.BookTag;

import examples.pubhub.utilities.DAOUtilities;

@MultipartConfig // This annotation tells the server that this servlet has
					// complex data other than forms
// Notice the lack of the @WebServlet annotation? This servlet is mapped the old
// fashioned way - Check the web.xml!
@WebServlet("/FindBooksByBookTag")
public class FindBooksByBookTagServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("findBookByBookTag.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String isbn13 = req.getParameter("isbn13");
		String tag = req.getParameter("title");
		
		List<Book> book_list = new ArrayList<>();

		BookDAO database = DAOUtilities.getBookDAO();
		Book tempBook = database.getBookByISBN(isbn13);
		if (tempBook == null) {
			// ASSERT: book with isbn already exists

			BookTagDAO bookTagdatabase = DAOUtilities.getBookTagDAO();
			book_list = bookTagdatabase.getBooksByTag(tag);
			
			if(book_list.isEmpty()) {
				req.getSession().setAttribute("message", "No books found");
				req.getSession().setAttribute("messageClass", "alert-danger");
				
			} 
			
			req.getSession().setAttribute("bookResults", book_list);
			req.getRequestDispatcher("findBookByBookTag.jsp").forward(req, resp);
		} else {
			
			book_list.add(tempBook);
			req.getSession().setAttribute("bookResults", book_list);
			req.getRequestDispatcher("findBookByBookTag.jsp").forward(req, resp);
			

		}
	}

}

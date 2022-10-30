package examples.pubhub.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

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
@WebServlet("/AddBookTag")
public class AddBookTagServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("addBookTag.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String isbn13 = req.getParameter("isbn13");
		String tag = req.getParameter("title");

		BookDAO database = DAOUtilities.getBookDAO();
		Book tempBook = database.getBookByISBN(isbn13);
		if (tempBook == null) {
			// ASSERT: book with isbn already exists

			req.getSession().setAttribute("message", "ISBN of " + isbn13 + " does not exist in database");
			req.getSession().setAttribute("messageClass", "alert-danger");
			req.getRequestDispatcher("addBookTag.jsp").forward(req, resp);

		} else {
			
			BookTagDAO bookTagdatabase = DAOUtilities.getBookTagDAO();
			BookTag tempBookTag = bookTagdatabase.getBookTag(isbn13, tag);
			
			if (tempBookTag == null){
				BookTag book_tag = new BookTag();
				book_tag.setIsbn13(req.getParameter("isbn13"));
				book_tag.setTag(req.getParameter("title"));


				boolean isSuccess = bookTagdatabase.addBookTag(book_tag);
				
				if(isSuccess){
					req.getSession().setAttribute("message", "Book Tag successfully added");
					req.getSession().setAttribute("messageClass", "alert-success");

					// We use a redirect here instead of a forward, because we don't
					// want request data to be saved. Otherwise, when
					// a user clicks "refresh", their browser would send the data
					// again!
					// This would be bad data management, and it
					// could result in duplicate rows in a database.
					resp.sendRedirect(req.getContextPath() + "/BookPublishing");
			} else {
	
				req.getSession().setAttribute("message", "There was a problem adding this tag");
				req.getSession().setAttribute("messageClass", "alert-danger");
				req.getRequestDispatcher("addBookTag.jsp").forward(req, resp);
				
			}

			
				
				
			}else {
				req.getSession().setAttribute("message", "Book Tag found in the system");
				req.getSession().setAttribute("messageClass", "alert-danger");
				req.getRequestDispatcher("addBookTag.jsp").forward(req, resp);
				
			}
		}
	}

}

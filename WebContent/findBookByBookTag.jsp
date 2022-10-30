	<!-- Header -->
	<jsp:include page="header.jsp" />
	
	<!-- JSTL includes -->
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
	
<!-- 	Just some stuff you need -->
	<header>
	  <div class="container">
	
	  
	<c:choose>
	<c:when test="${not empty message }">
	  <p class="alert ${messageClass}">${message }</p>
	<%
	  session.setAttribute("message", null);
	  session.setAttribute("messageClass", null);
	%>
	</c:when>
	</c:choose>
	
		<h1>PUBHUB <small>Add book tag</small></h1>
		<hr class="book-primary">

		<!-- NOTE: This form uses the enctype="multipart/form-data" attribute because it contains a file upload control (<input type="file" ... />).
				To support this special enctype, the PublishBookServlet also has the @MultiPartConfig annotation. You only need to use this
				enctype and its corresponding annotation if you need to use a file upload control. Do not use it otherwise. -->
				
		<form action="FindBooksByBookTag" method="post" class="form-horizontal" enctype="multipart/form-data">
		  <div class="form-group">
		    <label for="isbn13" class="col-sm-4 control-label">ISBN 13</label>
		    <div class="col-sm-5">
		      <input type="text" class="form-control" id="isbn13" name="isbn13" placeholder="ISBN 13" required="required" value="${param.isbn13 }" />
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="title" class="col-sm-4 control-label">Tag</label>
		    <div class="col-sm-5">
		      <input type="text" class="form-control" id="title" name="title" placeholder="Tag" required="required" value="${param.title }" />
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-sm-offset-4 col-sm-1">
		      <button type="submit" class="btn btn-info">Add</button>
		    </div>
		  </div>
		</form>
		<div class="container">
	  
	<c:choose>
	<c:when test="${not empty message }">
	  <p class="alert ${messageClass}">${message }</p>
	<%
	  session.setAttribute("message", null);
	  session.setAttribute("messageClass", null);
	%>
	</c:when>
	</c:choose>
	
		<h1>PUBHUB <small>Book Publishing</small></h1>
		<hr class="book-primary">

		<table class="table table-striped table-hover table-responsive pubhub-datatable">
			<thead>
				<tr>
					<td>ISBN-13:</td>
					<td>Title:</td>
					<td>Author:</td>
					<td>Publish Date:</td>
					<td>Price:</td>
					<td></td>
					<td></td>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="book" items="${bookResults}">
					<tr>
						<td><c:out value="${book.isbn13}" /></td>
						
						<td><c:out value="${book.title}" /></td>
						<td><c:out value="${book.author}" /></td>
						<td><c:out value="${book.publishDate}" /></td>
						<td><fmt:formatNumber value="${book.price }" type="CURRENCY"/></td>
						<td><form action="DownloadBook" method="get">
								<input type="hidden" name="isbn13" value="${book.isbn13}">
								<button class="btn btn-success">Download</button>
							</form></td>
						<td><form action="ViewBookDetails?isbn=${book.isbn13}" method="get">
								<input type="hidden" name="isbn13" value="${book.isbn13}">
								<button class="btn btn-primary">Details</button>
							</form></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

	  </div>	

	  </div>
	</header>



	<!-- Footer -->
	<jsp:include page="footer.jsp" />
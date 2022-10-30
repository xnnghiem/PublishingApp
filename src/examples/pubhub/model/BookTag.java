package examples.pubhub.model;

public class BookTag {
	
	private String isbn13;
	private String tag;	
	
	//Default constructor
	public BookTag() {
		this.isbn13 = null;
		this.tag = null;
	}
	
	public String getIsbn13() {
		return isbn13;
	}
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}

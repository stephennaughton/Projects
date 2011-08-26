package com.application.android.data.vo;

public class BookInfo {

	private int id;
	private String title;
	private String author;
	private String edition;
	private String description;
	
	private int pages;
	private String releaseDate;
	private String publisher;
	private String isbn;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";	
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_EDITION = "edition";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PAGES = "pages";
	public static final String KEY_RELEASEDATE = "releaseDate";
	public static final String KEY_PUBLISHER = "publisher";
	public static final String KEY_ISBN = "isbn";

	public BookInfo() {
		
	}
	
	public BookInfo(String title, String author, String edition,
			String description, int pages, String releaseDate,
			String publisher, String isbn) {
		super();
		this.title = title;
		this.author = author;
		this.edition = edition;
		this.description = description;
		this.pages = pages;
		this.releaseDate = releaseDate;
		this.publisher = publisher;
		this.isbn = isbn;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BookInfo [id=" + id + ", title=" + title + ", author=" + author
				+ ", edition=" + edition + ", description=" + description
				+ ", pages=" + pages + ", releaseDate=" + releaseDate
				+ ", publisher=" + publisher + ", isbn=" + isbn + "]";
	}	
	
}

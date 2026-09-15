package model;

public class Libros {

	private String isbn;
	private String category;
	private String title;
	private String lang;
	private String author;
	private String year;
	private double price;
	private String date;

	public Libros(String isbn, String category, String title, String lang, String author, double price) {
		this.isbn = isbn;
		this.category = category;
		this.title = title;
		this.lang = lang;
		this.author = author;
		this.price = price;
	}

	public Libros(String title, double price) {
		this.title = title;
		this.price = price;
	}

	public Libros() {
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String IzenaPrezioa() {
		return title + " (" + price + ")";
	}

	public String isbnIzena() {
		return isbn + "		" + title;
	}
}

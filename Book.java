package www.bigbee.com.Lamdba.action;

import java.time.LocalDate;

public class Book {
	
	private int id;
	private double price;
	private String type;
	private String name;
	private LocalDate publishDate;
	
	
	
	public Book(int id, double price, String type, String name, LocalDate publishDate) {
		super();
		this.id = id;
		this.price = price;
		this.type = type;
		this.name = name;
		this.publishDate = publishDate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", price=" + price + ", type=" + type + ", name=" + name + ", publishDate="
				+ publishDate + "]";
	}
	
	

}

package servlets;

import javax.servlet.annotation.WebServlet;

/**
 * Servlet implementation class Node
 */
@WebServlet(name = "Node", urlPatterns = {"/Node"})
public class Node {
	
	private Node next;
	private int bounciness;
	private String category;
	private String color;
	private String size;
	private String subcategory;
	
	public Node() {
		this.next = null;
		this.bounciness = 0;
		this.category = null;
		this.color = null;
		this.size = null;
		this.subcategory = null;
	}
	
    public Node(Node next, int bounciness, String category, String color, String size, String subcategory) {
        this.next = next;
    	this.bounciness = bounciness;
        this.category = category;
        this.color = color;
        this.size = size;
        this.subcategory = subcategory;
    }
}

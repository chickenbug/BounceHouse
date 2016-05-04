package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SQLConnector;

/**
 * Servlet implementation class AddQuestion
 */
@WebServlet(name = "AddQuestion", urlPatterns = {"/AddQuestion"})
public class AddQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddQuestion() {
		super();
		// TODO Auto-generated constructor stub
	}

	// Inserts item into auction table returns zeor in case things go pear shaped
	private int insert_question(String QText, int RepID, int UserID, String topic, HttpServletResponse response) throws IOException{
		try {
			Connection con = SQLConnector.getConnection();
			String sql = "INSERT INTO Question (QText, RepID, UserID, Topic) VALUES (?,?,?,?)";
			PreparedStatement s = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			s.setString(1, QText);
			s.setInt(2, RepID);
			s.setInt(3, UserID);
			s.setString(4, topic);
			s.executeUpdate();

			ResultSet r = s.getGeneratedKeys();
			r.next();
			int key = r.getInt(1);
			con.close();
			s.close();
			r.close();
			return key;


		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.sendRedirect("error.html");
			return 0;
		}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
		request.getRequestDispatcher(("WEB-INF/askQuestion.jsp")).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("userID") == null) {
			response.sendError(403, "You are not authorized to access this page.");
			return;
		}
		Connection con;
		try {
			con = SQLConnector.getConnection();
			String sql = "SELECT UserID FROM User WHERE Role = \"rep\";";
			PreparedStatement s = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			String qtext = request.getParameter("qtext");
			String topic = request.getParameter("topic");
			ResultSet rs = s.executeQuery();
			ArrayList<Integer> repNums = new ArrayList<Integer>();
			while(rs.next()){
				repNums.add(Integer.parseInt(rs.getString("userID")));
			}
			Random randomGenerator = new Random();
			int rando = randomGenerator.nextInt(repNums.size());
			int repid = repNums.get(rando);
			int userid = (Integer)request.getSession().getAttribute("userID");
			int QuestionID = insert_question(qtext, repid, userid, topic, response);
			con.close();
			s.close();
			rs.close();
			if(QuestionID==0){
				response.sendRedirect("error.html");
				return;
			}
			response.sendRedirect("questionSubmitted.jsp");
			return;
			} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			}
		}

	}

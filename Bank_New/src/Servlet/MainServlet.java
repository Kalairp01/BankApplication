package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdbc.Create_object;
import jdbc.Db_operations;
import jdbc.JsonUtils;
import tables.Account;
import tables.Branch;
import tables.Loan;
import tables.Main;
import tables.User;


@WebServlet("/*")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MainServlet() {
        super();
       
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		methodhandler(request,response,"get");
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		methodhandler(request,response,"post");
		
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		methodhandler(request,response,"put");
		
	}
	
	private void methodhandler(HttpServletRequest request, HttpServletResponse response,String type) throws IOException {
		
		if(type.equals("get")) {
			getmethod(request,response);
		}
		else if(type.equals("post")) {
			postmethod(request,response);
		}
		else if(type.equals("put")) {
			putmethod(request,response);
		}
	}
	
	private void getmethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uri = request.getPathInfo();
		PrintWriter pw = response.getWriter();
		String[] path = uri.split("/");
		switch(path[path.length-1]) {
			case "login":{
				String e_mail = request.getParameter("e_mail");
				String password = request.getParameter("password");
				String status = Db_operations.login(e_mail, password);
				pw.println(status);
			}
			case "home":{
				List<Main> mains = Db_operations.getMain();
				String res = JsonUtils.list_main(mains);
				pw.println(res);
			}
			break;
			case "bank":{
				int main_id = Integer.parseInt(request.getParameter("main_id"));
				Main main = Create_object.create_main(main_id);
				String res = JsonUtils.list_bank(Db_operations.branches(main));
				pw.println(res);
			}
			break;
			case "loan":{
				User user = Create_object.create_user(Integer.parseInt(request.getParameter("user_id")));
				Main main = Create_object.create_main(Integer.parseInt(request.getParameter("main_id")));
				String res = JsonUtils.loans(Db_operations.availed_loans(user, main));
				pw.println(res);
			}
			break;
			case "pendingloan":{
				Main main = Create_object.create_main(Integer.parseInt(request.getParameter("main_id")));
				String res = JsonUtils.loans(Db_operations.pending_loans(main));
				pw.println(res);
			}
		}
	}
	private void postmethod(HttpServletRequest request, HttpServletResponse response)throws IOException {
		String uri = request.getPathInfo();
		PrintWriter pw = response.getWriter();
		String[] path = uri.split("/");
		switch(path[path.length-1]) {
			case "signup":{
				Map<String,String> details = new HashMap<>();
				details.put("name",request.getParameter("name"));
				details.put("email",request.getParameter("email"));
				details.put("phone",request.getParameter("phone"));
				details.put("address",request.getParameter("address"));
				details.put("password",request.getParameter("password"));
				Db_operations.signup(details);	
			}
			break;
			case "createbranch":{
				Map<String,String> details = new HashMap<>();
				details.put("name",request.getParameter("name"));
				details.put("address",request.getParameter("address"));
				Main main = Create_object.create_main(Integer.parseInt(request.getParameter("main_id")));
				Db_operations.create_branch(details, main);
			}
			break;
			case "createaccount":{
				Map<String,String> details = new HashMap<>();
				User user = Create_object.create_user(Integer.parseInt(request.getParameter("user_id")));
				String type = request.getParameter("type");
				details.put("account_type",request.getParameter("account_type"));
				details.put("balance",request.getParameter("balance"));
				if(type.equals("main")) 
					details.put("id",request.getParameter("id"));				
				else {
					details.put("id",request.getParameter("id"));
					details.put("main_id",request.getParameter("main_id"));
					}
				pw.println(Db_operations.create_account(details, type, user));
				}
			break;
			case "createloan":{
				Map<String,String> details = new HashMap<>();
				Account account = Create_object.create_account(Integer.parseInt(request.getParameter("account_id")));
				details.put("loan_type",request.getParameter("loan_type"));
				details.put("amount",request.getParameter("amount"));
				details.put("period",request.getParameter("period"));
				Db_operations.loan(details, account);
			}
		}
	}
	private void putmethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uri = request.getPathInfo();
		PrintWriter pw = response.getWriter();
		String[] path = uri.split("/");
		switch(path[path.length-1]) {
			case "approveloan":{
				Loan loan = Create_object.create_loan(Integer.parseInt(request.getParameter("loan_id")));
				boolean approve = Boolean.parseBoolean(request.getParameter("approve"));
				if(approve)
				Db_operations.approve_loan(loan);
				else
				Db_operations.reject_loan(loan);	
			}
			break;
			case "transaction":{
				Account account = Create_object.create_account(Integer.parseInt(request.getParameter("holder_id")));
				Map<String,String> details = new HashMap<>();
				details.put("account",request.getParameter("account"));
				details.put("amount",request.getParameter("amount"));
				Db_operations.transaction(details, account);
			}
			case "deleteaccount":{
				Account account = Create_object.create_account(Integer.parseInt(request.getParameter("account_id")));
				Db_operations.delete_account(account);
			}
			case "deletebranch":{
				Branch branch = Create_object.create_branch(Integer.parseInt(request.getParameter("branch_id")));
				Db_operations.delete_branch(branch);
			}
			case "payloan":{
				Loan loan = Create_object.create_loan(Integer.parseInt(request.getParameter("loan_id")));
				Db_operations.pay_loan(loan);
			}
		}
		
	}
}

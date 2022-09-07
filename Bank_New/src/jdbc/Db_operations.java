//$Id$
package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tables.Account;
import tables.Branch;
import tables.Loan;
import tables.Main;
import tables.Transaction;
import tables.User;

public class Db_operations {
	public static String login(String e_mail,String password)  {
		
		try {
			Connection con =Utils.init_connection();
			String query = "select id,password from users where email=?;";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, e_mail);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int id = rs.getInt("id");
				String pwd = rs.getString("password");
				con.close();
				ps.close();
				rs.close();
				if(pwd.equals(password)) {
					return Integer.toString(id);
				}
				else 
					return "incorrect password";				
			}else 
				return "user not found";
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void signup(Map<String,String> details) {
		try {
			Utils.insert_query(details, "users");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
		
	public static void transaction(Map<String,String> details,Account account)  {
		int holder_balance = account.getBalance();
		int amount = Integer.parseInt(details.get("amount"));
		if((holder_balance - amount )>0 ) {
			Account transfer_account = Create_object.create_account(Integer.parseInt(details.get("account")));
			transfer_account.setBalance(transfer_account.getBalance()+amount);
			account.setBalance(account.getBalance()-amount);
			update_balance(account);
			update_balance(transfer_account);
			Transaction transaction = new Transaction();
			transaction.setAccount_id(account.getId());
			transaction.setBalance(account.getBalance());
			transaction.setAmount(amount);
			transaction.setTransaction_type("debit");
			transaction.setTransacted_account_no(transfer_account.getId());
			transaction.setTransacted_date(Utils.get_date());
			insert_transaction(transaction);
			transaction.setBalance(transfer_account.getBalance());
			transaction.setAccount_id(transfer_account.getId());
			transaction.setTransaction_type("credit");
			transaction.setTransacted_account_no(account.getId());
			insert_transaction(transaction);
		}
	}
	
	public static boolean loan(Map<String,String> details,Account account) {
		if(account.isPending_loan())
			return false;
		else {
			account.setPending_loan(true);
			account.setLoans_no(account.getLoans_no()+1);
			Loan loan = new Loan();
			loan.setLoan_amount(Integer.parseInt(details.get("amount")));
			if(account.getLoans_no()>1)
				loan.setInterest(11);
			else
				loan.setInterest(15);
			loan.setAccount_id(account.getId());
			loan.setLoan_type(details.get("loan_type"));
			loan.setApplied_date(Utils.get_date());
			loan.setPeriod(Integer.parseInt(details.get("period")));
			insert_loan(loan);
			update_loan(account);
			return true;
		}
	}
	
	public static String create_account(Map<String,String> details , String type , User user) {
		Account account = new Account();
		account.setAccount_type(details.get("account_type"));
		account.setBalance(Integer.parseInt(details.get("balance")));
		account.setUser_id(user.getId());
		if(type.equals("main")) {
			if(already_exists(Integer.parseInt(details.get("id")),user.getId(),"main"))
				return "account exists";
			else {
				account.setMain_id(Integer.parseInt(details.get("id")));				
			}
		}
		else if(type.equals("branch")) {
			if(already_exists(Integer.parseInt(details.get("id")),user.getId(),"branch"))
				return "account exists";
			else {
				account.setBranch_id(Integer.parseInt(details.get("id")));
				account.setMain_id(Integer.parseInt(details.get("main_id")));
			}
		}
		insert_account(account,type);
		return "account created";
	}
	
	public static void create_branch(Map<String,String> details , Main main) {
		details.put("main_id",Integer.toString(main.getId()));
		try {
			Utils.insert_query(details,"branch");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void delete_branch(Branch branch) {
		branch.setIs_active(false);
		update_branch(branch);
		move_accounts(branch);
		
	}
	
	public static void pay_loan(Loan loan) {
		Account account = Create_object.create_account(loan.getAccount_id());
		if(account.getBalance()-loan.getDue_amount()>0) {
			account.setBalance(account.getBalance()-loan.getDue_amount());
			update_balance(account);
			loan.setDue_date(Utils.get_duedate(loan.getDue_date()));
			loan.setPaid_amount(loan.getPaid_amount()+loan.getDue_amount());
			loan.setRemaining_amount(loan.getRemaining_amount()-loan.getDue_amount());
			String query = "update loan set due_date=?,paid_amount=?,remaining_amount=? where id=?;";
			try {
				Connection con =Utils.init_connection();
				PreparedStatement ps = con.prepareStatement(query);
				ps.setDate(1,loan.getDue_date());
				ps.setInt(2, loan.getPaid_amount());
				ps.setInt(3, loan.getRemaining_amount());
				ps.setInt(4, loan.getId());
				ps.executeUpdate();
			}catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void reject_loan(Loan loan) {
		loan.setIs_pending(false);
		loan.setIs_approved(false);
		Account account = Create_object.create_account(loan.getAccount_id());
		account.setPending_loan(false);
		account.setLoans_no(account.getLoans_no()-1);
		update_loan(account);
		String query = "update loan set is_pending=?,is_approved=? where id=?;";
		try{
			Connection con = Utils.init_connection();
			PreparedStatement ps= con.prepareStatement(query);
			ps.setBoolean(1, loan.isIs_pending());
			ps.setBoolean(2, loan.isIs_approved());
			ps.setInt(3,loan.getId());
			ps.executeUpdate();
		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void approve_loan(Loan loan) {
		loan.setIs_pending(false);
		loan.setIs_approved(true);
		loan.setStart_date(Utils.get_date());
		loan.setEnd_date(Utils.get_enddate(loan.getStart_date(), loan.getPeriod()));
		loan.setDue_date(Utils.get_duedate(loan.getStart_date()));
		loan.setTotal_amount(Utils.total_amount(loan.getLoan_amount(), loan.getInterest()));
		loan.setDue_amount( (int) Math.ceil((double)loan.getTotal_amount()/(loan.getPeriod()*12)));
		loan.setPaid_amount(0);
		loan.setRemaining_amount(loan.getTotal_amount());
		loan.setIs_closed(false);
		update_loandata(loan);
	}
	
	public static void close_loan(Loan loan) {
		loan.setIs_closed(true);
		String query = "update loan set ts_closed=true where id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, loan.getId());
			ps.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Loan> paid_loans(Main main){
		List<Loan> paid_loans = new ArrayList<>();
		String query = "select id from account where main_id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String query1 = "select id from loan where remaining_amount=0 and is_closed=false and account_id=?;";
				PreparedStatement ps1 = con.prepareStatement(query1);
				ResultSet rs1 = ps1.executeQuery();
				if(rs1.next()) {
					Loan loan = Create_object.create_loan(rs1.getInt("id"));
					paid_loans.add(loan);
				}
				rs1.close();
				ps1.close();
			}
			rs.close();
			ps.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return paid_loans;
	}
	public static List<Loan> availed_loans(User user,Main main){
		List<Loan> loans = new ArrayList<>();
		String query = "select id from account where user_id=? and pending_loan=true and main_id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps=con.prepareStatement(query);
			ps.setInt(1, user.getId());
			ps.setInt(2,main.getId());
			ResultSet rs =ps.executeQuery();
			while(rs.next()) {
				String query1 = "select id from loan where account_id=? and is_approved = true "
						+ "and is_closed=false;";
				PreparedStatement ps1=con.prepareStatement(query1);
				ps1.setInt(1, rs.getInt("id"));
				ResultSet rs1 = ps1.executeQuery();
				if(rs1.next()) {
					Loan loan = Create_object.create_loan(rs1.getInt("id"));
					loans.add(loan);
				}
				rs1.close();
			}
			con.close();
			rs.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return loans;
	}
	
	public static List<Transaction> transactions(User user,Main main){
		List<Transaction> transactions = new ArrayList<>();
		String query = "select id from account where user_id=? and is_active=true and main_id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String query1 = "select id from transaction where account_id=?;";
				PreparedStatement ps1 = con.prepareStatement(query1);
				ps1.setInt(1, rs.getInt("id"));
				ResultSet rs1 = ps1.executeQuery();
				while(rs.next()) {
					Transaction transaction = Create_object.create_transaction(rs1.getInt("id"));
					transactions.add(transaction);
				}
				rs1.close();
				ps1.close();
			}
			rs.close();
			ps.close();
		}catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		return transactions;
	}
	
	public static List<Account> accounts(User user,Main main){
		List<Account> accounts = new ArrayList<>();
		String query = "select id from account where user_id=? and is_active=true and main_id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, user.getId());
			ps.setInt(2, main.getId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Account account = Create_object.create_account(rs.getInt("id"));
				accounts.add(account);
			}
			con.close();
			rs.close();
			
		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		return accounts;
	}
	
	public static List<Branch> branches(Main main){
		List<Branch> branches = new ArrayList<>();
		String query = "select id from branch where main_id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, main.getId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Branch branch = Create_object.create_branch(rs.getInt("id"));
				branches.add(branch);
			}
			rs.close();
			ps.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return branches;
	}
	
	public static List<Loan> pending_loans(Main main){
		List<Loan> pending_loans = new ArrayList<>();
		String query = "select id from account where main_id=? and pending_loan = true;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, main.getId());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String query1="select id from loan where account_id=? and is_pending=true;";
				PreparedStatement ps1 = con.prepareStatement(query1);
				ps1.setInt(1, rs.getInt("id"));
				ResultSet rs1 = ps1.executeQuery();
				if(rs1.next()) {
					Loan loan = Create_object.create_loan(rs1.getInt("id"));
					pending_loans.add(loan);
				}
				
				rs1.close();
				ps1.close();
			}
			rs.close();
			ps.close();
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		
		return pending_loans;
	}
	public static List<Main> getMain(){
		List<Main> mains= new ArrayList<>();
		String query = "select id from main";
		try {
			Connection con =Utils.init_connection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				Main main = Create_object.create_main(rs.getInt("id"));
				mains.add(main);
				
			}
		}catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		return mains;
	}
	
	public static void delete_account(Account account) {
		account.setBalance(account.getBalance()-1000);
		account.setIs_active(false);
		String query = "update account set is_active=?, balance=? where id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	private static void update_loandata(Loan loan) {
		String query = "update loan set is_pending=?,is_approved=?,"
				+ "start_date=?,end_date=?,due_date=?,total_amount=?,"
				+ "due_amount=?,paid_amount=?,remaining_amount=?,is_closed=? where id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps= con.prepareStatement(query);
			ps.setBoolean(1, loan.isIs_pending());
			ps.setBoolean(2, loan.isIs_approved());
			ps.setDate(3, loan.getStart_date());
			ps.setDate(4, loan.getEnd_date());
			ps.setDate(5, loan.getDue_date());
			ps.setInt(6, loan.getTotal_amount());
			ps.setInt(7, loan.getDue_amount());
			ps.setInt(8, loan.getPaid_amount());
			ps.setInt(9, loan.getRemaining_amount());
			ps.setBoolean(10, loan.isIs_closed());
			ps.setInt(11,loan.getId());
			ps.executeUpdate();
		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	private static void move_accounts(Branch branch) {
		String query="update account set branch_id=null , main_id=? where branch_id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps=con.prepareStatement(query);
			ps.setInt(1, branch.getMain_id());
			ps.setInt(2,branch.getId());
			ps.executeUpdate();
			con.close();
			ps.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void update_branch(Branch branch) {
		String query = "update branch set is_active=false where id=?;";
		try {
			Connection con =Utils.init_connection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, branch.getId());
			ps.executeUpdate();
			con.close();
			ps.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insert_account(Account account , String type) {
		Map<String,String> details = new HashMap<>();
		details.put("account_type",account.getAccount_type());
		details.put("balance",Integer.toString(account.getBalance()));
		details.put("user_id",Integer.toString(account.getUser_id()));
		if(type.equals("main"))
			details.put("main_id",Integer.toString(account.getMain_id()));
		else if(type.equals("branch"))
			details.put("branch_id",Integer.toString(account.getBranch_id()));
			details.put("main_id",Integer.toString(account.getMain_id()));
		try {
			Utils.insert_query(details,"account");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean already_exists(int id,int user_id, String type) {
		
			String query = "select id from account where user_id=? and "+type+"_id=?";
			try {
				Connection con = Utils.init_connection();
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, user_id);
				ps.setInt(2, id);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					return true;
				}
			}catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			return false;
		
	}
	private static void update_loan(Account account) {
		String query = "update account set pending_loan=?, loans_no = ? where id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps=con.prepareStatement(query);
			ps.setBoolean(1, account.isPending_loan());
			ps.setInt(2, account.getLoans_no());
			ps.setInt(3, account.getId());
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	private static void update_balance(Account account) {
		String query = "update account set balance=? where id=?;";
		try {
			Connection con = Utils.init_connection();
			PreparedStatement ps=con.prepareStatement(query);
			ps.setInt(1, account.getBalance());
			ps.setInt(2, account.getId());
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insert_transaction(Transaction transaction) {
		Map<String,String> details = new HashMap<>();
		details.put("transaction_type",transaction.getTransaction_type());
		details.put("transacted_account_no",Integer.toString(transaction.getTransacted_account_no()));
		details.put("amount",Integer.toString(transaction.getAmount()));
		details.put("transacted_date",transaction.getTransacted_date().toString());
		details.put("account_id", Integer.toString(transaction.getAccount_id()));
		details.put("balance",Integer.toString(transaction.getBalance()));
		try {
			Utils.insert_query(details, "transaction");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void insert_loan(Loan loan) {
		Map<String,String> details = new HashMap<>();
		details.put("loan_type", loan.getLoan_type());
		details.put("loan_amount",Integer.toString(loan.getLoan_amount()));
		details.put("applied_date", loan.getApplied_date().toString());
		details.put("period",Integer.toString(loan.getPeriod()));
		details.put("interest",Integer.toString(loan.getInterest()));
		details.put("account_id",Integer.toString(loan.getAccount_id()));
		try {
			Utils.insert_query(details, "loan");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
		
}

	
class detailnotfoundexception extends Exception{
	public detailnotfoundexception (String str) {
		super(str);
	}
}

class passwordmismatchexception extends Exception{
	public passwordmismatchexception(String str) {
		super(str);
	}
}

class insufficientbalanceexception extends Exception{
	public insufficientbalanceexception(String str) {
		super(str);
	}
}

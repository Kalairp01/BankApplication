
package jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;

import tables.Account;
import tables.Branch;
import tables.Loan;
import tables.Main;
import tables.Transaction;
import tables.User;

public class Create_object{
	
	public static User create_user(int id) {
		User user = new User();
		ResultSet rs = Utils.get_result_set("users",id);
		try {
			if(rs.next()) {
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setE_mail(rs.getString("email"));
				user.setPhone_no(rs.getString("phone"));
				user.setAddress(rs.getString("address"));
				user.setIs_manager(rs.getBoolean("is_manager"));
				user.setIs_active(rs.getBoolean("is_active"));
				user.setPassword(rs.getString("password"));
				user.setId(rs.getInt("id"));
				user.setMain_id(rs.getInt("main_id"));
				user.setIs_active_manager(rs.getBoolean("is_active_manager"));
				rs.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public static Account create_account(int id) {
		Account account = new Account();
		ResultSet rs = Utils.get_result_set("account",id);
		try {
			if(rs.next()){
				account.setId(rs.getInt("id"));
				account.setAccount_type(rs.getString("account_type"));
				account.setBalance(rs.getInt("balance"));
				account.setInterest(rs.getInt("interest"));
				account.setLoans_no(rs.getInt("loans_no"));
				account.setPending_loan(rs.getBoolean("pending_loan"));
				account.setBranch_id(rs.getInt("branch_id"));
				account.setMain_id(rs.getInt("main_id"));
				account.setUser_id(rs.getInt("user_id"));
				account.setIs_active(rs.getBoolean("is_active"));
				rs.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}
	
	public static Transaction create_transaction(int id) {
		Transaction transaction = new Transaction();
		ResultSet rs = Utils.get_result_set("transaction", id);
		try {
			if(rs.next()) {
				transaction.setId(rs.getInt("id"));
				transaction.setTransacted_account_no(rs.getInt("transacted_account_no"));
				transaction.setAmount(rs.getInt("amount"));
				transaction.setTransacted_date(rs.getDate("transacted_date"));
				transaction.setAccount_id(rs.getInt("account_id"));
				transaction.setTransaction_type(rs.getString("transaction_type"));
				rs.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return transaction;
	}
	
	public static Loan create_loan(int id) {
		Loan loan = new Loan();
		ResultSet rs = Utils.get_result_set("loan", id);
		try {
			if(rs.next()) {
				loan.setId(rs.getInt("id"));
				loan.setLoan_type(rs.getString("loan_type"));
				loan.setLoan_amount(rs.getInt("loan_amount"));
				loan.setInterest(rs.getInt("interest"));
				loan.setApplied_date(rs.getDate("applied_date"));
				loan.setIs_pending(rs.getBoolean("is_pending"));
				loan.setIs_approved(rs.getBoolean("is_approved"));
				loan.setStart_date(rs.getDate("start_date"));
				loan.setEnd_date(rs.getDate("end_date"));
				loan.setDue_date(rs.getDate("due_date"));
				loan.setDue_amount(rs.getInt("due_amount"));
				loan.setPaid_amount(rs.getInt("paid_amount"));
				loan.setRemaining_amount(rs.getInt("remaining_amount"));
				loan.setTotal_amount(rs.getInt("total_amount"));
				loan.setIs_closed(rs.getBoolean("is_closed"));
				loan.setAccount_id(rs.getInt("account_id"));
				loan.setPeriod(rs.getInt("period"));
				rs.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return loan;
	}
	
	public static Main create_main(int id) {
		Main main = new Main();
		ResultSet rs = Utils.get_result_set("main", id);
		try {
			if(rs.next()) {
				main.setAddress(rs.getString("address"));
				main.setId(rs.getInt("id"));
				main.setName(rs.getString("name"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return main;
	}
	
	public static Branch create_branch(int id) {
		Branch branch = new Branch();
		ResultSet rs = Utils.get_result_set("branch", id);
		try {
			if(rs.next()) {
				branch.setId(rs.getInt("id"));
				branch.setName(rs.getString("name"));
				branch.setAddress(rs.getString("address"));
				branch.setMain_id(rs.getInt("main_id"));
				branch.setIs_active(rs.getBoolean("is_active"));
				rs.close();
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return branch;
	}
	
	
	
}

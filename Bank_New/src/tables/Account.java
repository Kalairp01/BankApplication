//$Id$
package tables;

public class Account {
	private int id;
	private String account_type;
	private int balance;
	private int interest;
	private int loans_no;
	private boolean pending_loan;
	private int branch_id;
	private int main_id;
	private int user_id;
	private boolean is_active;
	public int getId() {
		return id;
	}
	public String getAccount_type() {
		return account_type;
	}
	public int getBalance() {
		return balance;
	}
	public int getInterest() {
		return interest;
	}
	public int getLoans_no() {
		return loans_no;
	}
	public boolean isPending_loan() {
		return pending_loan;
	}
	public int getBranch_id() {
		return branch_id;
	}
	public int getMain_id() {
		return main_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public void setInterest(int interest) {
		this.interest = interest;
	}
	public void setLoans_no(int loans_no) {
		this.loans_no = loans_no;
	}
	public void setPending_loan(boolean pending_loan) {
		this.pending_loan = pending_loan;
	}
	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}
	public void setMain_id(int main_id) {
		this.main_id = main_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	@Override
	public String toString() {
		return "Account [id=" + id + ", account_type=" + account_type + ", balance=" + balance + ", interest=" + interest + ", loans_no=" + loans_no + ", pending_loan=" + pending_loan + ", branch_id=" + branch_id + ", main_id=" + main_id + ", user_id=" + user_id + ", is_active=" + is_active + "]";
	}
	
	
}

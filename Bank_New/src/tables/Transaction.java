package tables;

import java.sql.Date;

public class Transaction {
	private int id;
	private String transaction_type;
	private int transacted_account_no;
	private int amount;
	private Date transacted_date;
	private int account_id;
	private int balance;
	
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getId() {
		return id;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public int getTransacted_account_no() {
		return transacted_account_no;
	}
	public int getAmount() {
		return amount;
	}
	public Date getTransacted_date() {
		return transacted_date;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public void setTransacted_account_no(int transacted_account_no) {
		this.transacted_account_no = transacted_account_no;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setTransacted_date(Date transacted_date) {
		this.transacted_date = transacted_date;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", transaction_type=" + transaction_type + ", transacted_account_no=" + transacted_account_no + ", amount=" + amount + ", transacted_date=" + transacted_date + ", account_id=" + account_id + ", balance=" + balance + "]";
	}
	
	
}

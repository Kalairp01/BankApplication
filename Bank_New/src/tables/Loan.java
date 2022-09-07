package tables;

import java.sql.Date;

public class Loan {
	private int id;
	private String loan_type;
	private int loan_amount;
	private int interest;
	private Date applied_date;
	private boolean is_pending;
	private boolean is_approved;
	private Date start_date;
	private Date end_date;
	private Date due_date;
	private int due_amount;
	private int paid_amount;
	private int remaining_amount;
	private int total_amount;
	private boolean is_closed;
	private int account_id;
	private int period;
	public int getId() {
		return id;
	}
	public String getLoan_type() {
		return loan_type;
	}
	public int getLoan_amount() {
		return loan_amount;
	}
	public int getInterest() {
		return interest;
	}
	public Date getApplied_date() {
		return applied_date;
	}
	public boolean isIs_pending() {
		return is_pending;
	}
	public boolean isIs_approved() {
		return is_approved;
	}
	public Date getStart_date() {
		return start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public Date getDue_date() {
		return due_date;
	}
	public int getDue_amount() {
		return due_amount;
	}
	public int getPaid_amount() {
		return paid_amount;
	}
	public int getRemaining_amount() {
		return remaining_amount;
	}
	public int getTotal_amount() {
		return total_amount;
	}
	public boolean isIs_closed() {
		return is_closed;
	}
	public int getAccount_id() {
		return account_id;
	}
	public int getPeriod() {
		return period;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLoan_type(String loan_type) {
		this.loan_type = loan_type;
	}
	public void setLoan_amount(int loan_amount) {
		this.loan_amount = loan_amount;
	}
	public void setInterest(int interest) {
		this.interest = interest;
	}
	public void setApplied_date(Date applied_date) {
		this.applied_date = applied_date;
	}
	public void setIs_pending(boolean is_pending) {
		this.is_pending = is_pending;
	}
	public void setIs_approved(boolean is_approved) {
		this.is_approved = is_approved;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}
	public void setDue_amount(int due_amount) {
		this.due_amount = due_amount;
	}
	public void setPaid_amount(int amount_paid) {
		this.paid_amount = amount_paid;
	}
	public void setRemaining_amount(int remaining_amount) {
		this.remaining_amount = remaining_amount;
	}
	public void setTotal_amount(int total_amount) {
		this.total_amount = total_amount;
	}
	public void setIs_closed(boolean is_closed) {
		this.is_closed = is_closed;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	@Override
	public String toString() {
		return "Loan [id=" + id + ", loan_type=" + loan_type + ", loan_amount=" + loan_amount + ", interest=" + interest + ", applied_date=" + applied_date + ", is_pending=" + is_pending + ", is_approved=" + is_approved + ", start_date=" + start_date + ", end_date=" + end_date + ", due_date=" + due_date + ", due_amount=" + due_amount + ", paid_amount=" + paid_amount + ", remaining_amount=" + remaining_amount + ", total_amount=" + total_amount + ", is_closed=" + is_closed + ", account_id=" + account_id + ", period=" + period + "]";
	}
	
	
}

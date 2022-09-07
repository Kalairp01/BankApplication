
package tables;

import java.util.Date;

public class User {
	private int id;
	private String name;
	private String e_mail;
	private String phone_no;
	private String address;
	private boolean is_manager;
	private boolean is_active;
	private String password;
	private Date birth_date;
	private int main_id;
	private boolean is_active_manager;
	
	public int getMain_id() {
		return main_id;
	}
	public boolean isIs_active_manager() {
		return is_active_manager;
	}
	

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getE_mail() {
		return e_mail;
	}
	public String getPhone_no() {
		return phone_no;
	}
	public String getAddress() {
		return address;
	}
	public boolean isIs_manager() {
		return is_manager;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public String getPassword() {
		return password;
	}
	public Date getBirth_date() {
		return birth_date;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setIs_manager(boolean is_manager) {
		this.is_manager = is_manager;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}
	public void setMain_id(int main_id) {
		this.main_id = main_id;
	}
	public void setIs_active_manager(boolean is_active_manager) {
		this.is_active_manager = is_active_manager;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", e_mail=" + e_mail + ", phone_no=" + phone_no + ", address=" + address + ", is_manager=" + is_manager + ", is_active=" + is_active + ", password=" + password + ", birth_date=" + birth_date + ", main_id=" + main_id + ", is_active_manager=" + is_active_manager + "]";
	}
	
	
}

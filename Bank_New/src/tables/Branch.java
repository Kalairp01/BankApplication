//$Id$
package tables;

public class Branch {
	private int id;
	private String name;
	private String address;
	private boolean is_active;
	private int main_id;
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getAddress() {
		return address;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public int getMain_id() {
		return main_id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	public void setMain_id(int main_id) {
		this.main_id = main_id;
	}
	
	@Override
	public String toString() {
		return "Branch [id=" + id + ", name=" + name + ", address=" + address + ", is_active=" + is_active + ", main_id=" + main_id + "]";
	}
	
	
}

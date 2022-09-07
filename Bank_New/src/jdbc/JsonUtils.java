//$Id$
package jdbc;

import java.util.List;

import com.google.gson.Gson;

import tables.Branch;
import tables.Loan;
import tables.Main;

public class JsonUtils {
	public static Gson gson = new Gson();
	public static String Create_jsonMain(Main main) {
		String res = gson.toJson(main);
		return res;
	}
	
	public static String list_main(List<Main> mains) {
		String res = gson.toJson(mains);
		return res;
	}
	
	public static String list_bank(List<Branch> branches) {
		return gson.toJson(branches);
	}
	public static String loans(List<Loan> loans) {
		return gson.toJson(loans);
	}
}

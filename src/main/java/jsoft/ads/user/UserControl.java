package jsoft.ads.user;

import jsoft.*;
import jsoft.library.ORDER;
import jsoft.objects.*;

import java.sql.ResultSet;
import java.util.*;
import org.javatuples.*;

public class UserControl {
	private UserModel um;

	public UserControl(ConnectionPool cp) {
		this.um = new UserModel(cp);
	}

	public ConnectionPool getCP() {
		return this.um.getCP();
	}

	public void releaseConnection() {
		this.um.releaseConnection();
	}

//	------------------------------------------
	public boolean addUser(UserObject item) {
		return this.um.addUser(item);
	}

	public boolean editUser(UserObject item, USER_EDIT_TYPE et) {
		return this.um.editUser(item, et);
	}

	public boolean delUser(UserObject item) {
		return this.um.delUser(item);
	}

//	---------------------------------------------
	public UserObject getUserObject(int id) {
		return this.um.getUserObject(id);
	}

	public UserObject getUserObject(String username, String userpass) {
		return this.um.getUserObject(username, userpass);
	}

//	----------------------------------------------
	public ArrayList<String> viewUser(Triplet<UserObject, Integer, Byte> infos, Pair<USER_SOFT, ORDER> so) {
		Pair<ArrayList<UserObject>, Short> datas = this.um.getUserObjects(infos, so);

		ArrayList<String> views = new ArrayList<>();
		views.add(UserLibrary.viewUser(datas, infos));

		return views;
	}

	public static void main(String[] args) {
		ConnectionPool cp = new ConnectionPoolImpl();
		UserControl uc = new UserControl(cp);

		Triplet<UserObject, Integer, Byte> infos = new Triplet<>(null, 0, (byte) 15);

		ArrayList<String> views = uc.viewUser(infos, new Pair<>(USER_SOFT.NAME, ORDER.ASC));

		uc.releaseConnection();
		System.out.println(views);
	}
}

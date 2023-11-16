package jsoft.ads.user;

import jsoft.ShareControl;
import jsoft.ads.basic.Basic;
import jsoft.library.ORDER;
import jsoft.objects.*;
import java.sql.*;
import java.util.*;

import org.javatuples.*;

public interface User extends ShareControl {
	// các chức năng cập nhật
	public boolean addUser(UserObject item);

	public boolean editUser(UserObject item, USER_EDIT_TYPE et);

	public boolean delUser(UserObject item);

	// các chức năng lấy dữ liệu
	public ResultSet getUser(int id);

	public ResultSet getUser(String username, String userpass);

//	public ResultSet getUsers(UserObject similar, int at, byte total);
//	public ResultSet getUsers(UserObject similar, Integer at, Byte total);

	public ArrayList<ResultSet> getUsers(Triplet<UserObject, Integer, Byte> infos, Pair<USER_SOFT, ORDER> so);
}

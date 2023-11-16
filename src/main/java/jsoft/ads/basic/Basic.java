package jsoft.ads.basic;

import java.sql.*;
import jsoft.*;
import java.util.*;

public interface Basic extends ShareControl {
	//PreparedStatement pre - đã đc biên dịch và truyền đầy đủ giá trị( các đối tượng khác nhau về thuộc tính nên dùng PreparedStatement )
	public boolean add(PreparedStatement pre);

	public boolean edit(PreparedStatement pre);

	public boolean del(PreparedStatement pre);

	public ResultSet get(String sql, int id);

	public ResultSet get(ArrayList<String> sql, String name, String pass);

	public ResultSet gets(String sql);
	
	public ResultSet[] gets(String[] sql);
	
	// thuc hien nhieu SELECT trong 1 lan bien dich
	public ArrayList<ResultSet> getMR(String multiSelect);
	

}

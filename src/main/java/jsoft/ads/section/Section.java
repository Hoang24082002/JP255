package jsoft.ads.section;

import jsoft.objects.*;
import java.sql.*;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import jsoft.ShareControl;
import jsoft.ads.user.USER_EDIT_TYPE;
import jsoft.library.*;

public interface Section extends ShareControl {
	// các chức năng cập nhật
	public boolean addSection(SectionObject item);

	public boolean editSection(SectionObject item, SECTION_EDIT_TYPE et);

	public boolean delSection(SectionObject item);

	
	// các chức năng lấy dữ liệu
	public ResultSet getSection(short id);

	public ArrayList<ResultSet> getSections(Quartet<SectionObject, Short, Byte, UserObject> infos, Pair<SECTION_SOFT, ORDER> so);
}

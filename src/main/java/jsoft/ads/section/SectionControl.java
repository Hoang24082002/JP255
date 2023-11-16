package jsoft.ads.section;

import jsoft.*;
import jsoft.library.ORDER;
import jsoft.objects.*;

import java.sql.ResultSet;
import java.util.*;
import org.javatuples.*;

public class SectionControl {
	private SectionModel sm;

	public SectionControl(ConnectionPool cp) {
		this.sm = new SectionModel(cp);
	}

	public ConnectionPool getCP() {
		return this.sm.getCP();
	}

	public void releaseConnection() {
		this.sm.releaseConnection();
	}
	
//	------------------------------------------
	public boolean addSection(SectionObject item) {
		return this.sm.addSection(item);
	}

	public boolean editSection(SectionObject item, SECTION_EDIT_TYPE et) {
		return this.sm.editSection(item, et);
	}

	public boolean delSection(SectionObject item) {
		return this.sm.delSection(item);
	}

//	---------------------------------------------
	public SectionObject getSectionObject(short id) {
		return this.sm.getSectionObject(id);
	}

//	----------------------------------------------
	public ArrayList<String> viewSection(Quartet<SectionObject, Short, Byte, UserObject> infos, Pair<SECTION_SOFT, ORDER> so, UserObject user) {
		Quartet<ArrayList<SectionObject>, Short, HashMap<Integer, String>, ArrayList<UserObject>> datas = this.sm.getSectionObjects(infos, so);

//		ArrayList<String> views = new ArrayList<>();
//		views.add(SectionLibrary.viewSection(datas.getValue0(), infos.getValue0(), user, datas.getValue2(), datas.getValue3()));

		return SectionLibrary.viewSection(datas.getValue0(), infos.getValue0(), user, datas.getValue2(), datas.getValue3());
	}

//	public static void main(String[] args) {
//		ConnectionPool cp = new ConnectionPoolImpl();
//		SectionControl sc = new SectionControl(cp);
//
//		Triplet<SectionObject, Integer, Byte> infos = new Triplet<>(null, 0, (byte) 15);
//
//		ArrayList<String> views = sc.viewSection(infos, new Pair<>(SECTION_SOFT.NAME, ORDER.ASC));
//		
//		sc.releaseConnection();
//		System.out.println(views);
//	}
}

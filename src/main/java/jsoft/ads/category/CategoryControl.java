package jsoft.ads.category;

import java.util.ArrayList;
import java.util.HashMap;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;

import jsoft.ConnectionPool;
import jsoft.ads.section.SECTION_EDIT_TYPE;
import jsoft.ads.section.SECTION_SOFT;
import jsoft.ads.section.SectionLibrary;
import jsoft.ads.section.SectionModel;
import jsoft.library.ORDER;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

public class CategoryControl {
	private CategoryModel cm;

	public CategoryControl(ConnectionPool cp) {
		this.cm = new CategoryModel(cp);
	}

	public ConnectionPool getCP() {
		return this.cm.getCP();
	}

	public void releaseConnection() {
		this.cm.releaseConnection();
	}
	
//	------------------------------------------
	public boolean addCategory(CategoryObject item) {
		return this.cm.addCategory(item);
	}

	public boolean editCategory(CategoryObject item, CATEGORY_EDIT_TYPE et) {
		return this.cm.editCategory(item, et);
	}

	public boolean delCategory(CategoryObject item) {
		return this.cm.delCategory(item);
	}

//	---------------------------------------------
	public CategoryObject getCategoryObject(short id, UserObject userLogined) {
		return this.cm.getCategoryObject(id, userLogined);
	}
	
	public ArrayList<String> viewCategory(Quartet<CategoryObject, Short, Byte, UserObject> infos, Pair<CATEGORY_SOFT, ORDER> so, UserObject user) {
		Quintet<ArrayList<CategoryObject>,
				Short, 
				HashMap<Integer, String>, 
				ArrayList<UserObject>, 
				ArrayList<SectionObject>> datas = this.cm.getCategoryObjects(infos, so);


		return CategoryLibrary.viewCategory(datas.getValue0(), infos.getValue0(), user, datas.getValue2(), datas.getValue3(), datas.getValue4());
	}
}

package m2515029;
import java.io.File;
import java.util.List;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Constraint;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class Main {
	static String DB_NAME = "persons.db4o";

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		// 1. Xoá CSDL nếu CSDL đã có
		new File(".", DB_NAME).delete();

		ObjectContainer db = null;

		// 2. Mở CSDL nếu đã có, ngược lại tạo mới và mở
		try {
			db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_NAME);

			// 3. Thêm vào CSDL 5 đối tượng (Diem,20) (An,30) (Diem,18)
			// (Phuc,30)
			// (Tam,40)
			
			Person diem = new Person("Diem", 20);
			db.store(diem);
			Person an = new Person("An", 30);
			db.store(an);
			Person diem2 = new Person("Diem", 18);
			db.store(diem2);
			Person phuc = new Person("Phuc", 30);
			db.store(phuc);
			Person tam = new Person("Tam", 50);
			db.store(tam);
			// Truy vấn QBE

			// 4. Tìm những người tên Diễm
			System.out.println("-Tim nhung nguoi co ten Diem");
			ObjectSet result = db.queryByExample(new Person("Diem",0));
			System.out.println(result.toString());
			System.out.println("---------------------");
			
			// 5. Tìm những người có tuổi 30
			System.out.println("-Tim nhung nguoi co tuoi 30 ");
			result = db.queryByExample(new Person(null,30));
			System.out.println(result.toString());
			System.out.println("---------------------");
			
			// 6. Tìm tất cả mọi người sử dụng thuộc tính class
			System.out.println("-Tim tat ca su dung class");
			result = db.queryByExample(Person.class);
			System.out.println(result.toString());
			System.out.println("---------------------");

			// 7. Tìm tất cả mọi người sử dụng hàm tạo rỗng
			System.out.println("-Tim tat ca su dung hàm tạo rỗng");
			result = db.queryByExample(new Person());
			System.out.println(result.toString());
			System.out.println("---------------------");
			
			// Native Query
			System.out.println("----------NATIVE-----------");
			System.out.println("8. Tìm tất cả người tuổi từ 20 đến 30");
			
			List<Person> list = db.query(new Predicate<Person>() {
				public boolean match(Person candidate) {
					return candidate.getAge() >= 20 && candidate.getAge() <=30;
				}
			});
			System.out.println(list.toString());
			System.out.println("---------------------");
			
			System.out.println("9.Tìm tất cả người tên Diễm và có tuổi 20");
			list = db.query(new Predicate<Person>() {
				public boolean match(Person p) {
					return p.getAge() == 20 && p.getName().equals("Diem");
				}
			});
			System.out.println(list.toString());
			// SODA
			System.out.println("----------SODA-----------");
			System.out.println("----------10. SODA: tat ca moi nguoi");
			Query query=db.query();
			query.constrain(Person.class);
			result=query.execute();
			System.out.println(result.toString());
			
			
			System.out.println("----------11. SODA: tat ca moi nguoi: sap xep theo ten-----------");
			query=db.query();
			query.constrain(Person.class);
			query.descend("name").orderAscending();
			result=query.execute();
			System.out.println(result.toString());
			
			System.out.println("----------12. SODA: Nhung nguoi ten Diem-----------");
			query=db.query();
			query.constrain(Person.class);
			query.descend("name").constrain("Diem");
			result=query.execute();
			System.out.println(result.toString());
			
			System.out.println("----------13. SODA: Nhung nguoi khac 30 tuoi -----------");
			query=db.query();
			query.constrain(Person.class);
			query.descend("age").constrain(30).not();
			result=query.execute();
			System.out.println(result.toString());

			System.out.println("----------14. SODA: Nhung nguoi tuoi tu 20 den 30 tuoi -----------");
			query=db.query();
			query.constrain(Person.class);
			Constraint constr=query.descend("age")
			        .constrain(31).smaller();
			query.descend("age").constrain(19).greater().and(constr);
			result=query.execute();
			System.out.println(result.toString());
			
			System.out.println("----------15. SODA: Nhung nguoi ten bat dau bang T -----------");
			query=db.query();
			query.constrain(Person.class);
			query.descend("name").constrain("T").startsWith(true);
			result=query.execute();
			System.out.println(result.toString());
			
			db.close();
		} finally {
			if (db != null)
				db.close();
		}
	}

}

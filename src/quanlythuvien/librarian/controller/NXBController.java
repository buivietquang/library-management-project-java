package quanlythuvien.librarian.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import quanlythuvien.entity.NXB;

public class NXBController {
	private static NXBController me;

    /**
     *Hàm khởi tạo là private để không đối tượng nào bên ngoài có thể khởi tạo tuỳ ý đối tượng của lớp này
     */ 
    private NXBController(){}
	
    /**
     *Hàm này khởi tạo cho đối tượng dùng chung duy nhất của NXBController nếu đối tượng đố null
     * @return đối tượng dùng chung duy nhất của NXBController
     */ 
    public static NXBController getInstance(){
            if (me == null)
                    me = new NXBController();
            return me;
    }
    
    /**
	 * @throws SQLException 
	 * Phương thức thêm nhà xuất bản của sách 
	 * @param NXB map chứa các thông tin của nhà xuất bản cần thêm mới
	 * @return Map chứa thông tin kết quả thêm nhà xuất bản và thông điệp hiển thị tại lớp biên
	 * @exception SQLException
	 */
    public Map themNXB(Map<String, String> item) throws SQLException {
    	Map<String, Comparable> rs = new HashMap<String, Comparable>();
		rs.put("result", false);
	    NXB nxb = new NXB();
	    nxb.setDiachi(item.get("DiaChi"));
	    nxb.setEmail(item.get("Email"));
	    nxb.setFax(item.get("Fax"));
	    nxb.setTennxb(item.get("TenNXB"));
	    if (nxb.luuNXB()) {
			rs.put("result", true);
			rs.put("msg", "Thêm nhà xuất bản thành công");
		} else {
			rs.put("msg", "Thêm nhà xuất bản thất bại");
		}
	    return rs;
    }
        
    /**
     *Hàm này để lấy danh sách nhà xuất bản
     * @return List<Map> chứa danh sách các nhà xuất bản hiện có
     */
    public ArrayList<String> layDSNXB() {
        NXB NXB = new NXB();
        ArrayList<NXB> listnxbs = NXB.layDSNXB();
        ArrayList<String> ds = new ArrayList<String>();
        if (listnxbs != null) {
        	for (int i=0; i<listnxbs.size(); i++) {
        		ds.add(listnxbs.get(i).getTennxb());
            }
        }
        return ds;
    }
}

package quanlythuvien.librarian.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import quanlythuvien.entity.TheLoai;

public class TheLoaiController {
	private static TheLoaiController me;

    /**
     *Hàm khởi tạo là private để không đối tượng nào bên ngoài có thể khởi tạo tuỳ ý đối tượng của lớp này
     */ 
    private TheLoaiController(){}
	
    /**
     *Hàm này khởi tạo cho đối tượng dùng chung duy nhất của TheLoaiController nếu đối tượng đố null
     * @return đối tượng dùng chung duy nhất của TheLoaiController
     */ 
    public static TheLoaiController getInstance(){
            if (me == null)
                    me = new TheLoaiController();
            return me;
    }
    
    /**
	 * @throws SQLException 
	 * Phương thức thêm thể loại của sách 
	 * @param theloai map chứa các thông tin của thể loại cần thêm mới
	 * @return Map chứa thông tin kết quả thêm thể loại và thông điệp hiển thị tại lớp biên
	 * @exception SQLException
	 */
    public Map themTheLoai(Map<String, String> theloai) throws SQLException {
    	Map<String, Comparable> rs = new HashMap<String, Comparable>();
		rs.put("result", false);
		if (timTheLoaiTheoMa(theloai.get("MaTL")) != null) {
			rs.put("msg", "Mã thể loại này đã tồn tại. Vui lòng thử mã khác");
		} else {
	    	TheLoai tl = new TheLoai();
	    	tl.setMatl(theloai.get("MaTL"));
	    	tl.setTentl(theloai.get("TenTL"));
	    	if (tl.luuTheLoai()) {
				rs.put("result", true);
				rs.put("msg", "Thêm thể loại thành công");
			}
			else {
				rs.put("msg", "Thêm thể loại thất bại");
			}
		}
    	return rs;
    }
    
    /**
     *Hàm này để tìm thể loại bằng mã thể loại
     * @param matl mã thể loại
     * @return TheLoai là thể loại có mã tương ứng
	 * @throws SQLException 
     */
    public TheLoai timTheLoaiTheoMa(String matl) throws SQLException {
        TheLoai theloai = new TheLoai();
        if(theloai.layThongTinTheLoai(matl))
            return theloai;
        return null;
    }
    
    
    /**
     *Hàm này để lấy danh sách thể loại
     * @return List<Map> chứa danh sách các thể loại hiện có
     */
    public ArrayList<String> layDSTheLoai() {
        TheLoai theloai = new TheLoai();
        ArrayList<TheLoai> listtypes = theloai.layDSTL();
        ArrayList<String> ds = new ArrayList<String>();
        if (listtypes != null) {
        	for (int i=0; i<listtypes.size(); i++) {
        		ds.add(listtypes.get(i).getTentl());
            }
        }
        return ds;
    }
}

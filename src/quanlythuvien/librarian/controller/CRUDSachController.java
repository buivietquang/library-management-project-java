package quanlythuvien.librarian.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import quanlythuvien.entity.NXB;
import quanlythuvien.entity.Sach;
import quanlythuvien.entity.TheLoai;

public class CRUDSachController {
	
	private static CRUDSachController me;

    /**
     *Hàm khởi tạo là private để không đối tượng nào bên ngoài có thể khởi tạo tuỳ ý đối tượng của lớp này
     */ 
    private CRUDSachController(){}
	
    /**
     *Hàm này khởi tạo cho đối tượng dùng chung duy nhất của CRUDSachController nếu đối tượng đố null
     * @return đối tượng dùng chung duy nhất của CRUDSachController
     */ 
    public static CRUDSachController getInstance(){
            if (me == null)
                    me = new CRUDSachController();
            return me;
    }
    
	/**
	 * @throws SQLException 
	 * Phương thức thêm một cuốn sách 
	 * @param data map chứa các thông tin của cuốn sách cần thêm mới
	 * @return Map chứa thông tin kết quả thêm sách và thông điệp hiển thị tại lớp biên
	 * @exception SQLException
	 */
	public Map<String, Comparable> themSach(Map<?, ?> data) throws SQLException {
		Map<String, Comparable> rs = new HashMap<String, Comparable>();
		rs.put("result", false);
		if (timSachTheoISBN(data.get("ISBN").toString()) != null) {
			rs.put("msg", "Mã ISBN đã tồn tại. Vui lòng thử mã khác");
		} else {
			Sach sach = new Sach();
			sach.setISBN(data.get("ISBN").toString());
			sach.setNamxb(data.get("NamXB").toString());
			NXB nxb = new NXB();
			nxb.timNXBTheoTen(data.get("NXB").toString());
			sach.setNxb(nxb);
			TheLoai tl = new TheLoai();
			tl.timTheLoaiTheoTen(data.get("TheLoai").toString());
			sach.setTheloai(tl);
			sach.setTieude(data.get("TieuDe").toString());
			sach.setTacgia(data.get("TacGia").toString());
			if (sach.sinhMaSach()) { 
				if (sach.luuSach()) {
					rs.put("result", true);
					rs.put("msg", "Thêm sách thành công");
				}
				else {
					rs.put("msg", "Thêm sách thất bại");
				}
			} else {
				rs.put("msg", "Không thể sinh mã sách tự động");
			}
		}
		return rs;
	}
	
	/**
	 * @throws SQLException 
	 * Phương thức sửa thông tin một cuốn sách 
	 * @param data đối tượng sách chứa các thông tin cần sửa Map
	 * @param tlcu tên thể loại cũ String
	 * @return Map chứa thông tin kết quả sửa sách và thông điệp hiển thị tại lớp biên
	 * @throws SQLException
	 */
	public Map<String, Comparable> suaSach(Map<String,String> data,String tlcu) throws SQLException {
		Map<String, Comparable> rs = new HashMap<String, Comparable>();
		rs.put("result", false);
		Sach sach = new Sach();
		if (sach.timSachTheoISBN(data.get("ISBN"))) {
			if (!sach.getMasach().equals(data.get("MaSach"))) {
				 rs.put("msg", "Mã ISBN đã tồn tại. Vui lòng sử dụng mã khác");
				 return rs;
			}
		}
		sach.setMasach(data.get("MaSach"));
		sach.setTieude(data.get("TieuDe"));
		sach.setISBN(data.get("ISBN"));
		sach.setNamxb(data.get("NamXB"));
		sach.setTacgia(data.get("TacGia"));
		TheLoai tl = new TheLoai();
		tl.timTheLoaiTheoTen(data.get("TheLoai"));
		sach.setTheloai(tl);
		NXB nxb = new NXB();
		nxb.timNXBTheoTen(data.get("NXB"));
		sach.setNxb(nxb);
		rs.put("result", false);
		if (tlcu.equals(sach.getTheloai().getMatl())) {
			if (sach.suaThongTinSach(sach.getMasach())) {
				rs.put("result", true);
				rs.put("msg", "Sửa thông tin sách thành công");
			}
			else rs.put("msg", "Sửa thông tin sách thất bại");
		}
		else {
			if (sach.sinhMaSach()) {
				if (sach.suaThongTinSach(data.get("MaSach"))) {
					rs.put("result", true);
					rs.put("msg", "Sửa thông tin sách thành công");
				}
				else rs.put("msg", "Sửa thông tin sách thất bại");
			}
			else {
				rs.put("msg", "Không thể sinh mã sách mới phù hợp với thể loại đã sửa.");
			}
		}
		return rs;
	}
	
	/**
	 * @throws SQLException 
	 * Phương thức xóa một cuốn sách 
	 * @param sach cuốn sách cần xóa
	 * @return true nếu xóa sách thành công, false nếu thất bại
	 * @exception
	 */
	public boolean xoaSach(String masach) throws SQLException {
		Sach sach = new Sach();
        if(sach.layThongTinSach(masach)) return sach.xoaSach();
        return false;
	}
	
	/**
     *Hàm này để tìm sách bằng mã sách
     * @param masach
     * @return Sach là sach có mã sách tương ứng
	 * @throws SQLException 
     */
    public Sach timSachTheoMa(String masach) throws SQLException {
        Sach sach = new Sach();
        if(sach.layThongTinSach(masach))
            return sach;
        return null;
    }
    
    /**
     *Hàm này để tìm sách bằng isbn
     * @param isbn
     * @return Sach là sach có isbn tương ứng
	 * @throws SQLException 
     */
    public Sach timSachTheoISBN(String isbn) throws SQLException {
        Sach sach = new Sach();
        if(sach.timSachTheoISBN(isbn))
            return sach;
        return null;
    }
    
    /**
     *Hàm này để lấy danh sách 
     * @return List<Map> chứa danh sách các sách hiện có
     */
    public List<Map> layDSSach() {
        Sach sach = new Sach();
        ArrayList<Sach> listbooks = sach.layDSSach();
        List<Map> ds = new ArrayList<Map>();
        if (listbooks != null) {
        	Sach tmp;
        	for (int i=0; i<listbooks.size(); i++) {
        		tmp = listbooks.get(i);
            	Map<String,String> item = new HashMap<String, String>();
            	item.put("MaSach", tmp.getMasach());
            	item.put("TieuDe", tmp.getTieude());
            	item.put("TheLoai", tmp.getTheloai().getTentl());
            	item.put("NXB", tmp.getNxb().getTennxb());
            	item.put("TacGia", tmp.getTacgia());
            	item.put("NamXB", tmp.getNamxb());
            	item.put("ISBN", tmp.getISBN());
            	ds.add(item);
            }
        }
        return ds;
    }
    
    
    
    /**
     *Hàm này để tìm kiếm sách tùy vào tiêu chí
     * @return List<Map> chứa danh sách sách thỏa mãn
     * @throws SQLException 
     */
    public List<Map> timkiemSach(String choice, String content) throws SQLException {
        Sach sach = new Sach();
        ArrayList<Sach> listbooks = null;
        switch(choice) {
        case "TheLoai":
        	listbooks = sach.layDanhSachTheoLoai(content);
        	break;
        case "NXB":
        	listbooks = sach.layDanhSachTheoNXB(content);
        	break;
        case "TieuDe":
        	listbooks = sach.layDanhSachTheoTieuDeSach(content);
        	break;
        case "ISBN":
        	sach.timSachTheoISBN(content);
        	listbooks = new ArrayList<Sach>();
        	listbooks.add(sach);
        }
        List<Map> ds = new ArrayList<Map>();
        if (listbooks != null) {
        	Sach tmp;
        	for (int i=0; i<listbooks.size(); i++) {
        		tmp = listbooks.get(i);
            	Map<String,String> item = new HashMap<String, String>();
            	item.put("MaSach", tmp.getMasach());
            	item.put("TieuDe", tmp.getTieude());
            	item.put("TheLoai", tmp.getTheloai().getTentl());
            	item.put("NXB", tmp.getNxb().getTennxb());
            	item.put("TacGia", tmp.getTacgia());
            	item.put("NamXB", tmp.getNamxb());
            	item.put("ISBN", tmp.getISBN());
            	ds.add(item);
            }
        } 
        return ds;
    }
}

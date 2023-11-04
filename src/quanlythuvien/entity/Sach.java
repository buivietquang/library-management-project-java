package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

//import com.mysql.cj.jdbc.ConnectionImpl;

public class Sach {
	ConnectDB connectDB = ConnectDB.getInstance();
	private final String SEARCH_BOOK_BY_ID = "select * from tbl_book where id like ?";
	private final String GET_ALL_BOOK = "select * from tbl_book";
	private final String SEARCH_BOOK_BY_ISBN = "select * from tbl_book where ISBN = ?";
	private final String ADD_BOOK = "INSERT INTO tbl_book (id,state,NXB_id,book_type_id,title,ISBN,author,time_XB,created_at,updated_at) VALUES (?,?,?,?,?,?,?,?,NOW(),?)";
	private final String SEARCH_BOOK_BY_TYPE = "select tbl_book.* from tbl_book, tbl_book_type where tbl_book.book_type_id = tbl_book_type.id"
			+ " and tbl_book_type.name like ? and tbl_book.state = 1";
	private final String UPDATE_BOOK = "update tbl_book set id= ?,title=?,NXB_id=?,book_type_id=?,ISBN=?,author=?,time_XB=?,updated_at=now() where id = ?";
	private final String DELETE_BOOK = "delete from tbl_book where id = ?";
	private final String SEARCH_BOOK_BY_TITLE = "select* from tbl_book " + "where title like ? and state = 1";
	private final String FIND_MAX_ID = "select id from tbl_book where book_type_id = ? order by created_at DESC LIMIT 1";
	private final String SEARCH_BOOK_BY_NXB = "select tbl_book.* from tbl_book, tbl_nxb where tbl_book.NXB_id = tbl_nxb.id"
			+ " and tbl_nxb.name like ? and tbl_book.state = 1";
	private String masach;
	private String tieude;
	private String tacgia;
	private NXB nxb;
	private TheLoai theloai;
	private String ISBN;
	private String namxb;

	public String getMasach() {
		return masach;
	}

	public void setMasach(String masach) {
		this.masach = masach;
	}

	public String getTieude() {
		return tieude;
	}

	public void setTieude(String tieude) {
		this.tieude = tieude;
	}

	public String getTacgia() {
		return tacgia;
	}

	public void setTacgia(String tacgia) {
		this.tacgia = tacgia;
	}

	public NXB getNxb() {
		return nxb;
	}

	public void setNxb(NXB nxb) {
		this.nxb = nxb;
	}

	public TheLoai getTheloai() {
		return theloai;
	}

	public void setTheloai(TheLoai theloai) {
		this.theloai = theloai;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getNamxb() {
		return namxb;
	}

	public void setNamxb(String namxb) {
		this.namxb = namxb;
	}

	/**
	 * Phương thức lưu thông tin một cuốn sách vào cơ sở dữ liệu
	 * @return true nếu thêm sách vào cơ sở dữ liệu thành công, false nếu thất bại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean luuSach() throws SQLException {
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		ps = conn.prepareStatement(SEARCH_BOOK_BY_ID);
		ps.setString(1, masach);
		ResultSet rs = ps.executeQuery();
		if (rs != null && rs.next()) {
			rs.close();
			conn.close();
			return false;
		} else {
			ps = conn.prepareStatement(ADD_BOOK);
			ps.setString(1, masach);
			ps.setInt(2, 1);
			ps.setInt(3, nxb.getManxb());
			ps.setString(4, theloai.getMatl());
			ps.setString(5, tieude);
			ps.setString(6, ISBN);
			ps.setString(7, tacgia);
			ps.setString(8, namxb);
			ps.setDate(9, null);
			ps.executeUpdate();
			rs.close();
			ps.close();
			conn.close();
		}
		return true;
	}

	/**
	 * Phương thức sửa thông tin một cuốn sách trong cơ sở dữ liệu
	 * @param macu mã sách cũ nếu có sự thay đổi mã sách String
	 * @return true nếu update cơ sở dữ liệu thành công, false nếu thất bại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean suaThongTinSach(String macu) throws SQLException {
		Connection conn = connectDB.getConnection();
        PreparedStatement ps;
        ps = conn.prepareStatement(UPDATE_BOOK);
        ps.setString(1, masach);
        ps.setString(2, tieude);
        ps.setInt(3, nxb.getManxb());
        ps.setString(4, theloai.getMatl());
        ps.setString(5, ISBN);
        ps.setString(6, tacgia);
        ps.setString(7, namxb);
        ps.setString(8, macu);
        int count = ps.executeUpdate();
        ps.close();
        conn.close();
        if (count > 0) return true;
        else return false;
	}

	/**
	 * Phương thức xóa một cuốn sách trong cơ sở dữ liệu
	 * @return true nếu xóa sách trong cơ sở dữ liệu thành công, false nếu không thể xóa sách
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean xoaSach() throws SQLException {
		Connection conn = connectDB.getConnection();
        PreparedStatement ps;
        ps = conn.prepareStatement(DELETE_BOOK);
        ps.setString(1, masach);
        int count = ps.executeUpdate();
        ps.close();
        conn.close();
        if (count > 0) return true;
        else return false;
	}

	/**
	 * Tìm sách dựa theo mã sách
	 * @param masach mã sách cần lấy thông tin
	 * @return boolean nếu sách tồn tại return true, ngược lại thì return false;
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean layThongTinSach(String masach) throws SQLException {
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = conn.prepareStatement(SEARCH_BOOK_BY_ID);
		ps.setString(1, masach);
		ps.executeQuery();
		ResultSet rs = ps.executeQuery();
		if (rs != null && rs.next()) {
			TheLoai theloai = new TheLoai();
			theloai.layThongTinTheLoai(rs.getString("book_type_id"));
			NXB nxb = new NXB();
			nxb.layThongTinNXB(rs.getInt("NXB_id"));
			this.setTheloai(theloai);
			this.setNxb(nxb);
			this.setISBN(rs.getString("ISBN"));
			this.setTieude(rs.getString("title"));
			this.setTacgia(rs.getString("author"));
			this.setMasach(rs.getString("id"));
			this.setNamxb(rs.getString("time_XB"));
			rs.close();
			ps.close();
			conn.close();
			return true;
		}
		return false;
	}

	/**
	 * Tìm sách dựa theo mã ISBN
	 * @param ISBN mã ISBN cần lấy thông tin
	 * @return boolean nếu sách tồn tại return true, ngược lại thì return false;
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean timSachTheoISBN(String ISBN) throws SQLException {
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = conn.prepareStatement(SEARCH_BOOK_BY_ISBN);
		ps.setString(1, ISBN);
		ps.executeQuery();
		ResultSet rs = ps.executeQuery();
		if (rs != null && rs.next()) {
			TheLoai theloai = new TheLoai();
			theloai.layThongTinTheLoai(rs.getString("book_type_id"));
			NXB nxb = new NXB();
			nxb.layThongTinNXB(rs.getInt("NXB_id"));
			this.setTheloai(theloai);
			this.setNxb(nxb);
			this.setISBN(rs.getString("ISBN"));
			this.setTieude(rs.getString("title"));
			this.setTacgia(rs.getString("author"));
			this.setMasach(rs.getString("id"));
			this.setNamxb(rs.getString("time_XB"));
			rs.close();
			ps.close();
			conn.close();
			return true;
		}
		rs.close();
		ps.close();
		conn.close();
		return false;
	}

	/**
	 * Phương thức lấy danh sách sách từ cơ sở dữ liệu
	 * @param
	 * @return List<Sach> danh sách các đối tượng Sach duoc lay tu co so du lieu
	 * @exception SQLException khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<Sach> layDSSach() {
		ArrayList<Sach> l = new ArrayList<Sach>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(GET_ALL_BOOK);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Sach sach = new Sach();
				sach.setISBN(rs.getString("ISBN"));
				sach.setMasach(rs.getString("id"));
				sach.setNamxb(rs.getString("time_XB"));
				sach.setTacgia(rs.getString("author"));
				sach.setTieude(rs.getString("title"));
				TheLoai tl = new TheLoai();
				tl.layThongTinTheLoai(rs.getString("book_type_id"));
				sach.setTheloai(tl);
				NXB nxb = new NXB();
				nxb.layThongTinNXB(rs.getInt("NXB_id"));
				sach.setNxb(nxb);
				l.add(sach);
			}
			rs.close();
			ps.close();
			conn.close();
			return l;
		} catch (SQLException e) {
			e.getStackTrace();
			return null;
		}
	}

	/**
	 * Phương thức lấy danh sách theo thể loại từ cơ sở dữ liệu
	 * @param loaisach thể loại sách cần tìm
	 * @return List<Sach> danh sách các đối tượng Sach duoc lay tu co so du lieu
	 * @exception SQLException khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<Sach> layDanhSachTheoLoai(String loaisach) {
		ArrayList<Sach> l = new ArrayList<Sach>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SEARCH_BOOK_BY_TYPE);
			ps.setString(1, loaisach);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Sach sach = new Sach();
				sach.setISBN(rs.getString("ISBN"));
				sach.setMasach(rs.getString("id"));
				sach.setNamxb(rs.getString("time_XB"));
				sach.setTacgia(rs.getString("author"));
				sach.setTieude(rs.getString("title"));
				TheLoai tl = new TheLoai();
				tl.layThongTinTheLoai(rs.getString("book_type_id"));
				sach.setTheloai(tl);
				NXB nxb = new NXB();
				nxb.layThongTinNXB(rs.getInt("NXB_id"));
				sach.setNxb(nxb);
				l.add(sach);
			}
			rs.close();
			ps.close();
			conn.close();
			return l;
		} catch (SQLException e) {
			e.getStackTrace();
			return null;
		}
	}

	/**
	 * Phương thức lấy danh sách theo tieu de sách từ cơ sở dữ liệu
	 * @param title tiêu đề sách cần tìm kiếm
	 * @return List<Sach> danh sách các đối tượng Sach duoc lay tu co so du lieu
	 * @exception SQLException khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<Sach> layDanhSachTheoTieuDeSach(String title) {
		ArrayList<Sach> l = new ArrayList<Sach>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SEARCH_BOOK_BY_TITLE);
			ps.setString(1, "%" + title + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Sach sach = new Sach();
				sach.setISBN(rs.getString("ISBN"));
				sach.setMasach(rs.getString("id"));
				sach.setNamxb(rs.getString("time_XB"));
				sach.setTacgia(rs.getString("author"));
				sach.setTieude(rs.getString("title"));
				TheLoai tl = new TheLoai();
				tl.layThongTinTheLoai(rs.getString("book_type_id"));
				sach.setTheloai(tl);
				NXB nxb = new NXB();
				nxb.layThongTinNXB(rs.getInt("NXB_id"));
				sach.setNxb(nxb);
				l.add(sach);
			}
			rs.close();
			ps.close();
			conn.close();
			return l;
		} catch (SQLException e) {
			e.getStackTrace();
			return null;
		}
	}
	
	/**
	 * Phương thức lấy danh sách theo id sách từ cơ sở dữ liệu
	 * @param id mã sách cần tìm kiếm
	 * @return List<Sach> danh sách các đối tượng Sach duoc lay tu co so du lieu
	 * @exception SQLException khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<Sach> layDanhSachTheoID(String id) {
		ArrayList<Sach> l = new ArrayList<Sach>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SEARCH_BOOK_BY_ID);
			ps.setString(1, "%" + id + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Sach sach = new Sach();
				sach.setISBN(rs.getString("ISBN"));
				sach.setMasach(rs.getString("id"));
				sach.setNamxb(rs.getString("time_XB"));
				sach.setTacgia(rs.getString("author"));
				sach.setTieude(rs.getString("title"));
				TheLoai tl = new TheLoai();
				tl.layThongTinTheLoai(rs.getString("book_type_id"));
				sach.setTheloai(tl);
				NXB nxb = new NXB();
				nxb.layThongTinNXB(rs.getInt("NXB_id"));
				sach.setNxb(nxb);
				l.add(sach);
			}
			rs.close();
			ps.close();
			conn.close();
			return l;
		} catch (SQLException e) {
			e.getStackTrace();
			return null;
		}
	}
	
	/**
	 * Tìm mã sách lớn nhất của thể loại để phục vụ sinh mã sách
	 * @return true nếu sinh mã sách thành công, false nếu ngược lại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public Boolean sinhMaSach() throws SQLException {
		String id_book;
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = conn.prepareStatement(FIND_MAX_ID);
		ps.setString(1, theloai.getMatl());
		ResultSet rs = ps.executeQuery();
		int dem = 0;
		while (rs.next()) {
			dem++;
			String tmp1 = rs.getString("id");
			String tmp2 = tmp1.substring(2,6);
			int newid = Integer.parseInt(tmp2) + 1;
			NumberFormat nf = new DecimalFormat("0000");
			id_book = theloai.getMatl() + nf.format(newid);
			this.setMasach(id_book);
		} 
		if (dem == 0){
			id_book = theloai.getMatl() + "0001";
			this.setMasach(id_book);
		}
		rs.close();
		ps.close();
		conn.close();
		return true;
	}
	
	/**
	 * Phương thức lấy danh sách theo nhà xuất bản từ cơ sở dữ liệu
	 * @param nxb nhà xuất bản cần tìm
	 * @return List<Sach> danh sách các đối tượng Sach duoc lay tu co so du lieu
	 * @exception SQLException khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<Sach> layDanhSachTheoNXB(String nxb_name) {
		ArrayList<Sach> l = new ArrayList<Sach>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SEARCH_BOOK_BY_NXB);
			ps.setString(1, nxb_name);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Sach sach = new Sach();
				sach.setISBN(rs.getString("ISBN"));
				sach.setMasach(rs.getString("id"));
				sach.setNamxb(rs.getString("time_XB"));
				sach.setTacgia(rs.getString("author"));
				sach.setTieude(rs.getString("title"));
				TheLoai tl = new TheLoai();
				tl.layThongTinTheLoai(rs.getString("book_type_id"));
				sach.setTheloai(tl);
				NXB nxb = new NXB();
				nxb.layThongTinNXB(rs.getInt("NXB_id"));
				sach.setNxb(nxb);
				l.add(sach);
			}
			rs.close();
			ps.close();
			conn.close();
			return l;
		} catch (SQLException e) {
			e.getStackTrace();
			return null;
		}
	}
}

package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import com.mysql.cj.jdbc.ConnectionImpl;

import quanlythuvien.common.Constants;

public class BanSao {
	ConnectDB connectDB = ConnectDB.getInstance();
	private String maSach, maBanSao, author, title;
	private int loaiBanSao, trangThai, gia;
	private final String SEARCH_BANSAO_BY_ID = "select * from tbl_copy where id = ?";
	private final String ADD_BANSAO = "insert into tbl_copy(id, book_id, price, type, state, created_at) values(?, ?, ?, ?, ?, ?)";
	private final String UPDATE_STATE_BANSAO = "update tbl_copy set state = ?, updated_at = ? where id = ?";
	private final String UPDATE_BANSAO = "update tbl_copy set type = ?, price = ?, updated_at = ? where id = ?";
	private final String GET_MABANSAO_MOI = "select id from tbl_copy where book_id = ? order by abs(substring(id, 7)) desc limit 1";
	private final String DELETE_BANSAO = "delete from tbl_copy where id = ?";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private final String SEARCH_REGIST_BORROW = "SELECT  copy.id, copy.book_id, b.title, b.author, copy.price, copy.type, detail.created_at, book.id "
			+ "FROM tbl_borrow_card card, tbl_borrowed_books book,"
			+ " tbl_detail_borrowed_book detail, tbl_copy copy, tbl_book b, tbl_user u\r\n"
			+ "where u.id = ? and u.id = card.id_user and card.id = book.borrow_card_id and detail.borrowed_book_id = book.id and"
			+ " detail.copy_id = copy.id and b.id = copy.book_id and detail.state = 1";
	private final String GET_ALL_BAN_SAO_THEO_MA_SACH = "SELECT copy.id, copy.price, book.title, book.author"
			+ " FROM tbl_copy copy, tbl_book book\r\n" + "where copy.book_id = book.id and book.id = ? and type = 1 and copy.state = 0";
	private final String SEARCH_NOT_BORROW = "SELECT  copy.id, copy.book_id, b.title, b.author, copy.price, book.id, book.borrowed_time, book.deadline "
			+ "FROM tbl_borrow_card card, tbl_borrowed_books book,"
			+ " tbl_detail_borrowed_book detail, tbl_copy copy, tbl_book b, tbl_user u\r\n"
			+ "where u.id = ? and u.id = card.id_user and card.id = book.borrow_card_id and detail.borrowed_book_id = book.id and"
			+ " detail.copy_id = copy.id and b.id = copy.book_id and detail.state = 2";
	private final String SEARCH_HAS_BORROW = "SELECT  copy.id, copy.book_id, b.title, b.author, copy.price, copy.type, detail.created_at, detail.pay_date, book.id, book.deadline\r\n"
			+ "			FROM tbl_borrow_card card, tbl_borrowed_books book,"
			+ "			tbl_detail_borrowed_book detail, tbl_copy copy, tbl_book b, tbl_user u"
			+ "			where u.id = ? and u.id = card.id_user and card.id = book.borrow_card_id and detail.borrowed_book_id = book.id and"
			+ "			 detail.copy_id = copy.id and b.id = copy.book_id and detail.state = 3";

	public BanSao() {

	}

	public BanSao(String maSach, String maBanSao, int loaiBanSao, int trangThai, int gia, String author, String title) {
		super();
		this.maSach = maSach;
		this.maBanSao = maBanSao;
		this.loaiBanSao = loaiBanSao;
		this.trangThai = trangThai;
		this.gia = gia;
		this.author = author;
		this.title = title;
		
	}
	
	/**
	 * Phương thức kiem tra xem ban doc da tra du sach chua
	 * 
	 * @param
	 * @return true neu da tra het, false neu chua tra het
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public boolean checkChuaTra() {
		boolean b = true;
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SEARCH_NOT_BORROW);
			ps.setInt(1, BanDoc.getInstance().getMabandoc());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				b = false;
			}
			rs.close();
			ps.close();
			conn.close();
			return b;
		} catch (SQLException e) {
			e.getStackTrace();
			return b;
		}
	}

	/**
	 * Phương thức lấy danh sách sách chua tra cua doc gia
	 * 
	 * @param
	 * @return ArrayList<String> danh sách các đối tượng String duoc lay tu co so du
	 *         lieu
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<ArrayList<String>> layDanhSachChuaTra() {
		ArrayList<ArrayList<String>> l = new ArrayList<ArrayList<String>>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SEARCH_NOT_BORROW);
			ps.setInt(1, BanDoc.getInstance().getMabandoc());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("chua tra");
				ArrayList<String> list = new ArrayList<String>();
				list.add(rs.getString("id"));
				list.add(rs.getString("book_id"));
				list.add(rs.getString("title"));
				list.add(rs.getString("author"));
				list.add(rs.getString("price"));
				list.add(rs.getString("book.id"));
				list.add(rs.getString("book.borrowed_time"));
				list.add(rs.getString("book.deadline"));
				// System.out.println("title: " + rs.getString("title"));

				l.add(list);
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
	 * Phương thức lấy danh sách sách da tra cua doc gia
	 * 
	 * @param
	 * @return ArrayList<String> danh sách các đối tượng String duoc lay tu co so du
	 *         lieu
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<ArrayList<String>> layDanhSachDaTra() {
		ArrayList<ArrayList<String>> l = new ArrayList<ArrayList<String>>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SEARCH_HAS_BORROW);
			ps.setInt(1, BanDoc.getInstance().getMabandoc());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("da tra");
				ArrayList<String> list = new ArrayList<String>();
				list.add(rs.getString("id"));
				list.add(rs.getString("book_id"));
				list.add(rs.getString("title"));
				list.add(rs.getString("author"));
				list.add(rs.getString("price"));
				list.add(rs.getString("book.id"));
				list.add(rs.getString("detail.pay_date"));
				list.add(rs.getString("book.deadline"));
				l.add(list);
			}
			System.out.println("trc list");
			rs.close();
			ps.close();
			conn.close();
			System.out.println("list:");
			return l;
		} catch (SQLException e) {
			e.getStackTrace();
			return null;
		}
	}

	/**
	 * Phương thức lấy danh sách sách đăng ký mượn của độc giả
	 * 
	 * @param
	 * @return ArrayList<ArrayList<String>> danh sách các đối tượng
	 *         ArrayList<String> duoc lay tu co so du lieu
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<ArrayList<String>> layDanhSachDangKyMuon() {
		ArrayList<ArrayList<String>> l = new ArrayList<ArrayList<String>>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SEARCH_REGIST_BORROW);
			ps.setInt(1, BanDoc.getInstance().getMabandoc());
			System.out.println("Ma ban doc:" + BanDoc.getInstance().getMabandoc());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(rs.getString("id"));
				list.add(rs.getString("book_id"));
				list.add(rs.getString("title"));
				list.add(rs.getString("author"));
				list.add(rs.getString("price"));
				list.add(rs.getString("created_at"));
				list.add(rs.getString("book.id"));

				l.add(list);
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
	 * Phương thức lấy danh sách bản sao tìm kiếm
	 * 
	 * @param cot
	 *            trường tìm kiếm, nếu cot = "" thì lấy tất cả bản sao
	 * @return danh sách bản sao tìm được
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public List<BanSao> layDanhSachBanSaoTimKiem(String cot, int giaTri) {
		List<BanSao> dsBanSao = new ArrayList<>();
		Connection conn = ConnectDB.getInstance().getConnection();
		String sql;
		if (cot.equals("")) {
			sql = "select * from tbl_copy where book_id = ? order by abs(substring(id, 7)) asc";
		} else {
			sql = "select * from tbl_copy where book_id = ? and " + cot + " = ? order by abs(substring(id, 7)) asc";
		}
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			if (cot.equals("")) {
				ps.setString(1, maSach);
			} else {
				ps.setString(1, maSach);
				ps.setInt(2, giaTri);
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				BanSao banSao = new BanSao();
				banSao.setMaSach(maSach);
				banSao.setMaBanSao(rs.getString("id"));
				banSao.setGia(rs.getInt("price"));
				banSao.setLoaiBanSao(rs.getInt("type"));
				banSao.setTrangThai(rs.getInt("state"));

				dsBanSao.add(banSao);
			}

			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return dsBanSao;
	}

	/**
	 * Lấy thông tin bản sao dựa vào mã bản sao từ csdl
	 * 
	 * @param maBanSao
	 *            tham số tìm kiếm
	 * @return true nếu tìm thấy thông tin hoặc false nếu không tìm thấy thông tin
	 * @throws SQLException
	 *             nếu có lỗi truy vấn sql
	 */
	public boolean layThongTinBanSao(String maBanSao) {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(SEARCH_BANSAO_BY_ID);
			ps.setString(1, maBanSao);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				setMaSach(rs.getString("book_id"));
				setMaBanSao(rs.getString("id"));
				setLoaiBanSao(rs.getInt("type"));
				setTrangThai(rs.getInt("state"));
				setGia(rs.getInt("price"));
				b = true;
			}

			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * Xóa bản sao trong csdl
	 * 
	 * @return true nếu xóa thành công hoặc false nếu thất bại
	 * @throws SQLException
	 *             nếu có lỗi truy vấn sql
	 */
	public boolean xoaBanSao() {
		boolean b = false;
		if (trangThai == Constants.TRANGTHAI_DANGKYMUON || trangThai == Constants.TRANGTHAI_DAMUON) {
			return false;
		}
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(DELETE_BANSAO);
			ps.setString(1, maBanSao);
			if (ps.executeUpdate() > 0) {
				b = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return b;
	}

	/**
	 * Lưu thông tin bản sao vào csdl (thêm bản sao)
	 * 
	 * @param soLuongThem
	 *            số lượng bản sao c ần thêm
	 * @return true nếu thêm thành công hoặc false nếu thất bại
	 * @throws SQLException
	 *             nếu có lỗi truy vấn sql
	 */
	public boolean themBanSao(int soLuongThem) {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			String maBanSaoMoi = layMaBanSaoMoiNhat(maSach);
			int stt = 0;
			if (!maBanSaoMoi.equals(""))
				stt = Integer.parseInt(maBanSaoMoi.substring(6));
			for (int i = 0; i < soLuongThem; i++) {
				ps = conn.prepareStatement(ADD_BANSAO);
				maBanSao = maSach + (stt + i + 1);
				ps.setString(1, maBanSao);
				ps.setString(2, maSach);
				ps.setInt(3, gia);
				ps.setInt(4, loaiBanSao);
				ps.setInt(5, trangThai);
				String datetime = sdf.format(Calendar.getInstance().getTime());
				ps.setString(6, datetime);
				if (ps.executeUpdate() > 0)
					b = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return b;
	}

	/**
	 * Hàm lấy mã bản sao mới nhất
	 * 
	 * @param maSach
	 *            tham sô tryền vào
	 * @return mã bản sao mới nhất hoặc trả về ""
	 * @exception SQLException
	 *                nếu có lỗi truy vấn sql
	 */
	private String layMaBanSaoMoiNhat(String maSach) {
		String maBanSao = "";
		Connection conn = ConnectDB.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(GET_MABANSAO_MOI);
			ps.setString(1, maSach);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				maBanSao = rs.getString("id");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return maBanSao;
	}

	/**
	 * Cập nhật giá sách và loại bản sao trong csdl khi có sự thay đổi
	 * 
	 * @return true nếu cập nhật thành công hoặc false nếu thất bại
	 * @throws SQLException
	 *             nếu có lỗi truy vấn sql
	 */
	public boolean capNhatBanSao() {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(UPDATE_BANSAO);
			ps.setInt(1, loaiBanSao);
			ps.setInt(2, gia);
			String datetime = sdf.format(Calendar.getInstance().getTime());
			ps.setString(3, datetime);
			ps.setString(4, maBanSao);
			if (ps.executeUpdate() > 0)
				b = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return b;
	}

	/**
	 * Cập nhật trạng thái bản sao trong csdl khi có sự thay đổi
	 * 
	 * @return true nếu cập nhật thành công hoặc false nếu thất bại
	 * @throws SQLException
	 *             nếu có lỗi truy vấn sql
	 */
	public boolean capNhatTrangThaiBanSao() {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(UPDATE_STATE_BANSAO);
			ps.setInt(1, trangThai);
			String datetime = sdf.format(Calendar.getInstance().getTime());
			ps.setString(2, datetime);
			ps.setString(3, maBanSao);
			if (ps.executeUpdate() > 0)
				b = true;
			ps.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return b;
	}

	/**
	 * Phương thức lấy danh sách theo id sách từ cơ sở dữ liệu
	 * 
	 * @param id
	 *            mã sách cần tìm kiếm
	 * @return List<Sach> danh sách các đối tượng Sach duoc lay tu co so du lieu
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<BanSao> layDanhSachBanSaoTheoID(String id) {
		ArrayList<BanSao> l = new ArrayList<BanSao>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(GET_ALL_BAN_SAO_THEO_MA_SACH);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				BanSao banSao = new BanSao();
				banSao.setMaBanSao(rs.getString("id"));
				banSao.setAuthor(rs.getString("book.author"));
				banSao.setTitle(rs.getString("book.title"));
				banSao.setGia(rs.getInt("price"));
				l.add(banSao);
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

	public String getMaSach() {
		return maSach;
	}

	public void setMaSach(String maSach) {
		this.maSach = maSach;
	}

	public String getMaBanSao() {
		return maBanSao;
	}

	public void setMaBanSao(String maBanSao) {
		this.maBanSao = maBanSao;
	}

	public int getLoaiBanSao() {
		return loaiBanSao;
	}

	public void setLoaiBanSao(int loaiBanSao) {
		this.loaiBanSao = loaiBanSao;
	}

	public int getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}

	public int getGia() {
		return gia;
	}

	public void setGia(int gia) {
		this.gia = gia;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.sql.ConnectionPoolDataSource;
import javax.swing.JOptionPane;

//import com.mysql.cj.jdbc.ConnectionImpl;

import quanlythuvien.common.Constants;

public class ThongTinMuonTra {
	ConnectDB connectDB = ConnectDB.getInstance();
	private final String SEARCH_BORROW_INFO = "select DISTINCT tbl_borrowed_books.id,deposits,tbl_borrowed_books.state,borrow_card_id,register_time,borrowed_time,deadline from tbl_borrowed_books,tbl_detail_borrowed_book where tbl_detail_borrowed_book.borrowed_book_id = tbl_borrowed_books.id ";
	private final String SEARCH_BORROW_BY_ID = "select id, state, borrow_card_id, register_time, borrowed_time, deposits, deadline from tbl_borrowed_books where borrow_card_id = ? and state = ?";
	private final String ALL_BORROW_FOR_LIBRARIAN = "select bb.borrowed_time, bb.deadline, u.email, u.mssv, card.id, bb.id,bb.deadline, bb.register_time, bb.state\r\n"
			+ "from tbl_borrowed_books as bb, tbl_user as u, tbl_borrow_card as card\r\n"
			+ "where bb.borrow_card_id = card.id\r\n" + "	and u.id = card.id_user;";
	private final String UPDATE_STATE_TTT = "update tbl_borrowed_books set state = ? where id = ?";
	private final String DELETE_BORROW = "delete from tbl_borrowed_books where id = ?";
	private final String SEARCH_BOROW_DETAIL_BY_MT_ID = "select * from tbl_detail_borrowed_book where borrowed_book_id = ?";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	private final String CHECK_TO_SET_STATE = "select count(copy_id) as sl from tbl_detail_borrowed_book where borrowed_book_id = ? and pay_date is not NULL";
	// lấy mượn trả mà bạn đọc đã đky mượn nhưng chưa đến lấy, để thêm chi tiết mượn
	// trả theo mượn trả này
	private final String LAY_MUON_TRA_THEO_ID_BAN_DOC_DKY_MUON = "SELECT bb.id\r\n"
			+ "FROM tbl_borrowed_books bb, tbl_user user, tbl_borrow_card card\r\n"
			+ "where bb.state = 1 and user.id = card.id_user and bb.borrow_card_id = card.id and user.id = ? ";
	private final String THEM_MUON_TRA = "insert into tbl_borrowed_books(borrow_card_id, register_time, created_at) values(?, ?, ?)";

	private final String CHECK_BAN_DOC = "select count(detail.id) as so_luong_sach\r\n" + 
			"from tbl_detail_borrowed_book detail, tbl_user user, tbl_borrowed_books bb, tbl_borrow_card card\r\n" + 
			"where detail.borrowed_book_id = bb.id and user.id_card = card.id and bb.borrow_card_id = card.id\r\n" + 
			"and user.id = ? and detail.state = 1";
	private final String KT_QUA_HAN_DKY_MUON = "select mt.id as mt_id , copy.id as copy_id, detail.created_at from tbl_borrow_card card, tbl_borrowed_books mt, tbl_detail_borrowed_book detail, tbl_copy copy\r\n" + 
			" where mt.borrow_card_id = card.id and mt.id = detail.borrowed_book_id and detail.copy_id = copy.id and card.id = ?" + 
			" and detail.created_at < ? and mt.state = 1";
	
	private TheMuon themuon;
	private Date ngaydk;
	private Date ngaymuon;
	private Date hantra;
	private int datcoc;
	private int mamuontra;
	private int trangthai;
	private int maNhanVien;
	
	public int getTrangthai() {
		return trangthai;
	}

	public void setTrangthai(int trangthai) {
		this.trangthai = trangthai;
	}

	public TheMuon getThemuon() {
		return themuon;
	}

	public void setThemuon(TheMuon themuon) {
		this.themuon = themuon;
	}

	public Date getNgaydk() {
		return ngaydk;
	}

	public void setNgaydk(Date ngaydk) {
		this.ngaydk = ngaydk;
	}

	public Date getNgaymuon() {
		return ngaymuon;
	}

	public void setNgaymuon(Date ngaymuon) {
		this.ngaymuon = ngaymuon;
	}

	public Date getHantra() {
		return hantra;
	}

	public void setHantra(Date hantra) {
		this.hantra = hantra;
	}

	public int getDatcoc() {
		return datcoc;
	}

	public void setDatcoc(int datcoc) {
		this.datcoc = datcoc;
	}

	public int getMamuontra() {
		return mamuontra;
	}

	public void setMamuontra(int mamuontra) {
		this.mamuontra = mamuontra;
	}

	public int getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(int maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	
	/**
	 * Kiem tra xem co dang ky muon tra nao qua han khong
	 * @param maTheMuon ma the dky muon sach
	 * @return ResultSet 
	 * @exception SQLException khi truy van va ket noi csdl
	 */
	public ResultSet ktDkyQuaHan(int maTheMuon) {
		Connection conn = connectDB.getConnection();
		Date date = Calendar.getInstance().getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -2); // 2 day ago
		Date date2 = calendar.getTime();             
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
		String date1 = format1.format(date2);
		try {
			PreparedStatement ps = conn.prepareStatement(KT_QUA_HAN_DKY_MUON);
			ps.setInt(1, maTheMuon);
			ps.setString(2, date1);
			ResultSet rs =  ps.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}


	/**
	 * Lấy thông tin mượn trả từ csdl
	 * 
	 * @param mathe
	 *            Mã thẻ mượn int
	 * @param mabansao
	 *            mã bản sao String
	 * @param ngaymuon
	 *            ngày mượn String
	 * @param ngaytra
	 *            ngày trả String
	 * @return ArrayList<ThongTinMuonTra>
	 * @throws SQLException
	 *             nếu có lỗi truy vấn sql
	 */
	public ArrayList<ThongTinMuonTra> layThongTinMuonTra(int mathe, String mabansao, String ngaymuon, String ngaytra)
			throws SQLException {
		ArrayList<ThongTinMuonTra> listmt = new ArrayList<ThongTinMuonTra>();
		ResultSet rs;
		String queryForMaThe = " and borrow_card_id = ?";
		String queryForMaBanSao = " and copy_id like ?";
		String queryForNgayMuon = " and register_time like ?";
		String queryForNgayTra = " and pay_date like ?";
		String query = SEARCH_BORROW_INFO;
		if (mathe > 0)
			query += queryForMaThe;
		if (!mabansao.isEmpty())
			query += queryForMaBanSao;
		if (!ngaymuon.isEmpty())
			query += queryForNgayMuon;
		if (!ngaytra.isEmpty())
			query += queryForNgayTra;
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = conn.prepareStatement(query);
		int count = 1;
		if (mathe > 0) {
			ps.setInt(count, mathe);
			count++;
		}
		if (!mabansao.isEmpty()) {
			ps.setString(count, "%" + mabansao + "%");
			count++;
		}
		if (!ngaymuon.isEmpty()) {
			ps.setString(count, "%" + ngaymuon + "%");
			count++;
		}
		if (!ngaytra.isEmpty()) {
			ps.setString(count, "%" + ngaytra + "%");
		}
		rs = ps.executeQuery();
		while (rs.next()) {
			ThongTinMuonTra ttmt = new ThongTinMuonTra();
			ttmt.setHantra(rs.getDate("deadline"));
			ttmt.setNgaydk(rs.getDate("register_time"));
			ttmt.setNgaymuon(rs.getDate("borrowed_time"));
			ttmt.setMamuontra(rs.getInt("id"));
			ttmt.setTrangthai(rs.getInt("state"));
			ttmt.setDatcoc(rs.getInt("deposits"));
			TheMuon tm = new TheMuon();
			tm.layThongTinThe(rs.getInt("borrow_card_id"));
			ttmt.setThemuon(tm);
			listmt.add(ttmt);
		}
		rs.close();
		ps.close();
		conn.close();

		return listmt;
	}

	/**
	 * Cập nhật trạng thái của thông tin mượn trả
	 * 
	 * @return true nếu cập nhật thành công hoặc false nếu cập nhật thất bại
	 */
	public boolean capNhatThongTinMuonTra() {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			String UPDATE_STATE_TTM;
			if (trangthai == Constants.TRANGTHAI_DAMUON) {
				UPDATE_STATE_TTM = "update tbl_borrowed_books set state = ?, borrowed_time = ?, deadline = ?, updated_at = ?, deposits = ?, id_librarian = ? where id = ?";
			} else {
				UPDATE_STATE_TTM = "update tbl_borrowed_books set state = ?, updated_at = ? where id = ?";
			}
			ps = conn.prepareStatement(UPDATE_STATE_TTM);
			if (trangthai == Constants.TRANGTHAI_DAMUON) {
				Date date = Calendar.getInstance().getTime();
				ps.setInt(1, trangthai);
				ps.setString(2, sdf.format(date));
				ps.setString(3, sdf.format(getDateAfterTwoWeeks(date)));
				ps.setString(4, sdf.format(date));
				ps.setInt(5, datcoc);
				ps.setInt(6, maNhanVien);
				ps.setInt(7, mamuontra);
			} else {
				Date date = Calendar.getInstance().getTime();
				ps.setInt(1, trangthai);
				ps.setString(2, sdf.format(date));
				ps.setInt(3, mamuontra);
			}

			if (ps.executeUpdate() > 0) {
				b = true;
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;
	}

	/**
	 * Phương thức tìm thông tin mượn trả dựa vào thẻ mượn
	 * 
	 * @param theMuon
	 *            chứa mã lần mượn trả cần lấy thông tin
	 * @return boolean kết quả thông tin mượn trả tìm được dựa vào thẻ mượn
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu.
	 */
	public boolean layThongTinMuonTra(TheMuon theMuon, int trangThai) {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SEARCH_BORROW_BY_ID);
			ps.setInt(1, theMuon.getMathe());
			ps.setInt(2, trangThai);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				this.mamuontra = rs.getInt("id");
				this.trangthai = rs.getInt("state");
				this.ngaydk = rs.getDate("register_time");
				this.hantra = rs.getDate("deadline");
				this.ngaymuon = rs.getDate("borrowed_time");
				this.datcoc = rs.getInt("deposits");
				this.themuon = theMuon;
				b = true;
			}
			rs.close();
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
	 * Phương thức kiểm tra mượn trả của người mượn xem có đủ điều kiện mượn hay
	 * không
	 * 
	 * @return true nếu bạn đọc có khả năng mượn được sách false nếu bạn đọc không
	 *         thể mượn được sách
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public boolean checkBanDoc() {
		Connection conn = connectDB.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(CHECK_BAN_DOC);
			ps.setInt(1, BanDoc.getInstance().getMabandoc());
			rs = ps.executeQuery();
			while(rs.next()) {
				int count = rs.getInt("so_luong_sach");
				if(count < 5) {
					return true;
				}else 
					return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Phương thức kiểm tra sách
	 * 
	 * @param copyId
	 *            mã bản sao cần kiểm tra
	 * @return true nếu bản sao ở trạng thái chưa được mượn còn false nếu bản sao đã
	 *         được đăng ký mượn hoặc đã được mượn
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public boolean checkSach(int copyId) {
		return true;
	}

	/**
	 * Phương thức đăng ký mượn sách
	 * 
	 * @param copyID
	 *            ma ban sao
	 * @return true nếu đăng ký mượn thành công còn false nếu đăng ký mượn thất bại
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public boolean muonSach(String copyId) {
		TheMuon theMuon = new TheMuon();
		int maThe = theMuon.layMaTheBangMaBanDoc(BanDoc.getInstance().getMabandoc());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = dateFormat.format(date);
		
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(THEM_MUON_TRA);
			ps.setInt(1,maThe);
			ps.setString(2, strDate);
			ps.setString(3, strDate);
			
			int count = ps.executeUpdate();
			if(count > 0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Phương thức xóa thông tin mượn trả dựa vào mã mượn trả
	 * 
	 * @param muonTraId
	 *            mã mượn trả
	 * @return true nếu xóa thành công còn false nếu xóa thất bại
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public boolean xoaMuonTra(int muonTraId) {
		PreparedStatement ps = null;
		try {
			ps = connectDB.getConnection().prepareStatement(DELETE_BORROW);
			ps.setInt(1, muonTraId);
			if (ps.executeUpdate() != 0) {
				ps.close();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Phương thức lấy tất cả thông tin mượn cho thu thu
	 * 
	 * @param
	 * @return ThongTinMuonTra kết quả thông tin mượn trả tìm được
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<ThongTinMuonTra> layTatCaThongTinMuonTra() throws SQLException {
		ArrayList<ThongTinMuonTra> listmt = new ArrayList<ThongTinMuonTra>();
		ResultSet rs;
		Connection conn = connectDB.getConnection();
		String query = ALL_BORROW_FOR_LIBRARIAN;
		Statement statement = conn.createStatement();
		rs = statement.executeQuery(query);
		while (rs.next()) {
			ThongTinMuonTra ttmt = new ThongTinMuonTra();
			ttmt.mamuontra = rs.getInt("id");
			ttmt.trangthai = rs.getInt("state");
			ttmt.ngaydk = rs.getDate("register_time");
			ttmt.hantra = rs.getDate("deadline");
			ttmt.ngaymuon = rs.getDate("borrowed_time");
			ttmt.datcoc = rs.getInt("deposits");
			listmt.add(ttmt);
			System.out.println(ttmt.getMamuontra());
		}
		conn.close();
		
		return listmt;
	}

	/**
	 * Cập nhật trạng thái khi trả sách
	 * 
	 * @return true nếu cập nhật thành công hoặc false nếu cập nhật thất bại
	 * @throws SQLException
	 */
	public boolean capNhatTrangThaiTra(int state, int mamuontra) throws SQLException {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		ps = conn.prepareStatement(UPDATE_STATE_TTT);
		ps.setInt(1, state);
		ps.setInt(2, mamuontra);
		if (ps.executeUpdate() > 0) {
			b = true;
		}
		ps.close();
		conn.close();
		return b;
	}

	/**
	 * Lấy ngày hẹn trả cách ngày mượn 2 tuần
	 * 
	 * @param date
	 *            ngày mượn
	 * @return ngày hẹn trả
	 */
	private Date getDateAfterTwoWeeks(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 14); // 2 weeks
		return calendar.getTime();
	}

	/**
	 * phuong thuc kiem tra so chi tiet muon tra cua mot muon tra de xet xem co xoa
	 * muon tra do khong, khi ban doc huy muon sach
	 * 
	 * @param mtID
	 *            mã mượn trả
	 * 
	 * @return true số lượng bản ghi chi tiết mượn trả ung voi muon tra do la da tra
	 *         het hoac khong co va false neu con
	 */
	public boolean checkSoLuongChiTietMuonTra(int mtID) {
		Connection connect = connectDB.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = connect.prepareStatement(SEARCH_BOROW_DETAIL_BY_MT_ID);
			ps.setInt(1, mtID);
			rs = ps.executeQuery();
			if (rs.next()) {
				ps.close();
				connect.close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean checkTraDu(int slMuon, int mamuontra) throws SQLException {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		ps = conn.prepareStatement(CHECK_TO_SET_STATE);
		ps.setInt(1, mamuontra);
		ResultSet rs = ps.executeQuery();
		if (rs != null && rs.next()) {
			int count = rs.getInt("sl");
			if (count < slMuon)
				return false;
			else
				return true;
		}
		rs.close();
		ps.close();
		conn.close();
		return b;
	}

	/*
	 * Phuong thuc kiem tra xem ban doc do co muon tra nao la dang ky muon ma chua
	 * den lay hay khong, neu co thi se them cac chi tiet muon tra sau vao muon tra
	 * nay
	 * 
	 * @param userID ma ban doc
	 * 
	 * @return maMuonTra neu co, 0 neu khong co
	 */
	public int layMuonTraCuaNguoiDungChuaLay(int userID) {
		Connection conn = connectDB.getConnection();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(LAY_MUON_TRA_THEO_ID_BAN_DOC_DKY_MUON);
			ps.setInt(1, userID);
			rs = ps.executeQuery();
			if (rs.next()) {
				setMamuontra(rs.getInt("id"));
				return getMamuontra();
			} else {
				setMamuontra(0);
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}

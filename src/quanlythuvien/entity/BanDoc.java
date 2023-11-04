package quanlythuvien.entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import com.mysql.cj.jdbc.ConnectionImpl;
import java.sql.Connection;

public class BanDoc {
	private ConnectDB connect = ConnectDB.getInstance();
	private static BanDoc me;
	private int mabandoc;
	private int trangthai;
	private int gioitinh;
	private String email;
	private String sdt;
	private String hoten;
	private String mssv;
	private String password;
	private final String LAY_DS_BAN_DOC_CO_THE_DANG_HD = "select u.id, card.id from tbl_user u, tbl_borrow_card card where card.state = 1 and u.id = card.id_user ";
	private final String SEARCH_BANDOC_BY_ID = "select * from tbl_user where id = ?";

	public int getMabandoc() {
		return mabandoc;
	}

	public void setMabandoc(int mabandoc) {
		this.mabandoc = mabandoc;
	}

	public int getTrangthai() {
		return trangthai;
	}

	public void setTrangthai(int trangthai) {
		this.trangthai = trangthai;
	}

	public int getGioitinh() {
		return gioitinh;
	}

	public void setGioitinh(int gioitinh) {
		this.gioitinh = gioitinh;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public String getMssv() {
		return mssv;
	}

	public void setMssv(String mssv) {
		this.mssv = mssv;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Lay tat ca thong tin cua cac ban doc co the muon da duoc kich hoat trong he
	 * thong
	 * 
	 * @return ResultSet
	 * @exception SQLException
	 *                khi ket noi voi csdl
	 */
	public ResultSet layTatCaBanDocCoKhaNangMuonSach() {
		Connection conn = connect.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(LAY_DS_BAN_DOC_CO_THE_DANG_HD);
			ResultSet rs =  ps.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return null;
	}

	/**
	 * Lấy thông tin bạn đọc dựa vào mã bạn độc
	 * 
	 * @param maBanDoc
	 *            tham số tìm kiếm
	 * @return true nếu tìm thấy thông tin hoặc false nếu không tìm thấy thông tin
	 * @exception SQLException
	 *                khi kết nối với cơ sở dữ liệu
	 */
	public boolean layThongTinBanDoc(int maBanDoc) {
		boolean b = false;
		ResultSet rs = null;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			ps = (PreparedStatement) conn.prepareStatement(SEARCH_BANDOC_BY_ID);
			ps.setInt(1, maBanDoc);
			rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				this.setMabandoc(maBanDoc);
				this.setTrangthai(0);
				this.setGioitinh(rs.getInt("gender"));
				this.setEmail(rs.getString("email"));
				this.setSdt(rs.getString("phone_number"));
				this.setHoten(rs.getString("fullname"));
				this.setMssv(rs.getString("mssv"));
				this.setPassword(rs.getString("password"));
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

	public static BanDoc getInstance() {
		if (me == null) {
			me = new BanDoc();
		}

		return me;
	}
}

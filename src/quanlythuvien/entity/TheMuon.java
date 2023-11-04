package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import com.mysql.cj.jdbc.ConnectionImpl;

public class TheMuon {
	ConnectDB connectDB = ConnectDB.getInstance();
	private final String SEARCH_CARD_BY_ID = "select * from tbl_borrow_card where id = ?";
	private final String SEARCH_CARD_BY_USER_ID = "select * from tbl_borrow_card where id_user = ?";
	private int mathe;
	private int state;
	private BanDoc bandoc;

	public int getMathe() {
		return mathe;
	}

	public void setMathe(int mathe) {
		this.mathe = mathe;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public BanDoc getBandoc() {
		return bandoc;
	}

	public void setBandoc(BanDoc bandoc) {
		this.bandoc = bandoc;
	}

	/**
	 * Lấy thông tin thẻ mượn dựa vào mã thẻ
	 * 
	 * @param mathe
	 *            tham số tìm kiếm
	 * @return true nếu tìm thấy thông tin hoặc false nếu không tìm thấy thông tin
	 * @exception SQLException
	 *             khi kết nối với cơ sở dữ liệu
	 */
	public boolean layThongTinThe(int mathe) {
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SEARCH_CARD_BY_ID);
			ps.setInt(1, mathe);
			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				BanDoc bandoc = new BanDoc();
				bandoc.layThongTinBanDoc(rs.getInt("id_user"));
				this.setBandoc(bandoc);
				this.setMathe(rs.getInt("id"));
				this.setState(rs.getInt("state"));
				ps.close();
				rs.close();
				conn.close();
				
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Lấy thông tin thẻ mượn dựa vào mã ban doc
	 * 
	 * @param userID
	 *            tham số tìm kiếm
	 * @return maThe nếu tìm thấy thông tin hoặc 0 nếu không tìm thấy thông tin
	 * @throws SQLException
	 *             khi kết nối với cơ sở dữ liệu
	 */
	public int layMaTheBangMaBanDoc(int userID) {
		int maThe = 0;
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SEARCH_CARD_BY_USER_ID);
			ps.setInt(1, userID);
			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next()) {
				maThe = rs.getInt("id");
				rs.close();
				ps.close();
				conn.close();
				return maThe;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return maThe;
	}

}

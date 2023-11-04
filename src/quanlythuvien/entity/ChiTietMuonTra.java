package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import quanlythuvien.common.Constants;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import com.mysql.cj.jdbc.ConnectionImpl;

public class ChiTietMuonTra {
	ConnectDB connectDB = ConnectDB.getInstance();
	private final String SEARCH_BORROW_DETAIL = "select copy_id,pay_date,price,tbl_detail_borrowed_book.state,borrowed_book_id from tbl_detail_borrowed_book,tbl_copy where tbl_detail_borrowed_book.copy_id = tbl_copy.id and borrowed_book_id = ?";
	private final String UPDATE_STATE_BANSAO = "update tbl_copy set state = ?, updated_at = ? where id = ?";
	private final String UPDATE_STATE_CTMT = "update tbl_detail_borrowed_book set state = ?, updated_at = ? where borrowed_book_id = ? and copy_id = ?";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	private final String UPDATE_STATE_NGAYTRA = "update tbl_detail_borrowed_book set state = ?, received_user_id = ?, pay_date = now(), updated_at = now() where borrowed_book_id = ? and copy_id = ?";
	private final String THEM_DANG_KY_MUON = "insert into tbl_detail_borrowed_book (borrowed_book_id, copy_id, created_at) values(?, ?, ?) ";

	private BanSao bansao;
	private int mamuontra;
	private int trangthai;
	private Date ngaytra;
	private int maNhanVien;

	public int getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(int maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public BanSao getBanSao() {
		return bansao;
	}

	public void setBanSao(BanSao bansao) {
		this.bansao = bansao;
	}

	public int getMamuontra() {
		return mamuontra;
	}

	public void setMamuontra(int mamuontra) {
		this.mamuontra = mamuontra;
	}

	public int getTrangthai() {
		return trangthai;
	}

	public void setTrangthai(int trangthai) {
		this.trangthai = trangthai;
	}

	public Date getNgaytra() {
		return ngaytra;
	}

	public void setNgaytra(Date ngaytra) {
		this.ngaytra = ngaytra;
	}

	/**
	 * Lấy danh sách chi tiết mượn trả dựa theo mã lần mượn trả
	 * 
	 * @param mamuontra
	 *            mã lần mượn trả int
	 * @return ArrayList<ChiTietMuonTra> danh sách chi tiết mượn trả
	 * @throws SQLExeption
	 *             khi thực hiện câu truy vấn
	 */
	public ArrayList<ChiTietMuonTra> layChiTietMuonTra(int mamuontra) throws SQLException {
		ArrayList<ChiTietMuonTra> l = new ArrayList<ChiTietMuonTra>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps = (PreparedStatement) conn.prepareStatement(SEARCH_BORROW_DETAIL);
		ps.setInt(1, mamuontra);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ChiTietMuonTra ctmt = new ChiTietMuonTra();
			ctmt.setMamuontra(mamuontra);
			BanSao bansao = new BanSao();
			bansao.layThongTinBanSao(rs.getString("copy_id"));
			ctmt.setBanSao(bansao);
			ctmt.setMamuontra(rs.getInt("borrowed_book_id"));
			ctmt.setNgaytra(rs.getDate("pay_date"));
			ctmt.setTrangthai(rs.getInt("state"));
			l.add(ctmt);
		}
		rs.close();
		ps.close();
		conn.close();
		return l;
	}

	/*
	 * Them đăng ký mượn cho bạn đọc
	 * 
	 * @return true neu them thanh cong, flase neu that bai
	 * 
	 * @exception SQLException khi thuc hien truy van csdl
	 */
	public boolean themDangKyMuon() {
		ThongTinMuonTra ttmt = new ThongTinMuonTra();
		ttmt.layMuonTraCuaNguoiDungChuaLay(BanDoc.getInstance().getMabandoc());
		mamuontra = ttmt.getMamuontra();
		System.out.println("ma muon tra: " + mamuontra);
		System.out.println("ma ban sao :" + bansao.getMaBanSao());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = dateFormat.format(date);
		ConnectDB con = ConnectDB.getInstance();
		Connection conn = con.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(THEM_DANG_KY_MUON);
			ps.setInt(1, mamuontra);
			ps.setString(2, bansao.getMaBanSao());
			ps.setString(3, strDate);
			
			int count =  ps.executeUpdate();
			if(count > 0) {
				ps.close();
				conn.close();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	/*
	 * Hủy đăng ký mượn cho bạn đọc
	 * 
	 * @return true neu huy thanh cong, flase neu that bai
	 * 
	 * @exception SQLException khi thuc hien truy van csdl
	 */
	public boolean huyDangKyMuon() {
		boolean b = false;
		ConnectDB con = ConnectDB.getInstance();
		Connection conn = con.getConnection();
		String delete = "delete from tbl_detail_borrowed_book where borrowed_book_id = ? and copy_id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(delete);
			ps.setInt(1, mamuontra);
			ps.setString(2, bansao.getMaBanSao());
			if (ps.executeUpdate() > 0)
				b = true;
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
			
		return b;
	}

	/**
	 * Cập nhật trạng thái chi tiết mượn trả
	 * 
	 * @return true nếu cập nhật thành công, false nếu thất bại
	 * @exception SQLExeption
	 *                khi thực hiện câu truy vấn
	 */
	public boolean capNhatTrangThaiCTMT() {
		boolean b = false;
		if(bansao.capNhatTrangThaiBanSao()) {
			Connection conn = ConnectDB.getInstance().getConnection();
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(UPDATE_STATE_CTMT);
				ps.setInt(1, trangthai);
				ps.setString(2, sdf.format(Calendar.getInstance().getTime()));
				ps.setInt(3, mamuontra);
				ps.setString(4, bansao.getMaBanSao());
				if (ps.executeUpdate() > 0) {
					b = true;
				}
				ps.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		return b;
	}

	/**
	 * Cập nhật thông tin trả
	 * 
	 * @return true nếu cập nhật thành công, false nếu thất bại
	 * @exception SQLExeption
	 *                khi thực hiện câu truy vấn
	 */
	public boolean capNhatThongTinTra() {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(UPDATE_STATE_NGAYTRA);
			ps.setInt(1, trangthai);
			ps.setInt(2, maNhanVien);
			ps.setInt(3, mamuontra);
			ps.setString(4, bansao.getMaBanSao());
			int tmp = ps.executeUpdate();
			if (tmp > 0) {
				if (trangthai == Constants.TRANGTHAI_MAT) {
					bansao.setTrangThai(Constants.TRANGTHAI_MAT);
					return bansao.capNhatTrangThaiBanSao();
				}else {
					bansao.setTrangThai(Constants.TRANGTHAI_COSAN);
					return bansao.capNhatTrangThaiBanSao();
				}
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}
}

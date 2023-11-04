package quanlythuvien.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import quanlythuvien.boundary.HuyDangKyMuonSachForm;
import quanlythuvien.boundary.ViewUser;
import quanlythuvien.boundary.qlThuVien;
import quanlythuvien.entity.BanDoc;
import quanlythuvien.entity.ConnectDB;
import quanlythuvien.entity.TheMuon;
import quanlythuvien.entity.ThongTinMuonTra;
import quanlythuvien.user.controller.HuyDangKyMuonController;

public class DangNhapController {
	private final String SQL_DANGNHAP = "select * from tbl_user where email = ? and password = ?";
	private static DangNhapController controller;
	private BanDoc banDoc = BanDoc.getInstance();
	private ThongTinMuonTra thongTinMuonTra = new ThongTinMuonTra();
	private TheMuon theMuon = new TheMuon();
	
	public static DangNhapController getInstance() {
		if(controller == null)
			controller = new DangNhapController();
		
		return controller;
	}
	
	public boolean dangNhap(String email, String matKhau) {
		boolean b = false;
		Connection conn = ConnectDB.getInstance().getConnection();
		ResultSet rs = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SQL_DANGNHAP);
			ps.setString(1, email);
			ps.setString(2, matKhau);
			rs = ps.executeQuery();
			if(rs != null && rs.next()) {
				if(rs.getInt("role") == Constants.LOAITK_NHANVIEN) {
					BanDoc banDoc = BanDoc.getInstance();
					banDoc.setTrangthai(Constants.TRANGTHAITK_KICHHOAT);
					banDoc.setEmail(email);
					banDoc.setGioitinh(rs.getInt("gender"));
					banDoc.setHoten(rs.getString("fullname"));
					banDoc.setMabandoc(rs.getInt("id"));
					banDoc.setMssv("");
					banDoc.setPassword(matKhau);
					banDoc.setSdt(rs.getString("phone_number"));
					kiemTraMuonTraQuaHan();
					qlThuVien qlThuVien = new qlThuVien();
					b = true;
				}else {
					int state = rs.getInt("state");
					if(state == Constants.TRANGTHAITK_CHUAKICHHOAT) {
						JOptionPane.showMessageDialog(null, "Tài khoản chưa được kích hoạt");
					}else if(state == Constants.TRANGTHAITK_KHOA) {
						JOptionPane.showMessageDialog(null, "Tài khoản đã bị khóa");
					}else if(state == Constants.TRANGTHAITK_HETHAN) {
						JOptionPane.showMessageDialog(null, "Tài khoản đã hết hạn");
					}else {
						BanDoc banDoc = BanDoc.getInstance();
						banDoc.setTrangthai(Constants.TRANGTHAITK_KICHHOAT);
						banDoc.setEmail(email);
						banDoc.setGioitinh(rs.getInt("gender"));
						banDoc.setHoten(rs.getString("fullname"));
						banDoc.setMabandoc(rs.getInt("id"));
						banDoc.setMssv(rs.getString("mssv"));
						banDoc.setPassword(matKhau);
						banDoc.setSdt(rs.getString("phone_number"));
						new ViewUser();
						b = true;
					}
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
	}
	
	private void kiemTraMuonTraQuaHan() {
		try {
			ResultSet rs = banDoc.layTatCaBanDocCoKhaNangMuonSach();
			while(rs.next()) {
				int maTheMuon = rs.getInt("card.id");
				ResultSet rs_check = thongTinMuonTra.ktDkyQuaHan(maTheMuon);
				boolean b = true;
				while(rs_check.next() && b) {
					String copyID = rs_check.getString("copy_id");
					String mtID = rs_check.getString("mt_id");
					System.out.println("muon tra :" + mtID + " co ban sao: " + copyID + "bi qua han");
					if(!HuyDangKyMuonController.getInstance().huyMuon(copyID, mtID)) {
						b = false;
					}
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

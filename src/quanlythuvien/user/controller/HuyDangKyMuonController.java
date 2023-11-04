package quanlythuvien.user.controller;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import quanlythuvien.entity.BanSao;
import quanlythuvien.entity.ChiTietMuonTra;
import quanlythuvien.entity.ThongTinMuonTra;

public class HuyDangKyMuonController {

	private static HuyDangKyMuonController me;
	private BanSao banSao = new BanSao();

	/**
	 * Hàm khởi tạo là private để không đối tượng nào bên ngoài có thể khởi tạo tuỳ
	 * ý đối tượng của lớp này
	 */

	/**
	 * Hàm này khởi tạo cho đối tượng dùng chung duy nhất của CRUDSachController nếu
	 * đối tượng đố null
	 * 
	 * @return đối tượng dùng chung duy nhất của HuyDangKyMuonController
	 */
	public static HuyDangKyMuonController getInstance() {
		if (me == null)
			me = new HuyDangKyMuonController();
		return me;
	}

	/**
	 * Hủy mượn sách
	 * 
	 * @param copyID
	 *            ma ban sao, banDocID ma ban doc, mtID ma muon tra
	 * @return true nếu hủy thành công, false nếu thất bại
	 */
	public boolean huyMuon(String copyID, String mtID) {
		boolean b = false;
		int muontraID = Integer.parseInt(mtID);
		BanSao banSao = new BanSao();
		banSao.layThongTinBanSao(copyID);
		banSao.setTrangThai(0);
		String maID = banSao.getMaBanSao();
		ThongTinMuonTra mt = new ThongTinMuonTra();
		ChiTietMuonTra ctmt = new ChiTietMuonTra();
		ctmt.setBanSao(banSao);
		ctmt.setMamuontra(muontraID);
		if(banSao.capNhatTrangThaiBanSao() && ctmt.huyDangKyMuon()) {
			if(mt.checkSoLuongChiTietMuonTra(muontraID)) {
				mt.xoaMuonTra(muontraID);
			}
			b = true;
		}
		return b;
	}
	
	/*
	 *Lấy danh sách BanSao đã đăng ký mượn của độc giả
	 * @return ArrayList<BanSao> được lấy từ csdl
	 * @exception SQLException khi kết nối tới csdl
	 */
	public ArrayList<ArrayList<String>> layDanhSachDangKyMuon(){
		return banSao.layDanhSachDangKyMuon();
	}
}

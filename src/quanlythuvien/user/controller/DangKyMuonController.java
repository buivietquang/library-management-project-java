package quanlythuvien.user.controller;



import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

import quanlythuvien.boundary.DangKyMuonSachForm;
import quanlythuvien.entity.BanDoc;
import quanlythuvien.entity.BanSao;
import quanlythuvien.entity.ChiTietMuonTra;
import quanlythuvien.entity.Sach;
import quanlythuvien.entity.TheLoai;
import quanlythuvien.entity.ThongTinMuonTra;

public class DangKyMuonController {
	
	protected ThongTinMuonTra thongTinMuonTra = new ThongTinMuonTra();
	private TheLoai the_loai = new TheLoai();
	private Sach sach = new Sach();
	private BanSao banSao = new BanSao();
	
	protected DangKyMuonSachForm view;
	
	/**
	 * Mượn sách
	 * @param copyID mã bản sao
	 * @return true nếu mượn thành công, false nếu thất bại
	 */
	public boolean muonSach(int copyID) {
		return true;
	}
	
	/*
	 * Lấy danh sách tất cả thể loại sách và hiển thị ra DangKyMuonSachForm
	 * @return ArrayList<String> được lấy từ csdl
	 * @exception SQLException khi kết nối tới csdl
	 */
	public ArrayList<String> get_book_type() {
		ArrayList<TheLoai> theloai = new TheLoai().layDSTL();
		ArrayList<String> tl = new ArrayList<String>();
		for(TheLoai tmp : theloai){
			tl.add(tmp.getTentl());
		}
		return tl;		
	}
	
	/*
	 * Lấy danh sách tất cả sách
	 * @return ArrayList<ArrayList<String>> được lấy từ csdl
	 * @exception SQLException khi kết nối tới csdl
	 */
	public ArrayList<ArrayList<String>> layTatCaSach(){
		ArrayList<Sach> list_sach = sach.layDSSach();
		ArrayList<ArrayList<String>> list_str = new ArrayList<ArrayList<String>>();
		for(Sach tmp : list_sach) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(tmp.getMasach());
			list.add(tmp.getTieude());
			list.add(tmp.getTacgia());
			list.add(tmp.getTheloai().getTentl());
			list.add(tmp.getNxb().getTennxb());
			list.add(tmp.getNamxb());
			list.add(tmp.getISBN());
			
			list_str.add(list);
		}
		return list_str;
	}
	
	/*
	 * Lấy danh sách tất cả sách theo loại sách
	 */
	public ArrayList<ArrayList<String>> laySachTheoLoaiSach(String theloai){
		ArrayList<Sach> list_sach = sach.layDanhSachTheoLoai(theloai);
		ArrayList<ArrayList<String>> list_str = new ArrayList<ArrayList<String>>();
		for(Sach tmp : list_sach) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(tmp.getMasach());
			list.add(tmp.getTieude());
			list.add(tmp.getTacgia());
			list.add(tmp.getTheloai().getTentl());
			list.add(tmp.getNxb().getTennxb());
			list.add(tmp.getNamxb());
			list.add(tmp.getISBN());
			
			list_str.add(list);
		}
		return list_str;
	}
	
	/*
	 * Lấy danh sách sách theo thông tin sách
	 * @return ArrayList<ArrayList<String>> được lấy từ csdl
	 * @exception SQLException khi kết nối tới csdl
	 */
	public ArrayList<ArrayList<String>> laySachTheoTieuDeSach(String title){
		ArrayList<Sach> list_sach = sach.layDanhSachTheoTieuDeSach(title);
		ArrayList<ArrayList<String>> list_str = new ArrayList<ArrayList<String>>();
		for(Sach tmp : list_sach) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(tmp.getMasach());
			list.add(tmp.getTieude());
			list.add(tmp.getTacgia());
			list.add(tmp.getTheloai().getTentl());
			list.add(tmp.getNxb().getTennxb());
			list.add(tmp.getNamxb());
			list.add(tmp.getISBN());
			
			list_str.add(list);
		}
		return list_str;
	}
	
	/*
	 * Lấy danh sách sách theo id sách
	 * @return ArrayList<ArrayList<String>> được lấy từ csdl
	 * @exception SQLException khi kết nối tới csdl
	 */
	public ArrayList<ArrayList<String>> laySachTheoIdSach(String id){
		ArrayList<Sach> list_sach = sach.layDanhSachTheoID(id);
		ArrayList<ArrayList<String>> list_str = new ArrayList<ArrayList<String>>();
		for(Sach tmp : list_sach) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(tmp.getMasach());
			list.add(tmp.getTieude());
			list.add(tmp.getTacgia());
			list.add(tmp.getTheloai().getTentl());
			list.add(tmp.getNxb().getTennxb());
			list.add(tmp.getNamxb());
			list.add(tmp.getISBN());
			
			list_str.add(list);
		}
		return list_str;
	}
	
	/*
	 * Lấy danh sách bản sao theo id sách
	 * @return ArrayList<ArrayList<String> được lấy từ csdl
	 * @exception SQLException khi kết nối tới csdl
	 */
	public ArrayList<ArrayList<String>> layBanSaoTheoIdSach(String id){
		ArrayList<BanSao> list_bs = banSao.layDanhSachBanSaoTheoID(id);
		ArrayList<ArrayList<String>> list_str = new ArrayList<ArrayList<String>>();
		for(BanSao tmp : list_bs) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(tmp.getMaBanSao());
			list.add(tmp.getAuthor());
			list.add(tmp.getTitle());
			list.add(String.valueOf(tmp.getGia()));
			
			list_str.add(list);
		}
		
		return list_str;
	}
	
	/*
	 * Đăng ký mượn sách của độc giả mượn theo id bản sao
	 * @param 
	 *@return false neu dang ky muon khong thanh cong, true neu thanh cong
	 *@exception SQlException khi truy van csdl
	 */
	public boolean dangKyMuonSach(String copyID) {
		thongTinMuonTra.layMuonTraCuaNguoiDungChuaLay(BanDoc.getInstance().getMabandoc());
		
		ChiTietMuonTra ctmt = new ChiTietMuonTra();
		banSao.setMaBanSao(copyID);
		banSao.setTrangThai(1);
		ctmt.setBanSao(banSao);
		if(thongTinMuonTra.checkBanDoc() && banSao.checkChuaTra()) {
			if(thongTinMuonTra.getMamuontra() != 0) {
				if(banSao.capNhatTrangThaiBanSao() && ctmt.themDangKyMuon() ) {
					return true;
				}
				
			}else {
				if(banSao.capNhatTrangThaiBanSao() && thongTinMuonTra.muonSach(copyID) && ctmt.themDangKyMuon()) {
					return true;
				}
			}
		}else {
			JOptionPane.showMessageDialog(null, "Bạn không đủ điều kiện để mượn sách");
		}
		return false;
	}
	
}

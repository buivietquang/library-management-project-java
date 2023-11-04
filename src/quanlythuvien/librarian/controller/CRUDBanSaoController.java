package quanlythuvien.librarian.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import quanlythuvien.entity.BanSao;

public class CRUDBanSaoController {
	private static CRUDBanSaoController banSaoController;
	private CRUDBanSaoController() {
		
	};
	
	public static CRUDBanSaoController getInstance() {
		if(banSaoController == null)
			banSaoController = new CRUDBanSaoController();
		return banSaoController;
	}
	
	/**
	 * Thêm bản sao vào cơ sở dữ liệu
	 * @param data chứa thông tin bản sao cần thêm: maSach, loaiBanSao, trangThai, gia, soLuong
	 * @return true nếu thêm thành công hoặc false nếu thất bại
	 * @throws SQLException nếu có lỗi truy vấn sql*/
	public boolean themBanSao(Map<?, ?> data) {
		boolean b = false;
		BanSao banSao = new BanSao();
		banSao.setMaSach(data.get("maSach").toString());
		banSao.setLoaiBanSao((int) data.get("loaiBanSao"));
		banSao.setTrangThai((int) data.get("trangThai"));
		banSao.setGia((int) data.get("gia"));
		if(banSao.themBanSao((int) data.get("soLuong")))
			b = true;
		
		return b;
	}
	
	/**
	 * Lấy danh sách bản sao tìm kiếm
	 * @param data chứa thông tin tìm kiếm: maSach, cot, giaTri
	 * @return List<Map> chứa danh sách bản sao tìm được: maBanSao, loaiBanSao, trangThai, gia
	 * @exception SQLException nếu có lỗi truy vấn sql*/
	public List<Map> layDanhSachBanSao(Map<?, ?> data){
		BanSao banSao = new BanSao();
		banSao.setMaSach(data.get("maSach").toString());
		List<BanSao> dsBanSao = banSao.layDanhSachBanSaoTimKiem(data.get("cot").toString(), (int) data.get("giaTri"));
		List<Map> ds = new ArrayList<>();
		if(dsBanSao.size() > 0) {
			for(BanSao bs: dsBanSao) {
				Map<String, Comparable> map = new HashMap<>();
				map.put("maBanSao", bs.getMaBanSao());
				map.put("loaiBanSao", bs.getLoaiBanSao());
				map.put("trangThai", bs.getTrangThai());
				map.put("gia", bs.getGia());
				
				ds.add(map);
			}
		}
		
		return ds;
	}
	
	/**
	 * Xóa bản sao trong csdl
	 * @return true nếu xóa thành công hoặc false nếu thất bại
	 * */
	public boolean xoaBanSao(String maBanSao) {
		BanSao banSao = new BanSao();
		banSao.setMaBanSao(maBanSao);
		
		return  banSao.xoaBanSao();
	}

	/**
	 * Cập nhật bản sao trong csdl
	 * @return true nếu xóa thành công hoặc false nếu thất bại
	 * */
	public boolean capNhatBanSao(Map<String, Comparable> data) {
		BanSao banSao = new BanSao();
		banSao.setMaBanSao(data.get("maBanSao").toString());
		banSao.setLoaiBanSao((int) data.get("loaiBanSao"));
		banSao.setGia((int) data.get("gia"));
		
		return banSao.capNhatBanSao();
	}
}

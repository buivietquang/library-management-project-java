package quanlythuvien.librarian.controller;

import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import quanlythuvien.common.Constants;
import quanlythuvien.entity.BanDoc;
import quanlythuvien.entity.BanSao;
import quanlythuvien.entity.ChiTietMuonTra;
import quanlythuvien.entity.Sach;
import quanlythuvien.entity.TheMuon;
import quanlythuvien.entity.ThongTinMuonTra;

public class MuonTraController {
	private static MuonTraController me;
	private Format formatter = new SimpleDateFormat("dd-MM-yyyy");

    /**
     *Hàm khởi tạo là private để không đối tượng nào bên ngoài có thể khởi tạo tuỳ ý đối tượng của lớp này
     */ 
    private MuonTraController(){}
	
    /**
     *Hàm này khởi tạo cho đối tượng dùng chung duy nhất của CRUDSachController nếu đối tượng đố null
     * @return đối tượng dùng chung duy nhất của CRUDSachController
     */ 
    public static MuonTraController getInstance(){
            if (me == null)
                    me = new MuonTraController();
            return me;
    }
	
	/**
	 * Phương thức trả sách 
	 * @param dsTra danh sách thông tin trả sách List<Map>
	 * @param slMuon số lượng bản sao mượn trong lần mượn trả đó
	 * @param hantra hạn trả sách String
	 * @param mamuontra mã lần mượn trả int
	 * @return Map kết quả trả sách
	 * @throws SQLException
	 */
	public Map traSach(int slMuon, List<Map> dsTra, String hantra, int mamuontra) throws SQLException {
		Map rs = new HashMap();
		ArrayList<ChiTietMuonTra> ds = new ArrayList<ChiTietMuonTra>(); 
		int count = 0;
		rs.put("result", true);
		for (int i=0; i<dsTra.size(); i++) {
			Map item = dsTra.get(i);
			ChiTietMuonTra ctmt = new ChiTietMuonTra();
			System.out.println(item.get("TrangThai").toString());
			switch (item.get("TrangThai").toString()) {
			case "Đã trả sách": 
				ctmt.setTrangthai(Constants.TRANGTHAI_DATRA);
				break;
			case "Mất sách": 
				ctmt.setTrangthai(Constants.TRANGTHAI_MAT);
				break;
			case "Hỏng sách": 
				ctmt.setTrangthai(Constants.TRANGTHAI_HONG);
				break;
			}
			if (ctmt.getTrangthai() != 0) {
				ctmt.setNgaytra(new Date());
				BanSao bs = new BanSao();
				bs.layThongTinBanSao(item.get("MaBanSao").toString());
				ctmt.setBanSao(bs);
				BanDoc bandoc = BanDoc.getInstance();
				ctmt.setMaNhanVien(bandoc.getMabandoc());
				ctmt.setMamuontra(mamuontra);
				if (!ctmt.capNhatThongTinTra()) {
					rs.put("result", false);
					rs.put("msg", "Trả sách thất bại!");
					return rs;
				} else ds.add(ctmt);
			} else {
				count++;
			}
		}
		if (count == slMuon) {
			rs.put("result", false);
			rs.put("msg", "Vui lòng chọn trạng thái khi trả sách.");
			return rs;
		}
		else {
			int state;
			ThongTinMuonTra ttmt = new ThongTinMuonTra();
			if (!ttmt.checkTraDu(slMuon, mamuontra)) 
				state = Constants.TRANGTHAI_TRATHIEU;
			else state = Constants.TRANGTHAI_DATRA;
			if(!ttmt.capNhatTrangThaiTra(state, mamuontra)) {
				rs.put("result", false);
				rs.put("msg", "Trả sách thất bại!");
				return rs;
			}
			else {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				LocalDate endofCentury = LocalDate.parse(hantra, formatter);
				LocalDate now = LocalDate.now();
				Period diff = Period.between(endofCentury, now);
				int tienphat = tinhTienPhat(ds, diff.getDays());
				rs.put("msg", "Trả sách thành công!\nTiền phạt: "+tienphat);
				rs.put("state", state);
				return rs;
			}	
		}	
	}
	
	/**
	 * Phương thức tính tiền phạt khi mượn sách của bạn đọc
	 * tiền phạt = giá bản sao / 2 nếu làm hỏng bản sao
	 * tiền phạt = giá bản sao nếu mất bản sao
	 * tiền phạt = 2000*(số ngày trễ)
	 * @param 
	 * @return int tiền phạt bạn đọc cần nộp
	 * @exception NumberFormatException khi thực hiện tính toán 
	 */
	public int tinhTienPhat(ArrayList<ChiTietMuonTra> dsTra, long diffDay) {
		ThongTinMuonTra ttmt = new ThongTinMuonTra();
		int tienphat = 0, state;
		for (int i=0; i<dsTra.size(); i++) {
			state = dsTra.get(i).getTrangthai();
			if (state != Constants.TRANGTHAI_DATRA) {
					if (state == Constants.TRANGTHAI_MAT){
						tienphat += dsTra.get(i).getBanSao().getGia();
					}
					else 
						if (state == Constants.TRANGTHAI_HONG){
							tienphat += (int) dsTra.get(i).getBanSao().getGia()/2;
						}
			} 
		}
		if (diffDay >= 0) tienphat += 2000*diffDay*dsTra.size();
		return tienphat;
	}
		
	/**
	 * Lấy thông tin mượn trả cần tìm kiếm
	 * @param mathe mã thẻ mượn int
	 * @param mabansao mã bản sao String
	 * @param ngaymuon ngày mượn String
	 * @param ngaytra ngày trả String
	 * @return List<Map> 
	 * @throws SQLException nếu có lỗi truy vấn sql
	 */
	public List<Map> timKiemMuonTra(int mathe, String mabansao, String ngaymuon, String ngaytra) throws SQLException {
		List<Map> rs = new ArrayList<Map>();
		ThongTinMuonTra ttmt = new ThongTinMuonTra();
		ArrayList<ThongTinMuonTra> dsttmt = ttmt.layThongTinMuonTra(mathe, mabansao, ngaymuon, ngaytra);
		if (dsttmt != null) {
			for (int i=0; i<dsttmt.size(); i++) {
				ttmt = dsttmt.get(i);
				Map<String, Comparable> tmp = new HashMap<String, Comparable>();
				tmp.put("MaMuonTra", ttmt.getMamuontra());
				tmp.put("MaTheMuon", ttmt.getThemuon().getMathe());
				String s1,s2,s3;
				s1 = formatter.format(ttmt.getNgaydk());
				if (ttmt.getNgaymuon() != null) s2 = formatter.format(ttmt.getNgaymuon());
				else s2 = "";
				if (ttmt.getHantra() != null) s3 = formatter.format(ttmt.getHantra());
				else s3 = "";
				tmp.put("NgayDangKi", s1);
				tmp.put("NgayMuon", s2);
				tmp.put("HanTra", s3);
				switch (ttmt.getTrangthai()) {
		        case Constants.TRANGTHAI_DANGKYMUON: 
		        	tmp.put("TrangThai", "Đã đăng kí");
		        	break;
		        case Constants.TRANGTHAI_DAMUON: 
		        	tmp.put("TrangThai", "Đã nhận sách");
		        	break;
		        case Constants.TRANGTHAI_DATRA: 
		        	tmp.put("TrangThai", "Trả đủ sách");
		        	break;
		        case Constants.TRANGTHAI_TRATHIEU: 
		        	tmp.put("TrangThai", "Trả không đủ");
		        	break;
				}
				tmp.put("DatCoc", ttmt.getDatcoc());
				rs.add(tmp);
			}
		}
		return rs;
	}
	
	/**
	 * Lấy thông tin đăng ký mượn
	 * @param theMuon tham số  tìm kiếm
	 * @return Map chứa thông tin nếu tìm thấy
	 */
	public Map timKiemThongTinDangKyMuon(Map theMuon, int trangThai) {
		ThongTinMuonTra thongTinMuonTra = new ThongTinMuonTra();
		Map<String, Comparable> rs = new HashMap();
		rs.put("result", false);
		TheMuon tm = new TheMuon();
		tm.layThongTinThe(Integer.parseInt(theMuon.get("MaThe").toString()));
		if(thongTinMuonTra.layThongTinMuonTra(tm, trangThai)) {
			rs.put("result", true);
			rs.put("MaMuonTra", thongTinMuonTra.getMamuontra());
			rs.put("MaTheMuon", thongTinMuonTra.getThemuon().getMathe());
			String s1,s2,s3;
			s1 = formatter.format(thongTinMuonTra.getNgaydk());
			if (thongTinMuonTra.getNgaymuon() != null) s2 = formatter.format(thongTinMuonTra.getNgaymuon());
			else s2 = "";
			if (thongTinMuonTra.getHantra() != null) s3 = formatter.format(thongTinMuonTra.getHantra());
			else s3 = "";
			rs.put("NgayDangKi", s1);
			rs.put("NgayMuon", s2);
			rs.put("HanTra", s3);
			switch (thongTinMuonTra.getTrangthai()) {
	        case Constants.TRANGTHAI_DANGKYMUON: 
	        	rs.put("TrangThai", "Đã đăng kí");
	        	break;
	        case Constants.TRANGTHAI_DAMUON: 
	        	rs.put("TrangThai", "Đã nhận sách");
	        	break;
	        case Constants.TRANGTHAI_DATRA: 
	        	rs.put("TrangThai", "Trả đủ sách");
	        	break;
	        case Constants.TRANGTHAI_TRATHIEU: 
	        	rs.put("TrangThai", "Trả không đủ");
	        	break;
			}
			rs.put("DatCoc", thongTinMuonTra.getDatcoc());
		}	
		return rs;
	}
	
	
	/**
	 * cập nhật thông tin mượn trả và thông tin bản sao trong csdl khi cho bạn đọc mượn sách
	 * @param muonTra chứa thông tin mượn trả cần cập nhật: MaMuonTra, MaNhanVien, DatCoc
	 * @param dsCTMT danh sách chứa thông tin chi tiết mượn trả cập nhật: MaBanSao
	 * @return true nếu cập nhật thành công hoặc false nếu cập nhật thất bại
	 * @throws SQLException nếu có lỗi truy vấn sql
	 */
	public boolean capNhatThongTinMuon(Map<String, Comparable> muonTra, List<Map> dsCTMT) {
		boolean b = true;
		ThongTinMuonTra ttmt = new ThongTinMuonTra();
		ttmt.setMamuontra((int) muonTra.get("MaMuonTra"));
		ttmt.setTrangthai(Constants.TRANGTHAI_DAMUON);
		ttmt.setDatcoc((int) muonTra.get("DatCoc"));
		BanDoc bandoc = BanDoc.getInstance();
		ttmt.setMaNhanVien(bandoc.getMabandoc());
		
		if(ttmt.capNhatThongTinMuonTra()) {
			for(Map<String, Comparable> CTMT: dsCTMT) {
				ChiTietMuonTra ctmt = new ChiTietMuonTra();
				ctmt.setMamuontra((int) muonTra.get("MaMuonTra"));
				ctmt.setTrangthai(Constants.TRANGTHAI_DAMUON);
				BanSao banSao = new BanSao();
				banSao.setMaBanSao(CTMT.get("MaBanSao").toString());
				banSao.setTrangThai(Constants.TRANGTHAI_DAMUON);
				ctmt.setBanSao(banSao);
				if(!ctmt.capNhatTrangThaiCTMT()) {
					b = false;
				}
			}
		}
		
		return b;
	}
	
	/**
     *Hàm này để xác định sự tồn tại mã thẻ
     * @param masach
     * @return Map chứa thông thẻ có mã tương ứng
	 * @throws SQLException 
     */
    public Map timTheoMaThe(int maTheMuon) throws SQLException {
        TheMuon themuon = new TheMuon();
        Map<String, Object> rs = new HashMap();
        rs.put("result", false);
        if(themuon.layThongTinThe(maTheMuon)) {
        	rs.put("result", true);
        	rs.put("HoTen", themuon.getBandoc().getHoten());
        	rs.put("GioiTinh", themuon.getBandoc().getGioitinh()== 0?"Nam":"Nữ");
        	rs.put("SDT", themuon.getBandoc().getSdt());
        	rs.put("MaThe", themuon.getMathe());
        }
        return rs;
    }
    
    /**
	 * Lấy thông tin mượn
	 * @param mtID mã mượn trả
	 * @return ThongTinMuonTra nếu có thông tin or null nếu không có thông tin 	 
	 */
	public ThongTinMuonTra layThongTinMuon(int mtID) {
		return null;
	}
	
	/**
	 * Lấy tất cả thông tin mượn trả
	 * @param 
	 * @return ThongTinMuonTra[] nếu có thông tin or null nếu không có thông tin 	 
	 */
	public List<ThongTinMuonTra> layTatCaThongTinMuon() {
		return null;
	}
	
	/**
	 * Lấy thông tin mượn trả cần tìm kiếm
	 * @param mathe mã thẻ mượn int
	 * @param mabansao mã bản sao String
	 * @param ngaymuon ngày mượn String
	 * @param ngaytra ngày trả String
	 * @return List<Map>
	 * @throws SQLException nếu có lỗi truy vấn sql
	 */
	public List<Map> timKiemChiTiet(int mamuontra) throws SQLException {
		ChiTietMuonTra ctmt = new ChiTietMuonTra();
		List<Map> rs = new ArrayList<Map>();
		ArrayList<ChiTietMuonTra> dsct = ctmt.layChiTietMuonTra(mamuontra);
		if (dsct != null) {
			for (int i=0; i<dsct.size(); i++) {
				Map<String, Comparable> tmp = new HashMap();
				tmp.put("MaBanSao", dsct.get(i).getBanSao().getMaBanSao());
				tmp.put("Gia", dsct.get(i).getBanSao().getGia());
				switch (dsct.get(i).getTrangthai()) {
                case Constants.TRANGTHAI_DANGKYMUON: 
                	tmp.put("TrangThai", "Đăng kí mượn");
                	break;
                case Constants.TRANGTHAI_DAMUON:
                	tmp.put("TrangThai", "Đã mượn sách");
                	break;
                case Constants.TRANGTHAI_DATRA: 
                	tmp.put("TrangThai", "Đã trả sách");
                	break;
                case Constants.TRANGTHAI_MAT: 
                	tmp.put("TrangThai", "Mất sách");
                	break;
                case Constants.TRANGTHAI_HONG: 
                	tmp.put("TrangThai", "Hỏng sách");
                	break;
                }
				Sach sach = new Sach();
				sach.layThongTinSach(dsct.get(i).getBanSao().getMaSach());
				tmp.put("TenSach", sach.getTieude());
				tmp.put("TacGia", sach.getTacgia());
				String s1;
				if (dsct.get(i).getNgaytra() != null) s1 = formatter.format(dsct.get(i).getNgaytra());
				else s1 = "";
				tmp.put("NgayTra", s1);
				rs.add(tmp);
			}
		}
		return rs;
	}
}

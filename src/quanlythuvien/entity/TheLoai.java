package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//import com.mysql.cj.jdbc.ConnectionImpl;

public class TheLoai {
	ConnectDB connectDB = ConnectDB.getInstance();
	private final String SEARCH_TYPE_BY_ID = "select * from tbl_book_type where id = ?";
	private final String SEARCH_TYPE_BY_NAME = "select * from tbl_book_type where name = ?";
	private final String GET_ALL_TL = "select * from tbl_book_type";
	private final String ADD_TYPE = "insert into tbl_book_type (id,name,created_at) values (?,?,now())";
	private String matl;
	private String tentl;
	public String getMatl() {
		return matl;
	}
	public void setMatl(String matl) {
		this.matl = matl;
	}
	public String getTentl() {
		return tentl;
	}
	public void setTentl(String tentl) {
		this.tentl = tentl;
	}
	
	/**
	 * Phương thức lưu thông tin thể loại vào cơ sở dữ liệu
	 * @return true nếu thêm thể loại vào cơ sở dữ liệu thành công, false nếu thất bại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean luuTheLoai() throws SQLException {
		Connection conn = (Connection) connectDB.getConnection();
		PreparedStatement ps;
		ps = conn.prepareStatement(ADD_TYPE);
		ps.setString(1, matl);
		ps.setString(2, tentl);
		ps.executeUpdate();
		ps.close();
		conn.close();
		return true;
	}
	
	/**
	 * Phương thức lấy danh sách thể loại từ cơ sở dữ liệu
	 * @param 
	 * @return List<TheLoai> danh sách các đối tượng TheLoai duoc lay tu co so du lieu
	 * @exception SQLException khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<TheLoai> layDSTL() {
		ArrayList<TheLoai> l = new ArrayList<TheLoai>();
		Connection conn = (Connection) connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(GET_ALL_TL);
			ResultSet rs = ps.executeQuery();
	        while(rs.next()){
	        	TheLoai tl = new TheLoai();
	        	tl.setMatl(rs.getString("id"));
	        	tl.setTentl(rs.getString("name"));
	            l.add(tl);
	        }
	        ps.close();
	        conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}
	
	/**
	 * Phương thức lấy thông tin thể loại dựa vào mã
	 * @param matl mã thể loại String
	 * @return true nếu tồn tại thể loại có mã đó, false nếu ngược lại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean layThongTinTheLoai(String matl) throws SQLException {
		Connection conn = (Connection) connectDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(SEARCH_TYPE_BY_ID);
        ps.setString(1, matl);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();
        if(rs !=null && rs.next()){
            this.setMatl(rs.getString("id"));
            this.setTentl(rs.getString("name"));
            ps.close();
            conn.close();
            return true;
        }
		return false;
	}
	
	/**
	 * Phương thức lấy thông tin thể loại dựa vào ten
	 * @param tentl tên thể loại String
	 * @return true nếu tồn tại thể loại có tên đó, false nếu ngược lại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean timTheLoaiTheoTen(String tentl) throws SQLException {
		Connection conn = (Connection) connectDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(SEARCH_TYPE_BY_NAME);
        ps.setString(1, tentl);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();
        if(rs !=null && rs.next()){
            this.setMatl(rs.getString("id"));
            this.setTentl(rs.getString("name"));
            ps.close();
            conn.close();
            return true;
        }
		return false;
	}
}

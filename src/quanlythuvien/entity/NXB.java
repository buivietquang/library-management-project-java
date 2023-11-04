package quanlythuvien.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//import com.mysql.cj.jdbc.ConnectionImpl;

public class NXB {
	ConnectDB connectDB = ConnectDB.getInstance();
	private final String SEARCH_NXB_BY_ID = "select * from tbl_nxb where id = ?";
	private final String SEARCH_NXB_BY_NAME = "select * from tbl_nxb where name = ?";
	private final String GET_ALL_NXB = "select * from tbl_nxb";
	private final String ADD_NXB = "insert into tbl_nxb (id,name,address,fax,email,created_at) values (null,?,?,?,?,now())";
	private int manxb;
	private String tennxb;
	private String diachi;
	private String email;
	private String fax;
	public int getManxb() {
		return manxb;
	}
	public void setManxb(int manxb) {
		this.manxb = manxb;
	}
	public String getTennxb() {
		return tennxb;
	}
	public void setTennxb(String tennxb) {
		this.tennxb = tennxb;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	/**
	 * Phương thức lưu thông tin nhà xuất bản vào cơ sở dữ liệu
	 * @return true nếu thêm nhà xuất bản vào cơ sở dữ liệu thành công, false nếu thất bại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean luuNXB() throws SQLException {
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		ps = conn.prepareStatement(ADD_NXB);
		ps.setString(1, tennxb);
		ps.setString(2, diachi);
		ps.setString(3, fax);
		ps.setString(4, email);
		ps.executeUpdate();
		ps.close();
		conn.close();
		return true;
	}
	
	/**
	 * Phương thức lấy danh sách nhà xuất bản từ cơ sở dữ liệu
	 * @param 
	 * @return List<NXB> danh sách các đối tượng NXB được lấy từ cơ sở dữ liệu
	 * @exception SQLException khi kết nối với cơ sở dữ liệu
	 */
	public ArrayList<NXB> layDSNXB() {
		ArrayList<NXB> l = new ArrayList<NXB>();
		Connection conn = connectDB.getConnection();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(GET_ALL_NXB);
			ResultSet rs = ps.executeQuery();
	        while(rs.next()){
	        	NXB nxb = new NXB();
	        	nxb.setManxb(rs.getInt("id"));
	        	nxb.setTennxb(rs.getString("name"));
	        	nxb.setDiachi(rs.getString("address"));
	        	nxb.setEmail(rs.getString("email"));
	        	nxb.setFax(rs.getString("fax"));
	            l.add(nxb);
	        }
	        rs.close();
	        ps.close();
	        conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}
	
	/**
	 * Phương thức lấy thông tin nhà xuất bản dựa vào mã
	 * @param manxb mã nhà xuất bản int
	 * @return true nếu tồn tại nhà xuất bản có mã đó, false nếu ngược lại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean layThongTinNXB(int manxb) throws SQLException {
		Connection conn = connectDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(SEARCH_NXB_BY_ID);
        ps.setInt(1, manxb);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();
        if(rs !=null && rs.next()){
            this.setManxb(rs.getInt("id"));
            this.setTennxb(rs.getString("name"));
            this.setFax(rs.getString("fax"));
            this.setEmail(rs.getString("email"));
            this.setDiachi(rs.getString("address"));
            conn.close();
            return true;
        }
		return false;
	}
	
	/**
	 * Phương thức lấy thông tin nhà xuất bản dựa vào tên
	 * @param  tennxb tên nhà xuất bản String
	 * @return true nếu tồn tại nhà xuất bản có tên đó, false nếu ngược lại
	 * @throws SQLException khi kết nối với cơ sở dữ liệu
	 */
	public boolean timNXBTheoTen(String tennxb) throws SQLException {
		Connection conn = connectDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(SEARCH_NXB_BY_NAME);
        ps.setString(1, tennxb);
        ps.executeQuery();
        ResultSet rs = ps.executeQuery();
        if(rs !=null && rs.next()){
        	this.setManxb(rs.getInt("id"));
            this.setTennxb(rs.getString("name"));
            this.setFax(rs.getString("fax"));
            this.setEmail(rs.getString("email"));
            this.setDiachi(rs.getString("address"));
            conn.close();
            return true;
        }
		return false;
	}
}

package quanlythuvien.boundary;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

public class ViewUser extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewUser frame = new ViewUser();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public ViewUser() throws SQLException {
		setIconImage(Toolkit.getDefaultToolkit().getImage(qlThuVien.class.getResource("/Image/book.png")));
		setTitle("Quản lí thư viện của người dùng");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1200, 735);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1200, 730);
		contentPane.add(tabbedPane);
		
		XemDanhSachMuonSachForm panel = new XemDanhSachMuonSachForm();
		tabbedPane.addTab("Quản lý mượn trả", null, panel.getContentPane(), null);
		
		HuyDangKyMuonSachForm panel3 = new HuyDangKyMuonSachForm();
		tabbedPane.addTab("Danh sách sách đăng ký mươn", null, panel3.getContentPane(), null);
		
		DangKyMuonSachForm panel2 = new DangKyMuonSachForm();
		tabbedPane.addTab("Đăng ký mượn sách", null, panel2.getContentPane(), null);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}

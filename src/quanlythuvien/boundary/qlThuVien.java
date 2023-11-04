package quanlythuvien.boundary;
//main

import java.awt.EventQueue;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.JFrame;

public class qlThuVien extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				@SuppressWarnings("unused")
					qlThuVien frame = new qlThuVien();
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
	public qlThuVien() throws SQLException {
//		setIconImage(Toolkit.getDefaultToolkit().getImage(qlThuVien.class.getResource("/Image/book.png")));
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Image/book.png")));

		setTitle("Quản lí thư viện");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1184, 661);
		contentPane.add(tabbedPane);
		
		CRUDSachForm panel = new CRUDSachForm();
		tabbedPane.addTab("CRUD Sách", null, panel, null);
		
		MuonTraForm muonTraForm = new MuonTraForm();
		tabbedPane.addTab("Quản Lý Mượn Trả", null, muonTraForm, null);
		
		setLocationRelativeTo(null);
		setVisible(true);
	}

}

package quanlythuvien.boundary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import quanlythuvien.common.DangNhapController;

public class DangNhapForm extends JFrame{
	private JPanel contentPane;
	private JTextField tfEmail;
	private JPasswordField pwMatKhau;
	private JButton btnDangNhap, btnHuy;
	private DangNhapController dangNhapController;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DangNhapForm frame = new DangNhapForm();
					frame.setResizable(false);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DangNhapForm() {
		dangNhapController = DangNhapController.getInstance();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 464, 321);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(44, 62, 80));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(248, 148, 6));
		panel.setBounds(0, 0, 458, 62);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(2, 1, 0, 0));

		JLabel lblHThngQun = new JLabel("Hệ thống quản lý thư viện");
		lblHThngQun.setForeground(Color.WHITE);
		lblHThngQun.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblHThngQun.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblHThngQun);

		JLabel lblNguynTiTiu = new JLabel("Nhóm 2");
		lblNguynTiTiu.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNguynTiTiu.setForeground(Color.WHITE);
		lblNguynTiTiu.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNguynTiTiu);
		
		JLabel lblUserName = new JLabel("Email:");
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblUserName.setForeground(new Color(236, 240, 241));
		lblUserName.setBounds(32, 87, 96, 34);
		contentPane.add(lblUserName);
		
		JLabel lblPassword = new JLabel("Mật khẩu:");
		lblPassword.setForeground(new Color(236, 240, 241));
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPassword.setBounds(32, 137, 96, 34);
		contentPane.add(lblPassword);
		
		tfEmail = new JTextField();
		tfEmail.setForeground(new Color(228, 241, 254));
		tfEmail.setBackground(new Color(108, 122, 137));
		tfEmail.setBounds(127, 90, 207, 34);
		contentPane.add(tfEmail);
		tfEmail.setColumns(10);
		
		pwMatKhau = new JPasswordField();
		pwMatKhau.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String email = tfEmail.getText().toString();
					String matKhau = pwMatKhau.getText().toString();
					if(kiemTraDuLieu(email, matKhau)) {
						if(!dangNhapController.dangNhap(email, matKhau)) {
							
							JOptionPane.showMessageDialog(null, "Email hoặc Mật khẩu không đúng", "Error", JOptionPane.ERROR_MESSAGE);
						}else {
							setVisible(false);
						}
					}
					return;
				}
			}
		});
		pwMatKhau.setForeground(new Color(228, 241, 254));
		pwMatKhau.setBackground(new Color(108, 122, 137));
		pwMatKhau.setBounds(127, 140, 207, 34);
		contentPane.add(pwMatKhau);
		
		btnDangNhap = new JButton("Đăng nhập");
		btnDangNhap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = tfEmail.getText().toString();
				String matKhau = pwMatKhau.getText().toString();
				if(kiemTraDuLieu(email, matKhau)) {
					if(!dangNhapController.dangNhap(email, matKhau)) {
						JOptionPane.showMessageDialog(null, "Email hoặc Mật khẩu không đúng", "Error", JOptionPane.ERROR_MESSAGE);
					}else {
						setVisible(false);
					}
				}
				return;
			}
		});
		btnDangNhap.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDangNhap.setBackground(new Color(34, 167, 240));
		btnDangNhap.setForeground(new Color(255, 255, 255));
		btnDangNhap.setBounds(235, 193, 96, 34);
		contentPane.add(btnDangNhap);
		
		btnHuy = new JButton("Hủy");
		btnHuy.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnHuy.setForeground(new Color(255, 255, 255));
		btnHuy.setBackground(new Color(192, 57, 43));
		btnHuy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancel();
				return;
			}
		});
		btnHuy.setBounds(127, 193, 98, 34);
		contentPane.add(btnHuy);
		
		JLabel lblNhnVoy = new JLabel("Nhấn vào đây để đăng ký tài khoản");
		lblNhnVoy.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				
				return;
			}
		});
		lblNhnVoy.setForeground(new Color(255, 255, 255));
		lblNhnVoy.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		lblNhnVoy.setBounds(32, 250, 250, 31);
		contentPane.add(lblNhnVoy);
	}

	private void cancel() {
		tfEmail.setText("");
		pwMatKhau.setText("");
	}
	
	private boolean kiemTraDuLieu(String email, String matKhau) {
		if(email.equals("")) {
			JOptionPane.showMessageDialog(null, "Email trống");
			return false;
		}else if(matKhau.equals("")) {
			JOptionPane.showMessageDialog(null, "Mật khẩu trống");
			return false;
		}else {
			return true;
		}
	}
	
}

package quanlythuvien.boundary;


import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import quanlythuvien.librarian.controller.NXBController;

import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class ThemNXBForm extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CRUDSachForm sachform;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfTenNXB;
	private JTextField tfDiaChi;
	private JTextField tfFax;
	private JTextField tfEmail;
	private JLabel lbTenNXB,lbDiaChi,lbFax,lbEmail;
	public static final Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$");

	/**
	 * Create the dialog.
	 */
	public ThemNXBForm(CRUDSachForm form) {
		this.sachform = form;
		setTitle("Thêm nhà xuất bản");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ThemNXBForm.class.getResource("/Image/book.png")));
		setBounds(100, 100, 450, 315);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 434, 228);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lb1 = new JLabel("Tên nhà xuất bản: ");
		lb1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lb1.setBounds(27, 25, 116, 14);
		contentPanel.add(lb1);
		
		tfTenNXB = new JTextField();
		tfTenNXB.setBounds(153, 23, 251, 20);
		contentPanel.add(tfTenNXB);
		tfTenNXB.setColumns(10);
		
		lbTenNXB = new JLabel("");
		lbTenNXB.setForeground(Color.RED);
		lbTenNXB.setBounds(27, 49, 377, 14);
		contentPanel.add(lbTenNXB);
		
		JLabel lb2 = new JLabel("Địa chỉ:");
		lb2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lb2.setBounds(27, 74, 116, 14);
		contentPanel.add(lb2);
		
		tfDiaChi = new JTextField();
		tfDiaChi.setBounds(153, 72, 251, 20);
		contentPanel.add(tfDiaChi);
		tfDiaChi.setColumns(10);
		
		lbDiaChi = new JLabel("");
		lbDiaChi.setForeground(Color.RED);
		lbDiaChi.setBounds(27, 102, 377, 14);
		contentPanel.add(lbDiaChi);
		
		JLabel lb3 = new JLabel("Fax:");
		lb3.setFont(new Font("Tahoma", Font.BOLD, 12));
		lb3.setBounds(27, 127, 116, 14);
		contentPanel.add(lb3);
		
		tfFax = new JTextField();
		tfFax.setBounds(153, 125, 251, 20);
		contentPanel.add(tfFax);
		tfFax.setColumns(10);
		
		lbFax = new JLabel("");
		lbFax.setForeground(Color.RED);
		lbFax.setBounds(27, 155, 377, 14);
		contentPanel.add(lbFax);
		
		JLabel lb4 = new JLabel("Email:");
		lb4.setFont(new Font("Tahoma", Font.BOLD, 12));
		lb4.setBounds(27, 181, 116, 14);
		contentPanel.add(lb4);
		
		lbEmail = new JLabel("");
		lbEmail.setForeground(Color.RED);
		lbEmail.setBounds(27, 203, 377, 14);
		contentPanel.add(lbEmail);
		
		tfEmail = new JTextField();
		tfEmail.setBounds(153, 179, 251, 20);
		contentPanel.add(tfEmail);
		tfEmail.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 228, 434, 48);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							themNXB();
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "Lỗi xảy ra khi kết nối cơ sở dữ liệu","Error!", JOptionPane.ERROR_MESSAGE);
							e1.printStackTrace();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						resetInput();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 228);
		getContentPane().add(panel);
	}
	
	/**
     * Hàm set tất cả TextField về rỗng
     */
	public void resetInput() {
		tfTenNXB.setText("");
		tfDiaChi.setText("");
		tfFax.setText("");
		tfEmail.setText("");
		lbTenNXB.setText("");
		lbDiaChi.setText("");
		lbFax.setText("");
		lbEmail.setText("");
	}
	
	/**
     * Hàm thêm nhà xuất bản
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	public void themNXB() throws SQLException {
		if (kiemtraDuLieu()) {
			Map<String, String> nxb = new HashMap<String, String>();
			nxb.put("TenNXB", tfTenNXB.getText());
			nxb.put("DiaChi", tfDiaChi.getText());
			nxb.put("Fax", tfFax.getText());
			nxb.put("Email", tfEmail.getText());
			Map rs = NXBController.getInstance().themNXB(nxb);
			if ((boolean)rs.get("result") == true) themThanhCong();
			JOptionPane.showMessageDialog(null, rs.get("msg").toString());		
		}
	}
	
	/**
     * Hàm kiểm tra sự hợp lệ của dữ liệu nhập vào JTextField
     * @return nếu dữ liệu hợp lệ, false nếu ngược lại
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	public boolean kiemtraDuLieu() throws SQLException {
		String tennxb = tfTenNXB.getText();
		String diachi = tfDiaChi.getText();
		String fax = tfFax.getText();
		String email = tfEmail.getText();
		boolean hasError = false;
		Matcher matcher = VALID_EMAIL.matcher(email.toUpperCase());
		if (!matcher.find()) {
			hasError = true;
			lbEmail.setText("Email không hợp lệ");
		}
		else lbEmail.setText("");
		if (fax.isEmpty()) {
			hasError = true;
			lbFax.setText("Fax không hợp lệ");
		}
		else lbFax.setText("");
		if (diachi.isEmpty()) {
			hasError = true;
			lbDiaChi.setText("Địa chỉ không hợp lệ");
		}
		else lbDiaChi.setText("");
		if (tennxb.isEmpty()) {
			hasError = true;
			lbTenNXB.setText("Tên nhà xuất bản không hợp lệ");
		}
		else lbTenNXB.setText("");
		return !hasError;
	}
	
	/**
     * Hàm được gọi khi thêm thành công
     * Thực hiện việc load lại comboBox nhà xuất bản ở MuonTraForm
     */
	public void themThanhCong() throws SQLException {
		sachform.loadDataNXB();
		this.setVisible(false);
	}
}

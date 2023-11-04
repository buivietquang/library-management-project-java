package quanlythuvien.boundary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import quanlythuvien.librarian.controller.TheLoaiController;

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

public class ThemTheLoaiForm extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CRUDSachForm sachform;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfMaTL;
	private JTextField tfTenTL;
	private JLabel lbMaTL,lbTenTL;
	public static final Pattern VALID_THE_LOAI = Pattern.compile("^[A-Z]{2}$");

	/**
	 * Create the dialog.
	 */
	public ThemTheLoaiForm(CRUDSachForm form) {
		this.sachform = form;
		setIconImage(Toolkit.getDefaultToolkit().getImage(ThemTheLoaiForm.class.getResource("/Image/book.png")));
		setTitle("Thêm thể loại");
		setBounds(100, 100, 450, 248);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBounds(0, 0, 434, 169);
			contentPanel.add(panel);
			panel.setLayout(null);
			
			JLabel lb1 = new JLabel("Mã thể loại:");
			lb1.setFont(new Font("Tahoma", Font.BOLD, 12));
			lb1.setBounds(71, 55, 81, 14);
			panel.add(lb1);
			
			tfMaTL = new JTextField();
			tfMaTL.setBounds(177, 53, 181, 20);
			panel.add(tfMaTL);
			tfMaTL.setColumns(10);
			
			lbMaTL = new JLabel("");
			lbMaTL.setForeground(Color.RED);
			lbMaTL.setBounds(71, 80, 287, 14);
			panel.add(lbMaTL);
			
			JLabel lb2 = new JLabel("Tên thể loại");
			lb2.setFont(new Font("Tahoma", Font.BOLD, 12));
			lb2.setBounds(71, 105, 81, 14);
			panel.add(lb2);
			
			tfTenTL = new JTextField();
			tfTenTL.setBounds(177, 103, 181, 20);
			panel.add(tfTenTL);
			tfTenTL.setColumns(10);
			
			lbTenTL = new JLabel("");
			lbTenTL.setForeground(Color.RED);
			lbTenTL.setBounds(71, 130, 287, 14);
			panel.add(lbTenTL);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 168, 434, 41);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							themTheLoai();
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
	}
	
	/**
     * Hàm thêm thể loại
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	public void themTheLoai() throws SQLException {
		if (kiemtraDuLieu()) {
			Map<String, String> tl = new HashMap<String, String>();
			tl.put("MaTL", tfMaTL.getText());
			tl.put("TenTL", tfTenTL.getText());
			Map rs = TheLoaiController.getInstance().themTheLoai(tl);
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
		String matl = tfMaTL.getText();
		String tentl = tfTenTL.getText();
		Matcher matcher = VALID_THE_LOAI.matcher(matl);
		boolean hasError = false;
		if (!matcher.find()) {
			hasError = true;
			lbMaTL.setText("Mã thể loại không hợp lệ");
		}
		else lbMaTL.setText("");
		if (tentl.isEmpty()) {
			hasError = true;
			lbTenTL.setText("Tên thể loại không hợp lệ");
		} else lbTenTL.setText("");
		return !hasError;
	}
	
	/**
     * Hàm set tất cả TextField về rỗng
     */
	public void resetInput() {
		tfMaTL.setText("");
		tfTenTL.setText("");
		lbMaTL.setText("");
		lbTenTL.setText("");
	}
	
	/**
     * Hàm được gọi khi thêm thành công
     * Thực hiện việc load lại comboBox thể loại ở MuonTraForm
     */
	public void themThanhCong() throws SQLException {
		sachform.loadDataTL();
		this.setVisible(false);
	}
}

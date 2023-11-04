package quanlythuvien.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import quanlythuvien.common.Constants;
import quanlythuvien.librarian.controller.CRUDBanSaoController;

public class CRUDBanSaoForm extends JDialog{
	private JTable tblBanSao;
	private DefaultTableModel modelBanSao;
	private String[] titleCols = {"STT", "Mã Bản Sao", "Loại Bản Sao", "Trạng Thái", "Giá"};
	private String[] loaiBanSao  = {"Tham Khảo", "Cho Mượn"};
	private int[] loaiBS = {Constants.LOAIBS_THAMKHAO, Constants.LOAIBS_CHOMUON};
	private String[] timKiem = {"Loại Bản Sao", "Giá"};
	private String[][] data = new String[][] {};
	private JTextField tfSoLuong, tfGia;
	private JLabel lbMaSach, lbTenSach, lbTacGia, lbTheLoai, lbSoLuongBanSao, lbErSoLuong, lbErGia;
	private JComboBox<String> cbLoaiBanSao, cbTimKiem;
	private JButton btnTimKiem, btnThem, btnSua, btnXoa, btnLoadDuLieu, btnHuy;
	private Map<String, Comparable> thongTinSach;
	
	public CRUDBanSaoForm(Map<String, Comparable> thongTinSach) {
		this.thongTinSach = thongTinSach;
		setLayout(new  BorderLayout());
		add(createMainPanel());
		
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		
		hienThiDanhSachBanSao("", 0);
		setActions();
	}
	
	private void setActions() {
		btnTimKiem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbTimKiem.getSelectedIndex() == 0) {
					cancel();
					hienThiDanhSachBanSao("type", loaiBS[cbLoaiBanSao.getSelectedIndex()]);
				}
				if(cbTimKiem.getSelectedIndex() == 1) {
					String gia = tfGia.getText().toString();
					if(!BookHelper.validateGia(gia)) {
						JOptionPane.showMessageDialog(null, "Giá không hợp lệ");
					}else {
						cancel();
						hienThiDanhSachBanSao("price", Integer.parseInt(gia));
					}
				}
			}
		});
		
		btnThem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String soLuongThem = tfSoLuong.getText().toString().toUpperCase();
				String giaSach = tfGia.getText().toString();
				if(!kiemTraDuLieuHopLe(soLuongThem, giaSach)) {
					Map<String, Comparable> data = new HashMap<>();
					int gia = Integer.parseInt(giaSach);
					int loai = loaiBS[cbLoaiBanSao.getSelectedIndex()];
					data.put("maSach", thongTinSach.get("maSach"));
					data.put("loaiBanSao", loai);
					data.put("trangThai", Constants.TRANGTHAI_COSAN);
					data.put("gia", gia);
					data.put("soLuong", Integer.parseInt(soLuongThem));
					if(CRUDBanSaoController.getInstance().themBanSao(data)) {
						cancel();
						hienThiDanhSachBanSao("", 0);
						JOptionPane.showMessageDialog(null, "Thêm bản sao thành công");
					}else {
						JOptionPane.showMessageDialog(null, "Thêm bản sao thất bại", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		btnLoadDuLieu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
				hienThiDanhSachBanSao("", 0);
			}
		});
		
		tblBanSao.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int row = tblBanSao.getSelectedRow();
				if(row >= 0) {
					tfGia.setText((String) tblBanSao.getValueAt(row, 4));
					if(((String) tblBanSao.getValueAt(row, 2)).equals("Tham khảo")) {
						cbLoaiBanSao.setSelectedIndex(0);
					}else {
						cbLoaiBanSao.setSelectedIndex(1);
					}
				}
			}
		});
		
		btnXoa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tblBanSao.getSelectedRow();
				if(row >= 0) {
					int accept = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa bản sao này?", "Delete",
							JOptionPane.YES_NO_OPTION);
					if(accept == 0) {
						if(CRUDBanSaoController.getInstance().xoaBanSao((String) tblBanSao.getValueAt(row, 1))) {
							hienThiDanhSachBanSao("", 0);
							JOptionPane.showMessageDialog(null, "Xóa thành công");
						}else {
							JOptionPane.showMessageDialog(null, "Bản sao đang trong hoạt động mượn sách, không thể xóa", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}else {
					JOptionPane.showMessageDialog(null, "Chưa có bản sao được chọn");
				}
				
			}
		});
		
		btnSua.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tblBanSao.getSelectedRow();
				if(row >= 0) {
					String giaSach = tfGia.getText().toString();
					if(BookHelper.validateGia(giaSach)) {
						int gia = Integer.parseInt(giaSach);
						int loai = loaiBS[cbLoaiBanSao.getSelectedIndex()];
						Map<String, Comparable> data = new HashMap<>();
						data.put("maBanSao", (String) tblBanSao.getValueAt(row, 1));
						data.put("loaiBanSao", loai);
						data.put("gia", gia);
						if(CRUDBanSaoController.getInstance().capNhatBanSao(data)) {
							cancel();
							hienThiDanhSachBanSao("", 0);
							JOptionPane.showMessageDialog(null, "Cập nhật bản sao thành công");
						}else {
							JOptionPane.showMessageDialog(null, "Cập nhật bản sao thất bại", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						JOptionPane.showMessageDialog(null, "Giá sách không hợp lệ");
					}
				}else {
					JOptionPane.showMessageDialog(null, "Chưa có bản sao được chọn");
				}
				
			}
		});
		
		btnHuy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
				tblBanSao.getSelectionModel().clearSelection();
			}
		});
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
		panel.add(createHeaderPanel());
		panel.add(createTablePanel());
		panel.setBorder(new EmptyBorder(10, 50, 10, 50));
		return panel;
	}

	private JPanel createHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createTitlePanel(), BorderLayout.PAGE_START);
		panel.add(createHeaderMainPanel(), BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel createTitlePanel() {
		JPanel panel = new JPanel();
		JLabel lblNewLabel = new JLabel("Quản lí Bản Sao");
		lblNewLabel.setIcon(new ImageIcon(this.getClass().getResource("/Image/icon.png")));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(50, 11, 217, 31);
		panel.add(lblNewLabel);
		return panel;
	}

	private JPanel createHeaderMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createHeaderLeft(), BorderLayout.CENTER);
		panel.add(createHeaderRight(), BorderLayout.EAST);
		
		return panel;
	}

	private JPanel createHeaderLeft() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
		panel.add(createThongTinSach());
		panel.add(createThongTinBanSao());
		return panel;
	}

	private JPanel createThongTinSach() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel1 = new JPanel(new GridLayout(1, 2, 20, 5));
		
		JPanel panelL = new JPanel(new BorderLayout());
		JPanel panelL1 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelL1.add(new JLabel("Mã Sách: "));
		panelL1.add(new JLabel("Tên Sách: "));
		JPanel panelL2 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelL2.add(lbMaSach = new JLabel(thongTinSach.get("maSach").toString()));
		panelL2.add(lbTenSach = new JLabel(thongTinSach.get("tieuDe").toString()));
		panelL.add(panelL1, BorderLayout.WEST);
		panelL.add(panelL2, BorderLayout.CENTER);
		
		JPanel panelR = new JPanel(new BorderLayout());
		JPanel panelR1 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelR1.add(new JLabel("Tác Giả: "));
		panelR1.add(new JLabel("Thể Loại: "));
		JPanel panelR2 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelR2.add(lbTacGia = new JLabel(thongTinSach.get("tacGia").toString()));
		panelR2.add(lbTheLoai = new JLabel(thongTinSach.get("theLoai").toString()));
		panelR.add(panelR1, BorderLayout.WEST);
		panelR.add(panelR2, BorderLayout.CENTER);
		
		panel1.add(panelL, BorderLayout.WEST);
		panel1.add(panelR, BorderLayout.CENTER);
		panel.add(panel1);
		panel1.setBorder(new EmptyBorder(5, 25, 10, 25));
		panel.setBorder(new TitledBorder("Thông Tin Sách"));
		
		return panel;
	}

	private JPanel createThongTinBanSao() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel1 = new JPanel(new GridLayout(1, 2, 20, 5));
		
		JPanel panelL = new JPanel(new BorderLayout());
		JPanel panelL1 = new JPanel(new GridLayout(4, 1, 0, 0));
		panelL1.add(new JLabel("Số lượng: "));
		panelL1.add(new JLabel());
		panelL1.add(new JLabel("Giá: "));
		JPanel panelL2 = new JPanel(new GridLayout(4, 1, 0, 0));
		panelL2.add(tfSoLuong = new JTextField());
		panelL2.add(lbErSoLuong = new JLabel());
		lbErSoLuong.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbErSoLuong.setForeground(Color.RED);
		panelL2.add(tfGia = new JTextField());
		panelL2.add(lbErGia = new JLabel());
		lbErGia.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbErGia.setForeground(Color.RED);
		panelL.add(panelL1, BorderLayout.WEST);
		panelL.add(panelL2, BorderLayout.CENTER);
		
		JPanel panelR = new JPanel(new BorderLayout());
		JPanel panelR1 = new JPanel(new GridLayout(4, 1, 0, 0));
		panelR1.add(new JLabel("Loại Bản Sao: "));
		JPanel panelR2 = new JPanel(new GridLayout(4, 1, 0, 0));
		panelR2.add(cbLoaiBanSao = new JComboBox<>(loaiBanSao));
		cbLoaiBanSao.setSelectedIndex(0);
		panelR.add(panelR1, BorderLayout.WEST);
		panelR.add(panelR2, BorderLayout.CENTER);
		
		panel1.add(panelL, BorderLayout.WEST);
		panel1.add(panelR, BorderLayout.CENTER);
		panel.add(panel1);
		panel1.setBorder(new EmptyBorder(0, 25, 0, 25));
		panel.setBorder(new TitledBorder("Thông Tin Bản Sao"));
		
		return panel;
	}

	private JPanel createHeaderRight() {
		JPanel panelMain = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		JPanel panel1 = new JPanel(new BorderLayout(5, 5));
		JPanel panel11 = new JPanel(new BorderLayout(5, 5));
		panel11.add(new JLabel("Tìm kiếm theo: "), BorderLayout.WEST);
		panel11.add(cbTimKiem = new JComboBox<>(timKiem), BorderLayout.CENTER);
		panel1.add(panel11, BorderLayout.CENTER);
		panel1.add(btnTimKiem = new JButton(), BorderLayout.EAST);
		btnTimKiem.setIcon(new ImageIcon(this.getClass().getResource("/Image/find.png")));
		JPanel panel2 = new JPanel(new GridLayout(2, 2, 5, 5));
		panel2.add(btnThem = new JButton("Thêm"));
		panel2.add(btnSua = new JButton("Cập nhật"));
		panel2.add(btnXoa = new JButton("Xóa"));
		panel2.add(btnLoadDuLieu = new JButton("Load lại bảng"));
		JPanel panel3 = new JPanel(new BorderLayout());
		panel3.add(btnHuy = new JButton("Hủy"), BorderLayout.CENTER);
		panel.add(panel1, BorderLayout.PAGE_START);
		panel.add(panel2, BorderLayout.CENTER);
		panel.add(panel3, BorderLayout.PAGE_END);
		panel.setBorder(new EmptyBorder(5, 10, 65, 10));
		panelMain.add(panel);
		panelMain.setPreferredSize(new Dimension(320, 0));
		panelMain.setBorder(new TitledBorder("Tác Vụ"));
		return panelMain;
	}

	private JPanel createTablePanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		JPanel panelB = new JPanel();
		JLabel lbTong = new JLabel("Tổng Số Bản Sao: ");
		lbTong.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbSoLuongBanSao =  new JLabel("0");
		lbSoLuongBanSao.setFont(new Font("Tahoma", Font.BOLD, 13));
		panelB.add(lbTong);
		panelB.add(lbSoLuongBanSao);
		FlowLayout layoutC = (FlowLayout) panelB.getLayout();
		layoutC.setAlignment(FlowLayout.LEFT);
		
		panel.add(new JScrollPane(tblBanSao =  new JTable()), BorderLayout.CENTER);
		modelBanSao = new DefaultTableModel(data, titleCols) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblBanSao.setModel(modelBanSao);
		tblBanSao.getColumnModel().getColumn(0).setPreferredWidth(18);
		panel.add(panelB, BorderLayout.PAGE_END);
		
		return panel;
	}

	/**
	 * kiểm tra dữ liệu nhập vào khi thêm bản sao
	 * @return true nếu dữ liệu hợp lệ(có dữ liệu) hoặc false nếu dữ liệu không hợp lệ*/
	public boolean kiemTraDuLieuHopLe(String soLuongThem, String giaSach) {
		boolean hasError = false;
		
		if(!BookHelper.validateGia(soLuongThem)) {
			lbErSoLuong.setText("Số lượng bản sao thêm không hợp lệ");
			hasError = true;
		}else {
			lbErSoLuong.setText("");
		}
		if(!BookHelper.validateGia(giaSach)) {
			lbErGia.setText("Giá sách không hợp lệ");
			hasError = true;
		}else {
			lbErGia.setText("");
		}
		return hasError;
	}

	/**
	 * hiển thị danh sách bản sao lên bảng
	 * */
	public void hienThiDanhSachBanSao(String cot, int giaTri) {
		Map<String, Comparable> data = new HashMap<>();
		data.put("maSach", thongTinSach.get("maSach").toString());
		data.put("cot", cot);
		data.put("giaTri", giaTri);
		List<Map> dsBanSao = CRUDBanSaoController.getInstance().layDanhSachBanSao(data);
		modelBanSao.setRowCount(0);
		for(int i = 0; i < dsBanSao.size(); i++) {
			Map bs = dsBanSao.get(i);
			Vector<String> row = new Vector<>();
			row.add(String.valueOf(i + 1));
			row.add(bs.get("maBanSao").toString());
			if((int) bs.get("loaiBanSao") == Constants.LOAIBS_CHOMUON) {
				row.add("Cho mượn");
			}else {
				row.add("Tham khảo");
			}
			switch ((int) bs.get("trangThai")) {
				case Constants.TRANGTHAI_COSAN:
					row.add("Có sẵn");
					break;
				case Constants.TRANGTHAI_DANGKYMUON:
					row.add("Đã đăng ký");
					break;
				case Constants.TRANGTHAI_DAMUON:
					row.add("Đã mượn");
					break;
				case Constants.TRANGTHAI_MAT:
					row.add("Mất");
					break;
				case Constants.TRANGTHAI_HONG:
					row.add("Hư hỏng");
					break;
				default:
					row.add("");
					break;
			}
			row.add(String.valueOf(bs.get("gia")));
			
	        modelBanSao.addRow(row);
		}
		
		lbSoLuongBanSao.setText(String.valueOf(dsBanSao.size()));
	}
	
	/**
	 * Xóa dữ liệu dầu vào
	 * */
	private void cancel() {
		tfSoLuong.setText("");
		lbErSoLuong.setText("");
		tfGia.setText("");
		lbErGia.setText("");
	}
	
	public static void main(String[] args) {
		Map<String, Comparable> thongTinSach = new HashMap<>();
		thongTinSach.put("maSach", "AN0001");
		thongTinSach.put("tieuDe", "Âm nhạc các dân tộc Việt Nam");
		thongTinSach.put("tacGia", "Hoàng Hiên");
		thongTinSach.put("theLoai", "Am nhac");
		CRUDBanSaoForm banSaoForm = new CRUDBanSaoForm(thongTinSach);
	}
}

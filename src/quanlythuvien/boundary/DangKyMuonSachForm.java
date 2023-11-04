package quanlythuvien.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import quanlythuvien.entity.BanSao;
import quanlythuvien.entity.Sach;
import quanlythuvien.entity.TheLoai;
import quanlythuvien.user.controller.DangKyMuonController;

import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DangKyMuonSachForm extends JFrame {
	private DangKyMuonController dang_ky_controller = new DangKyMuonController();
	private JScrollPane sp_tbl_sach, sp_tbl_ban_sao;
	private static DangKyMuonSachForm dang_ky_form = DangKyMuonSachForm.getInstance();
	private JPanel contentPane;
	private JTextField tf_book_information;
	private JTable tbl_sach;
	private JComboBox<String> cb_book_type;
	private JTable tbl_ban_sao;
	private ArrayList<TheLoai> list_theloai;
	private String[] columnNames = new String[] { "Mã sách", "Tiêu đề", "Tác giả", "Thể loại", "Nhà xuất bản",
			"Năm xuất bản" };
	private JTextField tf_search_id;
	private String maSach = "";
	private String maBanSao = "";
	private JPanel panel_4;
	private JButton btn_xem_ds_dky_muon;

	/**
	 * Create the frame.
	 */
	public DangKyMuonSachForm() {
		setTitle("Đăng ký mượn sách");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1146, 712);
		setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/Image/book.png")));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBounds(30, 25, 200, 80);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(2, 2, 5, 5));

		JButton btnNewButton_1 = new JButton("Tìm kiếm theo loại sách");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(cb_book_type.getSelectedItem() + "  1");
				loadBangSachTheoLoai((String) cb_book_type.getSelectedItem());
			}
		});
		panel.add(btnNewButton_1);

		JButton btnNewButton = new JButton("Tìm kiếm theo thông tin sách");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadBangSachTheoTieuDeSach(tf_book_information.getText());
			}
		});
		panel.add(btnNewButton);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(257, 25, 268, 80);
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(2, 2, 5, 5));

		cb_book_type = new JComboBox<String>();
		cb_book_type.setMaximumRowCount(30);
		panel_1.add(cb_book_type);

		// add data for combobox
		this.loadDataTL();

		tf_book_information = new JTextField();
		panel_1.add(tf_book_information);
		tf_book_information.setColumns(10);

		tbl_sach = new JTable();
		tbl_sach.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = tbl_sach.getSelectedRow();
				if (row >= 0) {
					maSach = (String) tbl_sach.getValueAt(row, 0);
					maBanSao = "";
					loadBangBanSao(maSach);
				} else {
					JOptionPane.showMessageDialog(null, "Chọn dòng không hợp lệ");
				}
			}
		});
		tbl_sach.setBounds(30, 151, 492, 482);
		this.loadBangSach();

		// scroll
		sp_tbl_sach = new JScrollPane(tbl_sach);
		sp_tbl_sach.setBounds(30, 151, 495, 485);
		contentPane.add(sp_tbl_sach);

		tbl_ban_sao = new JTable();
		tbl_ban_sao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tbl_ban_sao.getSelectedRow();
				System.out.println(row);
				if (row >= 0) {
					maBanSao = (String) tbl_ban_sao.getValueAt(row, 0);
				} else {
					JOptionPane.showMessageDialog(null, "Chọn dòng không hợp lệ");
				}
			}
		});
		tbl_ban_sao.setBounds(600, 151, 492, 482);

		// scroll
		sp_tbl_ban_sao = new JScrollPane(tbl_ban_sao);
		sp_tbl_ban_sao.setBounds(600, 151, 495, 485);
		contentPane.add(sp_tbl_ban_sao);

		JButton btn_dang_ky_muon = new JButton("Đăng ký mượn sách");
		btn_dang_ky_muon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// check input of ban sao
				if (maBanSao == "") {
					JOptionPane.showMessageDialog(null, "Bạn chưa chọn bản sao muốn đăng ký mượn");
				} else {
					int confirm = JOptionPane.showConfirmDialog(null,
							"Bạn có chắc là muốn mượn cuốn sách với mã: " + maBanSao);
					if (confirm == 0) {
						if(dang_ky_controller.dangKyMuonSach(maBanSao)){
							loadBangBanSao(maSach);
							JOptionPane.showMessageDialog(null, "Đăng ký mượn sách thành công");
						}else {
							JOptionPane.showMessageDialog(null, "Đăng ký mượn sách có lỗi");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Huy thanh cong");
					}
				}
			}
		});
		btn_dang_ky_muon.setBounds(817, 644, 150, 23);
		contentPane.add(btn_dang_ky_muon);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(600, 25, 200, 34);
		contentPane.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));

		JButton btn_search_id = new JButton("Tìm kiếm theo mã sách");
		btn_search_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadBangSachTheoMaSach(tf_search_id.getText());
			}
		});
		btn_search_id.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel_2.add(btn_search_id);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(826, 25, 265, 34);
		contentPane.add(panel_3);
		panel_3.setLayout(new GridLayout(1, 0, 0, 0));

		tf_search_id = new JTextField();
		panel_3.add(tf_search_id);
		tf_search_id.setColumns(10);
		
		panel_4 = new JPanel();
		panel_4.setBounds(600, 70, 200, 34);
		contentPane.add(panel_4);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));
		
		btn_xem_ds_dky_muon = new JButton("Sách đã đăng ký mượn");
		btn_xem_ds_dky_muon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HuyDangKyMuonSachForm.getInstance().loadBangSachDaDangKyMuon();
				HuyDangKyMuonSachForm.getInstance().setVisible(true);
				dang_ky_form.setVisible(false);
			}
		});
		btn_xem_ds_dky_muon.setFont(new Font("Tahoma", Font.BOLD, 11));
//		panel_4.add(btn_xem_ds_dky_muon);
	}

	public static DangKyMuonSachForm getInstance() {
		if (dang_ky_form == null) {
			dang_ky_form = new DangKyMuonSachForm();
		}
		return dang_ky_form;
	}

	private void loadDataTL() {
		TheLoai tl = new TheLoai();
		ArrayList<String> l = new DangKyMuonController().get_book_type();
		if (l != null) {
			for (String theLoai : l) {
				cb_book_type.addItem(theLoai);
			}
		}
	}

	private void loadBangSach() {
		DangKyMuonController dangKyMuon = new DangKyMuonController();
		ArrayList<ArrayList<String>> listbooks = dangKyMuon.layTatCaSach();
		if (listbooks != null) {
			Vector<String> colunm, row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã sách", "Tiêu đề", "Tác giả", "Thể loại", "Nhà xuất bản",
					"Năm xuất bản" };
			numberColumn = columnNames.length;
			for (int i = 0; i < numberColumn; i++) {
				colunm.add(columnNames[i]);
			}
			model.setColumnIdentifiers(colunm);
			for (ArrayList<String> tmp : listbooks) {
				row = new Vector<String>();
				row.add(tmp.get(0));
				row.add(tmp.get(1));
				row.add(tmp.get(2));
				row.add(tmp.get(3));
				row.add(tmp.get(4));
				row.add(tmp.get(5));
				row.add(tmp.get(6));
				model.addRow(row);
			}
			tbl_sach.setModel(model);
		}
		return;
	}

	private void loadBangSachTheoLoai(String theloai) {
		DangKyMuonController dangKyMuon = new DangKyMuonController();
		ArrayList<ArrayList<String>> listbooks = dangKyMuon.laySachTheoLoaiSach(theloai);

		if (listbooks != null) {
			Vector<String> colunm, row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã sách", "Tiêu đề", "Tác giả", "Thể loại", "Nhà xuất bản",
					"Năm xuất bản" };
			numberColumn = columnNames.length;
			for (int i = 0; i < numberColumn; i++) {
				colunm.add(columnNames[i]);
			}
			model.setColumnIdentifiers(colunm);
			for (ArrayList<String> tmp : listbooks) {
				row = new Vector<String>();
				row.add(tmp.get(0));
				row.add(tmp.get(1));
				row.add(tmp.get(2));
				row.add(tmp.get(3));
				row.add(tmp.get(4));
				row.add(tmp.get(5));
				row.add(tmp.get(6));
				model.addRow(row);
			}
			tbl_sach.setModel(model);
		}
		return;
	}

	/*
	 * lấy danh sách sách theo title của sách
	 */
	private void loadBangSachTheoTieuDeSach(String title) {
		DangKyMuonController dangKyMuon = new DangKyMuonController();
		ArrayList<ArrayList<String>> listbooks = dangKyMuon.laySachTheoTieuDeSach(title);

		if (listbooks != null) {
			Vector<String> colunm, row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã sách", "Tiêu đề", "Tác giả", "Thể loại", "Nhà xuất bản",
					"Năm xuất bản" };
			numberColumn = columnNames.length;
			for (int i = 0; i < numberColumn; i++) {
				colunm.add(columnNames[i]);
			}
			model.setColumnIdentifiers(colunm);
			for (ArrayList<String> tmp : listbooks) {
				row = new Vector<String>();
				row.add(tmp.get(0));
				row.add(tmp.get(1));
				row.add(tmp.get(2));
				row.add(tmp.get(3));
				row.add(tmp.get(4));
				row.add(tmp.get(5));
				row.add(tmp.get(6));
				model.addRow(row);
			}
			tbl_sach.setModel(model);
		}
		return;
	}

	/*
	 * lấy danh sách sách theo id của sách
	 */
	private void loadBangSachTheoMaSach(String id) {
		DangKyMuonController dangKyMuon = new DangKyMuonController();
		ArrayList<ArrayList<String>> listbooks = dangKyMuon.laySachTheoIdSach(id);

		if (listbooks != null) {
			Vector<String> colunm, row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã sách", "Tiêu đề", "Tác giả", "Thể loại", "Nhà xuất bản",
					"Năm xuất bản" };
			numberColumn = columnNames.length;
			for (int i = 0; i < numberColumn; i++) {
				colunm.add(columnNames[i]);
			}
			model.setColumnIdentifiers(colunm);
			for (ArrayList<String> tmp : listbooks) {
				row = new Vector<String>();
				row.add(tmp.get(0));
				row.add(tmp.get(1));
				row.add(tmp.get(2));
				row.add(tmp.get(3));
				row.add(tmp.get(4));
				row.add(tmp.get(5));
				row.add(tmp.get(6));
				model.addRow(row);
			}
			tbl_sach.setModel(model);
		}
		return;
	}

	/*
	 * lấy danh sách bản sao của sách theo id của sách
	 */
	private void loadBangBanSao(String idSach) {
		DangKyMuonController dangKyMuon = new DangKyMuonController();
		ArrayList<ArrayList<String>> listbooks = dangKyMuon.layBanSaoTheoIdSach(idSach);

		if (listbooks != null) {
			Vector<String> colunm;
			Vector<Object> row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã bản sao", "Tác giả", "Tiêu đề", "Giá" };
			numberColumn = columnNames.length;
			for (int i = 0; i < numberColumn; i++) {
				colunm.add(columnNames[i]);
			}
			model.setColumnIdentifiers(colunm);
			for (ArrayList<String> tmp : listbooks) {
				row = new Vector<Object>();
				row.add(tmp.get(0));
				row.add(tmp.get(1));
				row.add(tmp.get(2));
				row.add(tmp.get(3));
				model.addRow(row);
			}
			tbl_ban_sao.setModel(model);
		}
		return;
	}

}

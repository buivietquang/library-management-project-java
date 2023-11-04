package quanlythuvien.boundary;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import quanlythuvien.user.controller.HuyDangKyMuonController;

public class HuyDangKyMuonSachForm extends JFrame {
	private HuyDangKyMuonController huyDangKyMuon = HuyDangKyMuonController.getInstance();
	private static HuyDangKyMuonSachForm me = HuyDangKyMuonSachForm.getInstance();
	private JPanel contentPane;
	private JButton btnXemDanhSachSach;
	private JButton btnHuyDangKyMuon;
	private JTable tbl_sach_da_muon;
	private JScrollPane sp_tbl_sach_da_muon;
	private String copyID, mtID;
	private JButton btnLoadLai;

	/**
	 * Create the frame.
	 */
	public HuyDangKyMuonSachForm() {
		setTitle("Danh sách sách đăng ký mượn");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1146, 700);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 30, 850, 700);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		tbl_sach_da_muon = new JTable();
		tbl_sach_da_muon.setToolTipText("Danh sách sách đã đăng ký mượn");
		sp_tbl_sach_da_muon = new JScrollPane(tbl_sach_da_muon);
		tbl_sach_da_muon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				int row = tbl_sach_da_muon.getSelectedRow();
				if (row >= 0) {
					copyID = (String) tbl_sach_da_muon.getValueAt(row, 0);
					mtID = (String) tbl_sach_da_muon.getValueAt(row, 6);
				} else {
					JOptionPane.showMessageDialog(null, "Chọn dòng không hợp lệ");
				}
			}
		});
		loadBangSachDaDangKyMuon();
		panel.add(sp_tbl_sach_da_muon);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(875, 100, 150, 150);
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(3, 1, 10, 10));

		btnHuyDangKyMuon = new JButton("Hủy mượn sách");
		btnHuyDangKyMuon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tbl_sach_da_muon.getSelectedRow() >= 0) {
					boolean check_delete = huyDangKyMuon.huyMuon(copyID, mtID);
					if (check_delete) {
						loadBangSachDaDangKyMuon();
						JOptionPane.showMessageDialog(null, "Xóa đăng ký mượn sách thành công");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng cần xóa");
				}
			}
		});
		panel_1.add(btnHuyDangKyMuon);

		btnXemDanhSachSach = new JButton("Tìm sách");
		btnXemDanhSachSach.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DangKyMuonSachForm.getInstance().setVisible(true);
				me.setVisible(false);
			}
		});
//		panel_1.add(btnXemDanhSachSach);

		btnLoadLai = new JButton("Load lại bảng");
		btnLoadLai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadBangSachDaDangKyMuon();
			}
		});
		panel_1.add(btnLoadLai);
		
		JLabel lb_table = new JLabel("Bảng danh sách sách đã đăng ký mượn");
		lb_table.setForeground(Color.RED);
		lb_table.setFont(new Font("Tahoma", Font.BOLD, 11));
		lb_table.setBounds(10, 11, 300, 20);
		contentPane.add(lb_table);
	}

	/*
	 * lấy danh sách đăng ký mượn sách của người dùng đã đăng nhập
	 */
	public void loadBangSachDaDangKyMuon() {
		ArrayList<ArrayList<String>> listbooks = huyDangKyMuon.layDanhSachDangKyMuon();
		if (listbooks != null) {
			Vector<String> colunm, row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã bản sao", "Mã sách", "Tiêu đề", "Tác giả", "Giá",
					"Thời gian mượn", "Mã mượn trả" };
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
			tbl_sach_da_muon.setModel(model);
		}
		return;
	}

	public static HuyDangKyMuonSachForm getInstance() {
		if (me == null) {
			me = new HuyDangKyMuonSachForm();
		}
		return me;
	}

}

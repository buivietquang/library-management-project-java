package quanlythuvien.boundary;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import quanlythuvien.user.controller.XemDanhSachController;

public class XemDanhSachMuonSachForm extends JFrame {

	private JPanel contentPane;
	private JTable tbl_hien_thi;
	private static XemDanhSachMuonSachForm me = XemDanhSachMuonSachForm.getInstance();
	private XemDanhSachController xemDSController = new XemDanhSachController();
	private JScrollPane sp_tbl_hien_thi;
	private JLabel lb_table;

	/**
	 * Create the frame.
	 */
	public XemDanhSachMuonSachForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 30, 850, 700);
		contentPane.add(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		tbl_hien_thi = new JTable();
		sp_tbl_hien_thi = new JScrollPane(tbl_hien_thi);
		loadBangSachChuaTra();
		panel.add(sp_tbl_hien_thi);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(875, 100, 150, 150);
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(3, 1, 10, 10));

		JButton btn_ds_muon = new JButton("Sách chưa trả");
		btn_ds_muon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lb_table.setText("Bảng danh sách các sách chưa trả");
				loadBangSachChuaTra();
			}
		});
		panel_1.add(btn_ds_muon);

		JButton btn_ds_tra = new JButton("Sách đã trả");
		btn_ds_tra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lb_table.setText("Bảng danh sách các sách đã trả");
				loadBangSachDaTra();
			}
		});
		panel_1.add(btn_ds_tra);

		JButton btn_ds_dky_muon = new JButton("Sách đăng ký mượn");
		btn_ds_dky_muon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HuyDangKyMuonSachForm.getInstance().setVisible(true);
				XemDanhSachMuonSachForm.getInstance().setVisible(false);
			}
		});
//		panel_1.add(btn_ds_dky_muon);

		lb_table = new JLabel("Bảng danh sách các sách chưa trả");
		lb_table.setForeground(Color.RED);
		lb_table.setFont(new Font("Tahoma", Font.BOLD, 11));
		lb_table.setBounds(10, 11, 200, 20);
		contentPane.add(lb_table);
	}

	/**
	 * Load dữ liệu danh sách mượn của bạn đọc truy cập vào cho bảng 
	 */
	public void loadBangSachChuaTra() {
		ArrayList<ArrayList<String>> listbooks = xemDSController.loadBangChuaTra();
		if (listbooks != null) {
			Vector<String> colunm, row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã bản sao", "Mã sách", "Tiêu đề", "Tác giả", "Giá",
					 "Mã mượn trả","Thời gian mượn", "Hạn trả" };
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
				row.add(tmp.get(7));

				model.addRow(row);
			}
			tbl_hien_thi.setModel(model);
		}
		return;
	}
	
	/**
	 * Load dữ liệu danh sách đã trả của bạn đọc truy cập vào cho bảng 
	 */
	public void loadBangSachDaTra() {
		ArrayList<ArrayList<String>> listbooks = xemDSController.loadBangDaTra();
		if (listbooks != null) {
			Vector<String> colunm, row;
			DefaultTableModel model = new DefaultTableModel();
			colunm = new Vector<String>();
			int numberColumn;
			String[] columnNames = new String[] { "Mã bản sao", "Mã sách", "Tiêu đề", "Tác giả", "Giá",
					 "Mã mượn trả","Thời gian trả", "Hạn trả" };
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
				row.add(tmp.get(7));

				model.addRow(row);
			}
			tbl_hien_thi.setModel(model);
		}
		return;
	}

	public static XemDanhSachMuonSachForm getInstance() {
		if (me == null) {
			me = new XemDanhSachMuonSachForm();
		}
		return me;
	}
}

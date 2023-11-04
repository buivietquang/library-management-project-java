package quanlythuvien.boundary;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import quanlythuvien.entity.TheLoai;
import quanlythuvien.librarian.controller.CRUDSachController;
import quanlythuvien.librarian.controller.NXBController;
import quanlythuvien.librarian.controller.TheLoaiController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;

public class CRUDSachForm extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField tfMaSach;
	private JTextField tfISBN;
	private JTextField tfTieuDe;
	private JTextField tfTacGia;
	private JTextField tfNamXB;
	private JComboBox<String> cbTheLoai;
	private JComboBox<String> cbNXB;
	private JLabel lbTong;
	private JLabel lbNXB;
	private JLabel lbMaSach;
	private JLabel lbTieuDe;
	private JLabel lbTacGia;
	private JLabel lbISBN;
	private JLabel lbNamXB;
	private JLabel lbTheLoai;
	private JButton btnSua,btnLoad,btnXoa,btnLuu,btnQLBS;
	private List<Map> listbooks;
	private JComboBox<String> cbTim;
	private Map sach;
	
	/**
	 * Create the panel.
	 * @throws SQLException 
	 */
	public CRUDSachForm() throws SQLException {
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 263, 1082, 316);
		add(scrollPane);
		
		JPanel panelInfo = new JPanel();
		Border thatBorder1 = new LineBorder(Color.BLACK);
	    Border thatBorder2 = new TitledBorder(thatBorder1, "Thông tin sách");
		panelInfo.setBorder(thatBorder2); 
		panelInfo.setBounds(50, 65, 752, 187);
		add(panelInfo);
		panelInfo.setLayout(null);
		
		JLabel lb1 = new JLabel("Mã sách:");
		lb1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lb1.setBounds(34, 36, 62, 14);
		panelInfo.add(lb1);
		
		JLabel lb7 = new JLabel("Tiêu đề:");
		lb7.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lb7.setBounds(34, 144, 62, 14);
		panelInfo.add(lb7);
		
		JLabel lb2 = new JLabel("ISBN:");
		lb2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lb2.setBounds(387, 36, 62, 14);
		panelInfo.add(lb2);
		
		JLabel lb3 = new JLabel("Tác giả:");
		lb3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lb3.setBounds(34, 72, 62, 14);
		panelInfo.add(lb3);
		
		JLabel lb4 = new JLabel("Thể loại: ");
		lb4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lb4.setBounds(387, 72, 62, 14);
		panelInfo.add(lb4);
		
		JLabel lb5 = new JLabel("Nhà xuất bản: ");
		lb5.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lb5.setBounds(34, 108, 87, 14);
		panelInfo.add(lb5);
		
		JLabel lb6 = new JLabel("Năm xuất bản:");
		lb6.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lb6.setBounds(387, 108, 87, 14);
		panelInfo.add(lb6);
		
		tfMaSach = new JTextField();
		tfMaSach.setEditable(false);
		tfMaSach.setBounds(131, 34, 200, 20);
		panelInfo.add(tfMaSach);
		tfMaSach.setColumns(10);
		
		tfISBN = new JTextField();
		tfISBN.setBounds(504, 34, 200, 20);
		panelInfo.add(tfISBN);
		tfISBN.setColumns(10);
		
		tfTieuDe = new JTextField();
		tfTieuDe.setBounds(131, 142, 573, 20);
		panelInfo.add(tfTieuDe);
		tfTieuDe.setColumns(10);
		
		tfTacGia = new JTextField();
		tfTacGia.setBounds(131, 70, 200, 20);
		panelInfo.add(tfTacGia);
		tfTacGia.setColumns(10);
		
		cbNXB = new JComboBox<String>();
		cbNXB.setBounds(131, 106, 200, 20);
		loadDataNXB();
		panelInfo.add(cbNXB);
		
		tfNamXB = new JTextField();
		tfNamXB.setBounds(504, 106, 200, 20);
		panelInfo.add(tfNamXB);
		tfNamXB.setColumns(10);
		
		cbTheLoai = new JComboBox<String>();
		cbTheLoai.setBounds(504, 70, 200, 20);
		loadDataTL();
		panelInfo.add(cbTheLoai);
		
		JButton btnAddNXB = new JButton("");
		btnAddNXB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				themNXB();
			}
		});
		btnAddNXB.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/plus.png")));
		btnAddNXB.setBounds(341, 105, 23, 23);
		panelInfo.add(btnAddNXB);
		
		JButton btnAddTL = new JButton("");
		btnAddTL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				themTheLoai();
			}
		});
		btnAddTL.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/plus.png")));
		btnAddTL.setBounds(714, 68, 23, 23);
		panelInfo.add(btnAddTL);
		
		lbMaSach = new JLabel("");
		lbMaSach.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbMaSach.setForeground(Color.RED);
		lbMaSach.setBounds(131, 56, 200, 14);
		panelInfo.add(lbMaSach);
		
		lbISBN = new JLabel("");
		lbISBN.setForeground(Color.RED);
		lbISBN.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbISBN.setBounds(504, 56, 200, 14);
		panelInfo.add(lbISBN);
		
		lbTacGia = new JLabel("");
		lbTacGia.setForeground(Color.RED);
		lbTacGia.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbTacGia.setBounds(131, 92, 200, 14);
		panelInfo.add(lbTacGia);
		
		lbTheLoai = new JLabel("");
		lbTheLoai.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbTheLoai.setForeground(Color.RED);
		lbTheLoai.setBounds(504, 92, 200, 14);
		panelInfo.add(lbTheLoai);
		
		lbNXB = new JLabel("");
		lbNXB.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbNXB.setForeground(Color.RED);
		lbNXB.setBounds(131, 129, 200, 14);
		panelInfo.add(lbNXB);
		
		lbNamXB = new JLabel("");
		lbNamXB.setForeground(Color.RED);
		lbNamXB.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbNamXB.setBounds(504, 129, 200, 14);
		panelInfo.add(lbNamXB);
		
		lbTieuDe = new JLabel("");
		lbTieuDe.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbTieuDe.setForeground(Color.RED);
		lbTieuDe.setBounds(131, 162, 200, 14);
		panelInfo.add(lbTieuDe);
		
		JPanel panelTask = new JPanel();
		Border thatBorder3 = new TitledBorder(thatBorder1, "Tác vụ");
		panelTask.setBorder(thatBorder3); 
		panelTask.setBounds(812, 65, 320, 187);
		add(panelTask);
		panelTask.setLayout(null);
		
		btnLoad = new JButton("Load lại trang");
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				loadLaiTrang();
			}
		});
		btnLoad.setIcon(null);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnLoad.setBounds(26, 66, 130, 25);
		panelTask.add(btnLoad);
		
		btnSua = new JButton("Chỉnh sửa");
		btnSua.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSua.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					suaSach();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btnSua.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/change.png")));
		btnSua.setBounds(26, 102, 130, 25);
		panelTask.add(btnSua);
		
		btnXoa = new JButton("Xóa sách");
		btnXoa.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/delete.png")));
		btnXoa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xoaSach();
			}
		});
		btnXoa.setBounds(164, 102, 130, 25);
		panelTask.add(btnXoa);
		
		btnLuu = new JButton("Thêm sách");
		btnLuu.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/add.png")));
		btnLuu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					themSach();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnLuu.setBounds(164, 66, 130, 25);
		panelTask.add(btnLuu);
		
		JLabel lblTmKim = new JLabel("Tìm kiếm theo");
		lblTmKim.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTmKim.setBounds(20, 30, 96, 14);
		panelTask.add(lblTmKim);
		
		JButton btnTim = new JButton("");
		btnTim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					timSach();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Lỗi xảy ra khi kết nối cơ sở dữ liệu","Error!", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
		});
		btnTim.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/find.png")));
		btnTim.setBounds(235, 26, 27, 25);
		panelTask.add(btnTim);
		
		cbTim = new JComboBox<String>();
		cbTim.setModel(new DefaultComboBoxModel(new String[] {"Nhà xuất bản", "ISBN", "Thể loại", "Tiêu đề"}));
		cbTim.setBounds(108, 26, 117, 25);
		panelTask.add(cbTim);
		
		btnQLBS = new JButton("Quản lí bản sao");
		btnQLBS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				qlbansao();
			}
		});
		btnQLBS.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/copy.png")));
		btnQLBS.setBounds(58, 138, 209, 25);
		panelTask.add(btnQLBS);
		
		lbTong = new JLabel("");
		lbTong.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbTong.setBounds(50, 586, 162, 24);
		add(lbTong);
		
		table = new JTable();
		table.setBounds(0, 0, 1, 1);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				hienChiTiet(table.getSelectedRow());
	        }
	    });
    	
		JLabel lblNewLabel = new JLabel("Quản lí sách");
		lblNewLabel.setIcon(new ImageIcon(CRUDSachForm.class.getResource("/Image/icon.png")));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setBounds(50, 11, 217, 31);
		add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBounds(77, 38, 120, 16);
		add(separator);
		
		loadLaiTrang();
	}
	
	/**
     * Hàm này để set text cho tfMaSach trên giao diện
     * Mục đích là dùng cho test
     * @param masach là mã sách
     */
    public void setTfMaSach(String masach){
        this.tfMaSach.setText(masach);
    }
    
    /**
     * Hàm này để set text cho tfTieuDe trên giao diện
     * Mục đích là dùng cho test
     * @param tieude là tiêu đề của sách
     */
    public void setTfTieuDe(String tieude){
        this.tfTieuDe.setText(tieude);
    }
    
    /**
     * Hàm này để set text cho tfISBN trên giao diện
     * Mục đích là dùng cho test
     * @param isbn là mã ISBN của sách
     */
    public void setTfISBN(String isbn){
        this.tfISBN.setText(isbn);
    }
    
    /**
     * Hàm này để set text cho tfTacGia trên giao diện
     * Mục đích là dùng cho test
     * @param tacgia là tác giả
     */
    public void setTfTacGia(String tacgia){
        this.tfTacGia.setText(tacgia);
    }
    
    /**
     * Hàm này để set text cho tfNamXB trên giao diện
     * Mục đích là dùng cho test
     * @param namxb là năm xuất bản
     */
    public void setTfNamXB(String namxb){
        this.tfNamXB.setText(namxb);
    }
    
    /**
     * Hàm này để set lựa chọn cho cbTheLoai trên giao diện
     * Mục đích là dùng cho test
     * @param theloai là thể loại của sách
     */
    public void setCbTheloai(TheLoai theloai) {
    	cbTheLoai.setSelectedItem(theloai);
    }
    
    /**
     * Hàm thêm sách
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	private void themSach() throws SQLException {
		if(kiemtraDuLieu())
            luuSach();
	}
	
	/**
     * Hàm kiểm tra sự hợp lệ của dữ liệu nhập vào JTextField
     * @return nếu dữ liệu hợp lệ, false nếu ngược lại
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	public boolean kiemtraDuLieu() throws SQLException {
		String ISBN = tfISBN.getText();
		String TieuDe = tfTieuDe.getText();
		String TacGia = tfTacGia.getText();
		String NamXB = tfNamXB.getText();
		String nxb = cbNXB.getSelectedItem().toString();
		String theloai = cbTheLoai.getSelectedItem().toString();
        boolean  hasError = false;
		if (!BookHelper.validateTheLoai(theloai)) {
			lbTheLoai.setText("Thể loại không hợp lệ");
			hasError = true;
		}
		else lbTheLoai.setText("");
		if (!BookHelper.validateNXB(nxb)) {
			lbNXB.setText("Nhà xuất bản không hợp lệ");
			hasError = true;
		}
		else lbNXB.setText("");
		if (!BookHelper.validateISBN(ISBN)) {
			lbISBN.setText("Mã ISBN sai định dạng");
			hasError = true;
		}
		else lbISBN.setText("");
		if (!BookHelper.validateTieuDe(TieuDe)) {
			lbTieuDe.setText("Tiêu đề không hợp lệ");
			hasError = true;
		}
		else lbTieuDe.setText("");
		if (!BookHelper.validateTacGia(TacGia)) {
			lbTacGia.setText("Tác giả không hợp lệ");
			hasError = true;
		}
		else lbTacGia.setText("");
		if (!BookHelper.validateNamXB(NamXB)) {
			lbNamXB.setText("Năm xuất bản không hợp lệ");
			hasError = true;
		}
		else lbNamXB.setText("");
		return !hasError;
	}
	
	/**
     * Hàm thực hiện việc lưu sách
     * @exception SQLException khi thực hiện truy vấn csdl
     */
	private void luuSach(){
		try {
			Map<String, Object> sach = new HashMap<String, Object>(); 
            sach.put("TieuDe", tfTieuDe.getText());
            sach.put("ISBN", tfISBN.getText());
            sach.put("TacGia", tfTacGia.getText());
            sach.put("NamXB", tfNamXB.getText());
            sach.put("TheLoai", cbTheLoai.getSelectedItem());
            sach.put("NXB", cbNXB.getSelectedItem());
            Map<String, ?> rs = CRUDSachController.getInstance().themSach(sach); 
            if ((boolean) rs.get("result") == true) loadLaiTrang();
            JOptionPane.showMessageDialog(null, rs.get("msg").toString());
        } catch (SQLException ex) {
        	ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xảy ra khi kết nối cơ sở dữ liệu","Error!", JOptionPane.ERROR_MESSAGE);
        } 
	}
	
	/**
     * Hàm thực hiện load dữ liệu Nhà xuất bản vào comboBox
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	public void loadDataNXB() throws SQLException {
		cbNXB.removeAllItems();
		ArrayList<String> l = NXBController.getInstance().layDSNXB();
		if (l != null) {
			for (int i=0; i<l.size(); i++) {
				cbNXB.addItem(l.get(i));
			}
		}
	}
	
	/**
     * Hàm thực hiện load dữ liệu Thể loại vào comboBox
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	public void loadDataTL() throws SQLException {
		cbTheLoai.removeAllItems();
		ArrayList<String> l = TheLoaiController.getInstance().layDSTheLoai();
		if (l != null) {
			for (int i=0; i<l.size(); i++) {
				cbTheLoai.addItem(l.get(i));
			}
		}
	}	
	
	/**
     * Hàm tạo bảng
     */
	public int loadBangSach() {
		int count = 0;
		Vector<String> colunm ,row;
		DefaultTableModel model = new DefaultTableModel();
		colunm = new Vector<String>();
		int numberColumn;
		String[] columnNames = new String[] {"Mã sách", "Tiêu đề", "Tác giả", "Thể loại", "Nhà xuất bản", "Năm xuất bản", "ISBN", " "};
		numberColumn = columnNames.length;
		for(int i =0; i<numberColumn;i++){
			colunm.add(columnNames[i]);
		}
		model.setColumnIdentifiers(colunm);
		for(Map<String, String> tmp: listbooks){
			count++;
			row = new Vector<String>();
			row.add(tmp.get("MaSach"));
			row.add(tmp.get("TieuDe"));
			row.add(tmp.get("TacGia"));
			row.add(tmp.get("TheLoai"));
			row.add(tmp.get("NXB"));
			row.add(tmp.get("NamXB"));
			row.add(tmp.get("ISBN"));
			model.addRow(row);
		}
		table.setModel(model);
		TableColumn tc = table.getColumnModel().getColumn(7);
	    tc.setCellEditor(table.getDefaultEditor(Boolean.class));
	    tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
	    return count;
	}
	
	/**
     * Hàm thực hiện hiện chi tiết thông tin Sách khi click chọn một dòng trong bảng Sách
     */
	public void hienChiTiet(int pos) {
		sach = listbooks.get(pos);
		tfMaSach.setText(sach.get("MaSach").toString());
		tfTieuDe.setText(sach.get("TieuDe").toString());
		tfTacGia.setText(sach.get("TacGia").toString());
		tfNamXB.setText(sach.get("NamXB").toString());
		tfISBN.setText(sach.get("ISBN").toString());
		cbNXB.setSelectedItem(sach.get("NXB"));
		cbTheLoai.setSelectedItem(sach.get("TheLoai"));
		btnSua.setEnabled(true);
		btnXoa.setEnabled(true);
		btnQLBS.setEnabled(true);
	}
	
	/**
     * Hàm xóa sách
     * @exception SQLException khi thực hiện truy vấn csdl
     * @exception HeadlessException
     */
	private void xoaSach() {
		int dem = 0,thucxoa = 0;
		Boolean flag = true;
		for (int i = 0; i<table.getRowCount(); i++) {
			if (table.getValueAt(i, 7) == flag) {
				dem++;
				try {
					if(CRUDSachController.getInstance().xoaSach(listbooks.get(i).get("MaSach").toString())) {
						thucxoa++;
					}
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (dem != 0) {
			loadLaiTrang();
			JOptionPane.showMessageDialog(null, "Đã xóa được "+thucxoa+" / "+dem+" cuốn sách được chọn");
		}
		else JOptionPane.showMessageDialog(null, "Vui lòng tích chọn sách cần xóa", "Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
     * Hàm sửa thông tin sách
     * @throws SQLException khi thực hiện truy vấn csdl
     */
	private void suaSach() throws SQLException {
		if(kiemtraDuLieu())
            luuThongTinSua();
	}
	
	/**
     * Hàm lưu thông tin sửa
     * @exception SQLException khi thực hiện truy vấn csdl
     */
	private void luuThongTinSua(){
		String tlcu = table.getValueAt(table.getSelectedRow(),3).toString();
		Map<String, String> sach = new HashMap<String, String>(); 
        sach.put("TieuDe", tfTieuDe.getText());
        sach.put("ISBN", tfISBN.getText());
        sach.put("TacGia", tfTacGia.getText());
        sach.put("NamXB", tfNamXB.getText());
        sach.put("TheLoai", cbTheLoai.getSelectedItem().toString());
        sach.put("NXB", cbNXB.getSelectedItem().toString());
		sach.put("MaSach", tfMaSach.getText());
		Map rs;
		try {
			rs = CRUDSachController.getInstance().suaSach(sach,tlcu);
			if ((boolean) rs.get("result") == true) loadLaiTrang();
			JOptionPane.showMessageDialog(null, rs.get("msg"));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi xảy ra khi kết nối cơ sở dữ liệu","Error!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	/**
	 *  Hàm load lại toàn bộ Frame
     */
	private void loadLaiTrang() {
		tfTieuDe.setText("");
    	tfMaSach.setText("");
    	tfTacGia.setText("");
    	tfNamXB.setText("");
    	tfISBN.setText("");
    	listbooks = CRUDSachController.getInstance().layDSSach();
    	int count = loadBangSach();
    	lbTong.setText("Tổng số sách hiện có: "+count);
    	btnXoa.setEnabled(false);
    	btnLuu.setEnabled(true);
    	btnSua.setEnabled(false);
    	btnQLBS.setEnabled(false);
	}
	
	/**
	 *  Hiển thị Dialog quản lí bản sao
     */
	private void qlbansao() {
		Map thongtinsach = new HashMap();
		thongtinsach.put("maSach", sach.get("MaSach"));
		thongtinsach.put("theLoai", sach.get("TheLoai"));
		thongtinsach.put("tacGia", sach.get("TacGia"));
		thongtinsach.put("tieuDe", sach.get("TieuDe"));
		CRUDBanSaoForm crudbansao = new CRUDBanSaoForm(thongtinsach);
	}
	
	/**
	 *  Hiển thị Dialog thêm thể loại
     */
	private void themTheLoai() {
		ThemTheLoaiForm tlform = new ThemTheLoaiForm(this);
		tlform.setLocationRelativeTo(null);
		tlform.setVisible(true);
	}
	
	/**
	 *  Hiển thị Dialog thêm nhà xuất bản
     */
	private void themNXB() {
		ThemNXBForm nxbform = new ThemNXBForm(this);
		nxbform.setLocationRelativeTo(null);
		nxbform.setVisible(true);
	}
	
	/**
	 *  Hàm tìm kiếm sách
	 *  @throws SQLException khi thực hiện truy vấn
     */
	private void timSach() throws SQLException {
		
		int tieuchi = cbTim.getSelectedIndex();
		String choice = "";
		String content = "";
		switch (tieuchi) {
			case 0:
				choice = "NXB";
				content = cbNXB.getSelectedItem().toString();
				break;
			case 1:
				if (BookHelper.validateISBN(tfISBN.getText())) {
					choice = "ISBN";
					content = tfISBN.getText();
				} 
				else JOptionPane.showMessageDialog(null, "Mã ISBN cần tìm không hợp lệ");
				break;
			case 2:
				choice = "TheLoai";
				content = cbTheLoai.getSelectedItem().toString();
				break;
			case 3:
				if (BookHelper.validateTieuDe(tfTieuDe.getText())) {
					choice = "TieuDe";
					content = tfTieuDe.getText();
				}
				else JOptionPane.showMessageDialog(null, "Tiêu đề cần tìm không hợp lệ");
				break;
		}		
		listbooks = CRUDSachController.getInstance().timkiemSach(choice, content);
		System.out.println(listbooks.size());
		if (listbooks != null) {
			loadBangSach();
			JOptionPane.showMessageDialog(null, "Tìm thấy " + listbooks.size() + " kết quả");
		} 
	}
	
}

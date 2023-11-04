package quanlythuvien.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import quanlythuvien.common.Constants;
import quanlythuvien.librarian.controller.MuonTraController;

public class MuonTraForm extends JPanel{
	private JTable tblThongTinMuonTra, tblChiTietMuonTra;
	private JTextField tfMaTheMuon, tfNgayMuon, tfNgayTra, tfMaBanSao, tfDatCoc;
	private JLabel lbMaTheMuon, lbTenBanDoc, lbGioiTinh, lbSoDT, lbErMaTheMuon, lbErMaBanSao, lbErNgayTra, lbErNgayMuon, lbErDatCoc;
	private JButton btnTimKiemTTMuon, btnTimKiemMuonTra, btnMuonSach, btnTraSach;
	private DefaultTableModel modelMuonTra, modelChiTietMuonTra;
	private String titleCols[] = {"Mã Mượn Trả", "Mã Thẻ Mượn", "Ngày Đăng Ký", "Ngày Mượn", "Hạn Trả", "Đặt Cọc", "Trạng Thái"};
	private String titleCols2[] = {"Mã Bản Sao", "Tên Sách", "Tác Giả", "Giá", "Trạng Thái", "Ngày Trả"};
	private Map theMuon;
	private Format formatter = new SimpleDateFormat("dd-MM-yyyy");
	private List<Map> chiTietMuonTras;
	private Map muonTra;
	private List<Map> listborrow;
	
	public MuonTraForm() {
		setLayout(new BorderLayout(5, 5));
		add(createMainPanel());
		btnTraSach.setEnabled(false);
		setActions();
	}
	
	private void setActions() {
		btnTimKiemTTMuon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modelMuonTra.setRowCount(0);
				modelChiTietMuonTra.setRowCount(0);
				timKiemDangKyMuon();
			}
		});
		
		btnTimKiemMuonTra.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				timkiemMuonTra();
			}
		});
		
		tblThongTinMuonTra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblThongTinMuonTra.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				modelChiTietMuonTra.setRowCount(0);
	            hienChiTiet();
	        }
	    });
		
		btnMuonSach.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String datCoc = tfDatCoc.getText().toString();
				if(!muonTra.get("TrangThai").equals("Đã đăng kí")) {
					JOptionPane.showMessageDialog(null, "Sách đã được cho mượn");
				}else if(!kiemTraThongTinChoMuon(datCoc)) {
					if(xacNhanChoMuon())
						JOptionPane.showMessageDialog(null, "Cho mượn sách thành công");
				}
			}
		});
		
		btnTraSach.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					xacNhanTraSach();
				} catch (HeadlessException e) {
					JOptionPane.showMessageDialog(null, "Lỗi! Không thể trả sách. Vui lòng thử lại.", "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu", "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
     * Hàm này để set text cho tfMaBanSao trên giao diện
     * Mục đích là dùng cho test
     * @param mabansao mã bản sao
     */
    public void setTfMaBS(String mabansao){
        this.tfMaBanSao.setText(mabansao);
    }
    
    /**
     * Hàm này để set text cho tfMaTheMuon trên giao diện
     * Mục đích là dùng cho test
     * @param mathe mã thẻ mượn
     */
    public void setTfMaTM(String mathe){
        this.tfMaTheMuon.setText(mathe);
    }
    
    /**
     * Hàm này để set text cho tfNgayTra trên giao diện
     * Mục đích là dùng cho test
     * @param ngaytra ngày trả sách
     */
    public void setTfNgayTra(String ngaytra){
        this.tfNgayTra.setText(ngaytra);
    }
    
    /**
     * Hàm này để set text cho tfNgayMuon trên giao diện
     * Mục đích là dùng cho test
     * @param ngaymuon ngày mượn
     */
    public void setTfNgayMuon(String ngaymuon){
        this.tfNgayMuon.setText(ngaymuon);
    }
    
	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createTitlePanel("Quản Lý Mượn Trả"), BorderLayout.PAGE_START);
		panel.add(createBodyPanel(), BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel createBodyPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 0, 0));
		panel.add(createTablePanel());
		panel.add(createBottomPanel());
		
		return panel;
	}

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel(new BorderLayout(15, 15));
		panel.setBorder(new EmptyBorder(10, 50, 10, 50));
		panel.add(createBottomLeft(), BorderLayout.CENTER);
		panel.add(createButtonPanel(), BorderLayout.EAST);
		return panel;
	}

	private JPanel createInfoReader() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel1 = new JPanel(new GridLayout(1, 2, 5, 5));
		
		JPanel panelL = new JPanel(new BorderLayout());
		JPanel panelL1 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelL1.add(new JLabel("Mã Thẻ Mượn: "));
		panelL1.add(new JLabel("Tên Bạn Đọc: "));
		JPanel panelL2 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelL2.add(lbMaTheMuon = new JLabel());
		panelL2.add(lbTenBanDoc = new JLabel());
		panelL.add(panelL1, BorderLayout.WEST);
		panelL.add(panelL2, BorderLayout.CENTER);
		
		JPanel panelR = new JPanel(new BorderLayout());
		JPanel panelR1 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelR1.add(new JLabel("Giới Tính: "));
		panelR1.add(new JLabel("Số Điện Thoại: "));
		JPanel panelR2 = new JPanel(new GridLayout(2, 1, 5, 5));
		panelR2.add(lbGioiTinh = new JLabel());
		panelR2.add(lbSoDT = new JLabel());
		panelR.add(panelR1, BorderLayout.WEST);
		panelR.add(panelR2, BorderLayout.CENTER);
		
		panel1.add(panelL, BorderLayout.WEST);
		panel1.add(panelR, BorderLayout.CENTER);
		panel.add(panel1);
		panel1.setBorder(new EmptyBorder(5, 100, 10, 100));
		panel.setBorder(new TitledBorder("Thông Tin Thẻ Mượn"));
		
		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panelMain = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new  GridLayout(4, 1, 5, 10));
		panelMain.setPreferredSize(new Dimension(355, 0));
		panel.setBorder(new EmptyBorder(5, 25, 120, 25));
		panel.add(btnTimKiemTTMuon = new JButton("Tìm Kiếm Đăng Ký Mượn"));
		panel.add(btnTimKiemMuonTra = new JButton("Tìm Kiếm Mượn Trả"));
		panel.add(btnMuonSach = new JButton("Cho Mượn Sách"));
		panel.add(btnTraSach = new JButton("Nhận Trả Sách"));
		
		panelMain.setBorder(new TitledBorder("Tác Vụ"));
		panelMain.add(panel);
		
		return panelMain;
	}
	private JPanel createBottomLeft() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(createInputPanel(), BorderLayout.PAGE_START);
		panel.add(createTableChiTietMuonTra(), BorderLayout.CENTER);
		
		return panel;
	}
	private JPanel createTableChiTietMuonTra() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createTitlePanel("Chi Tiết Mượn Trả"), BorderLayout.PAGE_START);
		JScrollPane scrollPane = new JScrollPane(tblChiTietMuonTra = new JTable());
		modelChiTietMuonTra = new DefaultTableModel() {
			public java.lang.Class<?> getColumnClass(int columnIndex) {
				if(columnIndex == 6) return Boolean.class;
				else return String.class;
			};
			
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column == 6) return true;
				else return false;
			}
		};
		modelChiTietMuonTra.setColumnIdentifiers(titleCols2);
		tblChiTietMuonTra.setModel(modelChiTietMuonTra);
		tblChiTietMuonTra.getColumnModel().getColumn(1).setPreferredWidth(175);
		tblChiTietMuonTra.getColumnModel().getColumn(2).setPreferredWidth(120);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel createInputPanel() {
		JPanel panelMain = new JPanel(new BorderLayout());
		JPanel panel = new JPanel(new GridLayout(1, 2, 35, 5));
		panel.add(createInputPanelLeft());
		panel.add(createInputPanelRight());
		
		panelMain.add(panel);
		panel.setBorder(new EmptyBorder(5, 10, 5, 10));
		panelMain.setBorder(new TitledBorder("Thông Tin Mượn Trả"));
		return panelMain;
	}

	private JPanel createInputPanelLeft() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		JPanel panelL = new JPanel(new GridLayout(6, 1, 0, 0));
		panelL.add(new JLabel("Mã Thẻ Mượn: "));
		panelL.add(new JLabel());
		panelL.add(new JLabel("Mã Bản Sao: "));
		panelL.add(new JLabel());
		panelL.add(new JLabel("Đặt cọc"));
		
		JPanel panelR = new JPanel(new GridLayout(6, 1, 0, 0));
		panelR.add(tfMaTheMuon = new JTextField());
		tfMaTheMuon.setPreferredSize(new Dimension(80, 22));
		panelR.add(lbErMaTheMuon = new JLabel());
		lbErMaTheMuon.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbErMaTheMuon.setForeground(Color.RED);
		panelR.add(tfMaBanSao = new JTextField());
		panelR.add(lbErMaBanSao = new JLabel());
		lbErMaBanSao.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbErMaBanSao.setForeground(Color.RED);
		panelR.add(tfDatCoc = new JTextField());
		panelR.add(lbErDatCoc = new JLabel());
		lbErDatCoc.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbErDatCoc.setForeground(Color.RED);
		panel.add(panelL, BorderLayout.WEST);
		panel.add(panelR, BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel createInputPanelRight() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		JPanel panelL = new JPanel(new GridLayout(6, 1, 0, 0));
		panelL.add(new JLabel("Ngày Mượn: "));
		panelL.add(new JLabel());
		panelL.add(new JLabel("Ngày Trả: "));
		
		JPanel panelR = new JPanel(new  GridLayout(6, 1, 0, 0));
		panelR.add(tfNgayMuon = new JTextField());
		panelR.add(lbErNgayMuon = new JLabel());
		lbErNgayMuon.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbErNgayMuon.setForeground(Color.RED);
		panelR.add(tfNgayTra = new JTextField());
		panelR.add(lbErNgayTra = new JLabel());
		lbErNgayTra.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lbErNgayTra.setForeground(Color.RED);
		panel.add(panelL, BorderLayout.WEST);
		panel.add(panelR, BorderLayout.CENTER);
		
		return panel;
	}

	private JPanel createTablePanel() {
		JPanel panelMain = new JPanel(new BorderLayout(10, 10));
		panelMain.setBorder(new EmptyBorder(5, 50, 10, 50));
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.setBorder(new TitledBorder(null, ""));
		panel1.add(new JScrollPane(tblThongTinMuonTra = createTable()));
		modelMuonTra = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		modelMuonTra.setColumnIdentifiers(titleCols);
		tblThongTinMuonTra.setModel(modelMuonTra);
		
		panelMain.add(createInfoReader(), BorderLayout.PAGE_START);
		panelMain.add(panel1, BorderLayout.CENTER);
		
		return panelMain;
	}

	private JTable createTable() {
		JTable table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		return table;
	}

	private JPanel createTitlePanel(String title) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(title);
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(label);

		return panel;
	}
	
	private boolean kiemTraThongTinChoMuon(String datCoc) {
		boolean hasError = false;
		
		if(datCoc.equals("")) {
			lbErDatCoc.setText("Tiền đặt cọc trống");
			hasError = true;
		}else if(!BookHelper.validateGia(datCoc)) {
			lbErDatCoc.setText("Tiền đặt cọc không hợp lệ");
			hasError = true;
		}else {
			lbErDatCoc.setText("");
		}
		
		return hasError;
	}
	
	/**
	 * Xác nhận cho bạn đọc mượn sách
	 * @return true nếu cho mượn thành công hoặc false nếu thất bại*/
	public boolean xacNhanChoMuon() {
		boolean b = true;
		MuonTraController controller = MuonTraController.getInstance();
		String datCocS = tfDatCoc.getText().toString();
		if(!BookHelper.validateGia(datCocS)) {
			lbErDatCoc.setText("");
			return false;
		}
		int size = tblChiTietMuonTra.getRowCount();
		if(size > 0) {
			muonTra.put("DatCoc", Integer.parseInt(datCocS));
			if(controller.capNhatThongTinMuon(muonTra, chiTietMuonTras)) {
				muonTra = controller.timKiemThongTinDangKyMuon(theMuon, Constants.TRANGTHAI_DAMUON);
				modelMuonTra.setRowCount(0);
				if(muonTra != null) addRowMuonTra(muonTra);
				for(int i = 0; i < modelChiTietMuonTra.getRowCount(); i++) {
					tblChiTietMuonTra.setValueAt("Đã mượn sách", i, 4);
				}
			};
		}else {
			JOptionPane.showMessageDialog(null, "Chưa có thông tin mượn trả được chọn", "Warning", JOptionPane.WARNING_MESSAGE);
			b = false;
		}
		
		return b;
	}
	
	/**
     * Hàm tìm kiếm thông tin đăng kí mượn
     * @exception SQLException khi thực hiện truy vấn csdl
     */
	private void timKiemDangKyMuon() {
		int maTheMuon = -1;
		try {
			maTheMuon = Integer.parseInt(tfMaTheMuon.getText().toString());
		} catch (NumberFormatException e2) {
			e2.printStackTrace();
		}
		if(maTheMuon == -1) {
			JOptionPane.showMessageDialog(null, "Mã thẻ mượn không hợp lệ", "Error", JOptionPane.ERROR_MESSAGE);
		}else {
			try {
				theMuon = MuonTraController.getInstance().timTheoMaThe(maTheMuon);
				if((boolean) theMuon.get("result") == false) {
					JOptionPane.showMessageDialog(null, "Thẻ mượn không tồn tại");
				}else {
					lbMaTheMuon.setText(String.valueOf(theMuon.get("MaThe")));
					lbTenBanDoc.setText(theMuon.get("HoTen").toString());
					lbGioiTinh.setText(theMuon.get("GioiTinh").toString());
					lbSoDT.setText(theMuon.get("SDT").toString());
					muonTra = MuonTraController.getInstance().timKiemThongTinDangKyMuon(theMuon, Constants.TRANGTHAI_DANGKYMUON);
					if((boolean) muonTra.get("result") == false) {
						JOptionPane.showMessageDialog(null, "Thẻ mượn chưa có thông tin đăng ký mượn");
					}else {
						modelMuonTra.setRowCount(0);
						addRowMuonTra(muonTra);
					}
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu "+e.toString());
				e.printStackTrace();
			}
			
		}
	}
	
	
	private void addRowMuonTra(Map ttmt) {
		Vector<String> row = new Vector<>();
		row.add(String.valueOf(ttmt.get("MaMuonTra")));
        row.add(String.valueOf(ttmt.get("MaTheMuon")));
        row.add(ttmt.get("NgayDangKi").toString());
        row.add(ttmt.get("NgayMuon").toString());
        row.add(ttmt.get("HanTra").toString());
        row.addElement(String.valueOf(ttmt.get("DatCoc")));
        row.add(ttmt.get("TrangThai").toString());
        
        modelMuonTra.addRow(row);
	}
	
	/**
     * Hàm tìm kiếm thông tin mượn trả
     * @exception SQLException khi thực hiện truy vấn csdl
     * @exception NumberFormatException
     */
	private void timkiemMuonTra() {
		try {
			if(kiemtraDuLieu()) {
				try {		
					int MaTheMuon = 0;
					if (!tfMaTheMuon.getText().isEmpty()) MaTheMuon = Integer.parseInt(tfMaTheMuon.getText());
					String NgayMuon,NgayTra;
					if (tfNgayMuon.getText().isEmpty()) NgayMuon = "";
					else NgayMuon = MuonTraHelper.getDate(tfNgayMuon.getText());
					if (tfNgayTra.getText().isEmpty()) NgayTra = "";
					else NgayTra = MuonTraHelper.getDate(tfNgayTra.getText());
					String MaBanSao = tfMaBanSao.getText();
					int count = 0;
	                listborrow = MuonTraController.getInstance().timKiemMuonTra(MaTheMuon, MaBanSao, NgayMuon, NgayTra);
	                Vector<String> colunm;
	                modelMuonTra = new DefaultTableModel();
	                colunm = new Vector<String>();
	                for(int i =0; i<titleCols.length;i++){
	                    colunm.add(titleCols[i]);
	                }
	                modelMuonTra.setColumnIdentifiers(colunm);
	                for(Map<String,String> ttmt:listborrow){
	                	count++;
	                	Vector<String> row = new Vector<>();
	                	row.addElement(String.valueOf(ttmt.get("MaMuonTra")));
	                	row.addElement(String.valueOf(ttmt.get("MaTheMuon")));
	                    row.add(ttmt.get("NgayDangKi"));
	                    row.add(ttmt.get("NgayMuon"));
	                    row.add(ttmt.get("HanTra"));
	                    row.addElement(String.valueOf(ttmt.get("DatCoc")));
	                    row.add(ttmt.get("TrangThai"));
	                    modelMuonTra.addRow(row);
	                }
	                if(count!=0){
	                	tblThongTinMuonTra.setModel(modelMuonTra);
	                	tblThongTinMuonTra.setVisible(true);
	                }else{
	                	tblThongTinMuonTra.setVisible(false);
	                	JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin!");
	                }
			    } catch (SQLException ex) {
			    	JOptionPane.showMessageDialog(this, "Lỗi kết nối với cơ sở dữ liệu"+ex.toString(),"Error!", JOptionPane.ERROR_MESSAGE);
			    } 
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Xảy ra lỗi khi chạy: "+e.toString(),"Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
     * Hàm kiểm tra sự hợp lệ của dữ liệu nhập vào JTextField
     * @return nếu dữ liệu hợp lệ, false nếu ngược lại
     * @throws SQLException khi thực hiện truy vấn csdl
     * @throws NumberFormatException
     */
	public boolean kiemtraDuLieu() throws NumberFormatException, SQLException {
		String MaTheMuon = tfMaTheMuon.getText();
		String NgayMuon = tfNgayMuon.getText();
		String NgayTra = tfNgayTra.getText();
		String MaBanSao = tfMaBanSao.getText();
		if (MaTheMuon.isEmpty() && NgayMuon.isEmpty() && NgayTra.isEmpty() && MaBanSao.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Bạn phải nhập ít nhất một trường tìm kiếm","Error!", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		boolean  hasError = false;
		if (!MaTheMuon.isEmpty()) {
			if (!MuonTraHelper.validateMaTheMuon(MaTheMuon)) {
				hasError = true;
				lbErMaTheMuon.setText("Mã thẻ sai định dạng");
			}
			else lbErMaTheMuon.setText("");
		}
		if (!MaBanSao.isEmpty()) {
			if (!MuonTraHelper.validateMaBanSao(MaBanSao)) {
				hasError = true;
				lbErMaBanSao.setText("Mã bản sao không đúng định dạng");
			}
			else  lbErMaBanSao.setText("");
		}
		if (!NgayMuon.isEmpty()) {
			if (!MuonTraHelper.validateNgay(NgayMuon)) {
				hasError = true;
				lbErNgayMuon.setText("Ngày mượn không đúng định dạng dd-MM-YYYY");
			}
			else lbErNgayMuon.setText("");
		}
		if (!NgayTra.isEmpty()) {
			if (!MuonTraHelper.validateNgay(NgayTra)) {
				hasError = true;
				lbErNgayTra.setText("Ngày trả không đúng định dạng dd-MM-YYYY");
			}
			else lbErNgayTra.setText("");
		}
		return !hasError;
	}
	
	/**
	 * Hàm hiển bảng chi tiết mượn trả
	 * @exception SQLExeption khi thực hiện câu truy vấn
	 * */
	public void hienChiTiet() {
		int selectedRow = tblThongTinMuonTra.getSelectedRow(); 
		if (tblThongTinMuonTra.getValueAt(selectedRow, 6).toString().equals("Đã nhận sách")
				|| tblThongTinMuonTra.getValueAt(selectedRow, 6).toString().equals("Trả không đủ")) {
			btnTraSach.setEnabled(true);
		}
		int matm = Integer.parseInt(tblThongTinMuonTra.getValueAt(selectedRow, 1).toString());
		int mamuontra = Integer.parseInt(tblThongTinMuonTra.getValueAt(selectedRow, 0).toString());
        try {
			theMuon = MuonTraController.getInstance().timTheoMaThe(matm);
			if((boolean) theMuon.get("result") == false) {
				JOptionPane.showMessageDialog(null, "Thẻ mượn không tồn tại");
			}else {
				lbMaTheMuon.setText(theMuon.get("MaThe").toString());
				lbTenBanDoc.setText(theMuon.get("HoTen").toString());
				lbGioiTinh.setText(theMuon.get("GioiTinh").toString());
				lbSoDT.setText(theMuon.get("SDT").toString());
			}
			chiTietMuonTras = MuonTraController.getInstance().timKiemChiTiet(mamuontra);
			Vector<String> colunm ,row;
            modelChiTietMuonTra = new DefaultTableModel();
            tblChiTietMuonTra.setModel(modelChiTietMuonTra);
            colunm = new Vector<String>();
            for(int i =0; i<titleCols2.length;i++){
                colunm.add(titleCols2[i]);
            }
            modelChiTietMuonTra.setColumnIdentifiers(colunm);
            for(Map<String,?> ctmt : chiTietMuonTras){
            	row = new Vector<String>();
        		row.add(ctmt.get("MaBanSao").toString());
                row.add(ctmt.get("TenSach").toString());
                row.add(ctmt.get("TacGia").toString());
                row.add(String.valueOf(ctmt.get("Gia")));
                row.add(ctmt.get("TrangThai").toString());
                row.add(ctmt.get("NgayTra").toString());
                modelChiTietMuonTra.addRow(row);
            }
            setUpStateColumn(tblChiTietMuonTra.getColumnModel().getColumn(4));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu "+e.toString());
			e.printStackTrace();
		}
        catch (Exception e1) {
        	e1.printStackTrace();
        }
	}
	
	/**
	 * Hàm tạo comboBox lựa chọn trạng thái trả sách
	 * */
	public void setUpStateColumn(TableColumn stateColumn) {
		JComboBox<String> comboBox = new JComboBox<String>();
	    comboBox.addItem("Đăng kí mượn");
	    comboBox.addItem("Đã mượn sách");
	    comboBox.addItem("Đã trả sách");
	    comboBox.addItem("Mất sách");
	    comboBox.addItem("Hỏng sách");
	    stateColumn.setCellEditor(new DefaultCellEditor(comboBox));
	    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	    renderer.setToolTipText("Click để chọn trạng thái trả");
	    stateColumn.setCellRenderer(renderer);
	}
	
	/**
	 * Hàm xác nhận trả sách
	 * Người dùng tích chọn các sách cần trả và ấn nút trả sách
	 * @return true nếu cập nhật thành công, false nếu thất bại
	 * @exception SQLExeption khi thực hiện câu truy vấn
	 * */
	public boolean xacNhanTraSach() throws SQLException {
		int slMuon = tblChiTietMuonTra.getRowCount();
		List<Map> dsTra = new ArrayList<Map>();
		for(int i = 0; i < slMuon; i++) {
			if (tblChiTietMuonTra.getValueAt(i, 5).toString().isEmpty()) {
				chiTietMuonTras.get(i).put("TrangThai", tblChiTietMuonTra.getValueAt(i, 4).toString());
				dsTra.add(chiTietMuonTras.get(i));
			}
		}
		if(dsTra.size() == 0) {
			JOptionPane.showMessageDialog(null, "Chưa chọn bản sao được trả", "Warning", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else {
			int mamuontra = Integer.parseInt(tblThongTinMuonTra.getValueAt(tblThongTinMuonTra.getSelectedRow(), 0).toString());
			String hantra = tblThongTinMuonTra.getValueAt(tblThongTinMuonTra.getSelectedRow(),4).toString();
			MuonTraController controller = MuonTraController.getInstance();
			Map rs = controller.traSach(slMuon, dsTra, hantra, mamuontra);
			if ((boolean) rs.get("result") == true) {
				if (Integer.parseInt(rs.get("state").toString()) == Constants.TRANGTHAI_DATRA) {
					tblThongTinMuonTra.setValueAt("Trả đủ sách", tblThongTinMuonTra.getSelectedRow(), 6);
				} else {
					tblThongTinMuonTra.setValueAt("Trả không đủ", tblThongTinMuonTra.getSelectedRow(), 6);
				}
				dsTra.clear();
				hienChiTiet();
				JOptionPane.showMessageDialog(null, rs.get("msg"));
				return true;
			} else  {
				JOptionPane.showMessageDialog(null, rs.get("msg"));
				dsTra.clear();
				return false;
			}
		}
	}
}

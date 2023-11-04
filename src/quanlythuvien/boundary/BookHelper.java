package quanlythuvien.boundary;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class này là lớp chứa các phương thức trợ giúp cho các chức năng liên quan
 * đến sách
 */
public class BookHelper {
	public static final Pattern VALID_ISBN_REGEX = Pattern.compile("^[0-9]{13}$");
	public static final Pattern VALID_MA_SACH_REGEX = Pattern.compile("^[A-Za-z]{2}[0-9]{4}$");
	public static final Pattern VALID_MA_BANSAO_REGEX = Pattern.compile("^[A-Za-z]{2}[0-9]{4}[1-9]{1}[0-9]*$");
	public static final Pattern VALID_NAM_XB_REGEX = Pattern.compile("^[12][0-9]{3}$");

	/**
	 * Hàm này để kiểm tra mã ISBN có hợp lệ không
	 * 
	 * @param isbn
	 *            là mã isbn của sách string
	 * @return boolean cho biết mã có hợp lệ hay không
	 */
	public static boolean validateISBN(String isbn) {
		Matcher matcher = VALID_ISBN_REGEX.matcher(isbn);
		if (matcher.find() == false)
			return false;
		if (!isbn.startsWith("978"))
			return false;
		try {
			int tot = 0;
			for (int i = 0; i < 12; i++) {
				int digit = Integer.parseInt(isbn.substring(i, i + 1));
				tot += (i % 2 == 0) ? digit * 1 : digit * 3;
			}
			int checksum = 10 - (tot % 10);
			if (checksum == 10) {
				checksum = 0;
			}
			return checksum == Integer.parseInt(isbn.substring(12));
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	//Mã sách được sinh tự động nên không kiểm tra

	/**
	 * Hàm này để kiểm tra mã bản sao có đúng định dạng không
	 * @param masach  là mã sách string
	 * @param maBanSao
	 *            là mã bản sao cần kiểm tra
	 * @return boolean cho biết mã bản sao có đúng định dạng không
	 */
	public static boolean validateMaBanSao(String maSach, String maBanSao) {
		Matcher matcher = VALID_MA_BANSAO_REGEX.matcher(maBanSao);
		if (!matcher.find())
			return false;
		else {
			if (maBanSao.indexOf(maSach) != 0)
				return false;
			return true;
		}
	}

	/**
	 * Hàm này để kiểm tra giá sách có hợp lệ không (số nguyên)
	 * 
	 * @param maBanSao
	 *            là giá sách cần kiểm tra
	 * @return boolean cho biết giá sách có hợp lệ không
	 * @exception NumberFormatException
	 *                bắt ngoại lệ khi chuyển gia từ kiểu String sang int
	 */
	public static boolean validateGia(String gia) {
		boolean b = false;
		try {
			Integer.parseInt(gia);
			b = true;
		} catch (NumberFormatException e) {
			// TODO: handle exception
		}

		return b;
	}

	/**
	 * Hàm này để kiểm tra tiêu đề có hợp lệ không
	 * 
	 * @param tieude
	 *            là tiêu đề của sách string
	 * @return boolean cho biết mã sách có hợp lệ không
	 */
	public static boolean validateTieuDe(String tieude) {
		if (tieude.isEmpty())
			return false;
		return true;
	}

	/**
	 * Hàm này để kiểm tra năm xuất bản có hợp lệ không
	 * 
	 * @param namxb
	 *            là năm xuất bản sách string
	 * @return boolean cho biết năm xuất bản sách có hợp lệ không
	 */
	public static boolean validateNamXB(String namxb) {
		Matcher matcher = VALID_NAM_XB_REGEX.matcher(namxb);
		if (matcher.find() == false)
			return false;
		Calendar date = new GregorianCalendar();
		if (date.get(Calendar.YEAR) >= Integer.parseInt(namxb))
			return true;
		else
			return false;

	}

	/**
	 * Hàm này để kiểm tra tên tác giả có hợp lệ không
	 * 
	 * @param tacgia
	 *            là tên tác giả của sách string
	 * @return boolean cho biết tác giả có hợp lệ không
	 */
	public static boolean validateTacGia(String tacgia) {
		if (tacgia.isEmpty())
			return false;
		return true;
	}

	/**
	 * Hàm này để kiểm tra thể loại sách có hợp lệ không
	 * 
	 * @param theloai
	 *            là thể loại sách string
	 * @return boolean cho biết thể loại sách có hợp lệ không
	 */
	public static boolean validateTheLoai(String theloai) {
		if (theloai.isEmpty()) return false;
		return true;
	}

	/**
	 * Hàm này để kiểm tra nhà xuất bản có hợp lệ không
	 * 
	 * @param nxb
	 *            là nhà xuất bản của sách string
	 * @return boolean cho biết nhà xuất bản có hợp lệ không
	 */
	public static boolean validateNXB(String nxb) {
		if (nxb.isEmpty()) return false;
		return true;
	}
}

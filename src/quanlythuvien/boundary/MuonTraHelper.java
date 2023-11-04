package quanlythuvien.boundary;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuonTraHelper {
	public static final Pattern VALID_MA_THE_REGEX = Pattern.compile("^[0-9]+");
	public static final Pattern VALID_MA_BS_REGEX = Pattern.compile("^[A-Za-z]{2}[0-9]{4}[0-9]+");
	public static final Pattern VALID_NGAY = Pattern.compile("^[0-3]?[0-9]-[0-3]?[0-9]-[0-9]{4}$");
	
	/**
     * Hàm này để kiểm tra mã thẻ mượn có đúng định dạng không
     * @param mathe là mã thẻ mượn string
     * @return boolean cho biết mã thẻ có đúng định dạng không
     */  
    public static boolean validateMaTheMuon(String mathe) {
    	Matcher matcher = VALID_MA_THE_REGEX .matcher(mathe);
        return matcher.find();
    }
    
    /**
     * Hàm này để kiểm tra mã bản sao có đúng định dạng không
     * @param mathe là mã bản sao string
     * @return boolean cho biết mã bản sao có đúng định dạng không
     */  
    public static boolean validateMaBanSao(String mabs) {
    	Matcher matcher = VALID_MA_BS_REGEX .matcher(mabs);
        return matcher.find();
    }
    
    /**
     * Hàm này để kiểm tra định ngày
     * @param ngay là ngày string
     * @return boolean cho biết ngày có đúng định dạng không
     */  
    public static boolean validateNgay(String ngay) {
    	ngay = ngay.replace("/", "-");
    	Matcher matcher = VALID_NGAY .matcher(ngay);
        return matcher.find();
    }
    
    public static String getDate(String ngay) {
    	ngay = ngay.replace("/", "-");
    	DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
			date = df.parse(ngay);
			DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			return df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return ngay;
    }	
}

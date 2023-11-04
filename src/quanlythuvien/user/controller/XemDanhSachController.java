package quanlythuvien.user.controller;

import java.util.ArrayList;

import quanlythuvien.entity.BanSao;

public class XemDanhSachController {
	
	private static XemDanhSachController me;
	private BanSao banSao = new BanSao();
	public XemDanhSachController() {
		
	}
	
	public static XemDanhSachController getInstance() {
		if(me == null) {
			me = new XemDanhSachController();
		}
		return me;
	}
	
	/*
	 * Xem danh sach sach chua tra
	 */
	public ArrayList<ArrayList<String>> loadBangChuaTra(){
		System.out.println("controller: chua tra");
		return banSao.layDanhSachChuaTra() ;
	}
	
	/*
	 * Xem danh sach sach da tra
	 */
	public ArrayList<ArrayList<String>> loadBangDaTra(){
		System.out.println("controller: da tra");
		return banSao.layDanhSachDaTra() ;
	}
}

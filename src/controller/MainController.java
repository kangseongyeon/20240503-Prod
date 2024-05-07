package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.MemberService;
import service.ProdService;
import util.ScanUtil;
import util.View;
import view.Print;

public class MainController extends Print {
	static public Map<String, Object> sessionStorage = new HashMap<>();
	
	MemberService memberService = MemberService.getInstance();
	ProdService prodService = ProdService.getInstance();
	
	boolean debug = true;

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
		View view = View.HOME;
		while (true) {
			switch (view) {
			case HOME:
				view = home();
				break;
			case ADMIN:
				view = admin();
				break;
			case MEMBER:
				view = member();
				break;
			case LOGIN:
				view = login();
				break;
			case ADMIN_PROD_INSERT:
				view = insert();
				break;
			case ADMIN_PROD_DELETE:
				view = delete();
				break;
			case ADMIN_PROD_UPDATE:
				view = update();
				break;
			case ADMIN_PROD_LIST:
				view = adminList();
				break;
			case ADMIN_PROD_DETAILE:
				view = detaile();
				break;
			default:
				break;
			}
		}
	}
	
	
	private View detaile() {
		if (debug) System.out.println("---------- 상품 전체 출력 ----------");
		System.out.println("1. 다음 페이지");
		System.out.println("2. 이전 페이지");
		System.out.println("3. 홈 (관리자)");
		
		int sel = ScanUtil.menu();
		if (sel == 1) return View.ADMIN_PROD_INSERT;
		else if (sel == 2) return View.ADMIN_PROD_DELETE;
		else if (sel == 3) return View.ADMIN_PROD_INSERT;
		
		return View.ADMIN_PROD_DELETE;
	}
	
	private View update() {
		if (debug) System.out.println("---------- 상품 변경 ----------");
		List<Map<String, Object>> list = prodService.list();
		
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
		
		int prodNo = ScanUtil.nextInt("상품 번호: ");
		String name = ScanUtil.nextLine("상품명: ");
		String type = ScanUtil.nextLine("타입: ");
		int price = ScanUtil.nextInt("가격: ");
		
		List<Object> param = new ArrayList();
		param.add(name);
		param.add(type);
		param.add(price);
		param.add(prodNo);
		
		prodService.update(param);
		
		return View.ADMIN_PROD_LIST;
	}
	
	
	private View delete() {
		if (debug) System.out.println("---------- 상품 삭제 ----------");	
		List<Map<String, Object>> list = prodService.list();
		
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
		int prodNo = ScanUtil.nextInt("상품 번호 : ");
		List<Object> param = new ArrayList();
		param.add(prodNo);
		
		int tesult = prodService.delete(param);
		
		int result = prodService.delete(param);
		if (result == 0) {
			System.out.println("삭제를 실패했습니다.");
			return View.ADMIN_PROD_DELETE;
		}
		
		return View.ADMIN_PROD_LIST;
	}
	
	
	private View insert() {
		if (debug) System.out.println("---------- 상품 추가 ----------");
		List<Object> param = new ArrayList();
		String name = ScanUtil.nextLine("상품명 : ");
		String type = ScanUtil.nextLine("타입 : ");
		int price = ScanUtil.nextInt("가격 : ");
		
		param.add(name);
		param.add(type);
		param.add(price);
		
		prodService.insert(param);
		
		return View.ADMIN_PROD_LIST;
	}
	

	private View adminList() {
		List<Map<String, Object>> list = prodService.list();
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
		
		System.out.println("1. 다음페이지");
		System.out.println("2. 이전페이지");
		System.out.println("3. 홈(관리자)");
		
		int sel = ScanUtil.menu();
		if (sel == 1) return View.ADMIN_PROD_LIST;
		else if (sel == 2) return View.ADMIN_PROD_LIST;
		else if (sel == 3) return View.ADMIN;
		else return View.ADMIN_PROD_LIST;
	}
	
	private View login() {
		String id = ScanUtil.nextLine("ID >> ");
		String pw = ScanUtil.nextLine("PASS >> ");
		int role = (int) sessionStorage.get("role");
		
		List<Object> param = new ArrayList();
		param.add(id);
		param.add(pw);
		param.add(role);
		
		boolean loginChk = memberService.login(param, role);
		if (!loginChk) {
			System.out.println("로그인 실패");
			return View.LOGIN;
		}
		
		if (role == 1) return View.MEMBER;
		else return View.ADMIN;		
	}
	
	private View admin() {
		// admin 값이 없으면 LOGIN으로 보냄
		if (!sessionStorage.containsKey("admin")) {
			sessionStorage.put("role", 2);
			return View.LOGIN;
		}
		
		System.out.println("---------- 관리자 ----------");
		System.out.println("1. 상품 추가");
		System.out.println("2. 상품 삭제");
		System.out.println("3. 상품 변경");
		System.out.println("4. 상품 전체 출력");
		System.out.println("5. 로그아웃");
		
		int sel = ScanUtil.menu();
		if (sel == 1) return View.ADMIN_PROD_INSERT;
		else if (sel == 2) return View.ADMIN_PROD_DELETE;
		else if (sel == 3) return View.ADMIN_PROD_UPDATE;
		else if (sel == 4) return View.ADMIN_PROD_LIST;
		
//		sessionStorage에서 값을 지우면 ? -> 로그아웃이 됨
		else if (sel == 5) {
			sessionStorage.remove("admin");
			return View.HOME;
		}
		else return View.ADMIN;
	}
	
	private View home() {
		System.out.println("---------- 홈 ----------");
		System.out.println("1. 관리자");
		System.out.println("2. 일반 회원");
		
		int sel = ScanUtil.menu();
		if (sel == 1) return View.ADMIN;
		else if (sel == 2) return View.MEMBER;
		else return View.HOME;
	}

	private View member() {
		if (debug) System.out.println("========== 멤버 ==========");
		
		return View.HOME;
	}
}

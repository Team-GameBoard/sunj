package board;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comment.Comment;
import comment.CommentDAO;
import game.GameDAO;


@WebServlet("/board")
public class BoardController extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
//		HttpSession session1 = request.getSession();
//		session1.setAttribute("userId", "test");
//		System.out.println(session1.getAttribute("userId"));
		String command = request.getParameter("command");

		if (command == null) {
			command = "list";
		}

		if (command.equals("list")) {
			list(request, response);
		} else if (command.contentEquals("write")) {
			write(request, response);
		} else if (command.equals("read")) {
			read(request, response);
		} else if (command.equals("updateForm")) {
			updateForm(request, response);
		} else if (command.equals("update")) {
			update(request, response);
		} else if (command.equals("delete")) {
			delete(request, response);
		}
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String strNum = request.getParameter("num");
		String userId = request.getParameter("userId");
		String gameNum = request.getParameter("game_num");

		if (strNum == null || strNum.trim().length() == 0) {
			response.sendRedirect("board?game_num=" + gameNum);
			return;
		}
		boolean result = false;
		try {
			result = BoardDAO.deleteContent(Integer.parseInt(strNum), userId);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "해당 게시글 삭제 실패했습니다.");
		}
		if (result) {
			response.sendRedirect("board?game_num=" + gameNum);
			return;
		} else {
			request.setAttribute("error", "삭제하려는 게시글이 존재하지 않습니다");
		}
		request.getRequestDispatcher("error.jsp").forward(request, response);
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strNum = request.getParameter("num");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String gameNum = request.getParameter("game_num");

		if (strNum == null || strNum.trim().length() == 0 || title == null || title.trim().length() == 0
				|| content == null || content.trim().length() == 0) {
			response.sendRedirect("board?game_num=" + gameNum);
			return;
		}

		boolean result = false;

		try {
			// ,userId
			result = BoardDAO.updateContent(new Board(Integer.parseInt(strNum), title, content));
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "게시글 수정 실패");
		}
		if (result) {
			response.sendRedirect("board?game_num=" + gameNum);
			return;
		}
		request.setAttribute("error", "게시글 수정 실패");
		request.getRequestDispatcher("error.jsp").forward(request, response);

	}

	private void updateForm(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String strNum = request.getParameter("num");

		if (strNum == null || strNum.trim().length() == 0) {
			response.sendRedirect("board");
			return;
		}
		String url = "error.jsp";
		Board gContent = null;
		try {
			gContent = BoardDAO.getContent(Integer.parseInt(strNum), false);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "수정하고자 하는 게시글 검색 실패했습니다");
		}
		if (gContent == null) {
			request.setAttribute("error", "수정하고자 하는 게시글이 존재하지 않습니다");
		} else {
			request.setAttribute("resultContent", gContent);
			url = "Update.jsp";
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

	private void read(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String strNum = request.getParameter("num");
		if (strNum == null || strNum.length() == 0) {
			response.sendRedirect("board");
			return;
		}
		String url = "error.jsp";
		Board gContent = null;
		// comment 에서 arrayList 값을 가져오기 위해 생성
		ArrayList<Comment> commentList = null;
		
		try {
			gContent = BoardDAO.getContent(Integer.parseInt(strNum), true);
			// comment 에서 arrayList 값 불러오기
			commentList = CommentDAO.getAllcomment(Integer.parseInt(strNum));
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "게시글 읽기 실패");
		}
		if (gContent == null) {
			request.setAttribute("error", "해당 게시글이 존재하지 않습니다");
		} else {
			request.setAttribute("resultContent", gContent);
			// comList 으로 commentList 를  GameboardDetail.jsp 에 값을 넣어준다.
			request.setAttribute("comList", commentList);
			url = "GameboardDetail.jsp";
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

	private void write(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session1 = request.getSession();
		String userId = (String) session1.getAttribute("userId");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String gameNum = request.getParameter("game_num");
//		String gameGrade = request.getParameter("tier");

		// 데이터값 입력 유무만 유효성 검증
		if (userId == null || userId.trim().length() == 0 || title == null || title.trim().length() == 0
				|| content == null || content.trim().length() == 0) {
			response.sendRedirect("NewWrite.jsp");
			return;// write() 메소드 종료
		}

		boolean result = false;

		try {
//			User_gameDAO.writeContent(new User_game(userId, Integer.parseInt(game_num), gameGrade));
			result = BoardDAO.writeContent(new Board(userId, title, content, Integer.parseInt(gameNum)));
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "게시글 저장 시도 실패 재 시도 하세요");
		}

		if (result) {
			response.sendRedirect("board?game_num=" + gameNum);
		} else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}

	}

	private void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "error.jsp";
		String game_num = request.getParameter("game_num");

		try {
			request.setAttribute("list", BoardDAO.getAllContents(Integer.parseInt(game_num)));
			request.setAttribute("game", GameDAO.getAllContents());
			url = "Gameboard.jsp";
			
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "모두 보기 실패 재 실행 해 주세요");
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

}

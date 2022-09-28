package comment;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CommentController
 */
@WebServlet("/comment")
public class CommentController extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("command");
			if (command == null) {
				command = "list";
			}else if(command.equals("commentSave")){
				commentSave(request,response);
			}
	}

	
	
	
	
	private void commentSave(HttpServletRequest request, HttpServletResponse response) {
		// 덧글 작성(저장)
		
		// 세션을 통한 id 값 가져오기
		HttpSession sessionId = request.getSession();
		
		// 세션값에서 가져온 유저 id 정보
		String userId = (String) sessionId.getAttribute("userId");
		// 해당 페이지 boardNum 가져오기
		int boardNum = Integer.parseInt(request.getParameter("boardNum"));
		// 덧글 작성 내용 
		String commentBoard = request.getParameter("commentWrite");
		
//		System.out.println(userId + " :유저ID");
//		System.out.println(boardNum + " :boardNum");
//		System.out.println(commentBoard + " :덧글내용");
		
		try {
			CommentDAO.commentWrite(new Comment(boardNum, userId, commentBoard));
			// 저장과 동시에 boardController 이동
			request.getRequestDispatcher("board?command=read&num=" + boardNum).forward(request, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}

package comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.DBUtil;

public class CommentDAO {
	
	// 덧글 작성(저장) DAO 
	public static boolean commentWrite(Comment comment) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		boolean result = false;
		
		
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement("insert into comment (board_num, user_id , comment_board) values (?,?,?)");
	        
			pstmt.setInt(1, comment.getBoardNum());
			pstmt.setString(2, comment.getUserId());
			pstmt.setString(3, comment.getCommentBoard());
			
			int count = pstmt.executeUpdate();		
			
			if(count != 0){
				result = true;
			}
		}finally{
			DBUtil.close(con, pstmt);
		}
		return result;
	}
	
	public static ArrayList<Comment> getAllcomment(int boardNum ) throws SQLException{
		// 페이지 덧글 읽어오기 
		Connection con = null;	
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Comment> data = null;
		String sql = "select * from comment where board_num =?";
		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setInt(1, boardNum);
			
			rset = pstmt.executeQuery();
			data = new ArrayList<Comment>();
			while(rset.next()) {
				data.add(new Comment(rset.getInt(1),
									 rset.getInt(2),
									 rset.getString(3),
									 rset.getString(4),
									 rset.getTimestamp(5)));
			}
			
			
		} finally {
			DBUtil.close(con, pstmt, rset);
		}
		return data;
		
	}
	
	// 삭제 하기
		public static boolean deleteComment(Comment comment) throws SQLException {
			Connection con = null;	
			PreparedStatement pstmt = null;
			boolean result = false;
			
			
			String sql="delete from comment where comment_num = ? and user_id = ? and board_num =?";
			
			try {
				con = DBUtil.getConnection();
				pstmt = con.prepareStatement(sql);

		        pstmt.setInt(1,comment.getCommentNum());
		        pstmt.setString(2,comment.getUserId());
		        pstmt.setInt(3, comment.getBoardNum());
		        
				int count = pstmt.executeUpdate();
				
				if(count != 0){
					result = true;
				}
			}finally{
				DBUtil.close(con, pstmt);
			}
			return result;
		}
	
}

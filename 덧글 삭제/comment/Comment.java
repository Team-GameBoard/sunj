package comment;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {
	private int commentNum;
	private int boardNum;
	private String userId;
	private String commentBoard;
	private Timestamp commentCreatedDate;
	
	// 덧글 작성
	public Comment (int boardNum, String userId , String commentBoard) {
		this.boardNum = boardNum;
		this.userId = userId;
		this.commentBoard = commentBoard;
	}
	
	
	// 덧글 삭제
	public Comment(int commentNum, String userId , int boardNum) {
		this.commentNum = commentNum;
		this.userId = userId;
		this.boardNum = boardNum;
	}
}

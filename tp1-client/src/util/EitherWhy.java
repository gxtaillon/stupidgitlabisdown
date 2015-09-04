package util;

public class EitherWhy <L,R,W> {
	L left;
	R right;
	W why;
	Boolean isRight;
	public L left() {
		if (isRight) {
			throw new RuntimeException("Invalid operation: called left on a right either.");
		}
		return left;
	}
	
	public R right() {
		if (!isRight) {
			throw new RuntimeException("Invalid operation: called right on a left either.");
		}
		return right;
	}
	
	public Boolean isRight() {
		return isRight;
	}
	
	public W why() {
		return why;
	}
	
	private EitherWhy() {
		left = null;
		right = null;
		why = null;
		isRight = false;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (isRight()) {
			sb.append("Right (");
			sb.append(String.valueOf(right()));
		} else {
			sb.append("Left (");
			sb.append(String.valueOf(left()));			
		}
		sb.append("): ");
		sb.append(String.valueOf(why()));
		return sb.toString();
	}
	
	static <L,R,W> EitherWhy<L,R,W> Left(L left, W why) {
		EitherWhy<L,R,W> ew = new EitherWhy<L,R,W>();
		ew.why = why;
		ew.left= left;
		ew.isRight = false;
		return ew;
	}

	static <L,R,W> EitherWhy<L,R,W> Right(R right, W why) {
		EitherWhy<L,R,W> ew = new EitherWhy<L,R,W>();
		ew.why = why;
		ew.right = right;
		ew.isRight = true;
		return ew;
	}
}

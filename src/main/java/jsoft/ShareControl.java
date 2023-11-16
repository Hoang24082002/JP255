package jsoft;

public interface ShareControl {
	// chia sẻ bộ quản lý kết nối giữa các thể hiện của Basic
	public ConnectionPool getCP();
	
	// yêu cầu các đối tượng trả lại kết nối sau khi lấy xog giữ liệu
	public void releaseConnection();
}

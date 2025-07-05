package e_commerce.monolithic.exeption;

/**
 * Exception được ném khi quá trình xác thực (authentication) thất bại.
 * Thường được sử dụng khi username hoặc password không chính xác,
 * để tránh tiết lộ thông tin về việc username có tồn tại hay không.
 */
public class AuthenticationFailedException extends RuntimeException {
    
    /**
     * Constructor với message mặc định
     */
    public AuthenticationFailedException() {
        super("Tài khoản hoặc mật khẩu không chính xác");
    }
    
    /**
     * Constructor với custom message
     * @param message thông báo lỗi tùy chỉnh
     */
    public AuthenticationFailedException(String message) {
        super(message);
    }
    
    /**
     * Constructor với message và cause
     * @param message thông báo lỗi
     * @param cause nguyên nhân gây ra exception
     */
    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
} 
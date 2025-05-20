package e_commerce.monolithic.exeption;

import e_commerce.monolithic.exeption.payload.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp này xử lý các ngoại lệ (exception) toàn cục trong ứng dụng Spring Boot.
 * Mọi exception ném ra từ controller sẽ được bắt ở đây và trả về phản hồi lỗi có định dạng chuẩn.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Xử lý mọi exception không xác định (Exception chung chung).
     * Trường hợp này là "bắt tất cả" — phòng ngừa lỗi không nằm trong các Exception cụ thể khác.
     * Trả về mã lỗi HTTP 500 (Internal Server Error).
     *
     * @param ex Exception xảy ra
     * @return ResponseEntity chứa ErrorResponse và mã lỗi 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Xử lý ngoại lệ IllegalArgumentException.
     * Thường dùng khi dữ liệu đầu vào không hợp lệ (ví dụ: đăng ký trùng username, email...).
     * Trả về mã lỗi HTTP 400 (Bad Request).
     *
     * @param ex IllegalArgumentException xảy ra
     * @return ResponseEntity chứa ErrorResponse và mã lỗi 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    /**
     * Xử lý ngoại lệ NotFoundException (tự định nghĩa).
     * Dùng khi không tìm thấy tài nguyên, ví dụ: không tìm thấy user, sản phẩm...
     * Trả về mã lỗi HTTP 404 (Not Found).
     *
     * @param ex NotFoundException xảy ra
     * @return ResponseEntity chứa ErrorResponse và mã lỗi 404
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}

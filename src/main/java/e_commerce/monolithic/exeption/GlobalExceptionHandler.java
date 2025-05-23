package e_commerce.monolithic.exeption;

import e_commerce.monolithic.exeption.payload.ErrorResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Lớp này xử lý các ngoại lệ (exception) toàn cục trong ứng dụng Spring Boot.
 * Mọi exception ném ra từ controller sẽ được bắt ở đây và trả về phản hồi lỗi có định dạng chuẩn.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");

        // Bước 1: Thu thập LỖI DUY NHẤT cho mỗi trường, ưu tiên lỗi "không được để trống"
        Map<String, String> errorsByField = new LinkedHashMap<>(); // Sử dụng LinkedHashMap để giữ thứ tự chèn tạm thời

        // Duyệt qua tất cả các lỗi được trả về
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            // Ưu tiên lỗi "không được để trống". Nếu lỗi này được tìm thấy cho một trường,
            // nó sẽ ghi đè bất kỳ lỗi nào khác đã được thêm trước đó cho trường đó.
            if (errorMessage != null && errorMessage.contains("không được để trống")) {
                errorsByField.put(fieldName, errorMessage);
            } else {
                // Nếu đây không phải lỗi "không được để trống" VÀ chưa có lỗi nào được ghi nhận
                // cho trường này (hoặc lỗi đã ghi nhận không phải là "không được để trống"),
                // thì thêm lỗi này vào.
                // putIfAbsent sẽ chỉ thêm nếu key chưa tồn tại.
                // Nếu key đã tồn tại (do lỗi "không được để trống" đã được thêm), thì không làm gì.
                errorsByField.putIfAbsent(fieldName, errorMessage);
            }
        }


        // Bước 2: Định nghĩa thứ tự mong muốn cho các trường.
        // Điều chỉnh danh sách này để khớp với thứ tự bạn muốn các lỗi xuất hiện.
        List<String> desiredFieldOrder = Arrays.asList(
                "username",
                "password",
                "email",
                "fullname",
                "phone"
        );

        // Bước 3: Tạo danh sách các thông báo lỗi cuối cùng theo thứ tự đã định nghĩa.
        List<String> orderedMessages = new ArrayList<>();
        for (String fieldName : desiredFieldOrder) {
            if (errorsByField.containsKey(fieldName)) {
                orderedMessages.add(errorsByField.get(fieldName));
            }
        }

        body.put("messages", orderedMessages);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

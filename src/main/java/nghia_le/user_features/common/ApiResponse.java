package nghia_le.user_features.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor comment because idk why ide will warrning
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean b, String ok, T data) {
        success = b;
        message = ok;
        this.data = data;
    }

    // tiện: tạo hàm static để dùng cho gọn
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "OK", data);
    }

    public static <T> ApiResponse<T> success( T data, String message ) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}

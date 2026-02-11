package invoice.com.demo.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private Integer code;
    private T payload;
    public static <T> BaseResponseBuilder<Object> builder() {
        return new BaseResponseBuilder<>();
    }
}
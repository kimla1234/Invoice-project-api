package invoice.com.demo.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private Integer code;
    private T description;
}
package invoice.com.demo.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  BaseMessage<T>{
    private String message;
    private T data;
}

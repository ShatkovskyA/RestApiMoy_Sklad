package app.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Объект ответа REST сервиса на входящий запрос")
@EqualsAndHashCode
public class HttpResponseBody {

    @ApiModelProperty(value = "Ответ REST сервиса", required = true)
    @Getter
    @Setter
    private String message;

    @ApiModelProperty(value = "Код ответа", required = true)
    @Getter
    @Setter
    private int code;

    @ApiModelProperty(value = "Описание кода ответа", required = true)
    @Getter
    @Setter
    private String codeMessage;



}

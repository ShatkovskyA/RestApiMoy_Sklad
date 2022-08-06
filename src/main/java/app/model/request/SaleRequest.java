package app.model.request;

import app.model.documents.Sale;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * test from "moysklad.ru"
 * app.model.request
 *
 * @author Anton Shatkovskiy
 * created 06.08.2022
 */
public class SaleRequest {

    @Getter
    @Setter
    @ApiModelProperty(value = "Список продаж товаров", required = true)
    private List<Sale> sales;


}

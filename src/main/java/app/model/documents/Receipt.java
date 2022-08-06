package app.model.documents;

import app.model.Product;
import app.model.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * test from "moysklad.ru"
 * app.model.documents
 * класс-сущность "Поступление"
 *
 * @author Anton Shatkovskiy
 * created 06.08.2022
 */

@ApiModel(description = "Поступление")
@Entity
@FetchProfile(
        name = "receipt_product",
        fetchOverrides = {
                @FetchProfile.FetchOverride(entity = Receipt.class, association = "product", mode = FetchMode.JOIN)
        }
)
@Table(name = "receipt")
@Check(constraints = "product_count > 0 AND product_price >= 0")
@Cacheable
@org.hibernate.annotations.Cache(region = "purchase_region", usage = CacheConcurrencyStrategy.TRANSACTIONAL)

@EqualsAndHashCode
public class Receipt {

    /**
     * Номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    /**
     * Количество товара
     */
    @ApiModelProperty(value = "Количество поступившего товара", required = true)
    @Min(value = 1)
    @Column(name = "product_count", nullable = false)
    @Getter
    @Setter
    private int productCount;

    /**
     * Закупочные цены
     */
    @ApiModelProperty(value = "Закупочная стоимость одной единицы товара", required = true)
    @Min(value = 0)
    @Column(name = "product_price", nullable = false)
    @Getter
    @Setter
    private double productPrice;

    /**
     * Склад
     */
    @ManyToOne
    @JoinColumn(name="storage_id")
    @JsonProperty(value ="storage_id" )
    @NotNull
    private Storage storageId;

    @ApiModelProperty(value = "Ссылка на товар", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonProperty(value = "product_id" )
    @Getter
    @Setter
    private Product product;

}

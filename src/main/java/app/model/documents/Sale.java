package app.model.documents;

import app.model.Product;
import app.model.Storage;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
 * класс-сущность "Продажа"
 *
 * @author Anton Shatkovskiy
 * created 06.08.2022
 */
@ApiModel(description = "Продажа")
@Entity
@FetchProfile(
        name = "salling_product",
        fetchOverrides = {
                @FetchProfile.FetchOverride(entity = Sale.class, association = "product", mode = FetchMode.JOIN)
        }
)
@Table(name = "salling")
@Check(constraints = "product_count > 0 AND sale_price >= 0")
@Cacheable
@org.hibernate.annotations.Cache(region = "salling_region", usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NoArgsConstructor
@EqualsAndHashCode
public class Sale {

    /**
     * Номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    /**
     * Количество товара
     */
    @ApiModelProperty(value = "Количество проданного товара", required = true)
    @Min(value = 1)
    @Column(name = "product_count", nullable = false)
    @Getter
    @Setter
    private int productCount;

    /**
     * Стоимость продажи
     */
    @NotNull
    @Column(name = "sale_price", nullable = false)
    @Getter
    @Setter
    private double salePrice;

    /**
     * Склад
     */
    @ManyToOne
    @JoinColumn(name = "storage_id")
    @JsonProperty(value = "storage_id" )
    @NotNull
    @Getter
    @Setter
    private Storage storage;

    @ApiModelProperty(value = "Ссылка на товар", required = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonProperty(value = "product_id" )
    @Getter
    @Setter
    private Product product;

}

package app.model.documents;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * test from "moysklad.ru"
 * app.model.documents
 * класс-сущность "Перемещение"
 *
 * @author Anton Shatkovskiy
 * created 06.08.2022
 */

@ApiModel(description = "Перемещение")
@Entity
@FetchProfile(
        name = "moving_product",
        fetchOverrides = {
                @FetchProfile.FetchOverride(entity = Moving.class, association = "product", mode = FetchMode.JOIN)
        }
)
@Table(name = "moving")
@EqualsAndHashCode
public class Moving {

    /**
     * Номер
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    /**
     * Количество товара
     */
    @ApiModelProperty(value = "Количество перемещяемого товара", required = true)
    @Min(value = 1)
    @Column(name = "product_count", nullable = false)
    @Getter
    @Setter
    private int productCount;



}

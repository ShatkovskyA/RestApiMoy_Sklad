package app.model;

import app.model.documents.Moving;
import app.model.documents.Receipt;
import app.model.documents.Sale;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Set;

@ApiModel(description = "Товар")
@Entity
@Table(name = "product")
/**
 * {@link Check} будет работать (генирировать соответствующую команду при создании схемы) только если определить эту аннотацию !над классом!
 */
@Check(constraints = "count >= 0")
/**
 * В кэше второго уровня сущности хранятся следующим образом:
 * в качестве ключа в кэше используется ключ сущности (id)
 * в качестве значения ключа кэша используется список, в котором хранятся:
 * значения полей(неассоативных - без связей) (transient поля кэшом игнорируются)
 * id сущностей, которые имеют *ToOne ассоциации (ManyToOne, OneToOne - ссылаются на одну сущность в другой таблице)
 * коллекции не хранятся в кэше, но это можно исправить если добавить аннотации {@link Cacheable} и
 * {@link org.hibernate.annotations.Cache} над коллекцией - тогда коллекции будут хранится в своём регионе
 * (для сущностей, хранящихся в БД, в колллекциях будут представляться ключи этих сущностей)
 */
@Cacheable
@org.hibernate.annotations.Cache(region = "product_region", usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@EqualsAndHashCode
public class Product {


    /**
     * ID Товара
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    /**
     * Артикул
     */
    @ApiModelProperty(value = "Артикул", required = true)
    @Column(name = "article", unique = true, nullable = false)
    @Getter
    @Setter
    private String article;

    /**
     * Наименование
     */
    @ApiModelProperty(value = "Наименование (уникальное значение)", required = true)
    @Column(name = "name", unique = true, nullable = false)
    @Getter
    @Setter
    private String name;

    @ApiModelProperty(hidden = true)
    @Min(value = 0)
    @Column(name = "count", nullable = false)
    @Getter
    @Setter
    private int count;

    /**
     * Цена последней закупки
     */
    @ApiModelProperty(value = "Цена последней закупки", required = true)
    @Column(name = "product_price", unique = true, nullable = false)
    @Getter
    @Setter
    private double productPrice;

    /**
     * Цена последней продажи
     */
    @ApiModelProperty(value = "Цена последней продажи", required = true)
    @Column(name = "sale_price", unique = true, nullable = false)
    @Getter
    @Setter
    private double salePrice;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Set<Sale> sales;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Set<Moving> movings;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @Getter
    @Setter
    private Set<Receipt> receipts;






}

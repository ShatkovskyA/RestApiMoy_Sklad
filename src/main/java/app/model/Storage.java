package app.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * test from "moysklad.ru"
 * app.entities
 * класс-сущность "Склад"
 *
 * @author Anton Shatkovskiy
 * created 06.08.2022
 */

@ApiModel(description = "Склад")
@Entity
@Table(name = "storage")
@EqualsAndHashCode
public class Storage {

    /**
     * ID Склада
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Наименование
     */
    @ApiModelProperty(value = "Наименование (уникальное значение)", required = true)
    @Column(name = "name", unique = true, nullable = false)
    @Getter
    @Setter
    @NotEmpty
    private String name;



}

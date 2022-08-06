package app.model;

import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@EqualsAndHashCode
public class Storage {

    /**
     * ID Склада
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotEmpty
    private String name;



}

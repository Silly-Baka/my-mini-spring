package sillybaka.springframework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2022/10/15
 * Time: 16:15
 *
 * @Author SillyBaka
 * Description：
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Car {
    private String brand;
    private double price;
    private String owner;
}

package framework.spring.pojo;

import framework.spring.pojo.base.Toy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Car extends Toy {

    public Car(String name) {
        super(name);
    }
}

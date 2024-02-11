package com.ovalm.reggie.dto;

import com.ovalm.reggie.entity.Setmeal;
import com.ovalm.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

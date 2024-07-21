package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishService {

    void add(DishDTO dishDTO);

    PageResult pagequery(DishPageQueryDTO dishPageQueryDTO);

    void batch_delete(List<Long> ids);


    DishVO getByIdwithFlavor(Long id);

    void updateDish(DishDTO dishDTO);
}

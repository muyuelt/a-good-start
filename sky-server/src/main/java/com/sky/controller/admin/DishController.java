package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public Result add(@RequestBody DishDTO dishDTO){
        log.info("新增菜品");
        dishService.add(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult = dishService.pagequery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    public Result batch_delete(@RequestParam List<Long> ids){
        dishService.batch_delete(ids);

        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> GetById(@PathVariable Long id){
        DishVO dish = dishService.getByIdwithFlavor(id);
        return Result.success(dish);
    }

    @PutMapping
    public Result UpdateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return Result.success();
    }
}

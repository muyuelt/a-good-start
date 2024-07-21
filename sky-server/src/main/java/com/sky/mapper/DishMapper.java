package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 添加菜品
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void add(Dish dish);

    @Select("select id from dish where name = #{name}")
    void checkIdByName(String name);


    Page<DishVO> pagequery(DishPageQueryDTO dishPageQueryDTO);


    void batch_delete(List ids);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @AutoFill(OperationType.UPDATE)
    void updateDish(Dish dish);
}

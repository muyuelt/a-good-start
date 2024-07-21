package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j

public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional
    public void add(DishDTO dishDTO) {
        Dish dish = new Dish();
        DishFlavor dishFlavor = new DishFlavor();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.add(dish);

        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();


        if(flavors!=null && flavors.size()>0){

            flavors.forEach(e ->{
                e.setDishId(dishId);
                    });
            dishFlavorMapper.insertbatch(flavors);

        }
    }

    @Override
    public PageResult pagequery(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = new PageResult();
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page<DishVO> voPage = dishMapper.pagequery(dishPageQueryDTO);

        pageResult.setTotal(voPage.getTotal());
        pageResult.setRecords(voPage.getResult());

        return pageResult;
    }

    @Override
    public void batch_delete(List<Long> ids) {
        for (Long id: ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setmeals = setMealDishMapper.getByDish_Id(ids);
        if(setmeals !=null && setmeals.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.batch_delete(ids);
        dishFlavorMapper.batch_delete(ids);
    }

    @Override
    public DishVO getByIdwithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List <DishFlavor> dishFlavor = dishFlavorMapper.getByDishId(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavor);

        return dishVO;
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishMapper.updateDish(dish);

        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        Long dishId = dish.getId();
        if(flavors!=null && flavors.size()>0){

            flavors.forEach(e ->{
                e.setDishId(dishId);
            });
            dishFlavorMapper.insertbatch(flavors);

        }
    }
}

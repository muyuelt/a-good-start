package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param emp
     */
    void add(EmployeeDTO emp);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult page_select(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用或者禁用
     * @param status,id
     */
    void start_stop(Integer status,long id);

    /**
     *
     * @return
     */
    Employee getById(Long id);

    /**
     * 编辑
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}

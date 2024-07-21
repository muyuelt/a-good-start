package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autofillPointcut(){
    }

    @Before("autofillPointcut()")
    public void autofillaround(JoinPoint point){
        log.info("开始进行公共字段填充");
        MethodSignature signature = (MethodSignature) point.getSignature();
        //得到注解
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        //得到注解的值
        OperationType value = annotation.value();
        //获取参数对象
        Object[] args = point.getArgs();
        if(args.length==0 || args == null){
            return;
        }
        Object o = args[0];

        LocalDateTime now = LocalDateTime.now();
        Long currentid = BaseContext.getCurrentId();

        if(value.equals(OperationType.INSERT)){
            try {
                Method setCreateTime = o.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setCreateUser = o.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = o.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = o.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(o,now);
                setUpdateTime.invoke(o,now);
                setCreateUser.invoke(o,currentid);
                setUpdateUser.invoke(o,currentid);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        else if(value.equals(OperationType.UPDATE)) {
            try {
                Method setUpdateTime = o.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = o.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(o,now);
                setUpdateUser.invoke(o,currentid);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

                throw new RuntimeException(e);
            }
        }



    }

}

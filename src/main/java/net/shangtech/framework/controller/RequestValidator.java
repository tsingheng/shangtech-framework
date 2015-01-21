package net.shangtech.framework.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Aspect
public class RequestValidator {
	
	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object validate(ProceedingJoinPoint pjp) throws Throwable{
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		if(!AjaxResponse.class.equals(method.getReturnType())){
			return pjp.proceed();
		}
		Object[] args = pjp.getArgs();
		Annotation[][] annotations = method.getParameterAnnotations();
		for(int i = 0; i < annotations.length-1; i++){
			if(hasValidAnnotation(annotations[i]) && args[i+1] instanceof BindingResult){
				BindingResult result = (BindingResult) args[i+1];
				if(result.hasErrors()){
					AjaxResponse ajaxResponse = AjaxResponse.instance();
					ajaxResponse.setSuccess(false);
					ajaxResponse.setErrors(processErrors(result));
					return ajaxResponse;
				}
			}
		}
		return pjp.proceed();
	}
	
	private boolean hasValidAnnotation(Annotation[] annotations){
		if(annotations == null){
			return false;
		}
		for(Annotation annotation : annotations){
			if(annotation instanceof Valid){
				return true;
			}
		}
		return false;
	}
	
	private List<BindingError> processErrors(BindingResult result){
		List<BindingError> errors = new ArrayList<>();
		for(FieldError e : result.getFieldErrors()){
			BindingError error = new BindingError();
			error.setName(e.getField());
			error.setMessage(e.getDefaultMessage());
			errors.add(error);
		}
		return errors;
	}
}
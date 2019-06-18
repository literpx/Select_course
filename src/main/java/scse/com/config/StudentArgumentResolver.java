package scse.com.config;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import scse.com.access.StudentContext;
import scse.com.domain.Student;

@Service
public class StudentArgumentResolver implements HandlerMethodArgumentResolver {

	//controller中方法绑定的参数如果是Student类型，返回true，继续执行下一步
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz==Student.class;
	}

	//上一步为true时执行，返回学生对象
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return StudentContext.getStudent();
	}

}

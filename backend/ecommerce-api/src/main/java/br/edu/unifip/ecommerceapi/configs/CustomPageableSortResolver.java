package br.edu.unifip.ecommerceapi.configs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CustomPageableSortResolver implements HandlerMethodArgumentResolver {

    private static final int DEFAULT_PAGE_NUMBER = 2;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_SORT_PROPERTY = "id";
    private static final String DEFAULT_SORT_DIRECTION = "asc"; // asc ou desc

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Pageable.class.equals(methodParameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(@NonNull MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        int pageNumber = getPageNumber(request) - 1; // Subtrai 1 do número da página
        int pageSize = getPageSize(request);
        Sort sort = getSort(request);

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private int getPageNumber(HttpServletRequest request) {
        String page = request.getParameter("page");
        return StringUtils.hasText(page) ? Integer.parseInt(page) : DEFAULT_PAGE_NUMBER;
    }

    private int getPageSize(HttpServletRequest request) {
        String size = request.getParameter("size");
        return StringUtils.hasText(size) ? Integer.parseInt(size) : DEFAULT_PAGE_SIZE;
    }

    private Sort getSort(HttpServletRequest request) {
        String sortProperty = request.getParameter("sort");
        String sortDirection = request.getParameter("direction");

        if (StringUtils.hasText(sortProperty)) {
            if (!StringUtils.hasText(sortDirection)) {
                sortDirection = DEFAULT_SORT_DIRECTION;
            }
            Sort.Direction direction = Sort.Direction.fromString(sortDirection);
            return Sort.by(direction, sortProperty);
        }

        return Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORT_PROPERTY);
    }
}

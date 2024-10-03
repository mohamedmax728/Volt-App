package Volt.example.Volt.ContentManagement.Application.Services;

import Volt.example.Volt.ContentManagement.Application.Dtos.Category.CategoryListDto;
import Volt.example.Volt.ContentManagement.Application.Interfaces.CategoryService;
import Volt.example.Volt.ContentManagement.Application.Mapping.CategoryProfiler;
import Volt.example.Volt.ContentManagement.Domain.Entities.Category;
import Volt.example.Volt.ContentManagement.Domain.Repositories.CategoryRepository;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.Helpers.Utilities;
import Volt.example.Volt.Shared.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryProfiler categoryProfiler;

    @Transactional(readOnly = true)
    public ServiceResponse<PagedResult<CategoryListDto>> getMany(SearchModel searchModel){
        Pageable page = Utilities.makePagable(
                searchModel
        );
        Page<Category> categoryPage = categoryRepository.findAllByNameContainingIgnoreCase(
                searchModel.getName(), page);

        if(categoryPage.isEmpty()){
            return new ServiceResponse<>(null, false,"there is no categories",
                    "لا يوجد تصنيفات", HttpStatus.NOT_FOUND);
        }

        return new ServiceResponse<PagedResult<CategoryListDto>>
                (categoryProfiler.ToCategoryListDto(categoryPage),
                        true, "","", HttpStatus.OK);
    }
}

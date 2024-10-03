package Volt.example.Volt.ContentManagement.Application.Interfaces;

import Volt.example.Volt.ContentManagement.Application.Dtos.Category.CategoryListDto;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;

public interface CategoryService {
    ServiceResponse<PagedResult<CategoryListDto>> getMany(SearchModel searchModel);
}

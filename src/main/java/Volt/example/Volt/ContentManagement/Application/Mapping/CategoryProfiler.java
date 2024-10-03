package Volt.example.Volt.ContentManagement.Application.Mapping;

import Volt.example.Volt.ContentManagement.Application.Dtos.Category.CategoryListDto;
import Volt.example.Volt.ContentManagement.Domain.Entities.Category;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CategoryProfiler {
    private final ModelMapper modelMapper;
    public PagedResult<CategoryListDto> ToCategoryListDto(Page<Category> categories){
        PagedResult<CategoryListDto> categoryListDtoPagedResult
                = new PagedResult<CategoryListDto>();
        categoryListDtoPagedResult.setData(
                categories.stream().map(s->modelMapper.map(s, CategoryListDto.class))
                        .collect(Collectors.toList())
        );

        categoryListDtoPagedResult.setPageSize(categories.getSize());
        categoryListDtoPagedResult.setPageNo(categories.getNumber() + 1);
        categoryListDtoPagedResult.setTotalElements(categories.getTotalElements());
        categoryListDtoPagedResult.setTotalPages(categories.getTotalPages());
        categoryListDtoPagedResult.setLast(categories.isLast());
        return categoryListDtoPagedResult;
    }
}

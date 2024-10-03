package Volt.example.Volt.ContentManagement.Api.Controller;

import Volt.example.Volt.ContentManagement.Application.Dtos.Category.CategoryListDto;
import Volt.example.Volt.ContentManagement.Application.Interfaces.CategoryService;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contentManagement/category")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryService categoryService;

    @GetMapping("/getMany")
    public ResponseEntity<ServiceResponse<PagedResult<CategoryListDto>>> getMany(
            @RequestBody SearchModel searchModel
            ){
        return ResponseEntity.ok(categoryService.getMany(searchModel));
    }
}

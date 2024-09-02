package Volt.example.Volt.Shared.Helpers;

import Volt.example.Volt.Shared.Dtos.SearchModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Utilities {

    public static String getFieldTrimed(String value){
        return value == null ? null : value.trim();
    }
    public static boolean isNullOrEmpty(String value){
        return value == null || value.trim().isEmpty();
    }
    public static boolean isNull(Object value){
        return value == null;
    }
    public static Sort makeSort(String sortBy, boolean isAsc){
        if(isNullOrEmpty(sortBy)){
            return null;
        }
        return isAsc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }
    public static Pageable makePagable(SearchModel searchModel){

        var sort = makeSort(searchModel.getSortBy(), searchModel.isAsc());
        return isNull(sort) ?
                  PageRequest.of(searchModel.getPageNumber() - 1 , searchModel.getSize())
                : PageRequest.of(searchModel.getPageNumber() - 1, searchModel.getSize(), sort);
    }
}

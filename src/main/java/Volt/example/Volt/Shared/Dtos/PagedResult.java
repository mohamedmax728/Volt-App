package Volt.example.Volt.Shared.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PagedResult<T> {

    private List<T> Data;
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLast;
}

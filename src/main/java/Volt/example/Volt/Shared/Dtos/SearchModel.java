package Volt.example.Volt.Shared.Dtos;

import Volt.example.Volt.Shared.Helpers.Utilities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SearchModel {
    private String name = "";
    private int size = 20;
    private int pageNumber = 1;
    private String sortBy = "";
    private boolean isAsc = true;

    public String getName() {
        return Utilities.getFieldTrimed(name);
    }

    public String getSortBy() {
        return Utilities.getFieldTrimed(sortBy);
    }
}
package com.example.demospringquartzschedulter.utils;


import com.example.demospringquartzschedulter.dto.SearchDto;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageUtil {

    /**
     * build thong tin paging cho query
     *
     * @param pageableDto
     * @return
     */
    public static Pageable getPageable(SearchDto.PageableDto pageableDto) {
        Sort sort;
        if (pageableDto.getSorts().isEmpty()) {
            sort = Sort.by(Sort.Order.desc("id"));
        } else {
            sort = Sort.by(pageableDto.getSorts().stream()
                    .map(el -> el.split(","))
                    .map(ar -> new Sort.Order(Sort.Direction.fromString(ar[1]), ar[0]))
                    .collect(Collectors.toList()));
        }
        return PageRequest.of(pageableDto.getPage(), pageableDto.getSize(), sort);
    }
}

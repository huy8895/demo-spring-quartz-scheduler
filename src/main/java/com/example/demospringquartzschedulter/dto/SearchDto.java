package com.example.demospringquartzschedulter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SearchDto implements Serializable {

  private String fullSearch;
  private List<SearchKey> search;
  private PageableDto pageable;

  @Setter
  @Getter
  @ToString
  public static class PageableDto implements Serializable {

//    @Schema(title = "Số trang, bắt đầu từ 0")
    @Min(0)
    private int page;
//    @Schema(title = "Số bản ghi mỗi trang")
    @Min(1)
    private int size;
//    @Schema(title = "Thứ tự sắp xếp", example = "[\"jobId,desc\"]")
    @NotNull
    private List<String> sorts;
  }

  @Setter
  @Getter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SearchKey implements Serializable {

    @JsonIgnore
//    @Schema(title = "Tìm kiếm theo điều kiện or")
    private boolean orPredicate = false;
    @NotBlank
//    @Schema(title = "Trường muốn tìm kiếm")
    private String key;
    @NotBlank
//    @Schema(title = "Kiểu so sánh: =, >=, <=, like ....")
    private String operator;
    @NotNull
//    @Schema(title = "Giá trị muốn tìm kếm")
    private String value;
  }
}

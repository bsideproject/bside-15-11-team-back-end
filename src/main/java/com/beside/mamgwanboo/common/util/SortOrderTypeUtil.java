package com.beside.mamgwanboo.common.util;

import org.springframework.data.domain.Sort;
import protobuf.common.type.SortOrderType;

public class SortOrderTypeUtil {
  public static Sort toSort(String field, SortOrderType sortOrderType) {
    switch (sortOrderType) {
      case ASC -> {
        return Sort.by(Sort.Direction.ASC, field);
      }
      case DESC -> {
        return Sort.by(Sort.Direction.DESC, field);
      }
      default -> {
        return Sort.by(Sort.Direction.DESC, field);
      }
    }
  }
}

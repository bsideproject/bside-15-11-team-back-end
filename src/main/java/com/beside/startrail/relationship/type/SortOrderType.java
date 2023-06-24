package com.beside.startrail.relationship.type;

import org.springframework.data.domain.Sort;

public enum SortOrderType {
  ASC {
    @Override
    public Sort getSort(String field) {
      return Sort.by(Sort.Direction.ASC, field);
    }
  },
  DESC {
    @Override
    public Sort getSort(String field) {
      return Sort.by(Sort.Direction.DESC, field);
    }
  };

  public abstract Sort getSort(String field);
}

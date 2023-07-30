package com.beside.startrail.relationship.validator;

import com.google.protobuf.Message;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import protobuf.relationship.RelationshipPostRequestProto;
import protobuf.relationship.RelationshipPutRequestProto;

@Component
public class RelationshipRequestValidator {
  public void createValidate(RelationshipPostRequestProto relationshipPostRequestProto) {
    if (relationshipPostRequestProto.getNicknamesCount() == 0
        || StringUtils.isBlank(relationshipPostRequestProto.getRelationship())) {
      onValidateErrors(relationshipPostRequestProto);
    }
  }

  public void updateValidate(RelationshipPutRequestProto relationshipPutRequestProto) {
    if (StringUtils.isBlank(relationshipPutRequestProto.getNickname())
        || StringUtils.isBlank(relationshipPutRequestProto.getRelationship())) {
      onValidateErrors(relationshipPutRequestProto);
    }
  }

  private void onValidateErrors(Message message) {
    throw new IllegalArgumentException(String.format("필수 파라미터 미존재. message: %s", message));
  }
}

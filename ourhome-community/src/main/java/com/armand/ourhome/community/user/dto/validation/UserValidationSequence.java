package com.armand.ourhome.community.user.dto.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;
import com.armand.ourhome.community.user.dto.validation.UserValidationGroups.*;

// 검증할 순서 지정
@GroupSequence({
        Default.class,
        EmailGroup.class,
        NotBlankEmailGroup.class,
        NotBlankPasswordGroup.class,
        PasswordSizeGroup.class,
        NotBlankNicknameGroup.class,
        NicknameSizeGroup.class
})
public interface UserValidationSequence {
}

package com.nupday.cache;

import com.nupday.bo.ValidationCodeBo;
import com.nupday.constant.ValidationCodeType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationCodeCahce {
    private static Map<Integer, List<ValidationCodeBo>> validationCode = new HashMap<>();

    public static void push(Integer ownerId, ValidationCodeBo validationCodebo) {
        List<ValidationCodeBo> validationCodes = validationCode.get(ownerId);
        if (validationCodes == null) {
            validationCodes = new ArrayList<>();
            validationCodes.add(validationCodebo);
            validationCode.put(ownerId, validationCodes);
        } else {
            int index = -1;
            for (int i = 0; i < validationCodes.size(); i ++) {
                if (validationCodes.get(i).getType().equals(validationCodebo.getType())) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                validationCodes.remove(index);
            }
            validationCodes.add(validationCodebo);
        }
    }

    public static ValidationCodeBo get(Integer ownerId, ValidationCodeType type) {
        List<ValidationCodeBo> validationCodes = validationCode.get(ownerId);
        if (CollectionUtils.isEmpty(validationCodes)) {
            return null;
        } else {
            for (ValidationCodeBo validationCodeBo : validationCodes) {
                if (validationCodeBo.getType().equals(type)) {
                    return validationCodeBo;
                }
            }
            return null;
        }
    }

    public static void remove(Integer ownerId, ValidationCodeType type) {
        List<ValidationCodeBo> validationCodes = validationCode.get(ownerId);
        if (!CollectionUtils.isEmpty(validationCodes)) {
            int index = -1;
            for (int i = 0; i < validationCodes.size(); i ++) {
                if (validationCodes.get(i).getType().equals(type)) {
                    index = i;
                    break;
                }
            }
            if (index > -1) {
                validationCodes.remove(index);
            }
        }
    }
}

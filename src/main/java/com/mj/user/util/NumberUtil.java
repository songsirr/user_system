package com.mj.user.util;

import java.security.SecureRandom;
import java.util.Random;

public class NumberUtil {

    public static String randomNumberGenerator(int length){
        Random random = new SecureRandom();
        String numStr = "";

        for (int i = 0; i < length; i++){
            String num = Integer.toString(random.nextInt(10));
            numStr += num;
        }

        return numStr;
    }
}

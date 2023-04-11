package com.sicobo.sicobo.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PaymentValidator {
    public PaymentValidator() {
    }

    public boolean validDate(Date dueDate, Date currentDate){
        long diffInMilliseconds = Math.round(dueDate.getTime() - currentDate.getTime());
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
        return diffInDays > 0;
    }

    public boolean validDateOut(Date dueDate, Date currentDate){
        long diffInMilliseconds = Math.round(dueDate.getTime() - currentDate.getTime());
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
        return diffInDays < -20;
    }
}

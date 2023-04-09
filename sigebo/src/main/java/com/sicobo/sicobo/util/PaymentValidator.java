package com.sicobo.sicobo.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PaymentValidator {
    public PaymentValidator() {
    }

    public boolean validDate(Date dueDate, Date currentDate){
        long diffInMilliseconds = Math.abs(dueDate.getTime() - currentDate.getTime());
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
        System.out.println("Diferencia de dias " +diffInDays);
        return diffInDays > 10;
    }

    public boolean validDateOut(Date dueDate, Date currentDate){
        long diffInMilliseconds = Math.abs(dueDate.getTime() - currentDate.getTime());
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds);
        System.out.println("Esta fuera de dueDate " + diffInDays);
        return diffInDays < 1;
    }
}

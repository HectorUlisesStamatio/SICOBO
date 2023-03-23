package com.sicobo.sicobo.util;

import lombok.*;
    
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @NonNull
    private String title;
    @NonNull
    private String message;
    @NonNull
    private String type;

    private int code;

    @NonNull
    private Object result;
}

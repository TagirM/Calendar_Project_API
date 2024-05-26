package ru.tomsknipineft.services.utilService;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;

@Data
public class ExcelFile {

    private String fileName;

    private ByteArrayResource byteResource;
}

package com.fsecure.tools.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailBean {
    //Asunto del email
    private String subject;

    //contenido del correo electrónico
    private String text;

    /*Accesorios
    //private FileSystemResource file;

    //nombre del accesorio
    private String attachmentFilename;*/

    private String destinationAddrs;
}

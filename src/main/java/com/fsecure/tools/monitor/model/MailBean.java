package com.fsecure.tools.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MailBean implements Serializable {
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

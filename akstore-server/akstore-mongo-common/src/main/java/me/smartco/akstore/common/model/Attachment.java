package me.smartco.akstore.common.model;

import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.util.ImgUtil;
import me.smartco.akstore.common.util.MD5Util;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.io.IOException;

/**
 * Created by libin on 14-11-12.
 */
@Document
public class Attachment extends AbstractDocument {
    public enum Type {jpg,png}
    private Type type;
    private String dir;
    private String contentType;
    private Long size;
    private String md5;
    private int baseSize=52;

    private int extraSize=320;


    @PersistenceConstructor
    public Attachment(){

    }

    public Attachment(byte[] data,Type type,int baseSize, int extraSize) throws IOException {
        this.type = type;
        buildThumbs(data);
        this.baseSize = baseSize;
        this.extraSize = extraSize;
    }

    public String getDir() {
        if(null==dir){
            dir="/uploads/"+md5.substring(0,2);
        }
        return dir;
    }

    public String getPath(){
        return getPath(1);
    }
    public String getPath(int s){
        return getDir()+"/"+md5+'x'+s+getExt();
    }

    public String getExt(){
        return "."+type.name();
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void buildThumbs(byte[] data) throws IOException {
        md5= MD5Util.MD5(data);
        File f=ImgUtil.buffer2File(data,getPath(0));
        this.size=f.length();
        ImgUtil.thumb2File(data, baseSize*1, baseSize*1,  getPath(1));
        ImgUtil.thumb2File(data, baseSize*2, baseSize*2,  getPath(2));
        ImgUtil.thumb2File(data, baseSize*3, baseSize*3,  getPath(3));
        if(extraSize>0){
            ImgUtil.thumb2File(data, extraSize, extraSize,  getPath(4));
            ImgUtil.thumb2File(data, extraSize*2, extraSize*2,  getPath(5));
            ImgUtil.thumb2File(data, extraSize*3, extraSize*3,  getPath(6));
        }
    }
}

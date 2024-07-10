package com.xuecheng.media;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.util.*;

public class BigFileTest {

    @Test
    public void testChunk() throws IOException {
        File source = new File("D:\\steam\\steamapps\\workshop\\content\\431960\\1864604777\\海底.mp4");
        String chunkPath = "E:\\Develop\\java项目\\xuecheng\\testChunkFile\\";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }

        //设置分块的大小
        long chunkSize = 1024 * 1024 * 5;
        //分块数量
        long chunkNum = (long) Math.ceil(source.length() * 1.0 / chunkSize);
        System.out.println("分块总数：" + chunkNum);
        //缓冲区大小
        byte[] b = new byte[1024];
        //使用RandomAccessFile访问文件
        RandomAccessFile rafRead = new RandomAccessFile(source, "r");

        for (int i = 0; i < chunkNum; i++) {
            //分块文件
            File newFile = new File(chunkPath + i);
            if (newFile.exists()) {
                newFile.delete();
            }
            boolean flag = newFile.createNewFile();
            if (flag) {
                //写入文件
                RandomAccessFile rafWrite = new RandomAccessFile(newFile, "rw");
                int len = -1;
                while ((len = rafRead.read(b)) != -1) {
                    rafWrite.write(b, 0, len);
                    if (newFile.length() >= chunkSize) {
                        break;
                    }
                }
                rafWrite.close();
                System.out.println("完成第" + i + "块的分块");
            }

        }

    }

    //测试文件合并方法
    @Test
    public void testMerge() throws IOException {
        //块文件目录
        File chunkFolder = new File("E:\\Develop\\java项目\\xuecheng\\testChunkFile\\");
        //原始文件
        File originalFile = new File("D:\\steam\\steamapps\\workshop\\content\\431960\\1864604777\\海底.mp4");
        //合并文件
        File mergeFile = new File("E:\\Develop\\java项目\\xuecheng\\testMergeFile\\merge.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        //建立缓冲区
        byte[] b = new byte[1024];
        boolean newFile = mergeFile.createNewFile();
        if (newFile) {
            //创建io用于读写
            RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");
            //将io指向文件的头
            rafWrite.seek(0);
            //获取分块文件夹上的全部文件
            File[] files = chunkFolder.listFiles();
            //将文件放入list方便排序，进行文件拼接
            List<File> list = Arrays.asList(files);
            //从小到大排序
            Collections.sort(list, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
                }
            });
            //进行分块合并
            for (File f : list) {
                RandomAccessFile rafRead = new RandomAccessFile(f, "r");
                int len = -1;
                while ((len = rafRead.read(b)) != -1) {
                    rafWrite.write(b, 0, len);
                }
                rafRead.close();
            }
            rafWrite.close();
        }
        try {
            FileInputStream originalFileInputStream=new FileInputStream(originalFile);
            FileInputStream mergeFileInputStream=new FileInputStream(mergeFile);
            String originalMd5 = DigestUtils.md5DigestAsHex(originalFileInputStream);
            String mergeMd5 = DigestUtils.md5DigestAsHex(mergeFileInputStream);
            if(originalMd5.equals(mergeMd5)){
                System.out.println("合并成功");
            }else{
                System.out.println("合并失败");
            }
        }catch (Exception e){

        }
    }


}

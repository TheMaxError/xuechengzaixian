package com.xuecheng.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class MinIOTest {
    // 创建MinioClient对象
    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.139.130:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    /**
     * 上传测试方法
     */
    @Test
    public void uploadTest() {
//        try {
//            minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket("testbucket")
//                            .object("pic01.png")    // 同一个桶内对象名不能重复
//                            .filename("C:\\Users\\ma\\Downloads\\面具.jpg")
//                            .build()
//            );
//            System.out.println("上传成功");
//        } catch (Exception e) {
//            System.out.println("上传失败");
//        }

        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("testbucket")
//                    .object("test001.mp4")
                    .object("001/test001.mp4")//添加子目录
                    .filename("D:\\develop\\upload\\1mp4.temp")//本地路径
                    .contentType(mimeType)//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }

    }


    @Test
    public void deleteTest() {
        try {
            minioClient.removeObject(RemoveObjectArgs
                    .builder()
                    .bucket("testbucket")
                    .object("pic01.png")
                    .build());
            System.out.println("删除成功");
        } catch (Exception e) {
            System.out.println("删除失败");
        }
    }

    @Test
    public void getFileTest() {
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket("testbucket")
                    .object("pic01.png")
                    .build());
            FileOutputStream fileOutputStream = new FileOutputStream("E:\\Develop\\java项目\\xuecheng\\testtosavafile\\save.png");
//            byte[] buffer = new byte[1024];
//            int len;
//            while ((len = inputStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer,0,len);
//            }
//            inputStream.close();
//            fileOutputStream.close();
            IOUtils.copy(inputStream, fileOutputStream);
            System.out.println("下载成功");
        } catch (Exception e) {
            System.out.println("下载失败");
        }
    }

    @Test
    public void checkFileTest() throws IOException {
        //校验文件的完整性对文件的内容进行md5
        FileInputStream fileInputStream1 = new FileInputStream(new File("D:\\develop\\upload\\1.mp4"));
        String source_md5 = DigestUtils.md5Hex(fileInputStream1);
        FileInputStream fileInputStream = new FileInputStream(new File("D:\\develop\\upload\\1a.mp4"));
        String local_md5 = DigestUtils.md5Hex(fileInputStream);
        if (source_md5.equals(local_md5)) {//对两个文件进行完整性校验
            System.out.println("下载成功");
        }

    }
    //将分块文件上传至minio
    @Test
    public void uploadChunk() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String chunkFolderPath="E:\\Develop\\java项目\\xuecheng\\testChunkFile\\";
        //分块后文件位置
        File chunkFolder=new File(chunkFolderPath);
        File[] files = chunkFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            UploadObjectArgs testbucket =
                    UploadObjectArgs.builder().bucket("testbucket")
                    .object("chunk/" + files[i].getName())
                    .filename(files[i].getAbsolutePath())
                    .build();
            minioClient.uploadObject(testbucket);
        }
    }

    //将文件分块合并成文件   要求分块文件最小5M
    @Test
    public void test_merge() throws Exception {
        List<ComposeSource> sources = Stream.iterate(0, i -> ++i)//从0开始生成连续整数序列
                .limit(30)//限制数量为30
                .map(i -> ComposeSource.builder()//将i做map映射
                        .bucket("testbucket")//储存桶的名字
                        .object("chunk/".concat(Integer.toString(i)))//对象名称concat做字符串拼接
                        .build())//
                .collect(Collectors.toList());
        //minio做文件合并
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder().bucket("testbucket")
                .object("merge/".concat("鲸落.mp4")).//在minio中生成的文件名而非本地
                sources(sources).build();
        minioClient.composeObject(composeObjectArgs);
    }
    //清除分块文件
    @Test
    public void test_removeObjects() {
        List<DeleteObject> deleteObjects= Stream.iterate(0, i -> ++i)
                .limit(30)
                .map(i -> new DeleteObject("chunk/".concat(Integer.toString(i))))
                .collect(Collectors.toList());
        RemoveObjectsArgs removeObjectArgs=RemoveObjectsArgs.builder()
                .bucket("testbucket")
                .objects(deleteObjects)
                .build();
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectArgs);
        results.forEach(r->{
            DeleteError deleteError=null;
            try{
                deleteError=r.get();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }



}

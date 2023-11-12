package org.apache.ydata.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import okhttp3.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import com.google.zxing.Result;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ObjectUtils;

public class ZXingUtil {

    /**
     * 解析二维码
     * @param ossPath oss地址
     * @return 二维码内容
     * @throws IOException
     * @throws NotFoundException
     */
    public static String decode(String ossPath) throws IOException, NotFoundException {

        String filepath = downloadFile(ossPath);
        if(ObjectUtils.isEmpty(filepath)) {
            return null;
        }
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(filepath));
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(binarizer);
        HashMap<DecodeHintType, Object> decodeHints = new HashMap<DecodeHintType, Object>();
        decodeHints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        Result result = new MultiFormatReader().decode(bitmap, decodeHints);
        return result.getText();
    }

    private static String downloadFile(String imgUrl) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //访问路径
                    .url(imgUrl)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            //转化成byte数组
            byte[] bytes = response.body().bytes();
            //本地文件夹目录（下载位置）
//            String folder = ResourceUtils.getURL("classpath:").getPath() + File.separatorChar + "download";
            String folder = "/tmp/yyy/images/daifu";
            String filename = StringUtils.substringAfterLast(imgUrl, "/");
            Path folderPath = Paths.get(folder);
            boolean desk = Files.exists(folderPath);
            if (!desk) {
                //不存在文件夹 => 创建
                Files.createDirectories(folderPath);
            }
            Path filePath = Paths.get(folder + File.separatorChar + filename);
            boolean exists = Files.exists(filePath, LinkOption.NOFOLLOW_LINKS);
            if (!exists) {
                //不存在文件 => 创建
                Files.write(filePath, bytes, StandardOpenOption.CREATE);
            }
            return filePath.toFile().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

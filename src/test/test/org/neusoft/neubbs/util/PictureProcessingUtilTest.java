package test.org.neusoft.neubbs.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 图片处理 测试类
 */

@RunWith(JUnit4.class)
public class PictureProcessingUtilTest {

    /**
     * 剪切图片
     * @param sourcePath  源路径
     * @param targetPath  目标路径
     * @param x           起点x坐标
     * @param y           起点y坐标
     * @param width       剪切宽度
     * @param height      剪切高度
     * @return            目标路径
     *
     */
    @Test
    public  static  void CutImage(String sourcePath, String targetPath, int x, int y,
                                   int width, int height)throws IOException {

        File imageFile = new File(sourcePath);
        if (!imageFile.exists()) {
            throw new IOException("Not found the images:" + sourcePath);
        }
        if (targetPath == null || targetPath.isEmpty()) {
            targetPath = sourcePath;
        }
       String format = sourcePath.substring(sourcePath.lastIndexOf(".") + 1, sourcePath.length());
//        String imageName = sourcePath.substring(sourcePath.lastIndexOf("\\")+1,sourcePath.lastIndexOf("."));
//        System.out.print(imageName);
        BufferedImage image = ImageIO.read(imageFile);
        int srcHeight = image.getHeight();//获取原图片高度
        int srcWidth = image.getWidth();//获取原图片宽度
        System.out.println("原图片高度:" + srcHeight + "原图片的宽度:" + srcWidth);

        if (width <= srcWidth && height <= srcHeight && width+x <= srcWidth && height+y <= srcHeight) {
            image = image.getSubimage(x, y, width, height);
            ImageIO.write(image, format, new File(targetPath));
            System.out.println("裁剪成功");

        } else {
            System.out.println("裁剪的大小超出原图片大小");
        }

    }

    /**
     * 压缩图片
     * @param sourcePath  源路径
     * @param targetPath  目标路径
     * @param width       压缩宽度
     * @param height      压缩高度
     * @return            目标路径t
     */
    public static void zoom(String sourcePath,String targetPath,int width,int height) throws IOException{
        File imageFile = new File(sourcePath);
        if(!imageFile.exists()){
            throw new IOException("Not found the images:"+sourcePath);
        }
        if(targetPath == null || targetPath.isEmpty()){
            targetPath = sourcePath;
        }
        String format = sourcePath.substring(sourcePath.lastIndexOf(".")+1,sourcePath.length());
        BufferedImage image = ImageIO.read(imageFile);
        int srcHeight = image.getHeight();//获取原图片高度
        int srcWidth = image.getWidth();//获取原图片宽度
        System.out.println("原图片高度:" + srcHeight + "原图片的宽度:" + srcWidth);
        if(width<=srcWidth && height<=srcHeight) {
            image = zoom(image, width, height);
            ImageIO.write(image, format, new File(targetPath));
            System.out.println("压缩成功");
        }
        else {
            System.out.println("超出原图片大小");
        }
    }

    /**
     * 压缩图片
     * @param sourceImage    待压缩图片
     * @param width          压缩图片高度
     * @param height          压缩图片宽度
     */
  private static BufferedImage zoom(BufferedImage sourceImage,int width,int height){
      BufferedImage zoomImage = new BufferedImage(width,height,sourceImage.getType());
      Image image = sourceImage.getScaledInstance(width,height,Image.SCALE_SMOOTH);
      Graphics gc =zoomImage.getGraphics();
      gc.setColor(Color.WHITE);
      gc.drawImage(image,0,0,null);
      return zoomImage;
  }

  public static void main(String args[])throws  IOException{
      PictureProcessingUtilTest izoom =new PictureProcessingUtilTest();
      String imageSorecePath="C:\\\\Users\\\\ay\\\\Pictures\\\\1355451372286.jpg";
      String ImageTargetPath="D:\\1355451372286.jpg";
      URL url = new URL("https://raw.githubusercontent.com/kayye/BlogBackup/master/photos/2017-05-01_123.jpg");
        izoom.CutImage("C:\\Users\\ay\\Pictures\\1355451413651.jpg","F:\\1355451413651.jpg",500,500,900,500);
      izoom.zoom("C:\\Users\\ay\\Pictures\\1355451413651.jpg","D:\\1355451413651.png",2000,500);
      //izoom.CutImage(imageSorecePath,ImageTargetPath,300,300,200,200);
  }

}
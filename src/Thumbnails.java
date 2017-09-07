import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by nick on 06.05.17.
 */
public class Thumbnails {
    public URL src;
    public int w,h;
    public BufferedImage bi;
    public Image ii2;
    public String file;



    public Thumbnails(String path){
        file=path;

        try {
            bi = ImageIO.read(new java.io.File(file));
            w = bi.getWidth(null);
            h = bi.getHeight(null);
        } catch (IOException e) {
            System.out.println("Image could not be read");
//            System.exit(1);
        }
    };

    public int scale(int w2){
        int h2 = (w2 * h) / w;
        try
        {

          //  System.out.println( w2 + " x "+ h2 );
            ImageIcon ii = new ImageIcon(file);

            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D)bi.createGraphics();
            g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY));
            boolean b = g2d.drawImage(ii.getImage(), 0, 0, w, h, null);
            ii2 = bi.getScaledInstance(w2,h2,0);
            //System.out.println(b);
            //ImageIO.write(bi, "png", new File("/home/nick/Workspace/Idea/JpegPacker/out/production/JpegPacker/JpegPacker/output/test_mp4/out1_new.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return h2;
    }

}

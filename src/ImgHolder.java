import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by nick on 06.05.17.
 */
public class ImgHolder {
    int rows,
     lines,
     border,
     outer_border,
     width,
     height ;

    int max_thumb_w;
    int max_thumb_h;
    String file_dir;
    boolean metadata;
    String logo;
    String bg;


    public ImgHolder(int rows,
                     int lines,
                     int border,
                     int outer_border,
                     int width,
                     int height,
                     String file,
                     boolean metadata,
                     MovieObject mo,
                     String logo,
                     String bg){

        this.rows = rows;
        this.lines=lines;
        this.border=border;
        this.outer_border=outer_border;
        this.width=width;
        this.height=height;
        file_dir = file;

        //max_img_width
        max_thumb_w = (width - (border * (rows - 1) + outer_border *2)) / rows;
        System.out.println("Max Width of thumbs: "+max_thumb_w);

        BufferedImage bi = new BufferedImage(800, 1200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D)bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        String fp = Jpegker.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        ImageIcon bgii = new ImageIcon(fp+"/JpegPacker/statics/bg.png");
        g2d.drawImage(bgii.getImage(),0,0,800,1200,null );
        int logo_offset =0;
        if(logo!=""){
            ImageIcon logoii = new ImageIcon(fp+"/JpegPacker/statics/logo.png");
            g2d.drawImage(logoii.getImage(),outer_border,outer_border,logoii.getIconWidth(),logoii.getIconHeight(),null );
            logo_offset = logoii.getIconWidth()+outer_border+border;
        }

        int r=0;
        int l=0;
        for (int i = 0; i<(rows*lines);i++){


            int e=i+1;

            Thumbnails tn1 =new Thumbnails(file_dir+"out"+e+".png");

            int thumb_height = tn1.scale(max_thumb_w);
            //System.out.println("Höhe: "+thumb_height);
            int meta_offset = 0;
            if(metadata){
                meta_offset =110;
                g2d.setFont(new Font("Serif", Font.BOLD, 20));
                g2d.setPaint(Color.black);
                g2d.drawString("Name: "+mo.filename,logo_offset,outer_border+20);
                g2d.drawString("Resolution: "+mo.res_w+"x"+mo.res_h,logo_offset,outer_border+45);
                g2d.drawString("Length: "+mo.duration_string,logo_offset,outer_border+70);
            }


            int x_ofs =outer_border+r*border+max_thumb_w*r;
            int y_ofs =outer_border+(l*border)+(thumb_height*l)+meta_offset;

            //System.out.println("Set to "+x_ofs+" x " + y_ofs);
            boolean b = g2d.drawImage(tn1.ii2, x_ofs, y_ofs, max_thumb_w, thumb_height, null);

           // System.out.println("Do out"+e+"png : "+b);
            if((i+1)%rows==0){
                r=-1;
                l++;
            }
            r++;
        }
        try {
            ImageIO.write(bi, "png", new File(file_dir+"/full_new.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

/*
    public static void main(String[] args) {
        Thumbnails tn1 =new Thumbnails(s1);
        Thumbnails tn2 =new Thumbnails(s2);
        tn1.scale(360,260);
        tn2.scale(360,260);

        BufferedImage bi = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D)bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

        boolean b = g2d.drawImage(tn1.ii2, 0, 0, 360, 260, null);
        System.out.println(b);
        b = g2d.drawImage(tn2.ii2, 360, 0, 360, 260, null);
        System.out.println(b);

        try {
            ImageIO.write(bi, "png", new File("/home/nick/Workspace/Idea/JpegPacker/out/production/JpegPacker/JpegPacker/output/test_mp4/out1_new.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(b);
    }
    */
}

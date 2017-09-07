package JpegPacker;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


public class Jpegker {
    static int rows = 3;                // rows with pictures
    static int lines = 4;               // picture per line
    static int border = 30;             // Border between previews
    static int outer_border = 40;       // Border on left and right side
    static int width = 800;             // width of prview
    static int height = 800;            // Height of prview
    static int first_offset_secounds = 5;   // secounds left for first capture
    static String logo ="logo.png";
    static String bg ="bg.png";
    static int web_max_height = 480;
    static int zip_max_height = 1024;

    static boolean metadata = true;
    static boolean debug =true;
    static String[] input_file = {"test.mp4"};

    static String path = JPacker.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    static List<MovieObject> mo = new LinkedList<>();

    static boolean make_thumbs = true;
    static boolean make_prevsheet = true;
    static boolean make_webvideo = true;
    static boolean make_zipfile = true;


    /*
    TODO
    Open Steps:
    1. Hardware Encoding
    2. MySQL Uplink

    Processes:
    1. for each File in input_file[] makes a new Directory
    2. grab images (rows * lines) every (secound_of_length / (rows * lines) ) bei FFMPEG and save it to the directory
    3. if needed, renders the Movie down to zip_max_height on aspect ratio
    4. renders zo web_max_height

     */

    private static String make_dir(String name){
        String n_name = name.replaceAll("[^a-zA-Z0-9]","_");
        File file = new File( path+"JpegPacker/output/" + n_name);
        file.mkdirs();
        return n_name;
    }

    public static void debug(String s){
        if(debug){
            System.out.println(s);
        }
    }

    public static void make_thumbs() throws IOException{
        System.out.println("Start prcessing");

        for (int i = 0; i < input_file.length; i++) {
            boolean status = true;

            MovieObject nmo = new MovieObject();

            String res = make_dir(input_file[i]);
            nmo.filename = input_file[i];
            nmo.new_name = res;

            String ret_string = "";


            Process pr = null;
            try {

                String inputFileName = path+"/JpegPacker/input/test.mp4";
                StringTokenizer tk = new StringTokenizer(inputFileName, ".");
                String outputFileName = tk.nextToken();

                String command = "ffmpeg -i " + inputFileName;
                pr = Runtime.getRuntime().exec(command);

                // Ab hier sind eigentlich nur Ausgaben, die für den Programmierer nützlich sein könnten,
                // bei möglichen Fehlern bzw. Erfolgen
                BufferedReader str = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

                String cache;
                String stdout = "";
                String stderr = "";

                while ((cache = str.readLine()) != null) {
                    stdout += cache + "\n";
                    nmo.ffmpeg_info.add(cache);
                }

                while ((cache = err.readLine()) != null) {
                    stderr += cache + "\n";
                    nmo.ffmpeg_info.add(cache);
                }

                str.close();
                err.close();

            } catch (IOException e) {
                System.out.println("Error");
            }

            nmo.gather_info();
            System.out.println("Object created: "+nmo.toString());
            mo.add(nmo);


            int pics = rows * lines;
            int every_n_sec = (nmo.duration_secounds/pics)+first_offset_secounds;
            String command = "ls";
            if(make_thumbs){
                command = "ffmpeg -i "+path+"JpegPacker/input/"+nmo.filename+" -y -vf fps=1/"+every_n_sec+" "+path+"/JpegPacker/output/"+nmo.new_name+"/out%d.png";
            }
            try{

                pr = Runtime.getRuntime().exec(command);
                BufferedReader str = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

                String cache;
                String stdout2 = "";
                String stderr2 = "";

                while ((cache = str.readLine()) != null) {
                    stdout2 += cache + "\n";

                }

                while ((cache = err.readLine()) != null) {
                    stderr2 += cache + "\n";

                }
                debug(stderr2);
                if(make_prevsheet) {
                    ImgHolder ih = new ImgHolder(
                            rows,
                            lines,
                            border,
                            outer_border,
                            width,
                            height,
                            path + "/JpegPacker/output/" + nmo.new_name + "/",
                            metadata,
                            nmo,
                            logo,
                            bg);
                }

                if(make_webvideo){
                    webvideo(path+"JpegPacker/input/"+nmo.filename,path+"JpegPacker/output/"+nmo.new_name+"/web_"+nmo.filename,nmo.res_h,nmo.res_w);
                }
                }catch (IOException e){
                System.out.println("Error while making Thumbnails");
            }
            debug("Have done: "+command);
        }
    }

    public static void webvideo(String file,String target ,int h, int w) {
        String command = "ffmpeg -i " + file + " -y -vf scale="+w+":"+h+" -acodec copy "+target;
        Process pr;
        try {
            System.out.println("Try to "+command);
            pr = Runtime.getRuntime().exec(command);
            BufferedReader str = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            String cache;
            String stderr2 = "";

            while ((cache = str.readLine()) != null) {
                stderr2 += cache + "\n";

            }

            while ((cache = err.readLine()) != null) {
                stderr2 += cache + "\n";

            }
            System.out.println(stderr2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        make_thumbs();
    }
}

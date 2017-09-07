import java.util.LinkedList;
import java.util.List;

/**
 * Created by nick on 06.05.17.
 */
public class MovieObject {

    public String filename = "";
    public String new_name ="";

    public String duration_string = "";
    public int duration_secounds =0;
    public String bitrate_v_string ="";
    public int res_h = 0;
    public int res_w = 0;



    public List<String> ffmpeg_info = new LinkedList<>();

    @Override
    public String toString(){
        String str ="";
        str += "Truename: "+filename+", ";
        str += "Newname "+new_name+", ";
        str += "Time: "+duration_string+", ";
        str += "secounds: "+duration_secounds+", ";
        str += "Bitrate: "+bitrate_v_string+", ";
        str += "Resolution H/W: "+res_h+" / "+res_w+", ";

        return str;
    }

    public String ffmpeg_duration(){
        String dur ="empty";
        for(int i = 0; i<ffmpeg_info.size();i++){
            //System.out.println("Out: "+ffmpeg_info.get(i));
            if(ffmpeg_info.get(i).contains("Duration") ) dur = ffmpeg_info.get(i);
          }
        return dur;
    }

    private void set_duration(String s){
        String[] lim_1 = s.split(",");
        String[] lim_2 =lim_1[0].split(":");
        int secounds =0;
        lim_2[1] = lim_2[1].replace(" ","");
        secounds += (Integer.parseInt(lim_2[1]) * 60 * 60 );
        secounds += (Integer.parseInt(lim_2[2]) * 60 );
        String[] lim_3 = lim_2[3].split("\\.");
        secounds += (Integer.parseInt(lim_3[0])  );
        duration_secounds = secounds;
        duration_string = lim_2[1]+":"+lim_2[2]+":"+lim_2[3];
        bitrate_v_string = lim_1[2].split(":")[1];

    }
    private void set_resolution(String s){
        String[] s2 = s.split(":");
        for (int i = 0 ; i<s2.length;i++){
            if(s2[i].contains(" Video")){
                String[] s3 =s2[i+1].split(",");

                for(int j = 0 ;j<s3.length;j++){
                    if(s3[j].contains("x") && !s3[j].contains(" 0x")){
                       String[] res = s3[j].split("x");
                       if(res.length==2){
                           res[0] = res[0].replace(" ","");
                           res[1] = res[1].split(" ")[0];
                           res_w = Integer.parseInt(res[0]);
                           res_h = Integer.parseInt(res[1]);
                       }else{
                           System.out.println("ERROR on resolution: "+s3[j]+" is not valid!!");
                       }
                    }
                }
            }
        }
    }


    public void gather_info(){
        for(int i = 0; i<ffmpeg_info.size();i++){
            if(ffmpeg_info.get(i).contains("Duration") ) set_duration(ffmpeg_info.get(i));
            if(ffmpeg_info.get(i).contains("Stream") && ffmpeg_info.get(i).contains("Video") ) set_resolution(ffmpeg_info.get(i));

        }
    }
}

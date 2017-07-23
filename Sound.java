import javax.swing.*;
import sun.audio.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Sound
{

    /*public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setSize(200,200);
        JButton button = new JButton("click here");
        frame.add(button);
        button.addActionListener(new AL());
        frame.show(true);
    }
    public static class AL implements ActionListener
    {
        public final void actionPerformed(ActionEvent e)
        {
            music();
        }

    }*/

   
    public static void music()
    {
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;
        ContinuousAudioDataStream loop = null;
       
        try{
            BGM =new AudioStream(new FileInputStream("POL-chubby-cat-short.wav"));
            MD = BGM.getData();
            loop = new ContinuousAudioDataStream    (MD);
        }catch(IOException error){
           
        }
        MGP.start(loop);
       

    }
}

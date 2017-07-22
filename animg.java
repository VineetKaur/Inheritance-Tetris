import java.awt.*;
import javax.swing.*;
class animg
{
    JFrame f;
    static public void main()
    {
        animg a = new animg();
        a.aniStart();
        a.aniEnd();
    }
    void aniStart()
    {
        f = new JFrame("Simple animation");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        f.setSize(680,660);
        f.setVisible(true);
        DrawPanel1 dp = new DrawPanel1();
        //dp.setBounds(10,10,500,500);
        f.add(dp);
        
        for(int i = 0;i<90;i++)
        {
            if(i==0)
            {
                dp.setsize(10);
                dp.change("GAME",20);
            }
            else if(i==15)
            {
                dp.setsize(10);
                dp.change("STARTS",35);
            }
            else if(i==30)
            {
                dp.setsize(10);
                dp.change("IN 3...",10);
            }
            else if(i==45)
            {
                dp.setsize(10);
                dp.change("2...",-10);
            }
            else if(i==60)
            {
                dp.setsize(10);
                dp.change("1...",-10);
            }
            else if(i==75)
            {
                dp.setsize(10);
                dp.change("GO!!!",10);    
            }
            f.repaint();
            try
            {
                Thread.sleep(85);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        f.setVisible(false);
    }
    void aniEnd()
    {
        f = new JFrame("Simple animation");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //f.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        f.setSize(680,660);
        f.setVisible(true);
        DrawPanel1 dp = new DrawPanel1();
        //dp.setBounds(10,10,500,500);
        f.add(dp);
        dp.setsize(20);
        for(int i=0; i<7; i++)
        {
            if(i%2==0)
                dp.change("GAME OVER",40);
            else
                dp.change("",0);
                
            f.repaint();
            try
            {
                Thread.sleep(500);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    class DrawPanel1 extends JPanel
    {
        String s;
        int size,t;
        void change(String str, int i )
        {
            t =i;
            s = str;
        }
        void setsize(int n)
        {
            size = n;
        }
       public void paintComponent(Graphics g)
        {
            g.fillRect(0,0,this.getWidth(),this.getHeight());
            g.setColor(Color.yellow);
            g.setFont(new Font("serif",Font.BOLD + Font.ITALIC, size));
            g.drawString(s,(this.getWidth()/2)-t-size,(this.getHeight()/2));
            size += 2;
        }
    }
}
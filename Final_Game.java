import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
class Final_Game implements Runnable
{
    static Final_Game obj;
    static JFrame f,pausedf,exit;
    boolean exitflag,gamebegin;
    JPanel p;
    JLabel pn,lev,hsc,sc;
    JButton pau,esc;
    Image dbimage;
    Graphics dbg;
    String name,fname;
    static int le;
    static int s;
    static Polygon poly[];
    static int shapeNo[];
    static Area ar[];
    Color rand_col[];
    boolean paused=false;
    boolean l,r,d,u;
    static boolean gameover;
    int a,b,w,y,h;
    static int z;
    final int fwidth,fheight;
    DrawPanel1 dp = new DrawPanel1();
    Runnable job = new MoveThread();
    Thread t = new Thread(job);
    Sound mus  = new Sound();
    BufferedReader brr;
    PrintWriter pw;
    public Final_Game()
    {
        fname ="highscore.txt";
        fwidth = 670;
        fheight = 650;
        le=1;
        s=0;
        z=0;
        w=40;
        a=480;
        b=600;
        l=false;
        r = false;
        d = false;
        u=false;
        exitflag =false;
        gamebegin = false;
        gameover = false;
        poly = new Polygon [150];
        rand_col = new Color[150];
        ar = new Area[150];
        shapeNo = new int[150];
        setPolygon();
        setRandColor(
        try
        {
            brr = new BufferedReader(new FileReader(fname));
            h = Integer.parseInt(brr.readLine());
            //System.out.println(h);
            brr.close();
        }
        catch(FileNotFoundException e)
        {
            h = 0;
        }
        catch(IOException e)
        {
            h=0;
        }
        catch(NumberFormatException e)
        {
            h=0;
        }
        
    }
    void init()
    {
        f= new JFrame("TETRIS");
        f.setSize(fwidth,fheight);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.addKeyListener(new KeyManager());
        f.getContentPane().add(BorderLayout.CENTER,dp);

        f.setFocusable(true);
        f.requestFocusInWindow();
        //obj.go();
        f.repaint();
        mus.music();
        /*obj.setPolygon();
        obj.setRandColor();
        f.repaint();*/
    }
    public void run()
    {
        init();
        //if(z==0)
        {
            t.start();
        }
        int flag = 0;
        boolean f2 = false; //is a line complete
        boolean f3 = false;
        //System.out.println("Initial: "+ z);
        while(true)
        {
            obj.setPolygon();
            obj.setRandColor();
            //obj.go();
            if(paused)
            {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
            }
            while(true)
            {
                //obj.go("VINEET ");
                /*if(paused)
                {
                    break;
                }*/
                while(paused)
                {
                                   
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                
                }
                
                for(int i=0; i<z; i++)
                {
                    if(intersectsShape(ar[z],ar[i]))
                    {
                        //To Correct Some Error(IMPORTANT)
                        //poly[z].translate(0,-40);
                        flag = 1;
                        break;
                    }
                    
                }
                /*if(flag ==1)
                {
                     Rectangle rec = ar[z].getBounds();
                     if(rec.y<=10)
                     {
                         f2 =1;
                     }
                }*/

                 if(collisionWithBottomWall(ar[z])||flag==1)
                {
                    
                    flag =0;
                    break;
                }
                obj.move();
                f.repaint();
                try
                {
                    Thread.sleep(70);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                //System.out.println("Inner While: "+ z);
            }
            Rectangle rec = ar[z].getBounds();
            for(int y = 30; y<=590; y+=40)
            {
                for(int x=30;x<=470;x+=40)
                {
                    for(int i=0; i<=z; i++)
                    {
                        if(ar[i].contains(x,y))
                        {
                            f3 = true;
                            break;
                        }
                    }
                    if(f3)
                    {
                        f2 = true;
                        f3 = false;
                    }
                    else
                    {
                        f2  =false;
                        break;
                    }
                }
                if(f2)
                {
                    //System.out.println("Line Complete "+ (630-y)/40);
                    s +=100;
                    if(s>=500)
                    {
                        le=2;
                    }
                    if(s>=1000)
                    {
                        le = 3;
                    }
                    //obj.changeLabels();
                    for(int i =0; i<=z; i++)
                    {
                        Rectangle r = new Rectangle(10,y-20,480,40);
                        Area a = new Area(r);
                        a.intersect(ar[i]);
                        ar[i].subtract(a);
                    
                    }
                    for(int i=0; i<=z; i++)
                    {
                        Rectangle r = new Rectangle(ar[i].getBounds());
                        if(r.y+r.height <=y)
                        {
                            AffineTransform at = new AffineTransform(1,0,0,1,0,40);
                            ar[i] = ar[i].createTransformedArea(at);
                        }
                        else if(r.y < y)
                        {
                            Rectangle r1 = new Rectangle(r.x,r.y,r.width,y-20-r.y);
                            Area a1 = new Area(r1);
                            Area a2 = new Area(r1);
                            a1.intersect(ar[i]);
                            a2.intersect(ar[i]);
                            AffineTransform at = new AffineTransform(1,0,0,1,0,40);
                            a1 = a1.createTransformedArea(at);
                            ar[i].subtract(a2);
                            ar[i].add(a1);
                        }
                    }
                    f2 = false;
                }
            }
                
            if(rec.y<=10)
            {
                gameover = true;
                break;
            }
            if(!paused)
            {
                z++;
            }
            // System.out.println("Outer While:" + z);
        }
        try
        {
            if(s>h)
            {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(fname)));
                pw.println(s);
            }
            
        }
        catch(IOException e)
        {}
        finally
        {
            if(s>h)
            pw.close();
        }
        //System.out.println("Game Over");
    }   
    
    public static void main()
    {
        animg a = new animg();
        
        obj = new Final_Game();
        /*obj.getName();
        a.aniStart();*/
        while(!gameover)
        {
            obj.run();
        }
        if(gameover)
        {
            f.setVisible(false);
            a.aniEnd();
        }
    }

    void setPolygon()
    {
        int k;
        int n=(int)(Math.ceil(Math.random()*(5)));
        shapeNo[z] = n;
        y=0;
        switch(n)
        {
            
            case 1: // L Shape
                    int []i1={a/2-w,a/2,a/2,a/2+w,a/2+w,a/2-w};
                    int []j1={10,10,2*w+10,2*w+10,10+3*w,3*w+10};
                    k=i1.length;
                    poly[z] = new Polygon(i1,j1,k);
                    poly[z].translate(10,-80);
                    ar[z] = new Area(poly[z]);
                    break;
            case 2: //T shape
                    int []i2={a/2-2*w ,a/2+w,a/2+w,a/2,a/2,a/2-w,a/2-w,a/2-2*w};
                    int []j2={10,10,w+10,w+10,2*w+10,2*w+10,w+10,w+10};
                    k=i2.length;
                    poly[z] = new Polygon(i2,j2,k);
                    poly[z].translate(10,-40);
                    ar[z] = new Area(poly[z]);
                    break;
            case 3: //Z Shape
                    int []i3={a/2-2*w,a/2,a/2,a/2+w,a/2+w,a/2-w,a/2-w,a/2-2*w};
                    int []j3={10,10,w+10,w+10,2*w+10,2*w+10,w+10,w+10};
                    k=i3.length;
                    poly[z] = new Polygon(i3,j3,k);
                    poly[z].translate(10,-40);
                    ar[z] = new Area(poly[z]);
                    break;
            case 4: //l shape
                    int []i4 = {(a/2)-(2*w),(a/2)+(2*w),(a/2)+(2*w),(a/2)-(2*w)};
                    int j4[] = {10,10,w+10,w+10};
                    k = i4.length;
                    poly[z] = new Polygon(i4,j4,k);
                    poly[z].translate(10,0);
                    ar[z] = new Area(poly[z]);
                    break;
            case 5: //Square Shape
                    int []i5 = {(a/2)-(w),(a/2)+w,(a/2)+w,(a/2)-w};
                    int j5[] = {10,10,2*w+10,2*w+10};
                    k = i5.length;
                    poly[z] = new Polygon(i5,j5,k);
                    poly[z].translate(10,-40);
                    ar[z] = new Area(poly[z]);
                    break;
            default:System.out.println("Wrong number generated: " + n);
                    break;
        }
        
    }
    
    void setRandColor()
    {
        int red = (int)(Math.random()*255);
        int blue = (int)(Math.random()*255);
        int green = (int)(Math.random()*255);
        Color random_color = new Color(red,green,blue);
        rand_col[z] = random_color;
    }
    
    class KeyManager implements KeyListener
    {
        public void keyPressed(KeyEvent ke)
        {
            
            if(ke.getKeyCode() == KeyEvent.VK_LEFT)
            {
                l = true;
            }
            else if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                r = true;
                
                //l =u =d =false;
            }
            //Temporary
            else if(ke.getKeyCode()== KeyEvent.VK_UP)
            {
                u=true;
                //l=d=r=false;
            }
            else if(ke.getKeyCode()== KeyEvent.VK_DOWN)
            {
                d=true;
                //l=u=r=false;
            }
            
            //f.repaint();
        }
        public void keyReleased(KeyEvent ke)
        {
            if(ke.getKeyCode() == KeyEvent.VK_LEFT)
            {
                l = false;
            }
            else if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                r = false;
            
            }
            else if(ke.getKeyCode()== KeyEvent.VK_UP)//Rotation
            {
                u=false;
            }
            else if(ke.getKeyCode()== KeyEvent.VK_DOWN)
            {
                d=false;
            }
            //obj.move();
        }
        public void keyTyped(KeyEvent ke)
        {
        }
    }
    
    void move()
    {
        Rectangle rec = new Rectangle(ar[z].getBounds());
        AffineTransform at;
        //00,10.01.11.02,12
        if(rec.x <=10)//or ==
        {
            l = false;
        }
        if((rec.x + rec.width)>=490)//or ==
        {
            r=false;
        }
         if((rec.y + rec.height)>=610)//or ==
        {
            d=false;
        }
        if(l==true)
        {
            //poly[z].translate(-40,0);
            at = new AffineTransform(1,0,0,1,-40,0);
            ar[z] = ar[z].createTransformedArea(at);
            for(int i=0; i<z; i++)
            {
                if(intersectsShape(ar[z],ar[i]))
                {
                    at = new AffineTransform(1,0,0,1,40,0);
                    ar[z] = ar[z].createTransformedArea(at);
                    //poly[z].translate(40,0);
                }
            }
        }
        if(r==true)
        {
            //poly[z].translate(40,0);
            at = new AffineTransform(1,0,0,1,40,0);
            ar[z] = ar[z].createTransformedArea(at);
             for(int i=0; i<z; i++)
            {
                if(intersectsShape(ar[z],ar[i]))
                {
                    at = new AffineTransform(1,0,0,1,-40,0);
                    ar[z] = ar[z].createTransformedArea(at);
                    //poly[z].translate(-40,0);
                }
            }
            
        }
        if(d==true)
        {
            //poly[z].translate(0,40);
            at = new AffineTransform(1,0,0,1,0,40);
            ar[z] = ar[z].createTransformedArea(at);
        }
        /*if(u==true)
        {
            AffineTransform at = new AffineTransform();
            at.rotate(Math.PI/2,rec.x+rec.width/2,rec.y+rec.height/2);
            Graphics2D g2d = (Graphics2D)dbg;
            //poly[z] =(Polygon) (at.createTransformedShape(poly[z]));
            Area a = new Area(at.createTransformedShape(poly[z]));
            g2d.setColor(Color.red);
            g2d.fill(a);
        }*/
        //poly[z].invalidate();
        //f.repaint();
    }
    
    /*boolean intersectsPolygon(Polygon shapeA, Polygon shapeB)
    {  
        //shapeA.translate(0,40);
        Area areaA = new Area(shapeA);
        Area areas =areaA.createTransformedArea(new AffineTransform(1,0,0,1,0,40));
        areas.intersect(new Area(shapeB));
        return !areas.isEmpty();
    }*/
    boolean intersectsShape(Area shapeA, Area shapeB)
    {  
        //shapeA.translate(0,40);
        Area areaA = new Area(shapeA);
        Area areas =areaA.createTransformedArea(new AffineTransform(1,0,0,1,0,40));
        areas.intersect(new Area(shapeB));
        return !areas.isEmpty();
    }
        
    /*void go()
    {
        /*if(z>0)
        {
            f.getContentPane().remove(p);
        }
        p = new JPanel();
        
        pn = new JLabel("NAME : " +name);
        lev = new JLabel("LEVEL : "+ le);
        sc = new JLabel("YOUR SCORE : " + s);
        obj.changeLabels();
        System.out.println("YOUR SCORE : " + s);
        pau = new JButton("Press to Pause");
        esc = new JButton("Press to Exit");
        int h = 0;
        hsc = new JLabel("HIGH SCORE: " + h);
        
        Font lf = new Font("serif",Font.BOLD + Font.ITALIC, 15);
        pn.setFont(lf);
        lev.setFont(lf);
        sc.setFont(lf);
        hsc.setFont(lf);
        pau.setFont(lf);
        esc.setFont(lf);
        pn.setForeground(Color.white);
        lev.setForeground(Color.white);
        sc.setForeground(Color.white);
        hsc.setForeground(Color.white);
        //pau.setForeground(Color.white);
        //esc.setForeground(Color.white); 
        //pau.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.cyan));
        pau.addActionListener(new PauseList());
        //esc.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.cyan));
        esc.addActionListener(new ExitListener());
        
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBackground(new Color(10,100,50));
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
       
        p.add(Box.createRigidArea(new Dimension(0,35)));
        p.add(pn);
        p.add(Box.createRigidArea(new Dimension(0,30)));
        p.add(lev);
        p.add(Box.createRigidArea(new Dimension(0,15)));
        p.add(sc);
        p.add(Box.createRigidArea(new Dimension(0,15)));
        p.add(hsc);
        p.add(Box.createRigidArea(new Dimension(0,35)));
        p.add(pau);
        p.add(Box.createRigidArea(new Dimension(0,10)));
        p.add(esc);
        f.getContentPane().add(BorderLayout.EAST,p);
        //Lshape dp = new Lshape();

        f.getContentPane().add(BorderLayout.CENTER,dp);
    }*/
    
    /*void changeLabels()
    {
        lev = new JLabel("LEVEL : "+ le);
        sc = new JLabel("YOUR SCORE : " + s);
    }/*/
    boolean collisionWithSideWalls(Area p)
    {
        Rectangle r = new Rectangle(p.getBounds());
        if(r.x <10 || (r.x + r.width)>490)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    boolean collisionWithBottomWall(Area p)
    {
        Rectangle r = new Rectangle(p.getBounds());
        if((r.y + r.height)>=610)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /*public void paint(Graphics g)
    {
        dbimage = f.createImage(a,b);
        dbg = dbimage.getGraphics();
        //dbg = g;
        dp.paintComponent(dbg);
        //draw(dbg);
        g.drawImage(dbimage,0,0,f);
    }*/
    void getName()
    {
        name= JOptionPane.showInputDialog("Your name please: ");
    }
    /*void draw(Graphics g)
    {
        g.setColor(Color.green);
        g.fillPolygon(poly[z-1]);
        g.setColor(Color.black);
        g.drawPolygon(poly[z-1]);
    }*/
    class DrawPanel1 extends JPanel
    {
        public void paintComponent(Graphics g)
        {
            //setBorder(BorderFactory.createMatteBorder(10,10,10,10,Color.cyan));
            //setBackground(Color.black);
            g.setColor(Color.cyan);
            //g.fillRect(0,0,this.getWidth(),this.getHeight());
            g.fillRect(0,0,500,620);
            g.setColor(Color.black);
            //g.fillRect(10,10,(this.getWidth()-19),(this.getHeight()-20));
            g.fillRect(10,10,480,600);
            g.setColor(Color.white);
            //a = this.getWidth()-19;
            //b = this.getHeight()-21;
            /*g.drawString("w=" + (this.getWidth()-19),200,200);
            g.drawString("h="  +(this.getHeight()-21),250,200);*/
            Graphics2D g2d = (Graphics2D)g;
                Rectangle rec = new Rectangle(ar[z].getBounds());
               /* g.drawString("x=" + (rec.x),300,200);
                g.drawString("y=" + (rec.y),350,200); */
                g2d.setColor(new Color(10,100,50));
                g2d.fillRect(500,0,170,650);
                g2d.setColor(Color.white);
                Font lf = new Font("serif",Font.BOLD + Font.ITALIC, 12);
                g2d.setFont(lf);
                g2d.drawString("NAME : " + name,510,50);
                g2d.drawString("LEVEL : "+le,510,100);
                g2d.drawString("YOUR SCORE: " + s,510,150);
                
                g2d.drawString("HIGH SCORE: "+h ,510,180);
                setLayout(null);
                
                pau = new JButton("Press to Pause");
                esc = new JButton("Press to Exit");
                pau.setFont(lf);
                esc.setFont(lf);
                pau.setBounds(512,210,130,30);
                esc.setBounds(512,250,130,30);
                pau.addActionListener(new PauseList());
                esc.addActionListener(new ExitListener());
                add(pau);
                add(esc);
            if(u==true)
            {
                y++;
                AffineTransform at = new AffineTransform();
               
                if(shapeNo[z]==5)
                {
                    at.rotate(Math.PI/2,rec.x+rec.width/2,rec.y+rec.height/2);

                }
                
                else if(y%2!=0)
                {
                    at.rotate(Math.PI/2,rec.x+rec.width/2-20,rec.y+rec.height/2);  
                }
                else 
                {
                    at.rotate(Math.PI/2,rec.x+rec.width/2+20,rec.y+rec.height/2);  
                }
                ar[z] = ar[z].createTransformedArea(at);
                rec = new Rectangle(ar[z].getBounds());
                int fl=0;
                for(int i=0;i<z;i++)
                {
                    if(intersectsShape(ar[z],ar[i]))
                    {
                        fl=1;
                    }
                }
                if(collisionWithBottomWall(ar[z])||collisionWithSideWalls(ar[z])||fl==1)
                {
                    AffineTransform at1 = new AffineTransform();
                    if(shapeNo[z]==5)
                    {
                        at1.rotate(-Math.PI/2,rec.x+rec.width/2,rec.y+rec.height/2);
    
                    }
                    
                    else if(y%2!=0)
                    {
                        at1.rotate(-Math.PI/2,rec.x+rec.width/2+20,rec.y+rec.height/2);  
                    }
                    else 
                    {
                        at1.rotate(-Math.PI/2,rec.x+rec.width/2-20,rec.y+rec.height/2);  
                    }
                    ar[z] = ar[z].createTransformedArea(at1);
                    fl=0;
                }
                /*g2d.setColor(Color.white);
                g2d.fill(at.createTransformedShape(poly[z]));
                Area a = new Area(at.createTransformedShape(poly[z]));
                g2d.setColor(Color.red);
                g2d.fill(a);*/
                
            }
            for(int i =0; i<=z;i++)
            {
                g.setColor(rand_col[i]);
                //g.fillPolygon(poly[i]);
                g2d.fill(ar[i]);
                g.setColor(Color.white);
                //g.drawPolygon(poly[i]);
                g2d.draw(ar[i]);
            }
                
        }
    }

    class PauseList implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            paused=true;
            pausedf = new JFrame("Paused");
            //pausedf.setSize((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
            pausedf.setSize(680,660);
            f.setVisible(false);
            f.dispose();
            //exit.setVisible(false);
            pausedf.setVisible(true);
            pausedf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JPanel pausedp = new JPanel();
            //pausedp.setBounds(0,0,650,650);
            pausedp.setBackground(Color.blue);
            pausedf.getContentPane().add(BorderLayout.CENTER,pausedp);
            //pausedp.setOpaque(false);
            //pausedf.add(pausedp);
            JLabel dis= new JLabel("THE GAME HAS BEEN PAUSED");
            JLabel ask = new JLabel("--> TO RESUME PRESS CONTINUE");
            Font lf = new Font("serif",Font.BOLD + Font.ITALIC, 20);
            ask.setFont(lf);
            ask.setForeground(Color.white);
            dis.setFont(lf);
            dis.setForeground(Color.white);
            
            JLabel ask1 = new JLabel("--> TO EXIT PRESS ESCAPE");
            ask1.setFont(lf);
            ask1.setForeground(Color.white);
            
            JButton con = new JButton("CONTINUE");
            JButton ex = new JButton("ESCAPE");
            //Font lf1 = new Font("timesNewRoman",Font.BOLD + Font.ITALIC + Font.CENTER_BASELINE, 18);
            con.setFont(lf);
            //con.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.black));
            con.addActionListener(new ContListener());
            ex.setFont(lf);
            ex.addActionListener(new ExitListener());
            //ex.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.black));
            
            pausedp.setLayout(new BoxLayout(pausedp,BoxLayout.Y_AXIS));
            pausedp.add(Box.createRigidArea(new Dimension(150,120)));
            pausedp.add(dis);
            pausedp.add(Box.createRigidArea(new Dimension(150,30)));
            pausedp.add(ask);
            pausedp.add(Box.createRigidArea(new Dimension(170,20)));
            pausedp.add(ask1);
            pausedp.add(Box.createRigidArea(new Dimension(200,100)));
            
            //pausedp.setLayout(new BoxLayout(pausedp,BoxLayout.X_AXIS));
            //pausedp.add(Box.createRigidArea(new Dimension(50,500)));
            pausedp.add(con);
            pausedp.add(Box.createRigidArea(new Dimension(250,20)));
            pausedp.add(ex);
            
        }
    }
    class ContListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e)
        {
            paused = false;
            pausedf.setVisible(false);
            if(exitflag)
            {
                exit.setVisible(false);
            }
            //f = new JFrame("Tetris");
            f.setVisible(true);
            f.repaint();
            //obj.run();
        }
    }
    class ExitListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(paused == true)
                  pausedf.setVisible(false);
            paused = true;
            exitflag = true;
            exit = new JFrame("EXIT");
            JPanel exitp = new JPanel();
            
            exit.setSize(680,660);

            f.setVisible(false);
            exit.setVisible(true);
            exit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            exitp.setBackground(Color.blue);
            exit.getContentPane().add(BorderLayout.CENTER,exitp);
            
            JLabel ask = new JLabel("ARE YOU SURE YOU WANT TO EXIT?");
            Font lf = new Font("serif",Font.BOLD + Font.ITALIC, 20);
            ask.setFont(lf);
            ask.setForeground(Color.white);
            
            JButton yes = new JButton("YES");
            JButton no = new JButton("NO");
            yes.setFont(lf);
            no.setFont(lf);
            yes.addActionListener(new Exit());
            no.addActionListener(new PauseList());
            
            exitp.setLayout(new BoxLayout(exitp,BoxLayout.Y_AXIS));
            exitp.add(Box.createRigidArea(new Dimension(150,100)));
            exitp.add(ask);
            exitp.add(Box.createRigidArea(new Dimension(170,70)));
            exitp.add(yes);
            exitp.add(Box.createRigidArea(new Dimension(250,20)));
            exitp.add(no);
            
        }
    }
    class Exit implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            f.dispose();
            exit.dispose();
            pausedf.dispose();
        }
    }
    
    class MoveThread implements Runnable
    {
        public void run()
        {
            while(true)
            {
                if(!paused)
                {
                    //poly[z].translate(0,40);
                    AffineTransform at;
                    at = new AffineTransform(1,0,0,1,0,40);
                    ar[z] = ar[z].createTransformedArea(at);
                    int delay;
                    switch(le)
                    {
                        case 1 : delay = 600;
                                break;
                        case 2: delay = 500;
                                break;
                        case 3: delay = 400;
                                break;
                        default:delay = 500;
                                break;
                    }
                    try
                    {
                        Thread.sleep(delay);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        
    }
}

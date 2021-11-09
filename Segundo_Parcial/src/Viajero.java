import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Viajero extends JFrame implements Runnable{
    Container c;
    Mapa mapa;
    JList Caminos;
    Vector<String> cam;
    JButton bt_comenzar,bt_camino;
    JList<String> list;
    JScrollPane pane;
    JComboBox<String> combo;
    int nodo_inicial,camino=-1;
    float[][] mat;
    Tablero tab;
    Thread hilo;
    Viajero yo;

    Viajero()
    {
        this.setTitle("AGENTE VIAJERO");
        c = this.getContentPane();
        c.setLayout(null);
        yo=this;
        Caminos = new JList();
        cam= new Vector<>();
        tab = new Tablero();
        mapa = new Mapa();
        mapa.setBounds(10,10,500,500);
        c.add(mapa);
        bt_comenzar = new JButton("Aceptar");
        bt_camino   = new JButton("Generar");
        bt_comenzar.addActionListener(e -> {
            cam.clear();
            int tam=getCam();
            String[] vec = new String[tam];
            for (int i=0;i<tam;i++)
            {
                vec[i]=""+(i+1);
                combo.addItem(vec[i]);
            }
            list.updateUI();
            list.setListData(cam);
            pane.updateUI();
            bt_comenzar.setVisible(false);
            bt_camino.setVisible(true);
            Matriz(tam);
            tab.setMatriz(mat);
        });
        list  = new JList<>();
        pane  = new JScrollPane(list);
        combo = new JComboBox<>();
        combo.setBounds(511,10,100,20);
        bt_comenzar.setBounds(511,240,100,20);
        bt_camino.setBounds(511,240,100,20);
        bt_camino.setVisible(false);
        pane.setBounds(511,35,100,200);
        tab.setBounds(511,270,800-511,200);
        c.add(tab);
        c.add(combo);
        c.add(bt_comenzar);
        c.add(bt_camino);
        c.add(pane);
        bt_camino.addActionListener(e -> {
            int tam=getCam();
            String temp="";
            nodo_inicial=combo.getSelectedIndex()+1;
            cam.clear();
            Sig(combo.getSelectedIndex(),tam,0,temp);
            list.updateUI();
            hilo = new Thread(yo);
            hilo.start();
        });
    }

    public int getCam()
    {
        return mapa.getCamino();
    }

    public void run()
    {
        int i=0;float menor=getValcad(""+cam.elementAt(0)),m;
        while (i<cam.size())
        {
            try
            {
                String cad;
                cad=""+cam.elementAt(i);
                m=getValcad(cad);
                System.out.println (cad.charAt(0) +" "+(i+1)+" "+m);
                mostrar(cad);
                Thread.sleep(10);
                if (m<=menor)
                {
                    camino=i;
                    menor=m;
                    System.out.println (" MENOR " +menor);
                }
                i++;
            }
            catch (Exception ignored)
            {
            }
        }
        mostrar(""+cam.elementAt(camino));
        JOptionPane.showMessageDialog(tab,menor+" en el camino "+"\n"+cam.elementAt(camino),"Mejor Ruta", JOptionPane.INFORMATION_MESSAGE);
    }

    public void Sig(int i, int n, int p, String acum)
    {
        if (p<n && i< n)// si se acabo un para o un contador
        {
            boolean no=false;
            for(int k = 0; k<acum.length(); k++)//si ya esta el numero de lo vuelo
            {
                if (acum.substring(k, k + 1).equals("" + (i + 1))) {
                    no = true;
                    break;
                }
            }
            if(!no) Sig(0,n,p+1,acum+""+(i+1));
            Sig(i+1,n,p,acum);
            no=false;
            for(int k = 0; k<acum.length(); k++)
            {
                if (acum.substring(k, k + 1).equals("" + (i + 1))) {
                    no = true;
                    break;
                }
            }
            if(!no && acum.length()==n-1)
            {
                acum=acum+""+(i+1)+nodo_inicial;
                if(acum.startsWith(""+nodo_inicial))
                    cam.add(acum);
            }
        }
    }

    public float getDistancia(String i, String j)
    {
        int a,b,c,d;
        float r,r1;
        a =mapa.getx(Integer.parseInt(i));
        b =mapa.getx(Integer.parseInt(j));
        c =mapa.gety(Integer.parseInt(i));
        d =mapa.gety(Integer.parseInt(j));
        r=(float)Math.pow(b-a,2);r1=(float)Math.pow(d-c,2);
        return (float)Math.sqrt((r+r1));
    }

    public void Matriz(int n)
    {
        mat = new float[n][n];
        for (int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                mat[i][j]=getDistancia(""+i,""+j);
            }
        }
    }

    public float getValcad(String cadena)
    {
        float num=0;
        int a,b;
        for (int i=0;i<cadena.length()-1;i++)
        {
            a=Integer.parseInt(cadena.substring(i,i+1));
            b=Integer.parseInt(cadena.substring(i+1,i+2));
            num=num+mat[a-1][b-1];
        }
        return num;
    }

    public void mostrar (String camino)
    {
        mapa.setResultado(camino);
    }

    public static void main (String[] arg)
    {
        Viajero frame = new Viajero();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setUndecorated(false);
        frame.setSize(size.width,size.height);
        frame.setVisible(true);
    }

    static class Mapa extends JPanel
    {
        int X,Y,y;
        Image Buffer;
        Graphics2D  graphic;
        Point[] puntos;
        int n_puntos=0;
        Vector<String> pos;
        Vector<Integer> x;
        Vector<Integer> y1;
        boolean limite=false;
        boolean result=false;
        String camino;

        Mapa()
        {
            this.setBackground(Color.black);
            puntos = new Point[100];
            pos    = new Vector<>();
            y1     = new Vector<>();
            x      = new Vector<>();
            this.setBorder( BorderFactory.createBevelBorder(1));
            this.addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseMoved(MouseEvent e)
                {
                    X=e.getX()/20;
                    y=e.getY();
                    Y= ((y-getHeight())*-1);
                    repaint();
                }
            });
            this.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked (MouseEvent e)
                {
                    if(!limite)
                    {
                        if (!((X==24)||(Y/20==24)))
                        {
                            puntos[n_puntos++]=e.getPoint();
                            pos.add("("+e.getX()/20+","+Y/20+")");
                            x.add(e.getX()/20);y1.add(Y/20);
                            repaint();
                            if(n_puntos==20)
                            {
                                limite=true;
                                JOptionPane.showMessageDialog(null,"LLegaste al limite de nodos","Info",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            });
        }

        public void update(Graphics g)
        {
            paint(g);
        }

        public void paintComponent(Graphics g)
        {
            Buffer = this.createImage(this.getWidth(),this.getWidth());
            graphic =(Graphics2D)Buffer.getGraphics();
            pintar(graphic);
            g.drawImage(Buffer,0,0,this.getWidth(),this.getWidth(),this);
        }

        public void pintar(Graphics2D g)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //para mejorar la calidad de la pintada
            g.setColor(Color.white);
            g.fillRect(0,0,getWidth(),getHeight() );
            g.setColor(Color.LIGHT_GRAY);
            for(int i=0;i<100;i+=4)
            {
                g.drawLine(5*i,500,5*i,0);
                g.drawLine(0,5*i,500,5*i);
            }
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD,12));
            g.drawString("("+X+","+Y/20+")",X*20 +2,y);
            if (this.result)
                this.resultado(g);
            for (int i=0;i<n_puntos;i++)
            {
                g.setColor(Color.BLUE);
                g.fillOval(puntos[i].x-8,puntos[i].y-8,18,18);
                g.setColor(Color.white);
                g.drawString(""+(i+1),(puntos[i].x-10) +9/2,(puntos[i].y-9)+15);
                g.setColor(Color.black);
                g.drawString(""+pos.elementAt(i),puntos[i].x-9,puntos[i].y-18);
            }
        }
        public int getCamino()
        {
            return this.n_puntos;
        }

        public int getx(int n)
        {
            return Integer.parseInt( ""+x.elementAt(n));
        }

        public int gety(int n)
        {
            return Integer.parseInt( ""+y1.elementAt(n));
        }

        public void setResultado(String camino)
        {
            this.result=true;
            this.camino=camino;
            repaint();
        }

        public void resultado(Graphics2D g )
        {
            int temp,temp2;
            g.setColor(Color.red);
            for(int i=0;i<camino.length()-1;i++)
            {
                temp=Integer.parseInt(""+camino.charAt(i));
                temp2=Integer.parseInt(""+camino.charAt(i+1));
                g.drawLine(puntos[temp-1].x,puntos[temp-1].y,puntos[temp2-1].x,puntos[temp2-1].y);
                g.drawString( ""+(i+1), (((puntos[temp-1].x+puntos[temp2-1].x)/2)+puntos[temp-1].x)/2, (((puntos[temp-1].y+puntos[temp2-1].y)/2)+puntos[temp-1].y)/2);
            }
        }
    }

    static class Tablero extends JPanel
    {
        JTextField[][] m;
        Tablero ()
        {
        }
        public void setMatriz(float[][] mat)
        {
            this.removeAll();
            m = new JTextField[mat.length][mat[0].length];
            this.setLayout(new GridLayout(mat.length,mat[0].length) );
            for (int i = 0; i<mat.length; i++)
            {  for (int j = 0; j<mat[0].length; j++)
            {
                m[i][j]= new JTextField();
                m[i][j].setEditable(false);
                m[i][j].setAutoscrolls(false);
                m[i][j].setFont((new Font("Arial", Font.BOLD,10)));
                m[i][j].setText(""+mat[i][j]);
                m[i][j].setCaretPosition(0);
                add(m[i][j]);
            }
            }
            this.updateUI();
        }
    }
}
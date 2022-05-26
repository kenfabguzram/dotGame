package Client;
import java.awt.event.*;

import javax.swing.JFrame;

import Common.Casilla;
import Common.Constantes;
import Common.Dot;
import Common.Mapa;
import Common.Target;

import java.net.*;
import java.io.*;

public class GUI implements ActionListener, Constantes{

    JFrame ventana;
    Mapa mapa;
    Target target;

    Dot dot;

    Socket client;
    ObjectOutputStream output;
    
    public GUI(){

        ventana = new JFrame();
        mapa = new Mapa(this);


        ventana.add(mapa.panelTablero);

        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.pack();
        ventana.setVisible(true);
        dot=new Dot();
        target = new Target();
        Server server = new Server(dot);
        Thread hilo = new Thread(server);
        hilo.start();

        moveDot();
        run();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(dot.currentPosition!=target.coords)
            mapa.tablero[target.coords[X]][target.coords[Y]].clearTarget();
        ((Casilla)e.getSource()).setAsTarget();
        target.coords = ((Casilla)e.getSource()).getCoords();

        try {
            client = new Socket("127.0.0.1", 4444);
            output = new ObjectOutputStream(client.getOutputStream());
            output.writeObject(target);
            output.flush();
            output.close();
            client.close();
        } catch (Exception ex) {
            //TODO: handle exception
        }
    }

    public void moveDot(){
        mapa.tablero[dot.lastPosition[X]][dot.lastPosition[Y]].clearDot();
        mapa.tablero[dot.currentPosition[X]][dot.currentPosition[Y]].setAsDot();
    
    }

    public void run(){
        mapa.tablero[dot.currentPosition[X]][dot.currentPosition[Y]].setAsDot();
        while (true){
            moveDot();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

}
package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;

import Common.Constantes;
import Common.Dot;
import Common.Mapa;

public class GUI implements ActionListener, Constantes{
    JFrame ventana;
    Mapa mapa;
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

        dot = new Dot();

        Server server = new Server(dot);
        Thread hilo = new Thread(server);
        hilo.start();

        moveDot();
        run();

    }

    @Override
    public void actionPerformed(ActionEvent e) {  
    }

    public void moveDot(){
        mapa.tablero[dot.lastPosition[X]][dot.lastPosition[Y]].clearDot();
        mapa.tablero[dot.currentPosition[X]][dot.currentPosition[Y]].setAsDot();
    }

    public void run(){
        while (true){
            dot.move();
            try {
                client = new Socket("127.0.0.1", 4445);
                output = new ObjectOutputStream(client.getOutputStream());
                output.writeObject(dot);
                output.flush();
                output.close();
                client.close();
            } catch (Exception ex) {
                //TODO: handle exception
            }
            moveDot();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

}
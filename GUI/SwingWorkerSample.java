package GUI;
// Java program to illustrate
// working of SwingWorker
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;


public class SwingWorkerSample
{

    private static JLabel statusLabel;
    private static JFrame mainFrame;

    private static JLabel statusLabel2;
    private static JFrame mainFrame2;

    public static void swingWorkerSample ()
    {
        mainFrame = new JFrame("Swing Worker0");
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new GridLayout(2,1));

        mainFrame.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        });

        statusLabel = new JLabel("Not Completed", JLabel.CENTER);
        mainFrame.add(statusLabel);

        JButton btn = new JButton("Start counter");
        btn.setPreferredSize(new Dimension(5,5));

        btn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Button clicked, thread started");
                startThread(0);
            }

        });

        mainFrame.add(btn);
        mainFrame.setVisible(true);

    //the second one

        mainFrame2 = new JFrame("Swing Worker1");
        mainFrame2.setSize(400, 400);
        mainFrame2.setLayout(new GridLayout(2,1));

        mainFrame2.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        });

        statusLabel2 = new JLabel("Not Completed", JLabel.CENTER);
        mainFrame2.add(statusLabel2);

        JButton btn2 = new JButton("Start counter");
        btn2.setPreferredSize(new Dimension(5,5));

        btn2.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Button2 clicked, thread started");
                startThread(1);
            }

        });

        mainFrame2.add(btn2);
        mainFrame2.setVisible(true);



    }

    private static void startThread(int i)
    {

        SwingWorker sw1 = new SwingWorker()
        {

            @Override
            protected String doInBackground() throws Exception
            {
                // define what thread will do here
                for ( int i=10; i>=0; i-- )
                {
                    Thread.sleep(500);
                    if(i==0) {
                        System.out.println("\033[0;31m"+"Value in thread 0: " + i+"\033[0m");
                    }else{
                        System.out.println("\033[0;34m"+"Value in thread 1: " + i+"\033[0m");
                    }
                    publish(i);
                }

                String res = "Finished Execution";

                return res;
            }

            @Override
            protected void process(List chunks)
            {
                // define what the event dispatch thread
                // will do with the intermediate results received
                // while the thread is executing
                int val = (int)chunks.get(chunks.size()-1);
                if(i==0) {
                    statusLabel.setText(String.valueOf(val));
                }else{
                    statusLabel2.setText(String.valueOf(val));
                }
            }

            @Override
            protected void done()
            {
                // this method is called when the background
                // thread finishes execution
                try {
                    String statusMsg = (String) get();
                    if (i == 0) {
                        System.out.println("\033[0;31m"+"Inside done function"+"\033[0m");
                        statusLabel.setText(statusMsg);
                    } else {
                        System.out.println("\033[0;34m"+"Inside done function"+"\033[0m");
                        statusLabel2.setText(statusMsg);

                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
            }
        };

        // executes the swingworker on worker thread
        sw1.execute();
    }

    public static void main(String[] args)
    {
//        swingWorkerSample();
        JFrame mainFrame = new JFrame("Swing Worker0");
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new GridLayout(2,1));

        mainFrame.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

        });

        JButton button = new JButton("button");
        button.setPreferredSize(new Dimension(100,100));
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("button");
            }
        });

        mainFrame.add(button);
        mainFrame.setVisible(true);
        mainFrame.pack();


    }

}

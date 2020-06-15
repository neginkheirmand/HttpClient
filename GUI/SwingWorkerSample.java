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

    public static void swingWorkerSample ()
    {
        mainFrame = new JFrame("Swing Worker");
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
                startThread();
            }

        });

        mainFrame.add(btn);
        mainFrame.setVisible(true);
    }

    private static void startThread()
    {

        SwingWorker sw1 = new SwingWorker()
        {

            @Override
            protected String doInBackground() throws Exception
            {
                // define what thread will do here
                for ( int i=10; i>=0; i-- )
                {
                    Thread.sleep(100);
                    System.out.println("Value in thread : " + i);
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

                statusLabel.setText(String.valueOf(val));
            }

            @Override
            protected void done()
            {
                // this method is called when the background
                // thread finishes execution
                try
                {
                    String statusMsg = (String) get();
                    System.out.println("Inside done function");
                    statusLabel.setText(statusMsg);

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
        swingWorkerSample();

    }

}

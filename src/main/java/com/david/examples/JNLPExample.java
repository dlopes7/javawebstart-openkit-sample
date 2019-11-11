package com.david.examples;

import javax.swing.*;
import com.dynatrace.openkit.DynatraceOpenKitBuilder;
import com.dynatrace.openkit.api.OpenKit;
import com.dynatrace.openkit.api.RootAction;
import com.dynatrace.openkit.api.Session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JNLPExample extends JFrame {

    private static final long serialVersionUID = 4968624166243565348L;

    private JLabel label = new JLabel("Hello from Java Code Geeks!");
    private JButton button1 = new JButton("Action 1");
    private JButton button2 = new JButton("Action 2");

    private OpenKit openKit;
    private Session session;

    public JNLPExample() {
        super("Java Web Start Example");
        this.setSize(350, 200);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);

        this.openKit = new DynatraceOpenKitBuilder("https://beacon.url.com/mbeacon",
                 "my-app-id", 1)
                .withApplicationName("Java WebStart")
                .withApplicationVersion("1.0")
                .withOperatingSystem(System.getProperty("os.name"))
                .build();
        openKit.waitForInitCompletion();

        session = openKit.createSession("127.0.0.1");
        session.identifyUser("David Lopes");

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                session.end();
                e.getWindow().dispose();
            }
        });
    }

    public void addButtons() {
        label.setSize(200, 30);
        label.setLocation(80, 0);
        this.getContentPane().add(label);

        button1.setSize(80, 40);
        button1.setLocation(10, 100);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Clicked Button 1...");
                RootAction rootAction = session.enterAction("Click Button 1");
                try {
                    Thread.sleep(1000);
                    rootAction.leaveAction();
                    session.end();
                }
                catch (InterruptedException ex) {
                    assert true;
                }
            }
        });
        this.getContentPane().add(button1);

        button2.setSize(80, 40);
        button2.setLocation(90, 100);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Clicked Button 2...");

                RootAction rootAction = session.enterAction("Click Button 2");
                try {
                    Thread.sleep(2000);
                    rootAction.leaveAction();

                }
                catch (InterruptedException ex) {
                    assert true;
                }
            }
        });
        this.getContentPane().add(button2);

    }

    public static void main(String[] args) {
        JNLPExample exp = new JNLPExample();
        exp.addButtons();
        exp.setVisible(true);
    }
}
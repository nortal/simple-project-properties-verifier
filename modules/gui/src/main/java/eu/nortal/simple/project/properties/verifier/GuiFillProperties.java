package eu.nortal.simple.project.properties.verifier;

import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.NotImplementedException;

/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class GuiFillProperties extends Frame implements FillProperties {

    private static final long serialVersionUID = 1L;

    public GuiFillProperties() {
        super();
    }

    @Override
    public void run() {
        final JPanel mainPanel = new JPanel();
        JPanel panelOne = new JPanel();
        JPanel panelTwo = new JPanel();
        final CardLayout card = new CardLayout();
        mainPanel.setLayout(card);
        mainPanel.add(panelOne, "one"); // id one refers panelOne
        mainPanel.add(panelTwo, "two"); // id two refers panelTwo

        panelOne.add(new JLabel("first panel"));
        JButton btn1 = new JButton("Show second");
        panelOne.add(btn1);
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                card.show(mainPanel, "two"); // shows panelTwo
            }
        });

        panelTwo.add(new JLabel("second panel"));
        JButton btn2 = new JButton("Show First");
        panelTwo.add(btn2);
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                card.show(mainPanel, "one"); // shows panelOne
            }
        });

        add(mainPanel);
        setVisible(true);

    }

    @Override
    public void verify() {
        throw new NotImplementedException("Method verify is not implemeted");

    }
}

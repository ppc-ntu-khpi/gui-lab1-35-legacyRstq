package com.mybank.gui;

import com.mybank.data.DataSource;
import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import com.mybank.reporting.CustomerReport;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class SWINGDemo {

    private final JEditorPane log;
    private final JButton show;
    private final JComboBox clients;
    private final JButton report;
    private final JScrollPane scrollPane;

    public SWINGDemo() {
        log = new JEditorPane("text/html", "");
        scrollPane = new JScrollPane(log);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 450));
        show = new JButton("Show");
        report = new JButton("Report");
        clients = new JComboBox();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            clients.addItem(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }

    }

    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());
        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 2));

        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);
        frame.add(cpane, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer current = Bank.getCustomer(clients.getSelectedIndex());
                log.setText(getCustomerDetails(current));
            }
        });

        report.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.setText(getReport());
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {

        DataSource dataSource = new DataSource("data\\test.dat");
        dataSource.loadData();

        SWINGDemo demo = new SWINGDemo();
        demo.launchFrame();
    }

    private String getReport() {
        StringBuilder report = new StringBuilder();
        report.append("&nbsp;<b><span style=\"font-size:2.5em; text-align:center;\">Report</span><br>");
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            report.append(getCustomerDetails(Bank.getCustomer(i))).append("<br>");
        }
        return report.toString();
    }

    private String getCustomerDetails(Customer customer) {
        StringBuilder details = new StringBuilder();
        details.append("<br>&nbsp;<b><span style=\"font-size:2em;\">").append(customer.getFirstName()).append(" ")
                .append(customer.getLastName()).append("</span><br><hr>");
        for (int i = 0; i < customer.getNumberOfAccounts(); i++) {
            Account account = customer.getAccount(i);
            if (account != null) {
                if (account instanceof SavingsAccount) {
                    details.append("&nbsp;<b>Acc Type: </b>Savings")
                            .append("<br>&nbsp;<b>Balance: <span style=\"color:red;\">$").append(account.getBalance())
                            .append("</span></b><br>");
                } else if (account instanceof CheckingAccount) {
                    details.append("&nbsp;<b>Acc Type: </b>Checking")
                            .append("<br>&nbsp;<b>Balance: <span style=\"color:red;\">$").append(account.getBalance())
                            .append("</span></b>");
                }
            }
        }
        return details.toString();
    }
}
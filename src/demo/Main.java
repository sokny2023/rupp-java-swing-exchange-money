package demo;

import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExchangeMoney::new);
    }
}

class ExchangeMoney extends JFrame {
    private final JTextField moneyInputField;
    private final JTextField outputReadEnglish;
    private final JTextField outputReadKhmer;
    private final JTextField outputResultMoney;

    public ExchangeMoney() {
        // Set up the frame
        setTitle("KHR Input Application");
        setSize(500, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        JLabel promptLabel = new JLabel("Enter amount in KHR: ");
        promptLabel.setBounds(20, 10, 150, 25);
        add(promptLabel);

        moneyInputField = new JTextField();
        moneyInputField.setBounds(160, 10, 120, 25);
        add(moneyInputField);

        // button calculate
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(30, 100, 120, 25);
        add(calculateButton);

        // button clear
        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(180,100,80,25);
        add(clearButton);

        //read word in English
        JLabel outputReadEnglishLabel = new JLabel(" អានជាភាសាអង់គ្លេស៖ ");
        outputReadEnglishLabel.setBounds(20, 150, 150, 25);
        add(outputReadEnglishLabel);
        outputReadEnglish = new JTextField();
        outputReadEnglish.setBounds(160, 150, 300, 25);
        outputReadEnglish.setEditable(false);
        add(outputReadEnglish);

        // read word in Khmer
        JLabel outputReadKhmerLabel = new JLabel(" អានជាភាសាខ្មែរ៖ ");
        outputReadKhmerLabel.setBounds(20, 200, 150, 25);
        add(outputReadKhmerLabel);
        outputReadKhmer = new JTextField();
        outputReadKhmer.setBounds(160, 200,300,25);
        outputReadKhmer.setEditable(false);
        add(outputReadKhmer);

        // output result money
        JLabel resultMoneyLabel = new JLabel(" លទ្ធផលគិតជាដុល្លារ៖ ");
        resultMoneyLabel.setBounds(20,250,300,25);
        add(resultMoneyLabel);
        outputResultMoney = new JTextField();
        outputResultMoney.setBounds(160,250,300,25);
        outputResultMoney.setEditable(false);
        add(outputResultMoney);

        // event
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processInput();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearData();
            }
        });

        setVisible(true);
    }

    private void processInput() {
        String inputText = moneyInputField.getText();
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount in KHR.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int amount = Integer.parseInt(inputText);
            String amountInWordsEnglish = readInEnglish(amount) + " Riel";
            String amountInWordsKhmer = readInKhmer(amount) + " រៀល";
            String resultMoney = resultMoney(amount) + "$";
            //resultOutputField.setText(" "+inputText + " KHR");
            outputReadEnglish.setText(amountInWordsEnglish);
            outputReadKhmer.setText(amountInWordsKhmer);
            outputResultMoney.setText(resultMoney);

            BufferedWriter writer = new BufferedWriter(new FileWriter("money_inputs.txt", true));
            writer.append(inputText).append(" KHR - ").append((char) amount).append("\n");
            writer.close();

            JOptionPane.showMessageDialog(this, "Data stored successfully: " + inputText + " KHR - " + amount, "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException | IOException exception) {
            JOptionPane.showMessageDialog(this, "Error processing input.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearData() {
        moneyInputField.setText("");
        outputReadEnglish.setText("");
        outputReadKhmer.setText("");
        outputResultMoney.setText("");
    }

    private String readInEnglish(int number) {
        String[] units = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
        String[] teens = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        // No need for a separate thousands array since handling is now more dynamic
        if (number == 0) {
            return "Zero";
        }
        String words = "";
        if (number / 1000000 > 0) {
            words += readInEnglish(number / 1000000) + " Million ";
            number %= 1000000;
        }
        if (number / 1000 > 0) {
            words += readInEnglish(number / 1000) + " Thousand ";
            number %= 1000;
        }
        if (number / 100 > 0) {
            words += readInEnglish(number / 100) + " Hundred ";
            number %= 100;
        }
        if (number > 0) {
            if (number < 20) {
                words += (number < 10) ? units[number] : teens[number - 10];
            } else {
                words += tens[number / 10];
                if (number % 10 > 0) {
                    words += " " + units[number % 10];
                }
            }
        }
        return words.trim();
    }


    private String readInKhmer(int number) {
        String[] units = {"", "មួយ", "ពីរ", "បី", "បួន", "ប្រាំ", "ប្រាំមួយ", "ប្រាំពីរ", "ប្រាំបី", "ប្រាំបួន"};
        String[] tens = {"", "ដប់", "ម្ភៃ", "សាមសិប", "សែសិប", "ហាសិប", "ហុកសិប", "ចិតសិប", "ប៉ែតសិប", "កៅសិប"};
        String hundred = "រយ";
        String thousand = "ពាន់";
        String tenThousand = "មុឺន";
        String hundredThousand = "សែន";
        String million = "លាន";

        if (number == 0) {
            return "សូន្យ"; // "Zero" in Khmer
        }

        String words = "";
        if (number / 1000000 > 0) {
            words += units[number / 1000000] + million + " ";
            number %= 1000000;
        }
        if (number / 100000 > 0) {
            words += readInKhmer(number / 100000) + tenThousand + " ";
            number %= 100000;
        }
        if (number / 10000 > 0) {
            words += readInKhmer(number / 100000) + hundredThousand + " ";
            number %= 10000;
        }
        if (number / 1000 > 0) {
            words += readInKhmer(number / 1000) + thousand + " ";
            number %= 1000;
        }
        if (number / 100 > 0) {
            words += units[number / 100] + hundred + " ";
            number %= 100;
        }
        if (number > 0) {
            if (number < 10) {
                words += units[number];
            } else if (number < 20) {
                // Handling 11-19, which follows the pattern "ten-one", "ten-two", etc.
                words += tens[number / 10] + units[number % 10];
            } else {
                words += tens[number / 10];
                if ((number % 10) > 0) {
                    words += " " + units[number % 10];
                }
            }
        }
        return words.trim();
    }

    private int resultMoney(int number){
        return (number / 400);
    }

}

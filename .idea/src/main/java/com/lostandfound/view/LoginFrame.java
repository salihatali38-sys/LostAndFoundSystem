package com.lostandfound.view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    // the window for loging in

    private JTextField studentIdField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheck;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        initializeUI();
    }

    private void initializeUI() {
        // Frame configuration
        setTitle("🔍 AAU Lost & Found Intelligence System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen
        setMinimumSize(new Dimension(700, 500));

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(44, 62, 80); // #2c3e50
                Color color2 = new Color(52, 152, 219); // #3498db
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Login form panel
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        // Title
        JLabel titleLabel = new JLabel("AAU Lost & Found");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Intelligence System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(236, 240, 241)); // #ecf0f1
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));

        // Login form container (white background)
        JPanel formContainer = new JPanel();
        formContainer.setBackground(new Color(255, 255, 255, 230)); // Semi-transparent white
        formContainer.setLayout(new BorderLayout());
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Form title
        JLabel formTitle = new JLabel("Login to Your Account");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitle.setForeground(new Color(44, 62, 80)); // #2c3e50
        formTitle.setHorizontalAlignment(SwingConstants.CENTER);
        formContainer.add(formTitle, BorderLayout.NORTH);

        // Form fields panel
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Student ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentIdLabel.setForeground(new Color(52, 73, 94)); // #34495e
        formPanel.add(studentIdLabel, gbc);

        gbc.gridy = 1;
        studentIdField = new JTextField(15);
        studentIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)), // #bdc3c7
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(studentIdField, gbc);

        // Password
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(52, 73, 94)); // #34495e
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        formPanel.add(passwordField, gbc);

            // Remember me checkbox
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        rememberCheck = new JCheckBox("Remember me");
        rememberCheck.setOpaque(false);
        rememberCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberCheck.setForeground(new Color(127, 140, 141)); // #7f8c8d
        formPanel.add(rememberCheck, gbc);

        // Forgot password link
        gbc.gridy = 5;
        JButton forgotPasswordBtn = new JButton("Forgot Password?");
        forgotPasswordBtn.setBorderPainted(false);
        forgotPasswordBtn.setContentAreaFilled(false);
        forgotPasswordBtn.setForeground(new Color(52, 152, 219)); // #3498db
        forgotPasswordBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordBtn.addActionListener(e -> onForgotPassword());
        formPanel.add(forgotPasswordBtn, gbc);

        formContainer.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Login button
        loginButton = new JButton("Login");
        styleButton(loginButton, new Color(52, 152, 219)); // Blue
        loginButton.addActionListener(e -> onLogin());
        buttonPanel.add(loginButton);

        // Register button
        registerButton = new JButton("Create New Account");
        styleButton(registerButton, new Color(46, 204, 113)); // Green
        registerButton.addActionListener(e -> onRegister());
        buttonPanel.add(registerButton);

        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(formContainer);
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel footerLabel = new JLabel("Addis Ababa University - Computer Science Department");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(255, 255, 255, 180)); // Semi-transparent white
        panel.add(footerLabel);

        return panel;
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    // Event handlers
    private void onLogin() {
        String studentId = studentIdField.getText();
        char[] password = passwordField.getPassword();

        if (studentId.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both student ID and password",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: Implement actual login logic
        JOptionPane.showMessageDialog(this,
                "Login logic to be implemented!\nStudent ID: " + studentId,
                "Login Feature",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void onRegister() {
        // TODO: Open registration frame
        JOptionPane.showMessageDialog(this,
                "Registration form will be implemented soon!",
                "Registration Feature",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void onForgotPassword() {
        JOptionPane.showMessageDialog(this,
                "Password recovery feature coming soon!",
                "Forgot Password",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

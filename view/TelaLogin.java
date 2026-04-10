package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class TelaLogin extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JButton btnAcessar, btnIrCadastro;
    private JLabel lblTitulo;

    public TelaLogin() {
        setLayout(new MigLayout("wrap 2, align center, insets 50", "[right][200!]", "[][][][][]"));

        lblTitulo = new JLabel("Acesso ao Sistema");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(lblTitulo, "span 2, center, gapbottom 30"); 

        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome, "growx");

        add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        add(txtCpf, "growx");

        btnAcessar = new JButton("Entrar");
        add(btnAcessar, "span 2, center, width 120!, gapy 20");

        btnIrCadastro = new JButton("Não tem conta? Cadastre-se");
        btnIrCadastro.setContentAreaFilled(false);
        btnIrCadastro.setBorderPainted(false);
        btnIrCadastro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(btnIrCadastro, "span 2, center");
    }

    public String getNome() { return txtNome.getText(); }
    public String getSenha() { return txtCpf.getText(); }
    
    public void acaoLogin(ActionListener listener) { btnAcessar.addActionListener(listener); }
    public void acaoIrParaCadastro(ActionListener listener) { btnIrCadastro.addActionListener(listener); }

    public void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
    }
}
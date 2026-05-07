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
        add(lblTitulo, "cell 0 0 2 1,alignx center,gapbottom 30"); 

        add(new JLabel("Nome:"), "cell 0 1");
        txtNome = new JTextField();
        add(txtNome, "cell 1 1,growx");

        add(new JLabel("CPF:"), "cell 0 2");
        txtCpf = new JTextField();
        add(txtCpf, "cell 1 2,growx");

        btnAcessar = new JButton("Entrar");
        add(btnAcessar, "cell 0 3 2 1,width 120!,alignx center,gapy 20");

        btnIrCadastro = new JButton("Não tem conta? Cadastre-se");
        btnIrCadastro.setContentAreaFilled(false);
        btnIrCadastro.setBorderPainted(false);
        btnIrCadastro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(btnIrCadastro, "cell 0 4 2 1,alignx center");
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
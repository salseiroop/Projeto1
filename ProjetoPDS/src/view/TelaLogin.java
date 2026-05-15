package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class TelaLogin extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtNome;
    private JFormattedTextField txtCpf;
    private JButton btnAcessar, btnIrCadastro;

    public TelaLogin() {
        // Uso de 'push' nas colunas externas para centralizar o bloco de login
        setLayout(new MigLayout("fill, insets 50", "[grow, push][right][300, grow][grow, push]", "[grow, push][][][][][grow, push]"));

        JLabel lblTitulo = new JLabel("Acesso ao Sistema");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 26));
        add(lblTitulo, "cell 1 0 2 1, center, gapbottom 30"); 

        add(new JLabel("Nome:"), "cell 1 1");
        txtNome = new JTextField();
        add(txtNome, "cell 2 1, growx");

        add(new JLabel("CPF:"), "cell 1 2");
        try {
            MaskFormatter mascara = new MaskFormatter("###.###.###-##");
            mascara.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(mascara);
        } catch (ParseException e) {
            txtCpf = new JFormattedTextField();
        }
        add(txtCpf, "cell 2 2, growx");
                                
        btnAcessar = new JButton("Entrar");
        add(btnAcessar, "cell 1 3 2 1, center, width 150!, height 40!, gapy 20");
                        
        btnIrCadastro = new JButton("Não tem conta? Cadastre-se");
        btnIrCadastro.setContentAreaFilled(false);
        btnIrCadastro.setBorderPainted(false);
        btnIrCadastro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(btnIrCadastro, "cell 1 4 2 1, center");
    }

    public String getNome() { return txtNome.getText(); }
    public String getSenha() { return txtCpf.getText().replaceAll("[^0-9]", ""); }
    public void acaoLogin(ActionListener l) { btnAcessar.addActionListener(l); }
    public void acaoIrParaCadastro(ActionListener l) { btnIrCadastro.addActionListener(l); }

    public void limparCampos() {
        txtNome.setText("");
        txtCpf.setValue(null);
    }
}
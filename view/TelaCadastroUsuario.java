package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class TelaCadastroUsuario extends JPanel {
    
    private static final long serialVersionUID = 1L;
    private JTextField txtNome, txtCPF;
    private JRadioButton rbAdmin, rbCliente;
    private ButtonGroup grupoPerfil;
    private JButton btnSalvar, btnVoltar;

    public TelaCadastroUsuario() {
        setLayout(new MigLayout("wrap 2, align center, insets 40", "[right][250!]", "[][][][][][]"));

        JLabel lblTitulo = new JLabel("Novo Usuário");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(lblTitulo, "span 2, center, gapbottom 20");
        
        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome, "growx");
        
        add(new JLabel("CPF:"));
        txtCPF = new JTextField();
        add(txtCPF, "growx");
        
        add(new JLabel("Tipo de Perfil:"));
        rbAdmin = new JRadioButton("Administrador");
        rbCliente = new JRadioButton("Cliente");
        rbCliente.setSelected(true);
        grupoPerfil = new ButtonGroup();
        grupoPerfil.add(rbAdmin);
        grupoPerfil.add(rbCliente);
        
        JPanel pnlR = new JPanel();
        pnlR.add(rbAdmin); pnlR.add(rbCliente);
        add(pnlR, "growx");

        btnSalvar = new JButton("Cadastrar");
        btnVoltar = new JButton("Voltar");
        add(btnSalvar, "split 2, center, gaptop 15, width 120!");
        add(btnVoltar, "width 100!");
    }

    public String getNome() { return txtNome.getText(); }
    public String getCpf() { return txtCPF.getText(); }
    public boolean isAdministrador() { return rbAdmin.isSelected(); }

    public void acaoCadastrar(ActionListener listener) { btnSalvar.addActionListener(listener); }
    public void acaoVoltar(ActionListener listener) { btnVoltar.addActionListener(listener); }
    
    public void limparCampos() {
        txtNome.setText(""); txtCPF.setText("");
        rbCliente.setSelected(true);
    }
}
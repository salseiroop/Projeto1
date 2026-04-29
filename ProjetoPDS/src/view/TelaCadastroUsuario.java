package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class TelaCadastroUsuario extends JPanel {
    
    private static final long serialVersionUID = 1l;
    private JTextField txtnome, txtcpf;
    private JRadioButton rbadmin, rbcliente;
    private ButtonGroup grupoperfil;
    private JButton btnsalvar, btnvoltar;

    public TelaCadastroUsuario() {
        setLayout(new MigLayout("wrap 2, align center, insets 40", "[right][250!]", "[][][][][]"));

        JLabel lbltitulo = new JLabel("Cadastro Usuário");
        lbltitulo.setFont(new Font("tahoma", Font.BOLD, 18));
        add(lbltitulo, "span 2, center, gapbottom 20");
        
        add(new JLabel("Nome:"));
        txtnome = new JTextField();
        add(txtnome, "growx");
        
        add(new JLabel("CPF:"));
        txtcpf = new JTextField();
        add(txtcpf, "growx");
        
        add(new JLabel("tipo de perfil:"));
        rbadmin = new JRadioButton("administrador");
        rbcliente = new JRadioButton("cliente");
        rbcliente.setSelected(true);
        grupoperfil = new ButtonGroup();
        grupoperfil.add(rbadmin);
        grupoperfil.add(rbcliente);
        
        JPanel pnlr = new JPanel();
        pnlr.setOpaque(false); 
        pnlr.add(rbadmin); 
        pnlr.add(rbcliente);
        add(pnlr, "left");

        btnsalvar = new JButton("cadastrar");
        btnvoltar = new JButton("voltar");

        add(btnsalvar, "span 2, split 2, center, gaptop 20, width 120!");
        add(btnvoltar, "width 120!");
    }

    public String getNome() { return txtnome.getText(); }
    public String getCpf() { return txtcpf.getText(); }
    public boolean isAdministrador() { return rbadmin.isSelected(); }

    public void acaocadastrar(ActionListener listener) { btnsalvar.addActionListener(listener); }
    public void acaovoltar(ActionListener listener) { btnvoltar.addActionListener(listener); }
    
    public void limparcampos() {
        txtnome.setText(""); 
        txtcpf.setText("");
        rbcliente.setSelected(true);
    }
}
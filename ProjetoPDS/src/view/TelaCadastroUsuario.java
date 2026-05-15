package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class TelaCadastroUsuario extends JPanel {
    
    private static final long serialVersionUID = 1l;
    private JTextField txtnome;
    private JFormattedTextField txtcpf;
    private JRadioButton rbadmin, rbcliente;
    private ButtonGroup grupoperfil;
    private JButton btnsalvar, btnvoltar;

    public TelaCadastroUsuario() {
        setLayout(new MigLayout("fill, insets 40", "[grow, push][right][350, grow][grow, push]", "[grow, push][][][][][grow, push]"));

        JLabel lbltitulo = new JLabel("Novo Usuário");
        lbltitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        add(lbltitulo, "cell 1 0 2 1, center, gapbottom 20");
        
        add(new JLabel("Nome:"), "cell 1 1");
        txtnome = new JTextField();
        add(txtnome, "cell 2 1, growx");
        
        add(new JLabel("CPF:"), "cell 1 2");
        try {
            MaskFormatter mascara = new MaskFormatter("###.###.###-##");
            mascara.setPlaceholderCharacter('_');
            txtcpf = new JFormattedTextField(mascara);
        } catch (ParseException e) {
            txtcpf = new JFormattedTextField();
        }
        add(txtcpf, "cell 2 2, growx");
        
        add(new JLabel("Perfil:"), "cell 1 3");
        rbadmin = new JRadioButton("Admin");
        rbcliente = new JRadioButton("Cliente");
        rbcliente.setSelected(true);
        grupoperfil = new ButtonGroup();
        grupoperfil.add(rbadmin);
        grupoperfil.add(rbcliente);
        
        JPanel pnlRadio = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        pnlRadio.setOpaque(false); 
        pnlRadio.add(rbadmin); pnlRadio.add(rbcliente);
        add(pnlRadio, "cell 2 3, left");

        btnsalvar = new JButton("Cadastrar");
        btnvoltar = new JButton("Voltar");

        add(btnsalvar, "cell 1 4 2 1, split 2, center, gaptop 20, width 140!, height 35!");
        add(btnvoltar, "width 140!, height 35!");
    }

    public String getNome() { return txtnome.getText(); }
    public String getCpf() { return txtcpf.getText().replaceAll("[^0-9]", ""); }
    public boolean isAdministrador() { return rbadmin.isSelected(); }
    public void acaocadastrar(ActionListener l) { btnsalvar.addActionListener(l); }
    public void acaovoltar(ActionListener l) { btnvoltar.addActionListener(l); }
    
    public void limparcampos() {
        txtnome.setText(""); txtcpf.setValue(null); rbcliente.setSelected(true);
    }
}
package view;

import javax.swing.*;
import java.awt.CardLayout;

public class JanelaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private CardLayout layout;
    private JPanel painelPrincipal;

    public JanelaPrincipal() {
        layout = new CardLayout();
        painelPrincipal = new JPanel(layout);
        
        getContentPane().add(painelPrincipal);
        
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void adicionarTela(JPanel tela, String nome) {
        painelPrincipal.add(tela, nome);
    }

    public void mostrarTela(String nome) {
        layout.show(painelPrincipal, nome);
    }
}
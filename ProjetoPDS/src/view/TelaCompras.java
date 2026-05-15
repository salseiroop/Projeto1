package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class TelaCompras extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tabelaVitrine, tabelaCarrinho;
    private DefaultTableModel modVitrine, modCarrinho;
    private JButton btnColocarCarrinho, btnRemoverCarrinho, btnFinalizarCompra, btnDeslogar;
    private JLabel lblTotalValor;

    public TelaCompras() {
       setLayout(new MigLayout("fill, insets 20", "[grow, fill][grow, fill]", "[][grow, fill][][][]"));

        JLabel lblTitVitrine = new JLabel("Produtos Disponíveis");
        lblTitVitrine.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblTitVitrine, "center");

        JLabel lblTitCarrinho = new JLabel("Seu Carrinho");
        lblTitCarrinho.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(lblTitCarrinho, "center, wrap");

        modVitrine = new DefaultTableModel(new String[]{"ID", "Produto", "Preço", "Estoque"}, 0);
        tabelaVitrine = new JTable(modVitrine);
        add(new JScrollPane(tabelaVitrine), "grow");

        modCarrinho = new DefaultTableModel(new String[]{"ID", "Produto", "Qtd", "Subtotal"}, 0);
        tabelaCarrinho = new JTable(modCarrinho);
        add(new JScrollPane(tabelaCarrinho), "grow, wrap");

        btnColocarCarrinho = new JButton("Adicionar >>");
        btnRemoverCarrinho = new JButton("<< Remover");
        add(btnColocarCarrinho, "span 2, split 2, center, gaptop 10, height 35!");
        add(btnRemoverCarrinho, "height 35!, wrap");

        lblTotalValor = new JLabel("Total: R$ 0.00");
        lblTotalValor.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(lblTotalValor, "span 2, center, gapy 10, wrap");

        btnDeslogar = new JButton("Sair");
        btnFinalizarCompra = new JButton("Finalizar e Emitir Nota");
        add(btnDeslogar, "left, gaptop 15, width 100!");
        add(btnFinalizarCompra, "right, gaptop 15, height 40!");
    }

    public void acaoAdicionarCarrinho(ActionListener l) { btnColocarCarrinho.addActionListener(l); }
    public void acaoRemoverCarrinho(ActionListener l) { btnRemoverCarrinho.addActionListener(l); }
    public void acaoFinalizarCompra(ActionListener l) { btnFinalizarCompra.addActionListener(l); }
    public void acaoLogout(ActionListener l) { btnDeslogar.addActionListener(l); }
    public void exibirAlerta(String m) { JOptionPane.showMessageDialog(this, m); }
    
    public JTable getTabelaProdutos() { return tabelaVitrine; }
    public JTable getTabelaCarrinho() { return tabelaCarrinho; }
    public DefaultTableModel getModVitrine() { return modVitrine; }
    public DefaultTableModel getModCarrinho() { return modCarrinho; }
    public JLabel getLblTotalValor() { return lblTotalValor; }
}
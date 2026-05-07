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
       setLayout(new MigLayout("fill, insets 20", "[grow][grow]", "[][grow][][]"));

        JLabel lblTitVitrine = new JLabel("Produtos Disponíveis");
        lblTitVitrine.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblTitVitrine, "center");

        JLabel lblTitCarrinho = new JLabel("Seu Carrinho");
        lblTitCarrinho.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblTitCarrinho, "center, wrap");

        modVitrine = new DefaultTableModel(new String[]{"ID", "Produto", "Preço", "Estoque"}, 0);
        tabelaVitrine = new JTable(modVitrine);
        add(new JScrollPane(tabelaVitrine), "grow");

        modCarrinho = new DefaultTableModel(new String[]{"ID", "Produto", "Qtd", "Subtotal"}, 0);
        tabelaCarrinho = new JTable(modCarrinho);
        add(new JScrollPane(tabelaCarrinho), "grow, wrap");

        btnColocarCarrinho = new JButton("Adicionar ao Carrinho >>");
        add(btnColocarCarrinho, "split 2, flowx, center, gaptop 10");

        btnRemoverCarrinho = new JButton("<< Remover do Carrinho");
        add(btnRemoverCarrinho, "center, gaptop 10");

        lblTotalValor = new JLabel("Total: R$ 0.00");
        lblTotalValor.setFont(new Font("Tahoma", Font.BOLD, 14));
        add(lblTotalValor, "center, gaptop 10, wrap");

        btnDeslogar = new JButton("Logout");
        add(btnDeslogar, "left, gaptop 15");

        btnFinalizarCompra = new JButton("Emitir Nota Fiscal");
        add(btnFinalizarCompra, "right, gaptop 15");
    }

    public void acaoAdicionarCarrinho(ActionListener l) { btnColocarCarrinho.addActionListener(l); }
    public void acaoRemoverCarrinho(ActionListener l) { btnRemoverCarrinho.addActionListener(l); }
    public void acaoFinalizarCompra(ActionListener l) { btnFinalizarCompra.addActionListener(l); }
    public void acaoLogout(ActionListener l) { btnDeslogar.addActionListener(l); }
    
    public void exibirAlerta(String msg) { JOptionPane.showMessageDialog(this, msg); }
    
    public JTable getTabelaProdutos() { return tabelaVitrine; }
    public JTable getTabelaCarrinho() { return tabelaCarrinho; }
    public DefaultTableModel getModVitrine() { return modVitrine; }
    public DefaultTableModel getModCarrinho() { return modCarrinho; }
    public JLabel getLblTotalValor() { return lblTotalValor; }
}
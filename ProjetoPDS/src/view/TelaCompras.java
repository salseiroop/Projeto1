package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaCompras extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tabelaVitrine, tabelaCarrinho;
    private DefaultTableModel modVitrine, modCarrinho;
    private JButton btnColocarCarrinho, btnRemoverCarrinho, btnFinalizarCompra, btnDeslogar;
    private JLabel lblTotalValor;

    private JPopupMenu menuVitrine, menuCarrinho;
    private JMenuItem itemAdicionarVitrine, itemRemoverCarrinho;

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
        configurarMenuVitrine();
        add(new JScrollPane(tabelaVitrine), "grow");

        modCarrinho = new DefaultTableModel(new String[]{"ID", "Produto", "Qtd", "Subtotal"}, 0);
        tabelaCarrinho = new JTable(modCarrinho);
        configurarMenuCarrinho();
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

    private void configurarMenuVitrine() {
        menuVitrine = new JPopupMenu();
        itemAdicionarVitrine = new JMenuItem("Adicionar ao carrinho");
        menuVitrine.add(itemAdicionarVitrine);

        tabelaVitrine.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selecionarLinha(tabelaVitrine, e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    selecionarLinha(tabelaVitrine, e);
                    if (tabelaVitrine.getSelectedRow() != -1) {
                        menuVitrine.show(tabelaVitrine, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void configurarMenuCarrinho() {
        menuCarrinho = new JPopupMenu();
        itemRemoverCarrinho = new JMenuItem("Remover do carrinho");
        menuCarrinho.add(itemRemoverCarrinho);

        tabelaCarrinho.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selecionarLinha(tabelaCarrinho, e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    selecionarLinha(tabelaCarrinho, e);
                    if (tabelaCarrinho.getSelectedRow() != -1) {
                        menuCarrinho.show(tabelaCarrinho, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void selecionarLinha(JTable tabela, MouseEvent e) {
        int linha = tabela.rowAtPoint(e.getPoint());
        if (linha >= 0) {
            tabela.setRowSelectionInterval(linha, linha);
        }
    }

    public void acaoAdicionarCarrinho(ActionListener l)  { btnColocarCarrinho.addActionListener(l); }
    public void acaoRemoverCarrinho(ActionListener l)    { btnRemoverCarrinho.addActionListener(l); }
    public void acaoFinalizarCompra(ActionListener l)    { btnFinalizarCompra.addActionListener(l); }
    public void acaoLogout(ActionListener l)             { btnDeslogar.addActionListener(l); }
    public void exibirAlerta(String m)                   { JOptionPane.showMessageDialog(this, m); }

    public void acaoMenuAdicionar(ActionListener l) { itemAdicionarVitrine.addActionListener(l); }
    public void acaoMenuRemover(ActionListener l)   { itemRemoverCarrinho.addActionListener(l); }

    public JTable getTabelaProdutos()          { return tabelaVitrine; }
    public JTable getTabelaCarrinho()          { return tabelaCarrinho; }
    public DefaultTableModel getModVitrine()   { return modVitrine; }
    public DefaultTableModel getModCarrinho()  { return modCarrinho; }
    public JLabel getLblTotalValor()           { return lblTotalValor; }
}